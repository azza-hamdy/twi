package com.thirdwayv.server.tcp;

import java.util.Scanner;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class TCPServer {
	
//	    static final int PORT = Integer.parseInt(System.getProperty("port", "9004"));

	    public static void main(String[] args) throws Exception {
	        
	    	Scanner sc = new Scanner(System.in);
	    	
	    	System.out.println("please enter port number!");
	    	int tcpPort = sc.nextInt();
	    	
	    	
	        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
	        EventLoopGroup workerGroup = new NioEventLoopGroup();
	        try {
	            ServerBootstrap serverBootstrap = new ServerBootstrap();
	            serverBootstrap.group(bossGroup, workerGroup)
	             .channel(NioServerSocketChannel.class)
	             .handler(new LoggingHandler(LogLevel.INFO))
	             .childHandler(new TCPServerInitializer());

	            serverBootstrap.bind(tcpPort).sync().channel().closeFuture().sync();
	        } finally {
	            bossGroup.shutdownGracefully();
	            workerGroup.shutdownGracefully();
	        }
	    }

}
