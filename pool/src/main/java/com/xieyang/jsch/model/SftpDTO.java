/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 ******************************************************************************/

package com.xieyang.jsch.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * SFTP DTO
 * 
 * 
 * @author 谢阳
 * @since 1.0
 * @version 2015年12月15日 谢阳
 */
public class SftpDTO {
    
    /* 主机 */
    private String host;
    
    /* 端口 */
    private String port;
    
    /* 用户 */
    private String username;
    
    /* 密码 */
    private String password;
    
    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }
    
    /**
     * @param host the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }
    
    /**
     * @return the port
     */
    public String getPort() {
        return port;
    }
    
    /**
     * @param port the port to set
     */
    public void setPort(String port) {
        this.port = port;
    }
    
    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * @return
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
    
    /**
     * @param obj
     * @return
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        boolean flag = super.equals(obj);
        if (!flag) {
            flag = EqualsBuilder.reflectionEquals(this, obj);
        }
        return flag;
    }
    
    /**
     * @return
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
    
}
