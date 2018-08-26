package com.thirdwayv.server.udp;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.socket.DatagramPacket;

public class InitialReceiver {
    private final static Logger logger = LoggerFactory.getLogger(InitialReceiver.class);

    public static void main(String[] args) throws Exception {
    	ch.qos.logback.classic.Logger orgNettyLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("io.netty");
    	orgNettyLogger.setLevel(ch.qos.logback.classic.Level.ERROR);
    	
    	Scanner sc = new Scanner(System.in);
    	
    	System.out.println("please enter port number!");
    	UDPReceiver.iiIncomingPort = sc.nextInt();
    	
    	
    	
        LinkedBlockingQueue<DatagramPacket> incomingPacketsQueue = new LinkedBlockingQueue<DatagramPacket>(2048);
        
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        
        UDPReceiver udpReceiver = UDPReceiver.newInstance(incomingPacketsQueue);
        PacketStorer packetStorer = PacketStorer.newInstance(incomingPacketsQueue);
        
        Future<Void> udpReceiverFuture = executorService.submit(udpReceiver);
        Future<Void> packetStorerFuture = executorService.submit(packetStorer);
        
        logger.debug("press any key to exit");
        System.out.println("press any key to exit");
        System.in.read();
        
        packetStorer.shutdownGracefully();
        udpReceiver.shutdownGracefully();
        
        executorService.shutdown();

        logger.debug("System has shut down");        
    }

}
