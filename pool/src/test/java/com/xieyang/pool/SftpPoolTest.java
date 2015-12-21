/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 ******************************************************************************/

package com.xieyang.pool;

import org.apache.commons.pool.impl.GenericObjectPool;
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
    
    @Test
    @SuppressWarnings("javadoc")
    public void testUpload() {
        SftpDTO dto = new SftpDTO();
        dto.setHost("192.168.1.112");
        dto.setUsername("root");
        dto.setPassword("root");
        dto.setPort(22);
        GenericObjectPool.Config config = new GenericObjectPool.Config();
        config.maxActive = 3;
        config.maxIdle = 1;
        config.maxWait = 60 * 1000;
        config.testOnBorrow = true;
        config.testOnReturn = true;
        SftpPool pool = new SftpPool(dto, config);
        
        SftpClient client = pool.getResource();
        client.upload("d:/xieyang.txt", "/atm/xieyang.txt");
        pool.returnResource(client);
    }
    
}
