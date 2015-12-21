/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 ******************************************************************************/

package com.xieyang.jsch.utils;

import org.apache.commons.pool.impl.GenericObjectPool;

import com.xieyang.jsch.common.AbstractPool;
import com.xieyang.jsch.model.SftpDTO;

/**
 * SFTP Pool
 * 
 * 
 * @author 谢阳
 * @since 1.0
 * @version 2015年12月16日 谢阳
 */
public class SftpPool extends AbstractPool<SftpClient> {
    
    /**
     * 构造函数
     * 
     * @param SftpDTO sftpDTO
     * @param config config
     */
    public SftpPool(SftpDTO sftpDTO, GenericObjectPool.Config config) {
        super(new SftpPoolableObjectFactory(sftpDTO), config);
    }
}
