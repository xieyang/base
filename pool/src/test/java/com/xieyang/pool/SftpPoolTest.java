/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 ******************************************************************************/

package com.xieyang.pool;

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
	@SuppressWarnings("javadoc")
	public void testUpload() {
		for (int i = 0; i < 5; i++) {
			SftpClient client = pool.getResource();
			client.upload("d:/iTestinyunceshi.zip",
					"/xieyang/hadoop_" + UUID.randomUUID() + ".zip");
			pool.returnResource(client);
			System.out.println("=============================");
		}
	}

	@Test
	public void testMultUpload() {
		Runnable running = new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 10; i++) {
					SftpClient client = pool.getResource();
					client.upload("d:/iTestinyunceshi.zip", "/xieyang/hadoop_"
							+ UUID.randomUUID() + ".zip");
					pool.returnResource(client);
				}
			}
		};

		for (int i = 0; i < 10; i++) {
			new Thread(running).start();
		}
	}

}
