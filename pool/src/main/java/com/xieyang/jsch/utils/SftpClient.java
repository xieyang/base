
package com.xieyang.jsch.utils;

import java.util.Properties;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.xieyang.jsch.JSCHContstants;
import com.xieyang.jsch.model.SftpDTO;

/**
 * SFTP Client
 * 
 * 
 * @author 谢阳
 * @since 1.0
 * @version 2015年12月16日 谢阳
 */
public class SftpClient {
    
    /** 回话 */
    private Session session = null;
    
    /** 通道 */
    private ChannelSftp channel = null;
    
    public SftpClient(SftpDTO sftpDTO) {
        // 传入参数校验
        if (sftpDTO == null) {
            throw new RuntimeException("[JSCH] 初始化jsch客户端错误,传入的SFTP服务器配置为null.");
        }
        if (sftpDTO.getHost() == null || "".equals(sftpDTO.getHost())) {
            throw new RuntimeException("[JSCH] 初始化jsch客户端错误,SFTP服务器的host配置为空.");
        }
        // 如果通过username和password登录SFTP的话，做如下判断
        if (sftpDTO.getUsername() == null || "".equals(sftpDTO.getUsername())) {
            throw new RuntimeException("[JSCH] 初始化jsch客户端错误,SFTP服务器的username配置为空.");
        }
        if (sftpDTO.getPassword() == null || "".equals(sftpDTO.getPassword())) {
            throw new RuntimeException("[JSCH] 初始化jsch客户端错误,SFTP服务器的password配置为空.");
        }
        
        int iPort = (sftpDTO.getPort() == 0) ? JSCHContstants.DEFAULT_PORT : sftpDTO.getPort();
        JSch client = new JSch();
        try {
            session = client.getSession(sftpDTO.getUsername(), sftpDTO.getHost(), iPort);
            Properties objConfig = new Properties();
            objConfig.put("StrictHostKeyChecking", "no");
            session.setConfig(objConfig);
            session.setTimeout(JSCHContstants.DEFAULT_TIMEOUT);
            session.connect();
            channel = (ChannelSftp) session.openChannel(JSCHContstants.DEFAULT_PROTOCOL);
            channel.connect();
        } catch (JSchException e) {
            throw new RuntimeException("[JSCH] 初始化jsch客户端错误.", e);
        }
    }
    
    /**
     * connet
     * 
     */
    public void connect() {
        boolean flag = false;
        try {
            if (!session.isConnected()) {
                session.connect();
            }
            flag = true;
        } catch (JSchException e) {
            throw new RuntimeException("[JSCH] session连接出错.", e);
        }
        if (flag && !channel.isConnected()) {
            try {
                channel.connect();
            } catch (JSchException e) {
                throw new RuntimeException("[JSCH] channel连接出错.", e);
            }
        }
    }
    
    /**
     * close
     * 
     */
    public void close() {
        if (!channel.isClosed()) {
            channel.disconnect();
        }
        if (session.isConnected()) {
            session.disconnect();
        }
    }
    
    public void upload() {
        // channel.getBulkRequests();
        // channel.getHome()
        // channel.getId()
        // channel.getServerVersion()
        // channel.lcd(path);
        // channel.isEOF()
        // channel.setBulkRequests(bulk_requests);
        // channel.start();
    }
    
    /**
     * isConnected
     * 
     * @return boolean
     */
    public boolean isConnected() {
        return session.isConnected() && channel.isConnected();
    }
}
