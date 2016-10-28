package com.datastax.lock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.demo.utils.PropertyHelper;
import com.datastax.demo.utils.Timer;
import com.datastax.lock.dao.LockService;

public class Main {
	private static Logger logger = LoggerFactory.getLogger(Main.class);
	private static int NO_OF_SEQUENCES = 100000;
	private int noOfThreads;
	private AtomicLong counter = new AtomicLong(0);

	public Main() {

		String noOfThreadsStr = PropertyHelper.getProperty("noOfThreads", "10");
		noOfThreads = Integer.parseInt(noOfThreadsStr);

		LockService service = new LockService();
		ExecutorService executor = Executors.newFixedThreadPool(noOfThreads);
				
		Timer timer = new Timer();
		timer.start();
		
		for (int i = 0; i < noOfThreads; i++) {
			executor.execute(new Writer(service));
		}

		while (counter.get()<1000000) {
			if (counter.get()%10000==0){
				logger.info("Total : " + counter.get());
			}
		}
		
		timer.end();
		logger.info("1000000 took " + timer.getTimeTakenSeconds() + " sec (" + (1000000/timer.getTimeTakenSeconds()) + ") a sec" );
	}

	class Writer implements Runnable {

		private LockService service;

		public Writer(LockService service) {
			this.service = service;
		}

		@Override
		public void run() {
			while(true){

				String issuer = "" + (new Double(Math.random() * NO_OF_SEQUENCES).intValue());
				service.getLock(issuer);
				counter.incrementAndGet();
				service.getReleaseLock(issuer);				
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Main();

		System.exit(0);
	}
}
