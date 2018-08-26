package com.thirdwayv.server.tcp;




import com.thirdwayv.server.tcp.message.codec.TwiMessageDecoder;
import com.thirdwayv.server.tcp.message.codec.TwiMessageEncoder;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.compression.ZlibCodecFactory;
import io.netty.handler.codec.compression.ZlibWrapper;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class TCPServerInitializer extends ChannelInitializer<SocketChannel> {


    public TCPServerInitializer() {
    }

    @Override
    public void initChannel(SocketChannel ch) {
    	 ch.config().setRecvByteBufAllocator(new FixedRecvByteBufAllocator(2048)); //set max read buffer size
    	 ch.config().setSendBufferSize(2048);			//set max write buffer size
         ChannelPipeline pipeline = ch.pipeline();
         pipeline.addLast("readTimeoutHandler", new ReadTimeoutHandler(60*2));

        // Enable stream compression (you can remove these two if unnecessary)
//        pipeline.addLast(ZlibCodecFactory.newZlibEncoder(ZlibWrapper.GZIP));
//        pipeline.addLast(ZlibCodecFactory.newZlibDecoder(ZlibWrapper.GZIP));

        // Add the number codec first,
//        pipeline.addLast(new TwiMessageDecoder());
//        pipeline.addLast(new TwiMessageEncoder());

        // and then business logic.
        // Please note we create a handler for every new channel
        // because it has stateful properties.
        pipeline.addLast(new TCPServerHandler());
    }
}
