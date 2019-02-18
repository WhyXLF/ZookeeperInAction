package com.xlf.demo.l04;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ZookeeperClient {


    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    private static ZooKeeper zooKeeper = null;

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, new ZookeeperCreateApiSyncUsage());
        System.out.println(zooKeeper.getState());
        connectedSemaphore.await();

        String path = "/zk-book";

        zooKeeper.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        zooKeeper.create(path + "/c1", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

        List<String> childrenList = zooKeeper.getChildren(path, true);
        System.out.println(childrenList);

        zooKeeper.create(path + "/c2", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);


        Thread.sleep(Integer.MAX_VALUE);
    }

    static class ZookeeperCreateApiSyncUsage implements Watcher {

        @Override
        public void process(WatchedEvent watchedEvent) {
            System.out.println("Receive watched event: " + watchedEvent);
            if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
                if (Event.EventType.None == watchedEvent.getType() && null == watchedEvent.getPath()) {
                    connectedSemaphore.countDown();
                } else if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged) {
                    try {
                        System.out.println("ReGet Child:" + zooKeeper.getChildren(watchedEvent.getPath(), true));
                    } catch (KeeperException | InterruptedException e) {
                    }
                }
            }

        }
    }
}
