package security;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;


/**
 * Observes the lifetime of Threads and stops them if the maximum of time
 * exceeded. It's possible for real time and cpu time.
 * 
 * @author Nicolas Dular <nicolas.dulars@myxcode.at>
 */
public class WatchDog implements Runnable {

	private static WatchDog instance = null;
	private static String securityKey = null;

	private ObservedThread toObserve = null;
	private ThreadMXBean threadManager = ManagementFactory.getThreadMXBean();
	private Thread ownThread;
	private boolean threadShouldStop;
	private long currentTime;
	private long overtime_percentage = (long) 1.2;

	/**
	 * Constructor for Singelton
	 */
	private WatchDog() {
		this.ownThread = new Thread(this);
		// Set the permission for this Thread
		GambooSecurityManager.getInstance().allowThread(securityKey, ownThread);
		this.ownThread.setName("WatchDogThread");
		this.ownThread.start();
	}

	/**
	 * Singelton Instance-Method
	 * 
	 * @return Singelton-Instance of WatchDog
	 */
	public synchronized static WatchDog getInstance() {
		if (WatchDog.instance == null) {
			WatchDog.instance = new WatchDog();
		}
		return WatchDog.instance;
	}

	/**
	 * Set the Security Key for the Security Manager
	 * 
	 * @param key
	 *            Security Key for the permissions
	 */
	public static void setSecurityKey(String key) {
		if (WatchDog.securityKey == null)
			WatchDog.securityKey = key;
	}

	/**
	 * Set the Thread to observe it.
	 * 
	 * @param thread
	 *            The Thread that should be observed
	 * @param maxRealTime
	 *            Maximum of real-time for the Thread in milliseconds
	 * @param maxCPUTime
	 *            Maximum of CPU time for the thread in milliseconds
	 */
	public void setThread(Thread thread, long maxRealTime, long maxCPUTime) {
		this.toObserve = new ObservedThread(thread, maxRealTime, maxCPUTime);
		synchronized (this) {
			this.notifyAll();
		}
	}

	/**
	 * Stops the WatchDog - only allowed with the security key
	 * 
	 * @param key
	 * 			Security key for permissions
	 */
	public synchronized void close(String key) {
		if (key.equals(WatchDog.securityKey)) {
			synchronized (this) {
				this.notifyAll();
			}
			toObserve = null;
			this.threadShouldStop = true;
		} else {
			Debugger.getInstance().print("It's not allowed to stop the WatchDog");
		}
	}

	/**
	 * Gives the status of the current WatchDog-Thread 
	 * 
	 * @return the status of closing from the WatchDog
	 */
	protected synchronized boolean isClosing() {
		return this.threadShouldStop;
	}

	/**
	 * Wakes up the thread for the first time, when no Thread is to observe.
	 * If a thread stopped, the WatchDog waits again and needs a wake up
	 */
	public void wakeUp() {
		synchronized (this) {
			this.notifyAll();
		}
	}

	/**
	 * If a thread already stopped, but the WatchDog is still observing the thread,
	 * the interrupt() method has to notify the WatchDog
	 */
	public void interrupt() {
		synchronized (this) {
			this.notify();
			toObserve.getThread().stop();
		}
	}

	/**
	 * The thread of the WatchDog observes a thread and look if the maximum of
	 * the time already exceeded. If that happened the WatchDog tries to stop the Thread.
	 * But first the WatchDog waits for the beginning of the to observed thread - after
	 * that, the WatchDog thread sleeps a while so it must not run the whole
	 * time.
	 * 
	 * @Override
	 */
	public void run() {
		long toWait;

		// If there is no thread to observe, the WatchDog waits until it gets notified
		if (this.toObserve == null) {
			synchronized (this) {
				try {
					this.wait();
				} catch (Exception e) {
					Debugger.getInstance().print(e);
				}
			}
		}

		while (!isClosing()) {
			try {
				if(toObserve != null){
					if (toObserve.isAlive()) {
						this.currentTime = System.currentTimeMillis();
					
						// set the current CPU time of the thread
						toObserve.setCurrentCPUTime( (toObserve.getMaxCPUTime() * 1000000 * this.overtime_percentage)
								 					- (threadManager.getThreadCpuTime(toObserve.getId())) / 1000000);
						
						// set the current real time of the thread
						toObserve.setCurrentRealTime(currentTime - toObserve.getStartTime());
							
						if (toObserve.getCurrentCPUTime() > 0
								&& toObserve.getCurrentRealTime() < this.toObserve.getMaxRealTime() * 1000000 * this.overtime_percentage) {
							
							if (toObserve.getCurrentCPUTime() >= toObserve.getCurrentRealTime())
								toWait = toObserve.getCurrentRealTime();
							else
								toWait = toObserve.getCurrentCPUTime();
								// Now we wait some time, then we will look, if the
							
							// Thread exceeded the Time
							synchronized (this) {
								this.wait(toWait + 1);
							}
							if (!toObserve.isAlive()) {
								continue;
							}
						}
	
						// Check if maximum of time exceeded
						if (toObserve.getCurrentRealTime() >= this.toObserve.getMaxRealTime()
								|| toObserve.getCurrentCPUTime() <= 0) {
							// if time exceeded - stop the thread
								if(toObserve.isAlive())
									toObserve.getThread().stop();
								
							Debugger.getInstance().print("The thread " + toObserve.getThread() + " was too slow");
						}
					} else {
						// If the thread isn't alive anymore, the WatchDog waits
						synchronized (this) {
							this.wait();
						}
					}
				}
			} catch (Exception e) {
				Debugger.getInstance().print(e);
			}
		}
	}

}
