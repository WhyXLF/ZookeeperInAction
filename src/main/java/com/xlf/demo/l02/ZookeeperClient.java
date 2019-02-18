package com.xlf.demo.l02;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZookeeperClient {


    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, InterruptedException {
        ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, new ZookeeperClient.ZookeeperConstructorUsageWithSidAndPassword());
        System.out.println(zooKeeper.getState());
        connectedSemaphore.await();


        long sessionId = zooKeeper.getSessionId();
        byte[] passwd = zooKeeper.getSessionPasswd();

        //use illegal sessionId and sessionPasswd
        zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, new ZookeeperClient.ZookeeperConstructorUsageWithSidAndPassword(), 1l, "test".getBytes());

        zooKeeper = new ZooKeeper("127.0.0.1:2181",
                5000,
                new ZookeeperConstructorUsageWithSidAndPassword(),
                sessionId,
                passwd);
        Thread.sleep(Integer.MAX_VALUE);


    }

    static class ZookeeperConstructorUsageWithSidAndPassword implements Watcher {

        @Override
        public void process(WatchedEvent watchedEvent) {
            System.out.println("Receive watched event: " + watchedEvent);
            if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
                connectedSemaphore.countDown();
            }

        }
    }
}
