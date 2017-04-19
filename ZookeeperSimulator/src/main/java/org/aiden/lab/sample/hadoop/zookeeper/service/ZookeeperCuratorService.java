package org.aiden.lab.sample.hadoop.zookeeper.service;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;

/**
 * Created by zhangzhe on 2017/4/18.
 */
public class ZookeeperCuratorService {

    private CuratorFramework client;

    public CuratorFramework getClient() {
        return client;
    }

    public void setClient(CuratorFramework client) {
        this.client = client;
    }

    public void createNode(String path, byte[] data) throws Exception {
        client.create().forPath(path, data);
    }

    public void createNode(String path, byte[] data, CreateMode mode) throws Exception {
        client.create().withMode(mode).forPath(path, data);
    }

    public void setData(String path, byte[] data) throws Exception {
        client.setData().forPath(path, data);
    }

    public void delete(String path) throws Exception {
        client.delete().forPath(path);
    }

}
