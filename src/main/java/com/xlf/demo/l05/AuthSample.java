package com.xlf.demo.l05;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

public class AuthSample {
    final static String PATH = "/zk-book-auth_test";

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        ZooKeeper zooKeeper = new ZooKeeper("127.0.0.1:2181", 50000, null);
        zooKeeper.addAuthInfo("digest", "foo:true".getBytes());
        zooKeeper.create(PATH, "init".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL);
//        Thread.sleep(Integer.MAX_VALUE);

        ZooKeeper zooKeeper2 = new ZooKeeper("127.0.0.1:2181", 50000, null);
        byte[] data = zooKeeper2.getData(PATH, false, null);
        System.out.println(data);
    }

}
