package org.hackDefender.util;

import org.hackDefender.common.ServerResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author vvings
 * @version 2020/5/17 19:50
 */
public class PythonUtil {
    static final String pythonUrl = PropertiesUtil.getProperty("python_path", "C:\\Users\\22080\\Desktop\\1.py");
    String[] cmd = new String[]{"C:\\python3\\python3.exe", "C:\\Users\\22080\\Desktop\\1.py", "1.py"};

    public static ServerResponse exec(String ScripUrl, String TargetUrl) {
        String[] cmd = new String[]{"C:\\python3\\python3.exe", pythonUrl, ScripUrl, TargetUrl};
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader er = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            String output = null;
            while ((output = br.readLine()) != null) {
                stringBuilder.append(output);
            }
            if (er.readLine() == null) {
                return ServerResponse.createBySuccess("python执行成功", stringBuilder.toString());
            } else {
                stringBuilder.delete(0, stringBuilder.length());
                while ((output = er.readLine()) != null) {
                    System.out.println(output);
                }
            }
            return ServerResponse.createBySuccess("python执行失败", stringBuilder.toString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return ServerResponse.createByError();
    }
}
