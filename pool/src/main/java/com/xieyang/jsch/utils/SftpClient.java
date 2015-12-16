package com.xieyang.jsch.utils;

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

	public SftpClient(SftpDTO sftpDTO){
		//传入参数校验
		 if(sftpDTO==null){
			 throw new RuntimeException("[JSCH] 初始化jsch客户端错误,传入的SFTP服务器配置为null.");
		 }
		 if(sftpDTO.getHost()==null || "".equals(sftpDTO.getHost())){
			 throw new RuntimeException("[JSCH] 初始化jsch客户端错误,SFTP服务器的host配置为空.");
		 }
		 //如果通过username和password登录SFTP的话，做如下判断
		 if(sftpDTO.getUsername()==null || "".equals(sftpDTO.getUsername())){
			 throw new RuntimeException("[JSCH] 初始化jsch客户端错误,SFTP服务器的username配置为空.");
		 }
		 if(sftpDTO.getPassword()==null || "".equals(sftpDTO.getPassword())){
			 throw new RuntimeException("[JSCH] 初始化jsch客户端错误,SFTP服务器的password配置为空.");
		 }
		 
		 int iPort = (sftpDTO.getPort()==0)? JSCHContstants.DEFAULT_PORT:sftpDTO.getPort();
		JSch client = new JSch();
		Session session = null;
		try {
			session = client.getSession(sftpDTO.getUsername(), sftpDTO.getHost(), iPort);
		} catch (JSchException e) {
			
		}
		session.setTimeout(timeout);
		session.connect(connectTimeout);
		
	}
}
