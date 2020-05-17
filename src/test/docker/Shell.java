package docker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.core.DockerClientBuilder;
import com.google.common.collect.Maps;
import org.hackDefender.util.PropertiesUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;

/**
 * @author vvings
 * @version 2020/5/15 10:20
 */
public class Shell {
    public static DockerClient connectDocker() {
        DockerClient dockerClient = DockerClientBuilder.getInstance(PropertiesUtil.
                getProperty("docker_APIUrl", "tcp://127.0.0.1:2375")).build();
        return dockerClient;
    }

    public static void main(String[] args) throws IOException {
        DockerClient dockerClient = connectDocker();
        ExecCreateCmdResponse exec = dockerClient.execCreateCmd("07bf38510cd9").withAttachStdin(true).withAttachStderr(true).withAttachStdout(true)
                .withCmd().withTty(true).withCmd("sh").exec();
        Socket socket = new Socket("127.0.0.1", 2375);
        String ip = "127.0.0.1";
        OutputStream outputStream = socket.getOutputStream();
        StringBuffer sb = new StringBuffer();
        sb.append("POST /exec/" + exec.getId() + "/start HTTP/1.1\r\n");
        sb.append("Host: " + ip + ":2375\r\n");
        sb.append("User-Agent: Docker-Client\r\n");
        sb.append("Content-Type: application/json\r\n");
        sb.append("Connection: Upgrade\r\n");
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Boolean> map = Maps.newHashMap();
        map.put("Detach", false);
        map.put("Tty", true);
        String json = mapper.writeValueAsString(map);
        sb.append("Content-Length: " + json.length() + "\r\n");
        sb.append("Upgrade: tcp\r\n");
        sb.append("\r\n");
        sb.append(json);
        outputStream.write(sb.toString().getBytes("UTF-8"));


        InputStream inputStream = socket.getInputStream();
        byte[] bytes = new byte[1024];
        StringBuffer returnMsg = new StringBuffer();
        
    }
}
