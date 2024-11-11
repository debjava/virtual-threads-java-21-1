package com.ddlab.rnd;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class TestBasicVirtualThread1 {
	
	public void m1(int timeInSec) {
		System.out.println("Executing m1() ....");
		try {
			TimeUnit.SECONDS.sleep(timeInSec);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Execution m1() completed ....");
	}
	
	public void execute1() {
		Runnable r1 = () -> m1(5);
		ThreadFactory virtualThreadFactory = Thread.ofVirtual().factory();
		ThreadFactory kernelThreadFactory = Thread.ofPlatform().factory(); //kernel or platform
		
		Thread virtualThread = virtualThreadFactory.newThread(r1);
		Thread kernelThread = kernelThreadFactory.newThread(r1);

		virtualThread.start();
		kernelThread.start();
		
		
		// Platform thread
		(new Thread(r1)).start();
		Thread platformThread = new Thread(r1);
		platformThread.start();
		
		// Virtual thread
		Thread virtualThread1 = Thread.startVirtualThread(r1);
		Thread ofVirtualThread = Thread.ofVirtual().start(r1);

		// Virtual thread created with a factory
		ThreadFactory factory = Thread.ofVirtual().factory();
		Thread virtualThreadFromAFactory = factory.newThread(r1);
		virtualThreadFromAFactory.start();
		
	}
	
	public void execute2() {
		Runnable r1 = () -> m1(3);
		Thread virtualThread = Thread.startVirtualThread(r1);
		try {
			virtualThread.join();
			System.out.println("Task completed ...");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void execute3() {
		Runnable r1 = () -> m1(3);
		try {
			Thread.ofVirtual().name("my-virtual-thread").start(r1).join();
			System.out.println("Task completed ...");
		} catch (InterruptedException e) {
			e.printStackTrace();
		};
	}
	
	public void execute4() {
		// Using ExecutorService
		Runnable r1 = () -> m1(3);
		try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {
		    for (int i = 0; i < 5; i++) {
		        executorService.submit(r1);
		    }
		}
	}
	
	
	public void check() {
//		execute2();
//		execute3();
//		execute4();
	}

	public static void main(String[] args) {
		new TestBasicVirtualThread1().check();
	}

}
