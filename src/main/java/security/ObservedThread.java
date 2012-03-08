
package security;

/**
 * This is a Structure for the observed Threads
 * 
 * @author Nicolas Dular <nicolas.dulars@myxcode.at>
 */
public class ObservedThread {

	private long curr_cputime = 0;
	private long curr_realtime = 0;
	private long lifeTime = 0;
	private long maxCPUTime = 0;
	private long maxRealTime = 0;
	private long startTime = 0;
	private final Thread thread;

	/**
	 * Constructor ObservedThread
	 * 
	 * @param thread
	 *            The Thread that should observed
	 * @param maxRealTime
	 *            Maximum of Realtime in ms
	 * @param maxCPUTime
	 *            Maximum of CPU time in ms
	 */
	ObservedThread(Thread thread, long maxRealTime, long maxCPUTime) {
		this.thread = thread;
		this.maxRealTime = maxRealTime;
		this.maxCPUTime = maxCPUTime;
		startTime = System.currentTimeMillis();
	}

	/**
	 * Getter for current CPU time
	 * 
	 * @return current cpu time left
	 */
	public long getCurrentCPUTime() {
		return curr_cputime;
	}

	/**
	 * Getter for current CPU time
	 * 
	 * @return current real time
	 */
	public long getCurrentRealTime() {
		return curr_realtime;

	}

	/**
	 * Getter Method for the ID of the Thread
	 * 
	 * @return long
	 */
	public long getId() {
		return thread.getId();
	}

	/**
	 * Getter Method for the current Lifetime
	 * 
	 * @return long in ms
	 */
	public long getLifeTime() {
		return lifeTime;
	}

	/**
	 * Getter Method for the CPU Time of the Thread
	 * 
	 * @return long
	 */
	public long getMaxCPUTime() {
		return maxCPUTime;
	}

	/**
	 * Getter Method for the Real Time of the Thread
	 * 
	 * @return long
	 */
	public long getMaxRealTime() {
		return maxRealTime;
	}

	/**
	 * Getter Method for the start Time of the Thread
	 * 
	 * @return long in ms
	 */
	public long getStartTime() {
		return startTime;
	}

	/**
	 * Getter Method getThread
	 * 
	 * @return Thread return the Thread Reference
	 */
	public Thread getThread() {
		return thread;
	}

	/**
	 * Getter Method for the live status of the Thread
	 * 
	 * @return boolean
	 */
	public boolean isAlive() {
		return thread.isAlive();
	}

	/**
	 * Setter for current CPU time
	 * 
	 * @param t
	 *            current cpu time
	 */
	public void setCurrentCPUTime(long t) {
		curr_cputime = t;
	}

	/**
	 * Setter Method for current real time of the Thread
	 * 
	 * @param t
	 *            current real time
	 */
	public void setCurrentRealTime(long t) {
		curr_realtime = t;
	}

	/**
	 * Setter Method for the current Lifetime
	 * 
	 * @param long in ms
	 */
	public void setLifeTime(long lifeTime) {
		this.lifeTime = lifeTime;
	}

	/**
	 * Stops the Thread
	 */
	public void stop() {
		thread.stop();
	}
}
