package org.aiden.lab.sample.hadoop.zookeeper.lock;

import org.aiden.lab.sample.hadoop.zookeeper.queue.DistributedQueue;
import org.aiden.lab.sample.hadoop.zookeeper.sample.ZookeeperTester;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.queue.SimpleDistributedQueue;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.KeeperException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Created by zhangzhe on 2017/4/21.
 */
public class SimpleQueueTest {

    private ZookeeperTester zookeeperTester;
    private static final String queueDir = "/QueueTest";

    @Before
    public void init() throws IOException {
        zookeeperTester = new ZookeeperTester();
    }

    @Test
    public void testQueue() throws KeeperException, InterruptedException {
        DistributedQueue distributedQueue
                = new DistributedQueue(zookeeperTester.getZk(), queueDir, null);
        distributedQueue.offer("FirstData".getBytes());
        String firstResult = new String(distributedQueue.element());
        System.out.println(firstResult);
        assertEquals(firstResult, "FirstData");
        String polled = new String(distributedQueue.poll());
        System.out.println(polled);
        assertEquals(polled, "FirstData");
        byte[] fin = distributedQueue.peek();
        System.out.println(fin);
        assertEquals(fin, null);

    }

    @Test
    public void testCuratorQueue() throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
        client.start();
        SimpleDistributedQueue queue = new SimpleDistributedQueue(client,"/TestSimpleQueue");
        queue.offer("FirstData".getBytes());
        System.out.println(new String(queue.element()));
        System.out.println(new String(queue.poll()));
    }

}
