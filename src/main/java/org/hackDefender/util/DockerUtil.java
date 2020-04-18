package org.hackDefender.util;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientBuilder;

/**
 * @author vvings
 * @version 2020/4/14 11:04
 */
public class DockerUtil {
    private static DockerClient dockerClient;

    private static void initConnect() {
        dockerClient = DockerClientBuilder.getInstance(PropertiesUtil.
                getProperty("docker_APIUrl", "tcp://127.0.0.1:2375")).build();
    }

    static {
        initConnect();
    }

    public static void add_new_docker_container(Integer userId, Integer challenge_id, String uuid, String dockerImage) {
        if (dockerImage.startsWith("{")) {
            dockerClient.createNetworkCmd();
        }
    }
}
