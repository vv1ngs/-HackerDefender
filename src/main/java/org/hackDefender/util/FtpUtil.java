package org.hackDefender.util;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author vvings
 * @version 2020/4/30 21:57
 */
public class FtpUtil {
    private static Logger logger = LoggerFactory.getLogger(FtpUtil.class);
    private static String ftpIP = PropertiesUtil.getProperty("ftp.server.ip");
    private static String ftpUser = PropertiesUtil.getProperty("ftp.user");
    private static String ftpPassword = PropertiesUtil.getProperty("ftp.password");
    private String ip;
    private int port;
    private String user;
    private String password;
    private FTPClient ftpClient;

    public FtpUtil(String ip, int port, String user, String password) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    public static boolean uploadFile(List<File> fileList) throws IOException {
        FtpUtil ftpUtil = new FtpUtil(ftpIP, 21, ftpUser, ftpPassword);
        boolean result = ftpUtil.uploadFile("script", fileList);
        logger.info("开始连接ftp服务器，结束上传上传结果:{}");
        return result;
    }

    private boolean uploadFile(String remotePath, List<File> fileList) throws IOException {
        boolean uploaded = true;
        FileInputStream fis = null;
        if (connectServer(this.getIp(), this.getPort(), this.getUser(), this.getPassword())) {
            try {
                ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                ftpClient.enterLocalActiveMode();
                for (File file : fileList) {
                    fis = new FileInputStream(file);
                    ftpClient.storeFile(file.getName(), fis);

                }
            } catch (IOException e) {
                logger.error("上传文件异常", e);
                e.printStackTrace();
                uploaded = false;
            } finally {
                ftpClient.disconnect();
                fis.close();
            }
        }
        return uploaded;
    }

    private boolean connectServer(String ip, int port, String user, String password) {
        ftpClient = new FTPClient();
        boolean isSuccess = false;
        try {
            ftpClient.connect(ip);
            isSuccess = ftpClient.login(user, password);
            isSuccess = true;
        } catch (IOException e) {
            logger.error("连接服务器异常", e);
            e.printStackTrace();
        }
        return isSuccess;

    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }
}
