package cn.sessiontech.xcx.utils;

import jodd.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 * @classname ThreadUtils
 * @description 线程池
 * @date 2019/5/23 14:42
 */
public class ThreadUtils {
    /**
     * 日志线程池
     */
    private static final  ExecutorService logThreadPool = new ThreadPoolExecutor(10, 20, 3L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1024),new ThreadFactoryBuilder().setNameFormat("log-pool-%d").get(),
            new ThreadPoolExecutor.AbortPolicy());
    /**
     * 其他部分线程池
     */
    private static final ExecutorService otherThreadPool =new ThreadPoolExecutor(3, 5, 3L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1024),new ThreadFactoryBuilder().setNameFormat("other-pool-%d").get(),
            new ThreadPoolExecutor.AbortPolicy());

    public static ExecutorService getLogThreadPool(){
        return logThreadPool;
    }


    public static final ExecutorService getOhterThreadPool(){
        return otherThreadPool;
    }
}
