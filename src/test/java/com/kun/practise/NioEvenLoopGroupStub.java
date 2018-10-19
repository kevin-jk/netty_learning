package com.kun.practise;

import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.MultithreadEventExecutorGroup;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

/**
 * Created by jrjiakun on 2018/10/11
 */
public class NioEvenLoopGroupStub extends MultithreadEventExecutorGroup {
    public NioEvenLoopGroupStub(int nThreads, ThreadFactory threadFactory, Object... args){
        super(nThreads,threadFactory,args);
    }
    protected EventExecutor newChild(Executor executor, Object... args) throws Exception {
        return null;
    }
}
