
package com.xieyang.jsch.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Properties;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
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
//    private ChannelSftp channel = null;
    
    /**
     * 构造函数
     * 
     * @param sftpDTO
     */
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
        long start = System.currentTimeMillis();
        try {
            session = client.getSession(sftpDTO.getUsername(), sftpDTO.getHost(), iPort);
            Properties objConfig = new Properties();
            // 设置第一次登陆的时候提示，可选值：(ask | yes | no)
            objConfig.put("StrictHostKeyChecking", "no");
            session.setConfig(objConfig);
            // 设置登录超时时间
//            session.setTimeout(JSCHContstants.DEFAULT_TIMEOUT);
            session.setTimeout(6000*1000);
            session.setPassword(sftpDTO.getPassword());
//            session.setServerAliveInterval(10*1000);
            session.connect();
            System.out.println("[JSCH] session连接耗时："+(System.currentTimeMillis() - start)/1000+"s");
//            channel = (ChannelSftp) session.openChannel(JSCHContstants.DEFAULT_PROTOCOL);
//            channel.connect();
        } catch (JSchException e) {
        	System.out.println("[JSCH] session连接出错，耗时："+(System.currentTimeMillis() - start) / 1000 + "s");
        	e.printStackTrace();
            throw new RuntimeException("[JSCH] 初始化jsch客户端错误.", e);
        }
    }
    
    
    /**
     * connet
     * 
     */
    public void connect() {
//        boolean flag = false;
    	long start = System.currentTimeMillis();
        try {
            if (!session.isConnected()) {
                session.connect();
                System.out.println("[JSCH] session连接耗时："+(System.currentTimeMillis() - start)/1000+"s.");
            }
//            flag = true;
        } catch (JSchException e) {
        	System.out.println("[JSCH] session连接出错，耗时："+(System.currentTimeMillis() - start) / 1000 + "s");
        	e.printStackTrace();
            throw new RuntimeException("[JSCH] session连接出错.", e);
        }
//        if (flag && !channel.isConnected()) {
//            try {
//                channel.connect();
//            } catch (JSchException e) {
//            	e.printStackTrace();
//                throw new RuntimeException("[JSCH] channel连接出错.", e);
//            }
//        }
    }
    
    /**
     * close
     * 
     */
    public void close() {
//        if (!channel.isClosed()) {
//            channel.disconnect();
//        }
        if (session.isConnected()) {
            session.disconnect();
        }
        session = null;
    }
    
    /**
     * upload
     * 
     * @param localFile localFile
     * @param destFile destFile
     */
    public void upload(String localFile, String destFile) {
        // channel.getBulkRequests();
        // channel.getHome()
        // channel.getId()
        // channel.getServerVersion()
        // channel.lcd(path);
        // channel.isEOF()
        // channel.setBulkRequests(bulk_requests);
        // channel.start();
    	long start = System.currentTimeMillis();
    	ChannelSftp channel = null;
		try {
			channel = (ChannelSftp) session.openChannel(JSCHContstants.DEFAULT_PROTOCOL);
			channel.connect();
			System.out.println("[JSCH] channel连接耗时："+(System.currentTimeMillis() - start) / 1000 + "s");
		} catch (JSchException e) {
			System.out.println("[JSCH] channel连接出错，耗时："+(System.currentTimeMillis() - start) / 1000 + "s");
			e.printStackTrace();
			throw new RuntimeException("[JSCH] channel连接出错.", e);
		}
		start = System.currentTimeMillis();
        try {
            channel.put(localFile, destFile);
            System.out.println("[JSCH] 将'" + localFile + "‘上传至’" + destFile + "'耗时："
                + (System.currentTimeMillis() - start) / 1000 + "s");
        } catch (SftpException e) {
            System.out.println("[JSCH] 将'" + localFile + "‘上传至’" + destFile + "'失败,耗时："
                + (System.currentTimeMillis() - start) / 1000 + "s");
            throw new RuntimeException("[JSCH] 上传文件出错;" + "参数是localFile：" + localFile + ",destFile：" + destFile + ".",
                e);
        }finally{
        	if(channel!=null){
        		channel.disconnect();
        	}
        	channel = null;
        }
    }
    
    /**
     * 
     * upload
     * 
     * @param inputStream inputStream
     * @param destFile destFile
     */
    public void upload(InputStream inputStream, String destFile) {
        long start = System.currentTimeMillis();
        ChannelSftp channel = null;
		try {
			channel = (ChannelSftp) session.openChannel(JSCHContstants.DEFAULT_PROTOCOL);
			channel.connect();
			System.out.println("[JSCH] channel连接耗时："+(System.currentTimeMillis() - start) / 1000 + "s");
		} catch (JSchException e) {
			System.out.println("[JSCH] channel连接出错，耗时："+(System.currentTimeMillis() - start) / 1000 + "s");
			e.printStackTrace();
			throw new RuntimeException("[JSCH] channel连接出错.", e);
		}
		start = System.currentTimeMillis();
        try {
            channel.put(inputStream, destFile);
            System.out
                .println("[JSCH] 将文件上传至'" + destFile + "'耗时：" + (System.currentTimeMillis() - start) / 1000 + "s");
        } catch (SftpException e) {
            System.out.println("[JSCH] 将inputStream上传至'" + destFile + "'失败,耗时：" + (System.currentTimeMillis() - start)
                / 1000 + "s");
            throw new RuntimeException("[JSCH] 上传文件出错;参数是inputStream,destFile：" + destFile + "", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException("[JSCH] 关闭inputStream失败.", e);
                }
            }
        	if(channel!=null){
        		channel.disconnect();
        	}
        	channel = null;
        }
    }
    
    /**
     * 
     * upload <br/>
     * inputStream 没有关闭
     * 
     * @param inputStream inputStream
     * @param destFile destFile
     */
    public void uploadNotCloseStream(InputStream inputStream, String destFile) {
        long start = System.currentTimeMillis();
        
        ChannelSftp channel = null;
		try {
			channel = (ChannelSftp) session.openChannel(JSCHContstants.DEFAULT_PROTOCOL);
			channel.connect();
			System.out.println("[JSCH] channel连接耗时："+(System.currentTimeMillis() - start) / 1000 + "s");
		} catch (JSchException e) {
			System.out.println("[JSCH] channel连接出错，耗时："+(System.currentTimeMillis() - start) / 1000 + "s");
			e.printStackTrace();
			throw new RuntimeException("[JSCH] channel连接出错.", e);
		}
		start = System.currentTimeMillis();
        try {
            channel.put(inputStream, destFile);
            System.out
                .println("[JSCH] 将文件上传至'" + destFile + "'耗时：" + (System.currentTimeMillis() - start) / 1000 + "s");
        } catch (SftpException e) {
            System.out.println("[JSCH] 将inputStream上传至'" + destFile + "'失败,耗时：" + (System.currentTimeMillis() - start)
                / 1000 + "s");
            throw new RuntimeException("[JSCH] 上传文件出错;参数是inputStream,destFile：" + destFile + "", e);
        }finally{
        	if(channel!=null){
        		channel.disconnect();
        	}
        	channel = null;
        }
    }
    
    /**
     * 执行shell命令
     * 
     * @param cmd 命令
     * @param outputFileName 输出的文件
     */
    @SuppressWarnings("static-access")
	public void shell(String cmd,String outputFileName){
    	long start = System.currentTimeMillis();
    	
    	ChannelShell shell = null;
    	try {
    		shell = (ChannelShell) session.openChannel(JSCHContstants.SHELL_PROTOCOL);
		} catch (JSchException e) {
			System.out.println("[JSCH] channelShell连接出错，耗时："+(System.currentTimeMillis() - start) / 1000 + "s");
			e.printStackTrace();
			throw new RuntimeException("[JSCH] channelShell连接出错.", e);
		}
    	PipedInputStream pipeIn = new PipedInputStream();
    	PipedOutputStream pipeOut = null;
    	FileOutputStream  fileOut = null;
    	try {
    		pipeOut = new PipedOutputStream(pipeIn);
    		fileOut = new FileOutputStream(outputFileName);
			shell.setInputStream(pipeIn);
			shell.setOutputStream(fileOut);
			shell.connect(3000);
			
			pipeOut.write(cmd.getBytes());
			Thread.currentThread().sleep(10000);
			fileOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSchException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally{
			try {
				if(pipeOut != null){
					pipeOut.close();
					pipeIn.close();
				}
				if(fileOut!=null){
					fileOut.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if(shell!=null){
				shell.disconnect();
			}
		}
    }
    
    /**
     * isConnected
     * 
     * @return boolean
     */
    public boolean isConnected() {
        return  session.isConnected();
    }
}
