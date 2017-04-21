package org.aiden.lab.sample.hadoop.zookeeper.lock;

import org.aiden.lab.sample.hadoop.zookeeper.sample.ZookeeperTester;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Created by zhangzhe on 2017/4/19.
 */
public class SimpleWriteLockTest {

    private ZookeeperTester zookeeperTester;
    private static final String lockKey = "/LockTest";

    @Before
    public void init() throws IOException {
        zookeeperTester = new ZookeeperTester();
    }

    @Test
    public void test() throws KeeperException, InterruptedException {
        ZooKeeper keeper = zookeeperTester.getZk();
        WriteLock leader = new WriteLock(keeper, lockKey, null);
        boolean result = leader.lock();
        System.out.println(result);
        assertEquals(true, result);
        WriteLock follower = new WriteLock(keeper, lockKey, null);
        boolean result2 = follower.lock();
        System.out.println(result2);
        assertEquals(false, result2);
        leader.unlock();
        boolean result3= follower.lock();
        System.out.println(result3);
        assertEquals(true, result3);
    }

}
