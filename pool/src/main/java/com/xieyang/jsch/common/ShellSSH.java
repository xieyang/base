package com.xieyang.jsch.common;

import java.io.InputStream;
import java.io.OutputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 * 通过JSCH执行SSH命令
 * 
 * @author 谢阳
 * @since 1.0
 * @version 2015年12月15日 谢阳
 */
public class ShellSSH {

	/**
	 * 利用JSch包实现远程主机SHELL命令执行  
	 *@param ip 主机IP  
	 *@param user 主机登陆用户名  
	 *@param psw  主机登陆密码  
	 *@param port 主机ssh2登陆端口，如果取默认值，传-1  
	 *@param privateKey 密钥文件路径  
	 *@param passphrase 密钥的密码  
	 */
	public static void sshShell(String ip, String user, String psw, int port,
			String privateKey, String passphrase) throws Exception {
		Session session = null;
		ChannelShell channel = null;
		JSch jsch = new JSch();
		// 设置密钥和密码
		if (privateKey != null && !"".equals(privateKey)) {
			if (passphrase != null && "".equals(passphrase)) {// 设置带口令的密钥
				jsch.addIdentity(privateKey, passphrase);
			} else {
				// 设置不带口令的密钥
				jsch.addIdentity(privateKey);
			}
		}

		if (port <= 0) {
			// 连接服务器，采用默认端口
			session = jsch.getSession(user, ip);
		} else {
			// 采用指定的端口连接服务器
			session = jsch.getSession(user, ip, port);
		}
		// 如果服务器连接不上，则抛出异常
		if (session == null) {
			throw new Exception("session is null");
		}
		// 设置登陆主机的密码
		session.setPassword(psw);// 设置密码  
		// 设置第一次登陆的时候提示，可选值：(ask | yes | no)
		session.setConfig("StrictHostKeyChecking", "no");// 设置登陆超时时间  
		session.connect(30000);
		try {// 创建sftp通信通道
			channel = (ChannelShell) session.openChannel("shell");
			channel.connect(1000);// 获取输入流和输出流
			InputStream instream = channel.getInputStream();
			OutputStream outstream = channel.getOutputStream();
			// 发送需要执行的SHELL命令，需要用\n结尾，表示回车
			String shellCommand = "ls /xieyang \n";
			outstream.write(shellCommand.getBytes());
//			outstream.flush();
			Thread.currentThread().sleep(1000);
			System.out.println("[SSH] instream:"+instream.available());
			// 获取命令执行的结果
			if (instream.available() > 0) {
				byte[] data = new byte[instream.available()];
				int nLen = instream.read(data);
				if (nLen < 0) {
					throw new Exception("network error.");
				}
				// 转换输出结果并打印出来
				String temp = new String(data, 0, nLen, "iso8859-1");
				System.out.println(temp);
			}
			outstream.close();
			instream.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.disconnect();
			channel.disconnect();
		}
	}
}
