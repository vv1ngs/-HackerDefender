package docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import org.hackDefender.util.PropertiesUtil;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author vvings
 * @version 2020/4/14 10:40
 */
public class Example {

    public static DockerClient connectDocker() {
        DockerClient dockerClient = DockerClientBuilder.getInstance(PropertiesUtil.
                getProperty("docker_APIUrl1", "tcp://120.78.200.182:2375")).build();
        return dockerClient;
    }

    public static void main(String[] args) throws InterruptedException {

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
        /*CreateNetworkResponse network = dockerClient.createNetworkCmd().
                withName("frp_test").
                withDriver("overlay").
                withIpam(new Network.Ipam().withConfig(new Network.Ipam.Config().withSubnet("172.0.0.1/16")).
                        withDriver("default")).
                withAttachable(true).exec();*/
        /*List<String> a = new ArrayList<>();
        a.add("65e00e69a4f3");
        List<Container> exec = dockerClient.listContainersCmd().withIdFilter(a).exec();
        System.out.println(exec);*/
        /*TaskSpec taskSpec = new TaskSpec().withContainerSpec(new ContainerSpec().withImage("ctftraining/qwb_2019_supersqli"))
                .withResources(new ResourceRequirements().withLimits(new ResourceSpecs().withNanoCPUs(cuplimit).withMemoryBytes(melimit)))
                .withPlacement(new ServicePlacement().withConstraints(Lists.<String>newArrayList("node.labels.name==linux-1")));

        ServiceSpec serviceSpec = new ServiceSpec().withName("testService").
                withNetworks(Lists.newArrayList(new NetworkAttachmentConfig().withTarget("hackerdefender_frp_containers")))
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
        System.out.println(a.length());
        System.out.println(a);*/
        /*File file = new File("C:\\Users\\22080\\Documents\\Paradox Interactive\\Europa Universalis IV\\mod\\1.30 Celestial empire on which the sun never sets\\common\\religions\\CE_religion.txt");
        DockerUtil.uploadFile(file.getAbsolutePath(), "/", "ca2d6411c053");*/
        String cmd = "tail /var/log/nginx/access.log";
        String[] str = cmd.split("\\s+");
        String cmdStdout = null;
        String cmdStderr = null;
        ExecCreateCmdResponse execCreateCmdResponse = dockerClient.execCreateCmd("1f")
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
        String out = cmdStdout.isEmpty() ? cmdStderr : cmdStdout;
        System.out.println(out);
    }
}
