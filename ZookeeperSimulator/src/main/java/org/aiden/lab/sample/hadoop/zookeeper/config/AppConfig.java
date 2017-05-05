package org.aiden.lab.sample.hadoop.zookeeper.config;

import org.aiden.lab.sample.hadoop.zookeeper.service.ZookeeperCuratorService;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by zhangzhe on 2017/4/19.
 */
@Configuration
public class AppConfig {

    @Bean
    public CuratorFramework curatorFramework() {
        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
        client.start();
        return client;
    }

    @Bean
    public ZookeeperCuratorService zookeeperCuratorService(CuratorFramework curatorFramework){
        ZookeeperCuratorService service = new ZookeeperCuratorService();
        service.setClient(curatorFramework);
        return service;
    }

}
