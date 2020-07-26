package org.hackDefender.util;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateServiceResponse;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author vvings
 * @version 2020/4/14 11:04
 */
public class DockerUtil {
    private static final Long cupLimit = new Long((long) (1E9 * 0.5));
    private static final Long meLimit = new Long((long) 128 * 1024 * 1024);
    private static final String SelfDockerApi = "unix:///var/run/docker.sock";
    private static final String dockerApi1 = PropertiesUtil.getProperty("docker_APIUrl1");
    private static final String dockerApi2 = PropertiesUtil.getProperty("docker_APIUrl2");

    private static DockerClient getDockerClient(String dockerApi) {
        final DefaultDockerClientConfig config = DefaultDockerClientConfig
                .createDefaultConfigBuilder()
                .withDockerHost(dockerApi)
                .build();
        return DockerClientBuilder
                .getInstance(config)
                .build();
    }

    public static Map<String, String> addContainer(Integer userId, String uuid, Integer challengePort, String dockerImage, String meLimit, Double cupLimit) {
        Map<String, String> map = Maps.newHashMap();
        List<String> list = Lists.newArrayList();
        Long meL = Long.valueOf(meLimit.substring(0, meLimit.length() - 1)) * 1024 * 1024;
        Long cupL = new Double(cupLimit * 1e9).longValue();
        String localIp = String.valueOf(userId) + "-" + uuid;
        String containerPort = null;
        if (RedisPoolSharedUtil.trylock("portPop_lock", 5000)) {
            containerPort = RedisPoolSharedUtil.sPop();
            RedisPoolSharedUtil.del("portPop_lock");
        }
        map.put(localIp, localIp);
        DockerClient dockerClient = getDockerClient(SelfDockerApi);
        TaskSpec taskSpec = new TaskSpec();
        taskSpec.withContainerSpec(new ContainerSpec().withImage(dockerImage));
        taskSpec.withResources(new ResourceRequirements().withLimits(new ResourceSpecs().withNanoCPUs(cupL).withMemoryBytes(meL)));
        taskSpec.withPlacement(new ServicePlacement().withConstraints(Lists.<String>newArrayList("node.labels.name==linux-1")));
        ServiceSpec serviceSpec = new ServiceSpec().withName(localIp)
                .withNetworks(Lists.newArrayList(new NetworkAttachmentConfig().withTarget("hackerdefender_frp_containers")))
                .withEndpointSpec(new EndpointSpec().withMode(EndpointResolutionMode.DNSRR))
                .withTaskTemplate(taskSpec)
                .withLabels(map)
                .withMode(new ServiceModeConfig().withReplicated(new ServiceReplicatedModeOptions().withReplicas(1)));
        dockerClient.createServiceCmd(serviceSpec).exec();
        String ContainId = null;
        while (ContainId == null) {
            List<Task> testService = dockerClient.listTasksCmd().withNameFilter(localIp).exec();
            for (Task task : testService) {
                if (task.getStatus().getContainerStatus() != null) {
                    ContainId = task.getStatus().getContainerStatus().getContainerID();
                }
            }
        }
        map.clear();
        map.put("containerPort", containerPort);
        map.put("ContainId", ContainId.substring(0, 32));
        list.add(containerPort);
        list.add(ContainId.substring(0, 32));
        return map;
    }

    public static void removeContainer(Integer userId, String uuid) {
        Map<String, String> map = Maps.newHashMap();
        String localIp = String.valueOf(userId) + "-" + uuid;
        map.put(localIp, localIp);
        DockerClient dockerClient = getDockerClient(dockerApi1);
        List<Service> listServicesCmd = dockerClient.listServicesCmd().withLabelFilter(map).exec();
        if (listServicesCmd.size() != 0) {
            for (Service c : listServicesCmd) {
                dockerClient.removeServiceCmd(c.getId()).exec();
            }
        }
        dockerClient = getDockerClient(dockerApi2);
        listServicesCmd = dockerClient.listServicesCmd().withLabelFilter(map).exec();
        if (listServicesCmd.size() != 0) {
            for (Service c : listServicesCmd) {
                dockerClient.removeServiceCmd(c.getId()).exec();
            }
        }

    }

    public static String execContainer(String containerId, String dockerApi) {
        DockerClient dockerClient = getDockerClient(dockerApi);
        ExecCreateCmdResponse exec = dockerClient.execCreateCmd(containerId).withAttachStdin(true).withAttachStderr(true).withAttachStdout(true)
                .withCmd().withTty(true).withCmd("sh").exec();
        return exec.getId();
    }

    public static boolean checkUrl(String containerId) {
        DockerClient dockerClient = getDockerClient(dockerApi1);
        List<String> containerList = new ArrayList<>();
        containerList.add(containerId);
        List<Container> containers = dockerClient.listContainersCmd().withIdFilter(containerList).exec();
        if (containers.size() == 0) {
            return false;
        } else {
            return true;
        }
    }


    public static boolean checkLocal(String containerId) {
        DockerClient dockerClient = getDockerClient(SelfDockerApi);
        List<String> containerList = new ArrayList<>();
        containerList.add(containerId);
        List<Container> containers = dockerClient.listContainersCmd().withIdFilter(containerList).exec();
        if (containers.size() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public static String getLogs(String containerId, String cmd) {
        String[] str = cmd.split("\\s+");
        String cmdStdout = null;
        String cmdStderr = null;
        DockerClient dockerClient = null;
        if (checkUrl(containerId)) {
            dockerClient = getDockerClient(dockerApi1);
        } else {
            dockerClient = getDockerClient(dockerApi2);
        }
        ExecCreateCmdResponse execCreateCmdResponse = dockerClient.execCreateCmd(containerId)
                .withAttachStdout(true)
                .withAttachStderr(true)
                .withTty(false)
                .withCmd(str)
                .exec();

        try (ByteArrayOutputStream stdout = new ByteArrayOutputStream();
             ByteArrayOutputStream stderr = new ByteArrayOutputStream();
             ExecStartResultCallback cmdCallback = new ExecStartResultCallback(stdout, stderr)) {
            dockerClient.execStartCmd(execCreateCmdResponse.getId()).exec(cmdCallback).awaitCompletion();
            cmdStdout = stdout.toString(StandardCharsets.UTF_8.name());
            cmdStderr = stderr.toString(StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            String format = "Exception was thrown when executing: %s, for container: %s ";
            throw new IllegalStateException(format, e);
        }
        return cmdStdout.isEmpty() ? cmdStderr : cmdStdout;
    }

    public static String test() {
        Long cuplimit = new Long((long) (1E9 * 0.5));
        Long melimit = new Long((long) 128 * 1024 * 1024);
        DockerClient dockerClient = getDockerClient(dockerApi1);
        TaskSpec taskSpec = new TaskSpec().withContainerSpec(new ContainerSpec().withImage("ctftraining/qwb_2019_supersqli"))
                .withResources(new ResourceRequirements().withLimits(new ResourceSpecs().withNanoCPUs(cuplimit).withMemoryBytes(melimit)))
                .withPlacement(new ServicePlacement().withConstraints(Lists.<String>newArrayList("node.labels.name==linux-1")));

        ServiceSpec serviceSpec = new ServiceSpec().withName("testService").
                withNetworks(Lists.newArrayList(new NetworkAttachmentConfig().withTarget("frp_test")))
                .withEndpointSpec(new EndpointSpec().withMode(EndpointResolutionMode.DNSRR))
                .withTaskTemplate(taskSpec)
                .withMode(new ServiceModeConfig().withReplicated(new ServiceReplicatedModeOptions().withReplicas(1)));
        CreateServiceResponse exec = dockerClient.createServiceCmd(serviceSpec).exec();
        String a = null;
        while (a == null) {
            List<Task> testService = dockerClient.listTasksCmd().withNameFilter("testService").exec();
            for (Task task : testService) {
                if (task.getStatus().getContainerStatus() != null) {
                    a = task.getStatus().getContainerStatus().getContainerID();
                }
            }
        }
        return a;
    }

    public static void uploadFile(String localPath, String remotePath, String containerId) {
        DockerClient dockerClient = null;
        if (checkUrl(containerId)) {
            dockerClient = getDockerClient(dockerApi1);
        } else {
            dockerClient = getDockerClient(dockerApi2);
        }
        dockerClient.copyArchiveToContainerCmd(containerId).withRemotePath(remotePath).withHostResource(localPath).exec();
    }
}
