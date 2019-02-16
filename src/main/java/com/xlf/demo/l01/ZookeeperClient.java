package com.xlf.demo.l01;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZookeeperClient {
    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws IOException {
        ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181", 5000, new ZookeeperConstructorUsageSimple());
        System.out.println(zooKeeper.getState());
        try {
            connectedSemaphore.await();
        } catch (InterruptedException e) {
        }

        System.out.println("Zookeeper session established.");
    }

    static class ZookeeperConstructorUsageSimple implements Watcher {


        @Override
        public void process(WatchedEvent watchedEvent) {
            System.out.println("Receive watched event: " + watchedEvent);
            if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
                connectedSemaphore.countDown();
            }
        }
    }
}
