package com.dou.document.ftp;

import org.apache.commons.validator.routines.InetAddressValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.StringUtils;

/**
 * Load all properties relate to FTP Server at application.properties file
 * With prefix: ftp.server
 *
 * Created by CHI_DOAN
 * Date: 02/14/2019
 */

@PropertySource("classpath:application.properties")
@ConfigurationProperties(prefix="ftp.server", ignoreInvalidFields = true)
public class ServerProperties {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerProperties.class);

    private Address address;

    private Auth auth;

    private Option option;

    public boolean isValidInfo() {
        InetAddressValidator IpValidator = InetAddressValidator.getInstance();
        if (!IpValidator.isValidInet4Address(this.address.getIp())) {
            LOGGER.error(String.format("FTP Server - IP address [%s] is not valid IPv4", this.address.getIp()));
            return false;
        }

        if (this.address.getPort() <= 0) {
            LOGGER.info(String.format(
                    "FTP Server - Port [%d] must be a positive number. Auto change to DEFAULT_PORT [%d]",
                    this.address.getPort(),
                    ClientInfo.DEFAULT_PORT
            ));
            this.address.setPort(ClientInfo.DEFAULT_PORT);
        }

        if (StringUtils.isEmpty(this.auth.getUsername()) || StringUtils.isEmpty(this.auth.getPassword())) {
            LOGGER.error(String.format(
                    "FTP Server - Username [%s] and password [%s] must be not empty",
                    this.auth.getUsername(),
                    this.auth.getPassword()
            ));
            return false;
        }

        if (this.option.getRetryTimes() <= 0) {
            LOGGER.info(String.format(
                    "FTP Server - Retry times [%d] must be least 1 time. Auto change to DEFAULT_RETRY_TIMES [%d]",
                    this.option.getRetryTimes(),
                    ClientInfo.DEFAULT_RETRY_TIMES
            ));
            this.option.setRetryTimes(ClientInfo.DEFAULT_RETRY_TIMES);
        }

        if (this.option.getWaiting() <= 0) {
            LOGGER.info(String.format(
                    "FTP Server - Value of waiting [%d] must be a positive number. Auto change to DEFAULT_WAITING [%d]",
                    this.option.getWaiting(),
                    ClientInfo.DEFAULT_WAITING
            ));
            this.option.setWaiting(ClientInfo.DEFAULT_WAITING);
        }

        return true;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Auth getAuth() {
        return auth;
    }

    public void setAuth(Auth auth) {
        this.auth = auth;
    }

    public Option getOption() {
        return option;
    }

    public void setOption(Option option) {
        this.option = option;
    }

    public static class Address {
        private String ip;

        private int port;

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
    }

    public static class Auth {
        private String username;

        private String password;

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
    }

    public static class Option {
        private int retryTimes;

        private int waiting;

        public int getRetryTimes() {
            return retryTimes;
        }

        public void setRetryTimes(int retryTimes) {
            this.retryTimes = retryTimes;
        }

        public int getWaiting() {
            return waiting;
        }

        public void setWaiting(int waiting) {
            this.waiting = waiting;
        }
    }
}
