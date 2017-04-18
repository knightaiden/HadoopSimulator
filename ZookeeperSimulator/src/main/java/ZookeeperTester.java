import org.apache.zookeeper.*;

import java.io.IOException;

/**
 * Created by zhangzhe on 2017/4/17.
 */
public class ZookeeperTester {

    public ZooKeeper zk;

    public Watcher watcher;

    public ZookeeperTester() throws IOException {
        //initwatcher与Zookeeper instance
        watcher = (event) -> System.out.println("monitor event ："+ event.toString());
        zk= new ZooKeeper("localhost:2181", 60*60,this.watcher);
    }

    public void createZKInstance() throws KeeperException, InterruptedException {
        String data = "see you!";
        zk.create("/test", data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        String reslut = new String(zk.getData("/test", watcher, null));
        System.out.println(reslut);
        zk.delete("/test",-1);

    }

    //close the Zookeeper instance
    public void ZKclose() throws InterruptedException{
        zk.close();
    }

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        ZookeeperTester zookeeperTester = new ZookeeperTester();
        zookeeperTester.createZKInstance();
        zookeeperTester.ZKclose();

    }

}
