package com.roger.javainterview.zk;

public class DisLock {

	private String PATH; // lock 888.R0000001

	private String LOCKID; // lock

	private boolean active;

	public String getPATH() {
		return PATH;
	}

	public void setPATH(String pATH) {
		PATH = pATH;
	}

	public String getLOCKID() {
		return LOCKID;
	}

	public void setLOCKID(String lOCKID) {
		LOCKID = lOCKID;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	

}
