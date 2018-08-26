package com.thirdwayv.server.udp;

import java.nio.ByteBuffer;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thirdwayv.server.utils.ByteOperationsHandlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;


public class UDPReceiverHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    private final static Logger logger = LoggerFactory.getLogger(UDPReceiverHandler.class);

    private LinkedBlockingQueue<DatagramPacket> incomingPacketsQueue = null;
    
	private long llQueuedPacketCount = 0;
	private long llDroppedPacketCount = 0;

	public UDPReceiverHandler(LinkedBlockingQueue<DatagramPacket> incomingPacketsQueue) {
		this.incomingPacketsQueue = incomingPacketsQueue;
	}
	
	@Override
	public void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
		logger.info("channelRead0");
		byte[] recievedBytes = new byte[packet.content().readableBytes()];
		ByteBuf byteBuf = packet.content().duplicate();
		byteBuf.readBytes(recievedBytes);
		logger.info("recieved bytes in hex : " + ByteOperationsHandlers.bytesToHexString(recievedBytes));
		packet.retain();
		boolean ffIsQueued = incomingPacketsQueue.offer(packet);
		if (!ffIsQueued) {
			packet.release();  // Drop if queue is full
			llDroppedPacketCount += 1;
			logger.info("channelRead0 - packet dropped becuase the queue is full");
		} else {
			llQueuedPacketCount += 1;
		}
		
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		logger.info("channelReadComplete");
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    	logger.info("exceptionCaught");
    	logger.info(cause.getMessage());
	}

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    	logger.info("handlerAdded");
    	super.handlerAdded(ctx);
    }
    @Override
    public boolean acceptInboundMessage(Object msg) throws Exception {
    	logger.info("acceptInboundMessage");
    	return super.acceptInboundMessage(msg);
    }
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
    	logger.info("handlerRemoved");
    	super.handlerRemoved(ctx);
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    	logger.info("channelActive");
    	PacketStorer.ctx = ctx;
    	super.channelActive(ctx);
    }
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    	logger.info("channelInactive");
    	super.channelInactive(ctx);
    }
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
    	logger.info("channelRegistered");
    	super.channelRegistered(ctx);
    }
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
    	logger.info("channelUnregistered");
    	super.channelUnregistered(ctx);
    }
    
    
    public long getQueuedPacketCount() {
		return llQueuedPacketCount;
	}

	public long getDroppedPacketCount() {
		return llDroppedPacketCount;
	}
	
	
}
