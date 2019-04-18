package com.dou.document.ftp;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Use to create connection to Server and otherwise
 *
 * Created by CHI DOAN
 * Date: 02/14/2019
 */
public final class Connection {
    private static final Logger LOGGER = LoggerFactory.getLogger(Connection.class);

    public static FTPClient open(ClientInfo clientInfo) {
        FTPClient client = new FTPClient();
        try {
            client.setConnectTimeout(clientInfo.getTimeout());
            int times = 1;
            while (times <= clientInfo.getRetryTime() && !client.isConnected()) {
                client.connect(clientInfo.getIp(), clientInfo.getPort());

                int reply = client.getReplyCode();
                if (!FTPReply.isPositiveCompletion(reply)) {
                    LOGGER.error(String.format("Retry time: %d. Could not connect FTP Server [%s].",
                            times,
                            clientInfo.getIp() + ':' + clientInfo.getPort()));
                    disconnect(client);
                }

                times++;
            }

            if (client.isConnected()) {
                client.login(clientInfo.getUsername(), clientInfo.getPassword());
                int reply = client.getReplyCode();
                if (!FTPReply.isPositiveCompletion(reply)) {
                    LOGGER.error(String.format("Authentication fail on FTP Server [%s, %s].",
                            clientInfo.getIp() + ':' + clientInfo.getPort(),
                            clientInfo.getUsername() + ":" +clientInfo.getPassword()
                    ));
                    disconnect(client);
                    return null;
                }

                client.setFileType(FTP.BINARY_FILE_TYPE);
                client.enterLocalPassiveMode();
            } else {
                LOGGER.error(String.format("Fail to connect FTP Server [%s].",
                        clientInfo.getIp() + ':' + clientInfo.getPort()));
                return null;
            }
        } catch (Exception e) {
            disconnect(client);
        }

        return client;
    }

    public static void close(FTPClient client) {
        disconnect(client);
    }

    private static void disconnect(FTPClient client) {
        if (client != null && client.isConnected()) {
            try {
                client.disconnect();
            }
            catch (IOException f) {
                // Nothing
            }
        }
    }
}
