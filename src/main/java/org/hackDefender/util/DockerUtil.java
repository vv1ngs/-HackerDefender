package org.hackDefender.util;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateServiceResponse;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * @author vvings
 * @version 2020/4/14 11:04
 */
public class DockerUtil {
    private static final Long cupLimit = new Long((long) (1E9 * 0.5));
    private static final Long meLimit = new Long((long) 128 * 1024 * 1024);

    private static DockerClient getDockerClient() {
        final String localDockerHost = PropertiesUtil.getProperty("docker_APIUrl", "unix://var/run/docker.sock");
        final DefaultDockerClientConfig config = DefaultDockerClientConfig
                .createDefaultConfigBuilder()
                .withDockerHost(localDockerHost)
                .build();
        return DockerClientBuilder
                .getInstance(config)
                .build();
    }

    public static List<String> addContainer(Integer userId, String uuid, Integer challengePort, String dockerImage, String meLimit, Double cupLimit) {
        Map<String, String> map = Maps.newHashMap();
        List<String> list = Lists.newArrayList();
        Long meL = Long.valueOf(meLimit.substring(0, meLimit.length() - 1)) * 1024 * 1024;
        Long cupL = new Double(cupLimit * 1e9).longValue();
        String localIp = String.valueOf(userId) + "-" + uuid;
        String containerPort = null;
        if (RedisPoolSharedUtil.trylock("portPop_lock", 5)) {
            containerPort = RedisPoolSharedUtil.sPop();
            RedisPoolSharedUtil.del("portPop_lock");
        }
        map.put(localIp, localIp);
        DockerClient dockerClient = getDockerClient();
        TaskSpec taskSpec = new TaskSpec();
        taskSpec.withContainerSpec(new ContainerSpec().withImage(dockerImage));
        taskSpec.withResources(new ResourceRequirements().withLimits(new ResourceSpecs().withNanoCPUs(cupL).withMemoryBytes(meL)));
        taskSpec.withPlacement(new ServicePlacement().withConstraints(Lists.<String>newArrayList("node.labels.name==linux-1")));

        ServiceSpec serviceSpec = new ServiceSpec().withName(localIp)
                .withNetworks(Lists.newArrayList(new NetworkAttachmentConfig().withTarget("test_frp_containers")))
                .withEndpointSpec(new EndpointSpec().withMode(EndpointResolutionMode.DNSRR))
                .withTaskTemplate(taskSpec)
                .withLabels(map)
                .withMode(new ServiceModeConfig().withReplicated(new ServiceReplicatedModeOptions().withReplicas(1)));
        dockerClient.createServiceCmd(serviceSpec).exec();
        FrpUtil.rewriteFrp(localIp, challengePort, containerPort);
        String ContainId = null;
        while (ContainId == null) {
            List<Task> testService = dockerClient.listTasksCmd().withNameFilter(localIp).exec();
            for (Task task : testService) {
                if (task.getStatus().getContainerStatus() != null) {
                    ContainId = task.getStatus().getContainerStatus().getContainerID();
                }
            }
        }
        list.add(containerPort);
        list.add(ContainId);
        return list;
    }

    public static void removeContainer(Integer userId, String uuid) {
        Map<String, String> map = Maps.newHashMap();
        String localIp = String.valueOf(userId) + "-" + uuid;
        map.put(localIp, localIp);
        DockerClient dockerClient = getDockerClient();
        List<Service> listServicesCmd = dockerClient.listServicesCmd().withLabelFilter(map).exec();
        for (Service c : listServicesCmd) {
            dockerClient.removeServiceCmd(c.getId()).exec();
        }
    }

    public static String execContainer() {
        DockerClient dockerClient = getDockerClient();
        ExecCreateCmdResponse exec = dockerClient.execCreateCmd("07bf38510cd9").withAttachStdin(true).withAttachStderr(true).withAttachStdout(true)
                .withCmd().withTty(true).withCmd("sh").exec();
        return exec.getId();
    }

    public static String test() {
        Long cuplimit = new Long((long) (1E9 * 0.5));
        Long melimit = new Long((long) 128 * 1024 * 1024);
        DockerClient dockerClient = getDockerClient();
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
}
