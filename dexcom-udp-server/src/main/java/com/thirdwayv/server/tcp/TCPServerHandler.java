package com.thirdwayv.server.tcp;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thirdwayv.server.packet.CTwiPacket;
import com.thirdwayv.server.utils.ByteOperationsHandlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

public class TCPServerHandler extends ChannelInboundHandlerAdapter{//SimpleChannelInboundHandler<byte[]> {

	PacketStorer tcpPacketStorer = new PacketStorer();
	private final static Logger logger = LoggerFactory.getLogger(TCPServerHandler.class);

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerAdded");
    	super.handlerAdded(ctx);
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)throws Exception {//byte[] msg) throws Exception {
    	System.out.println("channelRead");
//        System.out.println("server recieved message " + msg.length);
        ByteBuf buf = (ByteBuf) msg;
		byte[] receivedData = null;
		if (buf != null) {
//			logger.debug("-----------------------------Communication Server received message on (IP:"
//					+ address.getHostString() + ", Port:" + address.getPort() + ")--------------------------------");
			int length = buf.readableBytes();

			if (buf.hasArray()) { // convert received bufBytes to byte array
				receivedData = buf.array();
			} else {
				receivedData = new byte[length	];
				buf.getBytes(buf.readerIndex(), receivedData);
			}
	    	logger.debug("recieved data in hex : "+ByteOperationsHandlers.bytesToHexString(receivedData));
			CTwiPacket receivedPacket = parsePacket(receivedData);
			tcpPacketStorer.call(receivedPacket);
		}    	
    }
    
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
   	 	System.out.println("channelReadComplete");
    	ctx.flush();
    	//super.channelReadComplete(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    	System.out.println("channelInactive");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    	System.out.println("exceptionCaught");
        cause.printStackTrace();
        ctx.close();
    }
    
    @Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
    	System.out.println("channelRegistered");
		super.channelRegistered(ctx);
	}
	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channelUnregistered");
		super.channelUnregistered(ctx);
	}
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channelActive");	
		tcpPacketStorer.setCtx(ctx);
		super.channelActive(ctx);
	}
	private CTwiPacket parsePacket(byte[] payload) {
//		logger.info("Packet bytes to parse : " + ByteOperationsHandlers.bytesToHexString(recievedBytes));
		CTwiPacket receivedPacket = new CTwiPacket();
		receivedPacket = CTwiPacket.fromBytes(payload, false);
		return receivedPacket;

	}
}