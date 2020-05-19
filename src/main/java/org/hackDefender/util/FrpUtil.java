package org.hackDefender.util;

import java.util.List;

/**
 * @author vvings
 * @version 2020/4/17 11:04
 */
public class FrpUtil {
    private static final String templateURl = new StringBuilder().append("http://").append(PropertiesUtil.getProperty("frpc_admin_url")).append(":")
            .append(PropertiesUtil.getProperty("frpc_admin_port")).append("/api/").toString();
    private static final String putURL = templateURl + "config";
    private static final String reloadURL = templateURl + "reload";
    private static final String template = "\n\n[direct_%s_tcp]\n" +
            "type = tcp\n" +
            "local_ip = %s\n" +
            "local_port = %s\n" +
            "remote_port = %s\n" +
            "use_compression = true" +
            "\n\n[direct_%s_udp]\n" +
            "type = udp\n" +
            "local_ip = %s\n" +
            "local_port = %s\n" +
            "remote_port = %s\n" +
            "use_compression = true";

    /*public static void rewriteFrp(String localIp, int challengePort, String ContainerPort) {
        //String localIp = String.valueOf(userId) + "-" + String.valueOf(containUUid);
        //localIp = "127.0.0.1";
        String localPort = String.valueOf(challengePort);
        String remotePort = String.valueOf(ContainerPort);
        String newBuffer = PropertiesUtil.getProperty("frpc_template") + String.format(template, localIp, localIp, localPort, remotePort, localIp, localIp, localPort, remotePort);
        System.out.println(reloadURL);
        HttpClientUtil.putRequest(putURL, newBuffer);
        HttpClientUtil.getRequest(reloadURL);
        return;
    }*/

    public static void rewriteFrp(List<List<String>> lists) {
        StringBuilder builder = new StringBuilder();
        builder.append(PropertiesUtil.getProperty("frpc_template"));
        for (List<String> list : lists) {
            String localIp = list.get(0);
            String localPort = list.get(1);
            String remotePort = list.get(2);
            String newBuffer = String.format(template, localIp, localIp, localPort, remotePort, localIp, localIp, localPort, remotePort);
            builder.append(newBuffer);
        }
        HttpClientUtil.putRequest(putURL, builder.toString());
        HttpClientUtil.getRequest(reloadURL);
    }
}
