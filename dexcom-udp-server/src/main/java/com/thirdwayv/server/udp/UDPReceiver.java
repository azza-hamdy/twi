package com.thirdwayv.server.udp;

import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class UDPReceiver implements Callable<Void> {
    private final static Logger logger = LoggerFactory.getLogger(UDPReceiver.class);
    
    public static int iiIncomingPort;
    
    private LinkedBlockingQueue<DatagramPacket> incomingPacketsQueue = null;
    private UDPReceiverHandler udpReceiverHandler = null;

    EventLoopGroup group = new NioEventLoopGroup();
    
    static public UDPReceiver newInstance(LinkedBlockingQueue<DatagramPacket> incomingPacketsQueue) {

    	UDPReceiver udpReceiver = new UDPReceiver();

    	udpReceiver.incomingPacketsQueue = incomingPacketsQueue;
    	udpReceiver.udpReceiverHandler = new UDPReceiverHandler(udpReceiver.incomingPacketsQueue);
    	return udpReceiver;
    }
    
	@Override
	public Void call() throws Exception { 
        try {
	    	Bootstrap bootstrap = new Bootstrap()
	    		    .group(group)
	    		    .channel(NioDatagramChannel.class)
	    		    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
//	    		    .option(ChannelOption.SO_REUSEPORT, true)
	    		    .handler(udpReceiverHandler);

	    	logger.info("Beginning bind");
	    	ChannelFuture future = bootstrap.bind(iiIncomingPort).sync().channel().closeFuture().await();
	    	logger.info("Exiting bind");
	    	
	    	logger.info("Queued  packet count:"+udpReceiverHandler.getQueuedPacketCount());
	    	logger.info("Dropped packet count:"+udpReceiverHandler.getDroppedPacketCount());

	    	group.shutdownGracefully();
        } finally {
        	group.shutdownGracefully();
        }
        
        return null;
    }

	public void shutdownGracefully() {
		group.shutdownGracefully();
	}
}
