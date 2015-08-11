package fr.adrean.BlueCore;

public class CacheEntry {
	private long timeStamp;
	private String result;
	
	public CacheEntry(String r) {
		this.timeStamp = System.currentTimeMillis();
		result = r;
	}
	
	public boolean isStillValid(long timeout) {
		return timeStamp + timeout >= System.currentTimeMillis();
	}
	
	public String getResult() {
		return result;
	}
	
	public long getTimeStamp() {
		return timeStamp;
	}
	
}
