/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 ******************************************************************************/

package com.xieyang.pool;

import java.util.Random;
import java.util.UUID;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.junit.Before;
import org.junit.Test;

import com.xieyang.jsch.model.SftpDTO;
import com.xieyang.jsch.utils.SftpClient;
import com.xieyang.jsch.utils.SftpPool;

/**
 * Test SftpPool
 * 
 * 
 * @author 谢阳
 * @since 1.0
 * @version 2015年12月21日 谢阳
 */
public class SftpPoolTest {

	private SftpPool pool = null;

	 @Before
	public void init() {
		SftpDTO dto = new SftpDTO();
		dto.setHost("10.10.5.85");
		dto.setUsername("root");
		dto.setPassword("ctp#88");
//		dto.setHost("192.168.148.130");
//		dto.setUsername("root");
//		dto.setPassword("root");
		dto.setPort(22);
		GenericObjectPool.Config config = new GenericObjectPool.Config();
		config.maxActive = 3;
		config.maxIdle = 3;
		config.maxWait = 60 * 1000;
		config.testOnBorrow = true;
		config.testOnReturn = true;
		pool = new SftpPool(dto, config);
	}
	 
	 @Test
	 public void testCopyFile(){
		 SftpClient client = pool.getResource();
//		 client.shell("cp /xieyang/t.tar.gz /xieyang/t1.tar.gz \n", "d:/xieyang.log");
		 client.shell("ls /xieyang \n", "d:/xieyang.log");
		 pool.removeResource(client);
	 }
	 
	 @Test
	 public void testExec(){
		 SftpClient client = pool.getResource();
//		 client.excute("ls /xieyang");
//		 client.excute("cp /xieyang/t.tar.gz /xieyang/t1.tar.gz");
		 client.excute("ls -lR /atmFile/gd");
		 pool.removeResource(client);
	 }

	 @Test
	@SuppressWarnings("javadoc")
	public void testUpload() throws InterruptedException {
		for (int i = 0; i < 5; i++) {
			long start = System.currentTimeMillis();
			SftpClient client = pool.getResource();
			System.out.println("[TEST] 从对象池获取对象耗时："+(System.currentTimeMillis()-start)/1000+"s");
			client.upload("d:/Hadoop.zip",
					"/xieyang/hadoop_" + UUID.randomUUID() + ".zip");
			pool.returnResource(client);
			System.out.println("[TEST] 主线程休息中");
			Thread.currentThread().sleep(60*1000);
			System.out.println("=============================");
		}
	}

	// @Test
	public void testMultUpload() {
		Runnable running = new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 10; i++) {
					SftpClient client = pool.getResource();
					client.upload("d:/Hadoop.zip", "/xieyang/hadoop_"
							+ UUID.randomUUID() + ".zip");
					pool.returnResource(client);
				}
			}
		};

		for (int i = 0; i < 10; i++) {
			new Thread(running).start();
		}
	}

	public static void main(String[] args) {
		final SftpDTO dto = new SftpDTO();
		dto.setHost("10.10.5.85");
		dto.setUsername("root");
		dto.setPassword("ctp#88");
//		dto.setHost("192.168.148.130");
//		dto.setUsername("root");
//		dto.setPassword("root");
		dto.setPort(22);
		GenericObjectPool.Config config = new GenericObjectPool.Config();
		config.maxActive = 500;
		config.maxIdle = 100;
//		config.maxWait = 60 * 1000;
		config.testOnBorrow = true;
		config.testOnReturn = true;
		final SftpPool pool1 = new SftpPool(dto, config);

		Runnable running = new Runnable() {
			@Override
			public void run() {
				while (true) {
					long start = System.currentTimeMillis();
					SftpClient client = pool1.getResource();
					System.out.println("[Thread] 从对象池获取对象耗时："+(System.currentTimeMillis()-start)/1000+"s");
					// SftpClient client = new SftpClient(dto);
					System.out.println("[Thread] "+Thread.currentThread().getName() + " 开始上传文件...");
					try {
						client.upload("E:/document/03_基于5层级用户的工作台创新体验设计和应用v1.0_20150419（论文）(V1.1).docx",
								"/xieyang/test_" + UUID.randomUUID() + ".docx");
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						pool1.returnResource(client);
						// client.close();
					}
					try {
//						int sleep  = new Random().nextInt(6000)+1;
						int sleep  = 600;
						System.out.println("[Thread]" +Thread.currentThread().getName()+"休息:"+sleep+"s");
						Thread.currentThread().sleep(sleep*1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
				}
				// System.out.println("[Thead] "+Thread.currentThread().getName()+" 上传文件结束.");
			}
		};

		for (int i = 0; i < 500; i++) {
			new Thread(running).start();
		}
		System.out.println("[main] 主线程结束.");
	}
}
