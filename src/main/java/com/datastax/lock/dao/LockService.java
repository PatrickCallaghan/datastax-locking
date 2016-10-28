package com.datastax.lock.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.demo.utils.PropertyHelper;

public class LockService {
	
	private LockDao dao = new LockDao(PropertyHelper.getProperty("contactPoints", "localhost").split(","));
	private static Logger logger = LoggerFactory.getLogger( LockService.class );
	
	public boolean getLock(String id){
		return dao.getLock(id);
	}

	public boolean getReleaseLock(String id) {
		return dao.releaseLock(id);
		
	}
}
