package com.example.gh.rpc.demo.core;


import com.example.gh.rpc.demo.util.Constant;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.InitializingBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

import lombok.extern.slf4j.Slf4j;

/**
 * @author : gaohui
 * @Date : 2019/3/18 22:53
 * @Description :
 */
@Slf4j
//@Component
public class ServiceDiscovery implements InitializingBean {

    private CountDownLatch latch = new CountDownLatch(1);

    private volatile List<String> dataList = new ArrayList<>();

    //    @Value("${zookeeper.address}")
    private String registryAddress;

    public ServiceDiscovery(String zkAddress) {
        registryAddress = zkAddress;
        log.info("服务发现初始化");
    }

    private void watchNode(final ZooKeeper zk) {
        log.info("watchNode");
        try {
            List<String> nodeList = zk.getChildren(Constant.RPC_ROOT_PATH, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event.getType() == Event.EventType.NodeChildrenChanged) {
                        log.info("消费者观察节点 watchNode");
                        watchNode(zk);
                    }
                }
            });

            log.info("watchNode nodeList = " + nodeList);
            ArrayList<String> list = new ArrayList<>();
            for (String node : nodeList) {
                byte[] data = zk.getData(Constant.RPC_ROOT_PATH + "/" + node, false, null);
                list.add(new String(data));
                System.out.println("观察 zk , 发现服务 address = " + new String(data));
            }
            this.dataList = list;


        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    private ZooKeeper connectServer() {
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(registryAddress, Constant.SESSION_TIME_OUT, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event.getState() == Event.KeeperState.SyncConnected) {
                        latch.countDown();
                    }
                }
            });
            latch.await();
        } catch (IOException | InterruptedException e) {
            log.error("", e);
        }
        log.info("消费者连接 zk 成功");
        return zk;
    }


    //    private ZooKeeper connectServer() {
    //        ZooKeeper zk = null;
    //
    //        try {
    //            zk = new ZooKeeper(registryAddress, 5000, new Watcher() {
    //                @Override
    //                public void process(WatchedEvent event) {
    //                    if (event.getState() == Event.KeeperState.SyncConnected) {
    //                        log.info("连接 zookeeper 成功");
    //                        latch.countDown();
    //                    }
    //                }
    //            });
    //            latch.await();
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //        }
    //
    //        return zk;
    //    }


    public String discover() {
        String data = null;
        int size = dataList.size();
        if (size > 0) {
            if (size == 1) {
                data = dataList.get(0);
            } else {
                data = dataList.get(ThreadLocalRandom.current().nextInt(size)
                );

            }
        }
        log.info("发现了服务 serverAddress = " + data);
        return data;

    }

    @Override
    public void afterPropertiesSet() throws Exception {

        ZooKeeper zk = connectServer();
        if (zk != null) {
            watchNode(zk);
        }

    }
}
































































































