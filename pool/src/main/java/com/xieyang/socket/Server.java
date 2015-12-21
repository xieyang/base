/******************************************************************************
 * Copyright (C) 2015 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、
 * 复制、修改或发布本软件.
 ******************************************************************************/

package com.xieyang.socket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Server
 * 
 * 
 * @author 谢阳
 * @since 1.0
 * @version 2015年12月21日 谢阳
 */
public class Server {
    
    /**
     * 
     * @param args args
     */
    public static void main(String[] args) {
        SocketAddress address = new InetSocketAddress("192.168.1.1", 7005);
        try {
            System.out.println("=========running.");
            ServerSocket server = new ServerSocket();
            server.bind(address);
            Socket socket = server.accept();
            new Thread(new T(socket)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class T implements Runnable {
    
    /** socket */
    private Socket socket = null;
    
    /**
     * 构造函数
     * 
     * @param socket socket
     */
    public T(Socket socket) {
        this.socket = socket;
    }
    
    /**
     * 
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        try {
            System.out.println("=========开始");
            System.out.println(socket.toString());
            socket.setKeepAlive(true);
            socket.setSoTimeout(5 * 1000);
            String _pattern = "yyyy-MM-dd HH:mm:ss";
            SimpleDateFormat format = new SimpleDateFormat(_pattern);
            while (true) {
                System.out.println("开始：" + format.format(new Date()));
                try {
                    InputStream ips = socket.getInputStream();
                    ByteArrayOutputStream bops = new ByteArrayOutputStream();
                    int data = -1;
                    while ((data = ips.read()) != -1) {
                        System.out.println(data);
                        bops.write(data);
                    }
                    System.out.println(Arrays.toString(bops.toByteArray()));
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Thread.sleep(1000);
                System.out.println(socket.isBound()); // 是否邦定
                System.out.println(socket.isClosed()); // 是否关闭
                System.out.println(socket.isConnected()); // 是否连接
                System.out.println(socket.isInputShutdown()); // 是否关闭输入流
                System.out.println(socket.isOutputShutdown()); // 是否关闭输出流
                System.out.println("结束：" + format.format(new Date()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
