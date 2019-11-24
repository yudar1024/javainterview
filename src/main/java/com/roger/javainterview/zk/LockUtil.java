package com.roger.javainterview.zk;

import java.io.IOException;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class LockUtil implements Watcher {
	volatile boolean connected = false;
	volatile boolean expired = false;
	ZooKeeper zk;

//	获得锁
	public LockUtil() {
		try {
			zk= new ZooKeeper("127.0.0.1:3000", 30000, this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public DisLock getLock(String lockid, long timeout) {
		return null;
	}

	public DisLock tryActiveLock(DisLock disLock) {
		return null;
	}

	@Override
	public void process(WatchedEvent e) {
		System.out.println(e);
		if (e.getType() == Event.EventType.None) {
			switch (e.getState()) {
			case SyncConnected:
				connected = true;
				break;
			case Disconnected:
				connected = false;
				break;
			case Expired:
				expired = true;
				connected = false;
				System.out.println("Exiting due to session expiration");
			default:
				break;
			}
		}
	}

}