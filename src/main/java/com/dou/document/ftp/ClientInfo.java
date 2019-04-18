package com.dou.document.ftp;

/**
 * Store information of FTP server
 *
 * Created by CHI DOAN
 * Date: 02/14/2019
 */
public class ClientInfo {
    public static final int DEFAULT_PORT = 21;
    public static final int DEFAULT_RETRY_TIMES = 3;
    public static final int DEFAULT_WAITING = 20000;

    private String ip;

    private int port;

    private String username;

    private String password;

    private int retryTime;

    private int timeout;

    public ClientInfo() {
        this.port = DEFAULT_PORT;
    }

    public ClientInfo(String ip, String username, String password, int retryTime, int timeout) {
        this.ip = ip;
        this.port = DEFAULT_PORT;
        this.username = username;
        this.password = password;
        this.retryTime = retryTime;
        this.timeout = timeout;
    }

    public ClientInfo(String ip, int port, String username, String password, int retryTime, int timeout) {
        this.ip = ip;
        this.port = port;
        this.username = username;
        this.password = password;
        this.retryTime = retryTime;
        this.timeout = timeout;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRetryTime() {
        return retryTime;
    }

    public void setRetryTime(int retryTime) {
        this.retryTime = retryTime;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
