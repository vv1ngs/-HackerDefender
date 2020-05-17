package org.hackDefender.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import org.hackDefender.util.DockerUtil;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * @author vvings
 * @version 2020/5/15 20:29
 */

public class ContainerExecWSHandler extends TextWebSocketHandler {
    private Map<String, ExecSession> execSessionMap = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String ip = "127.0.0.1";
        String containerId = session.getAttributes().get("containerId").toString();
        //创建bash
        String execId = DockerUtil.execContainer();
        //连接bash
        Socket socket = connectExec(ip, execId);
        //获得输出
        getExecMessage(session, ip, containerId, socket);

    }

    private Socket connectExec(String ip, String execId) throws IOException {
        Socket socket = new Socket(ip, 2375);
        socket.setKeepAlive(true);
        OutputStream out = socket.getOutputStream();
        StringBuffer pw = new StringBuffer();
        pw.append("POST /exec/" + execId + "/start HTTP/1.1\r\n");
        pw.append("Host: " + ip + ":2375\r\n");
        pw.append("User-Agent: Docker-Client\r\n");
        pw.append("Content-Type: application/json\r\n");
        pw.append("Connection: Upgrade\r\n");
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Boolean> map = Maps.newHashMap();
        map.put("Detach", false);
        map.put("Tty", true);
        String json = mapper.writeValueAsString(map);
        pw.append("Content-Length: " + json.length() + "\r\n");
        pw.append("Upgrade: tcp\r\n");
        pw.append("\r\n");
        pw.append(json);
        out.write(pw.toString().getBytes("UTF-8"));
        out.flush();
        return socket;
    }

    private void getExecMessage(WebSocketSession session, String ip, String containerId, Socket socket) throws IOException {
        InputStream inputStream = socket.getInputStream();
        byte[] bytes = new byte[1024];
        StringBuffer returnMsg = new StringBuffer();
        while (true) {
            int n = inputStream.read(bytes);
            String msg = new String(bytes, 0, n);
            returnMsg.append(msg);
            bytes = new byte[10240];
            if (returnMsg.indexOf("\r\n\r\n") != -1) {
                session.sendMessage(new TextMessage(returnMsg.substring(returnMsg.indexOf("\r\n\r\n") + 4, returnMsg.length())));
                break;
            }
        }
        OutPutThread outPutThread = new OutPutThread(inputStream, session);
        outPutThread.start();
        execSessionMap.put(containerId, new ExecSession(ip, containerId, socket, outPutThread));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        String containerId = session.getAttributes().get("containerId").toString();
        ExecSession execSession = execSessionMap.get(containerId);
        if (execSession != null) {
            execSession.getOutPutThread().interrupt();
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String containerId = session.getAttributes().get("containerId").toString();
        ExecSession execSession = execSessionMap.get(containerId);
        OutputStream out = execSession.getSocket().getOutputStream();
        out.write(message.asBytes());
        out.flush();
    }
}
