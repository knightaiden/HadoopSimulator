package org.aiden.lab.sample.hadoop.zookeeper.sample;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhangzhe on 2017/4/19.
 */
public class CuratorLockTestTask {

    private final InterProcessMutex lock;
    private final String clientName;

    public CuratorLockTestTask(CuratorFramework client, String lockPath, String clientName) {
        this.lock = new InterProcessMutex(client, lockPath);
        this.clientName = clientName;
    }

    public void execute(long time, TimeUnit unit) throws Exception {
        if (!lock.acquire(time, unit)) {
            throw new IllegalStateException(clientName + " 不能得到互斥锁");
        }
        try {
            System.out.println(clientName + " 已获取到互斥锁");
            Thread.sleep(1000 * 1);
        } finally {
            System.out.println(clientName + " 释放互斥锁");
            lock.release(); // 总是在finally中释放
        }
    }

    private static final int QTY = 5;
    private static final int REPETITIONS = QTY * 10;
    private static final String PATH = "/examples/locks";

    public static void main(String[] args) throws Exception{
        List<CuratorFramework> clientList = new ArrayList();
        for (int i = 0; i < QTY; i++) {
            CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
            client.start();
            clientList.add(client);
        }
        System.out.println("连接初始化完成！");
        ExecutorService service = Executors.newFixedThreadPool(QTY);
        for (int i = 0; i < QTY; ++i) {
            int index = i;
            Callable<Void> task = () -> {
                try {
                   CuratorLockTestTask example = new CuratorLockTestTask(clientList.get(index), PATH, "Client " + index);
                    for (int j = 0; j < REPETITIONS; ++j) {
                        example.execute(10, TimeUnit.SECONDS);
                    }
                }
                catch (Throwable e) {
                    e.printStackTrace();
                }
                finally {
                    CloseableUtils.closeQuietly(clientList.get(index));
                }
                return null;
            };
            service.submit(task);
        }
        service.shutdown();
        service.awaitTermination(10, TimeUnit.MINUTES);
        System.out.println("Finished!");
    }
}
