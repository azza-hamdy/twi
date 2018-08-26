package com.thirdwayv.server.tcp.message.codec;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class TwiMessageEncoder extends MessageToByteEncoder<byte[]> {

    @Override
    protected void encode(ChannelHandlerContext ctx, byte[] msg, ByteBuf out) {
    	System.out.println("encode");
    	// Write a message.
        out.writeByte((byte) 'F'); // magic number
        out.writeInt(msg.length);  // data length
        out.writeBytes(msg);      // data
    }
}