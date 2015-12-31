/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 ******************************************************************************/

package com.xieyang.jsch.utils;

import org.apache.commons.pool.BasePoolableObjectFactory;

import com.xieyang.jsch.model.SftpDTO;

/**
 * poolableObject factory
 * 
 * 
 * @author 谢阳
 * @since 1.0
 * @version 2015年12月21日 谢阳
 */
public class SftpPoolableObjectFactory extends BasePoolableObjectFactory {
    
    /** SFTP */
    private SftpDTO sftpDTO = null;
    
    /**
     * 构造函数
     * 
     * @param dto
     */
    public SftpPoolableObjectFactory(SftpDTO dto) {
        this.sftpDTO = dto;
    }
    
    /**
     * @return SftpClient
     * @throws Exception Exception
     * @see org.apache.commons.pool.BasePoolableObjectFactory#makeObject()
     */
    @Override
    public Object makeObject() throws Exception {
    	System.out.println("[POOL] 对象池创建对象.");
        return new SftpClient(sftpDTO);
    }
    
    /**
     * @param obj SftpClient
     * @throws Exception Exception
     * @see org.apache.commons.pool.BasePoolableObjectFactory#destroyObject(java.lang.Object)
     */
    @Override
    public void destroyObject(Object obj) throws Exception {
    	System.out.println("[POOL] 对象池销毁对象.");
    	if(obj!=null){
    		SftpClient client = (SftpClient) obj;
            client.close();
    	}
    }
    
    /**
     * @param obj SftpClient
     * @return boolean
     * @see org.apache.commons.pool.BasePoolableObjectFactory#validateObject(java.lang.Object)
     */
    @Override
    public boolean validateObject(Object obj) {
    	boolean flag = false;
    	if(obj!=null){
    		SftpClient client = (SftpClient) obj;
    		flag = client.isConnected();
    	}
    	System.out.println("[POOL] 对象池校验对象是否可连接，结果："+flag+".");
    	return flag;
    }
    
    /**
     * @param obj
     * @throws Exception
     * @see org.apache.commons.pool.BasePoolableObjectFactory#activateObject(java.lang.Object)
     */
    @Override
    public void activateObject(Object obj) throws Exception {
    	System.out.println("[POOL] 对象池激活对象.");
        SftpClient client = (SftpClient) obj;
        client.connect();
    }
    
    /**
     * @param obj
     * @throws Exception
     * @see org.apache.commons.pool.BasePoolableObjectFactory#passivateObject(java.lang.Object)
     */
    @Override
    public void passivateObject(Object obj) throws Exception {
    	System.out.println("[POOL] 对象池钝化对象.");
//        SftpClient client = (SftpClient) obj;
//        client.closeChannel();
//        client.close();
    }
    
}
