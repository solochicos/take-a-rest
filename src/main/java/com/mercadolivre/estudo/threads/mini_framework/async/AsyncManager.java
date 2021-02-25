package com.mercadolivre.estudo.threads.mini_framework.async;

import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AsyncManager {

    private HashMap<AsyncPool, ExecutorService> threadsPools = new HashMap<>();
    int controllerThreadsSize;
    int workersThreadsSize;

    public AsyncManager(int controllerThreadsSize, int workersThreadsSize) {
        this.controllerThreadsSize = controllerThreadsSize;
        this.workersThreadsSize = workersThreadsSize;

        threadsPools.put(AsyncPool.REQUEST_HANDLING, Executors.newFixedThreadPool(controllerThreadsSize));
        threadsPools.put(AsyncPool.WORKERS, Executors.newFixedThreadPool(workersThreadsSize));
    }

    public Future<?> execute(Callable<?> callable) {
        synchronized (threadsPools) {
            return threadsPools.get(AsyncPool.WORKERS).submit(callable);
        }
    }

    public Future<?> execute(AsyncPool pool, Callable<?> callable) {
        synchronized (threadsPools) {
            return threadsPools.getOrDefault(pool, threadsPools.get(AsyncPool.WORKERS)).submit(callable);
        }
    }

    public void shutdown() {
        synchronized (threadsPools) {
            threadsPools.values().forEach(ExecutorService::shutdownNow);
        }
    }

    public ExecutorService getExecutorFor(AsyncPool pool) {
        synchronized (threadsPools) {
            return threadsPools.get(pool);
        }
    }

    public enum AsyncPool {
        REQUEST_HANDLING, WORKERS
    }

}
