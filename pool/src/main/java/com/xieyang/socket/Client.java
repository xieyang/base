/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 ******************************************************************************/

package com.xieyang.socket;

import java.net.Socket;

/**
 * client
 * 
 * 
 * @author 谢阳
 * @since 1.0
 * @version 2015年12月21日 谢阳
 */
public class Client {
    
    /**
     * client
     * 
     * @param args args
     */
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("192.168.1.1", 7005);
            socket.setKeepAlive(true);
            while (true && null != socket) {
                Thread.sleep(10 * 1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
