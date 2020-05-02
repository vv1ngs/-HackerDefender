package org.hackDefender.util;

import com.github.dockerjava.api.DockerClient;
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

    public static void addContainer(Integer userId, String uuid, Integer challengePort, String dockerImage, String meLimit, Double cupLimit) {
        Map<String, String> map = Maps.newHashMap();
        Long meL = (long) (Long.valueOf(meLimit) * 1024 * 1024);
        Long cupL = new Double(cupLimit * 1e9).longValue();
        String localIp = String.valueOf(userId) + "-" + uuid;
        String containerPort = RedisPoolSharedUtil.sPop();
        map.put(localIp, localIp);
        DockerClient dockerClient = getDockerClient();
        TaskSpec taskSpec = new TaskSpec();
        taskSpec.withContainerSpec(new ContainerSpec().withImage(dockerImage));
        taskSpec.withResources(new ResourceRequirements().withLimits(new ResourceSpecs().withNanoCPUs(cupL).withMemoryBytes(meL)));
        taskSpec.withPlacement(new ServicePlacement().withConstraints(Lists.<String>newArrayList("node.labels.name==linux-1")));

        ServiceSpec serviceSpec = new ServiceSpec().withName("testService")
                .withNetworks(Lists.newArrayList(new NetworkAttachmentConfig().withTarget("test_frp_containers")))
                .withEndpointSpec(new EndpointSpec().withMode(EndpointResolutionMode.DNSRR))
                .withTaskTemplate(taskSpec)
                .withLabels(map)
                .withMode(new ServiceModeConfig().withReplicated(new ServiceReplicatedModeOptions().withReplicas(1)));
        dockerClient.createServiceCmd(serviceSpec).exec();
        FrpUtil.rewriteFrp(localIp, challengePort, containerPort);
    }

    public static void removeContainer(Integer userId, String uuid) {
        Map<String, String> map = Maps.newHashMap();
        String localIp = String.valueOf(userId) + "-" + uuid;
        map.put(localIp, localIp);
        DockerClient dockerClient = getDockerClient();
        List<Service> listServicesCmd = dockerClient.listServicesCmd().withLabelFilter(map).exec();
        for (Service c : listServicesCmd) {
            dockerClient.removeServiceCmd(c.getId());
        }
    }
}
