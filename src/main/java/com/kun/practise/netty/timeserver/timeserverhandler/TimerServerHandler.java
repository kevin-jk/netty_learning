package com.kun.practise.netty.timeserver.timeserverhandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;

/**
 * Created by jrjiakun on 2018/9/16
 *
 * 异步： 当一个异步过程调用发出后，调用者不能立刻得到结果。实际处理这个调用的部件在完成后，通过状态、通知和回调来通知调用者。异步的好处是不会造成阻塞，在高并发情形下会更稳定和更高的吞吐量。
 */
@ChannelHandler.Sharable
public class TimerServerHandler extends ChannelInboundHandlerAdapter{


    //channelActive() 方法将会在连接被建立并且准备进行通信时被调用。
    @Override
    public void channelActive(final  ChannelHandlerContext ctx) throws Exception {
        // 分配4个字节，32为
        final ByteBuf time = ctx.alloc().buffer(4);
        time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));
        final ChannelFuture f = ctx.writeAndFlush(time);
        f.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) {
                // 不能直接close， 因为是异步的，可能close了，writeAndFlush操作还没有完成
                assert f == future;
                ctx.close();
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
