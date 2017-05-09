package com.miuhouse.zxcommunity.manager;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by khb on 2015/8/20.
 */
public class ThreadManager {
    private static ThreadPoolProxy poolProxy;
    private static Object object = new Object();

    //获取线程池的代理对象
    public static ThreadPoolProxy getPoolProxy(){
        synchronized (object) {
            if(poolProxy == null){
                poolProxy = new ThreadPoolProxy(5, 5, 5L);
            }
            return poolProxy;
        }
    }

    public static class ThreadPoolProxy{
        private int corePoolSize;
        private int maximumPoolSize;
        private long keepAliveTime;
        private ThreadPoolExecutor threadPoolExecutor;

        public ThreadPoolProxy(int corePoolSize,int maximumPoolSize,long keepAliveTime) {
            this.corePoolSize = corePoolSize;
            this.maximumPoolSize = maximumPoolSize;
            this.keepAliveTime = keepAliveTime;
        }

        public void execute(Runnable runnable){
            if(runnable == null){
                return ;
            }else{
                if(threadPoolExecutor == null || threadPoolExecutor.isShutdown()){
                    threadPoolExecutor = new ThreadPoolExecutor(
                            //核心线程数
                            corePoolSize,
                            //最大线程数
                            maximumPoolSize,
                            //线程不干活在池里面存活的事件
                            keepAliveTime,
                            TimeUnit.MILLISECONDS,
                            //任务多的时候排队的队列
                            new LinkedBlockingQueue<Runnable>(),
                            //获取线程工程的方法
                            Executors.defaultThreadFactory(),
                            //异常处理类
                            new ThreadPoolExecutor.AbortPolicy());
                }
                //执行任务
                threadPoolExecutor.execute(runnable);
            }
        }
    }
}
