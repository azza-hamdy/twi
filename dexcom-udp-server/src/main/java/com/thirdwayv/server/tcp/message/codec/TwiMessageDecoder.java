package com.thirdwayv.server.tcp.message.codec;


import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;

public class TwiMessageDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
    	
    	System.out.println("decode");
        // Wait until the length prefix is available.
        if (in.readableBytes() < 5) {
            return;
        }

        in.markReaderIndex();

        // Check the magic number.
        int magicNumber = in.readUnsignedByte();
        if (magicNumber != 'F') {
            in.resetReaderIndex();
            throw new CorruptedFrameException("Invalid magic number: " + magicNumber);
        }

        // Wait until the whole data is available.
        int dataLength = in.readInt();
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
            return;
        }

        byte[] decoded = new byte[dataLength];
        in.readBytes(decoded);

        out.add(decoded);
    }
}