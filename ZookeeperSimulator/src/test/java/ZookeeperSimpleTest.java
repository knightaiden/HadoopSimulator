import org.aiden.lab.sample.hadoop.zookeeper.sample.ZookeeperTester;
import org.apache.zookeeper.KeeperException;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by zhangzhe on 2017/4/18.
 */
public class ZookeeperSimpleTest {

    @Test
    public void sampleTest() throws IOException, KeeperException, InterruptedException {
        ZookeeperTester zookeeperTester = new ZookeeperTester();
        zookeeperTester.createZKInstance();
        zookeeperTester.ZKclose();
    }

}
