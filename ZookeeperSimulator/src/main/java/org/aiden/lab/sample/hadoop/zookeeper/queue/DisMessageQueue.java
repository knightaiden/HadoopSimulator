package org.aiden.lab.sample.hadoop.zookeeper.queue;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.queue.DistributedQueue;
import org.apache.curator.framework.recipes.queue.QueueBuilder;
import org.apache.curator.framework.recipes.queue.QueueConsumer;
import org.apache.curator.framework.recipes.queue.QueueSerializer;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * Created by zhangzhe on 2017/4/21.
 */
public class DisMessageQueue {

    public static void main(String[] args) throws Exception {

        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
        client.start();

        QueueConsumer<String> consumer = new QueueConsumer<String>() {
            @Override
            public void consumeMessage(String message) throws Exception {
                //线程等待5秒，模拟数据处理，以达到数据抢夺的目的
                Thread.sleep(5000);
                //打印出是哪个线程处理了哪些数据
                System.out.println(Thread.currentThread().getId() +  " consume: " + message);
            }

            @Override
            public void stateChanged(CuratorFramework client, ConnectionState newState) {
                System.out.println("new state: " + newState);
            }
        };

        QueueSerializer serializer =  new QueueSerializer<String>() {
            @Override
            public byte[] serialize(String item) {
                return item.getBytes();
            }

            @Override
            public String deserialize(byte[] bytes) {
                return new String(new String(bytes));
            }
        };

        DistributedQueue<String> queue = QueueBuilder.builder(
                client,consumer,serializer,"/testQueue"
                ).buildQueue();

        queue.start();

        queue.put("1");

    }

}
