package com.kun.practise.netty;

import com.kun.practise.netty.timeserver.timeClient.TimeClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * Created by jrjiakun on 2018/9/19
 */
public class ClientBootStrap {
    private final String host;
    private final int port;

    public ClientBootStrap(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start(final ChannelHandler handler) throws Exception {
        try {
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            Bootstrap bootstrap = new Bootstrap().group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //此处可以添加出站或者入站的handler,netty在处理的时候，数据流只会被同一个方向的Handler
                            // 当ChannelHandler被分配到ChannelPipeline的时候，会被分配一个ChannelHandlerContext,其主要用来写出站数据
                            socketChannel.pipeline().addLast(handler);
                        }
                    });
            // 阻塞，直到等待连接完成
            ChannelFuture channelFuture = bootstrap.connect().sync();
            // 阻塞，知道channel关闭
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {

        } finally {

        }
    }

    public static void main(String[] args) throws Exception {
        String host = "127.0.0.1";
        int port = 9000;
        TimeClientHandler echoClientHandler = new TimeClientHandler();
        new ClientBootStrap(host, port).start(echoClientHandler);
    }
}
