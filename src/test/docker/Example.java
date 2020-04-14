package docker;

import com.github.dockerjava.api.DockerClient;

/**
 * @author vvings
 * @version 2020/4/14 10:40
 */
public class Example {

    public static DockerClient connectDocker() {
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost("tcp://12.0.0.1:2376")
                .withDockerTlsVerify(true)
                .withDockerCertPath("/home/user/.docker")
                .withRegistryUsername(registryUser)
                .withRegistryPassword(registryPass)
                .withRegistryEmail(registryMail)
                .withRegistryUrl(registryUrl)
                .build();

// using jaxrs/jersey i
// mplementation here (netty impl is also available)
        DockerCmdExecFactory dockerCmdExecFactory = new JerseyDockerCmdExecFactory()
                .withReadTimeout(1000)
                .withConnectTimeout(1000).w;

        DockerClient dockerClient = DockerClientBuilder.getInstance(config)
                .withDockerCmdExecFactory(dockerCmdExecFactory)
                .build();

        return dockerClient;
    }

    public static void main(String[] args) {
        DockerClient dockerClient = connectDocker();
    }
}
