package com.kun.practise.netty;

import com.kun.practise.netty.timeserver.timeserverhandler.TimerServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 *
 *
 * /**
 * Created by jrjiakun on 2018/9/13
 *
 *
 * step1. 定义Handler
 * step2.  引导服务
 *          创建ServerBootStrap实例以引导和绑定服务器
 *          创建并分配一个NioEventLoopGroup实例以进行事件处理，如接受新的连接及读写数据
 *          指定服务器绑定本地的InetSocketAddress
 *          使用定义的Handler初始化一个新的Channel
 *          调用ServerBootStrap.bind()绑定服务器
 *
 *
 * ==========================网络抽象组件============================
 * Channel:
 *         对应于java Socket, 主要是IO操作
 Channel生命周期： ChannelRegistered[注册到EventLoop], ChannelActive, ChannelInactive[没有连接到远程节点], ChannelUnregistered
 *
 *
 * EventLoop:
 *         处理连接的生命周期所发生的事情
 *
 *         EventLoopGroup 包含 EventLoop
 *         一个EventLoop 绑定一个Thread, 所有的IO事件都由改Thread处理
 *         一个Channel在生命周期内只注册一个EventLoop
 *         一个EventLoop可以被分配给多个Channel
 *
 *
 * ChannelFuture:
 *
 *          Netty中所有的IO都是异步的，ChannelFuture可以注册一个ChannelFutureListener以便在某个操作完成时候得到通知
 *
 *
 *
 *
 *==========================数据流的处理，应用程序处理逻辑组件============================
 *
 * ChannelHandler:
 *          处理入站和出站数据的应用程序的逻辑
 *         常用的功能如：
 *              数据格式的转化
 *              提供异常的通知
 *              提供Channel注册到EventLoop货从EventLoop注销时候的通知
 *          生命周期： handlerAdded, handlerRemoved, exceptionCaught
 * ChannelPipeline:
 *
 *          组装ChannelHandler的容器，当Channel被创建的时候，会呗自动的分配到它专属的ChannelPipeline
 */

public class ServerBootStrap {
    private int port;

    public ServerBootStrap(int port) {
        this.port = port;
    }

    public void start(final ChannelInboundHandlerAdapter channelInboundHandlerAdapter) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap sb = new ServerBootstrap();
            // 指定NIO传输channel
            sb.group(group).channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    // 当一个新的连接被接受时候，一个新的子channel将被 创建，而ChannelInitializer将会把handler添加到改Channel的pipline中
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //在ChannelPipeline中安装自定义的Channel, 将Handler添加到pipline
                            socketChannel.pipeline().addLast(channelInboundHandlerAdapter);
                        }
                    });
            //异步地绑定服务器，调用sync方法阻塞等待直到绑定完成
            ChannelFuture f = sb.bind().sync();
            // 获取channel的closeFuture并阻塞当前线程直到它完成
            f.channel().closeFuture().sync();
        } catch (Exception e) {

        } finally {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception {
        ChannelInboundHandlerAdapter handler = new TimerServerHandler();
        int port = 9000;
        ServerBootStrap echoServer = new ServerBootStrap(port);
        echoServer.start(handler);
    }
}
