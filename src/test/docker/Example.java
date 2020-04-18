package docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateNetworkResponse;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DockerClientBuilder;
import com.google.common.collect.Lists;
import org.hackDefender.util.PropertiesUtil;

/**
 * @author vvings
 * @version 2020/4/14 10:40
 */
public class Example {

    public static DockerClient connectDocker() {
        DockerClient dockerClient = DockerClientBuilder.getInstance(PropertiesUtil.
                getProperty("docker_APIUrl", "tcp://127.0.0.1:2375")).build();
        return dockerClient;
    }

    public static void main(String[] args) {

       /* DockerClient dockerClient = connectDocker();
        ExposedPort tcp80 = ExposedPort.tcp(80);
        Ports portBindings = new Ports();
        portBindings.bind(tcp80, Ports.Binding.bindPort(8088));
        Map<String, String> map = Maps.newHashMap();
        map.put("m", "128m");
        map.put("cpus", "0.25");
        dockerClient.createNetworkCmd().withName("test").withOptions(map).exec();
        CreateContainerResponse container = dockerClient.createContainerCmd("ctftraining/qwb_supersqli")
                .withName("test")
                .withHostConfig(newHostConfig().withPortBindings(portBindings).withMemory((long) (128 * 1024 * 1024)).withCpusetCpus()).
                        withExposedPorts(tcp80).withAliases().exec();
        dockerClient.startContainerCmd(container.getId()).exec();
        TaskSpec taskSpec = new TaskSpec().withResources(new ResourceRequirements().withLimits(new ResourceSpecs().withNanoCPUs(1)))*/
        Long cuplimit = new Long((long) (1E9 * 0.5));
        Long melimit = new Long((long) 128 * 1024 * 1024);
        DockerClient dockerClient = connectDocker();
        CreateNetworkResponse network = dockerClient.createNetworkCmd().
                withName("frp_test").
                withDriver("overlay").
                withIpam(new Network.Ipam().withConfig(new Network.Ipam.Config().withSubnet("172.0.0.1")).
                        withDriver("default")).
                withAttachable(true).exec();


        TaskSpec taskSpec = new TaskSpec().withContainerSpec(new ContainerSpec().withImage("ctftraining/qwb_2019_supersqli"))
                .withResources(new ResourceRequirements().withLimits(new ResourceSpecs().withNanoCPUs(cuplimit).withMemoryBytes(melimit)))
                .withPlacement(new ServicePlacement().withConstraints(Lists.<String>newArrayList("")));

        ServiceSpec serviceSpec = new ServiceSpec().withName("testService").
                withNetworks(Lists.newArrayList(new NetworkAttachmentConfig().withTarget("frp_container")))
                .withEndpointSpec(new EndpointSpec().withMode(EndpointResolutionMode.DNSRR))
                .withTaskTemplate(taskSpec)
                .withMode(new ServiceModeConfig().withReplicated(new ServiceReplicatedModeOptions().withReplicas(1)));
        dockerClient.createServiceCmd(serviceSpec).exec();
    }
}
