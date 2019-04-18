package com.dou.document.ftp;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provide list useful APIs to interact with FTP server
 *
 * Created by CHI DOAN
 * Date: 02/14/2019
 */
@Component
public class FTPExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(FTPExecutor.class);

    @Autowired
    private ClientInfo clientInfo;

    /**
     * Check if a file exists on the ftp server
     * @param file The name of the file to check
     * @return true if file exists, false if not
     */
    public boolean fileExists(String file)
    {
        FTPClient ftpClient = Connection.open(this.clientInfo);
        try {
            if (ftpClient != null && ftpClient.isConnected()) {
                return (ftpClient.listFiles(file).length > 0);
            }
            return false;
        } catch (IOException e) {
            LOGGER.error("Error occurred while trying to check existing a remote file.", e);
            return false;
        } finally {
            Connection.close(ftpClient);
        }
    }

    /**
     * Check if a file exists on the ftp server
     * @param files The name of the list file to check
     * @return Map result
     */
    public Map<String, Boolean> fileExists(List<String> files)
    {
        FTPClient ftpClient = Connection.open(this.clientInfo);
        Map<String, Boolean> resultMap = new HashMap<>();
        try {
            if (ftpClient != null && ftpClient.isConnected()) {
                for (String filename: files) {
                    if (ftpClient.listFiles(filename).length > 0) {
                        resultMap.put(filename, true);
                    } else {
                        resultMap.put(filename, false);
                    }
                }
                return resultMap;
            }
            return null;
        } catch (IOException e) {
            LOGGER.error("Error occurred while trying to check existing a remote file.", e);
            return null;
        } finally {
            Connection.close(ftpClient);
        }
    }

    /**
     * Get a list of file names in a given directory for admin
     * @return List of files/directories
     * @throws IOException
     */
    public String[] getFileList(String path)
    {
        FTPClient ftpClient = Connection.open(this.clientInfo);
        try {
            if (ftpClient != null && ftpClient.isConnected()) {
                return ftpClient.listNames(path);
            }
            return new String[0];
        } catch (IOException e) {
            LOGGER.error("Error occurred while trying to list remote files in directory.", e);
            return new String[0];
        } finally {
            Connection.close(ftpClient);
        }
    }

    /**
     * Delete a single file.
     * @param name The file to delete
     * @return true if successful, false if not
     */
    public boolean deleteFile(String name)
    {
        FTPClient ftpClient = Connection.open(this.clientInfo);
        try {
            if (ftpClient != null && ftpClient.isConnected()) {
                return ftpClient.deleteFile(name);
            }
            return false;
        } catch (IOException e) {
            LOGGER.error("Error occurred while trying to delete a remote file.", e);
            return false;
        } finally {
            Connection.close(ftpClient);
        }
    }

    /**
     * Fetch a list remote
     * @param storage
     * @param paths
     * @return
     */
    public int fetch(Path storage, String ... paths) {
        FTPClient ftpClient = Connection.open(this.clientInfo);
        try {
            if (ftpClient != null && ftpClient.isConnected()) {
                if (paths != null && storage.toFile().isDirectory()) {
                    int result = 0;
                    boolean success;
                    OutputStream outputStream;

                    for (String path : paths) {
                        try {
                            outputStream = new BufferedOutputStream(
                                    new FileOutputStream(storage.resolve(path).toFile()));
                            success = ftpClient.retrieveFile(path, outputStream);
                            if(success) {
                                result++;
                            }
                            outputStream.close();
                        } catch (Exception e) {
                            // Do nothing
                        }
                    }

                    Connection.close(ftpClient);
                    return result;
                }
            }
            return -1;
        } catch (Exception e) {
            LOGGER.error("Error occurred while trying to fetch a list files on remote.", e);
            return -1;
        } finally {
            Connection.close(ftpClient);
        }
    }

    /**
     * Upload a file to remote server with name is "remoteFileName"
     * @param path The path of local file
     * @param remoteFileName Name saved on remote server
     * @return
     */
    public boolean push(String path, String remoteFileName) {
        FTPClient ftpClient = Connection.open(this.clientInfo);
        InputStream inputStream = null;
        try {
            if (ftpClient != null && ftpClient.isConnected()) {
                File file = new File(path);
                if (file.exists()) {
                    inputStream = new BufferedInputStream(
                            new FileInputStream(file)
                    );
                    return ftpClient.storeFile(remoteFileName, inputStream);
                }
            }
            return false;
        } catch (Exception e) {
            LOGGER.error(String.format("Error occurred while trying to upload [%s] to remote", remoteFileName), e);
            return false;
        } finally {
            Connection.close(ftpClient);
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e) {}
        }

    }

    /**
     * Upload a file to remote server. Default is filename of local file.
     * @param path The path of local file
     * @return
     */
    public boolean push(String path) {
        return push(path, FilenameUtils.getName(path));
    }

    /**
     * Upload a file to remote server. Default is filename of local file.
     * @param path The path of local file
     * @return
     */
    public boolean push(Path path) {
        return push(path.toString(), FilenameUtils.getName(path.toString()));
    }

    public boolean push(Path path, String remoteFileName) {
        return push(path.toString(), remoteFileName);
    }

    /**
     * Upload a file to remote server with name is "remoteFileName"
     * @param inputStream The InputStream from uploaded file
     * @param remoteFilename Name saved on remote server
     * @return
     */
    public boolean push(InputStream inputStream, String remoteFilename) {
        FTPClient ftpClient = Connection.open(this.clientInfo);
        if (ftpClient != null && ftpClient.isConnected()) {
            try {
                return ftpClient.storeFile(remoteFilename, inputStream);
            } catch (Exception e) {
                LOGGER.error("Error occurred while trying to upload a file to remote", e);
            } finally {
                try {
                    Connection.close(ftpClient);
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (Exception e) {}
            }
        }
        return false;
    }

    public boolean pull(Path storage, String remoteFilename) {
        FTPClient ftpClient = Connection.open(this.clientInfo);
        OutputStream outputStream = null;
        try {
            if (ftpClient != null && ftpClient.isConnected()) {
                if (!storage.toFile().exists()) {
                    try {
                        FileUtils.forceMkdir(storage.toFile());
                    } catch (Exception e) {
                        LOGGER.error("Error occurred while trying to create storage pulling file.", e);
                        return false;
                    }
                }
                outputStream = new BufferedOutputStream(new FileOutputStream(storage.resolve(remoteFilename).toFile()));
                return ftpClient.retrieveFile(remoteFilename, outputStream);
            }
            return false;
        } catch (Exception e) {
            LOGGER.error(String.format("Error occurred while trying to download [%s] from remote", remoteFilename), e);
            return false;
        } finally {
            Connection.close(ftpClient);
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (Exception e) {}
        }
    }

    public boolean pull(OutputStream outputStream, String remoteFilename) {
        FTPClient ftpClient = Connection.open(this.clientInfo);
        if (ftpClient != null && ftpClient.isConnected()) {
            try {
                if (outputStream != null) {
                    return ftpClient.retrieveFile(remoteFilename, outputStream);
                }
            } catch (Exception e) {
                LOGGER.error("Error occurred while trying to download a file from remote", e);
            } finally {
                Connection.close(ftpClient);
                try {
                    outputStream.close();
                } catch (Exception e) {}
            }
        }
        return false;
    }

    public byte[] pull(String remoteFilename) {
        FTPClient ftpClient = Connection.open(this.clientInfo);
        if (ftpClient != null && ftpClient.isConnected()) {
            try {
                InputStream stream = ftpClient.retrieveFileStream(remoteFilename);
                if (stream == null) {
                    LOGGER.error("Error occurred while trying to download a file from remote");
                } else {
                    return IOUtils.toByteArray(stream);
                }
            } catch (Exception e) {
                LOGGER.error("Error occurred while trying to download a file from remote", e);
            } finally {
                Connection.close(ftpClient);
            }
        }
        return null;
    }
}
