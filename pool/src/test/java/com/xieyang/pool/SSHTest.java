package com.xieyang.pool;

import org.junit.Test;

import com.xieyang.jsch.common.ShellSSH;


/**
 * SSH Test
 * 
 * @author xieyang
 *
 */
public class SSHTest {

	@Test
	public void testSSH(){
		try {
			ShellSSH.sshShell("10.10.5.85", "root", "ctp#88", 22, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println("[main]");
		ShellSSH.sshShell("10.10.5.85", "root", "ctp#88", 22, null, null);
	}
}
