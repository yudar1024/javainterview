package com.roger.javainterview.concurrence;

import org.openjdk.jol.info.ClassLayout;

import java.util.Arrays;

public class PrintJavaClassStructure {

	private static LockObject lockObject= new LockObject();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		展示加锁前的 对象头
		System.out.println(ClassLayout.parseInstance(lockObject).toPrintable());
		synchronized (lockObject){
			System.out.println("args = " + Arrays.deepToString(args));
		}
//		展示加锁后的对象头
		System.out.println(ClassLayout.parseInstance(lockObject).toPrintable());
	}

}
