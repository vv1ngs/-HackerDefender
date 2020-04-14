package org.hackDefender.util;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientBuilder;

/**
 * @author vvings
 * @version 2020/4/14 11:04
 */
public class DockerUtil {
    private DockerClient dockerClient;

    private void initConnect() {
        dockerClient = DockerClientBuilder.getInstance(PropertiesUtil.
                getProperty("docker_tcpConnect", "tcp://127.0.0.1:2375")).build();
    }
}
