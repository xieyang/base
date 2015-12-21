/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 ******************************************************************************/

package com.xieyang.jsch.common;

import java.io.Closeable;

import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;

/**
 * 对象池
 * 
 * <pre>
 *  有时间研究一下pool.evict()方法有什么特殊的作用
 * </pre>
 * 
 * @author 谢阳
 * @since 1.0
 * @version 2015年12月15日 谢阳
 */
/**
 * @author xieyang
 * 
 * @param <T>
 */
public abstract class AbstractPool<T> implements Closeable {
    
    /* pool */
    private GenericObjectPool pool;
    
    /**
     * 构造函数
     */
    public AbstractPool() {
    }
    
    /**
     * 构造函数
     * 
     * @param factory factory
     * @param config config
     */
    public AbstractPool(PoolableObjectFactory factory, GenericObjectPool.Config config) {
        pool = new GenericObjectPool(factory, config);
    }
    
    /**
     * 
     * 初始化
     * 
     * @param factory factory
     * @param config config
     */
    public void init(PoolableObjectFactory factory, GenericObjectPool.Config config) {
        if (pool != null) {
            this.close();
        }
        pool = new GenericObjectPool(factory, config);
    }
    
    /**
     * 
     * 从pool中获取对象
     * 
     * @return
     */
    public T getResource() {
        try {
            return (T) pool.borrowObject();
        } catch (Exception e) {
            throw new RuntimeException("[POOL] 从pool中获取对象出错.", e);
        }
    }
    
    /**
     * 
     * 归还对象
     * 
     * @param resource
     */
    public void returnResource(T resource) {
        if (resource == null) {
            return;
        }
        try {
            pool.returnObject(resource);
        } catch (Exception e) {
            throw new RuntimeException("[POOL] 归还对象至pool时出错.", e);
        }
    }
    
    /**
     * 
     * 在pool中实例化count个对象
     * 
     * @param count
     */
    public void addResource(int count) {
        for (int i = 0; i < count; i++) {
            try {
                this.pool.addObject();
            } catch (Exception e) {
                throw new RuntimeException("[POOL] pool实例化对象出错.", e);
            }
        }
    }
    
    /**
     * 
     * 从pool中移除对象
     * 
     * @param resource
     */
    public void removeResource(T resource) {
        if (resource == null) {
            return;
        }
        try {
            pool.invalidateObject(resource);
        } catch (Exception e) {
            throw new RuntimeException("[POOL] pool移除对象出错.", e);
        }
    }
    
    /**
     * 
     * 关闭pool
     * 
     */
    @Override
    public void close() {
        pool.clear();
        try {
            pool.close();
        } catch (Exception e) {
            throw new RuntimeException("[POOL] 关闭pool出错.", e);
        } finally {
            pool = null;
        }
    }
    
    /**
     * 
     * 获取在正在被使用的对象个数
     * 
     * @return
     */
    public int getNumActive() {
        if (pool == null) {
            return -1;
        }
        return pool.getNumActive();
    }
    
    /**
     * 
     * 获取对象池中闲置对象个数
     * 
     * @return
     */
    public int getNumIdle() {
        if (pool == null) {
            return -1;
        }
        return pool.getNumIdle();
    }
    
    /**
     * 
     * 从对象池中获取对象的最长等待时长
     * 
     * @return
     */
    public long getMaxWait() {
        return pool.getMaxWait();
    }
}
