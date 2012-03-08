
package security;

/**
 * With this Class it's possible to run the calls of a constructor in a Thread
 * You only have to call new SecureInstance('http://yourclassname'); and with
 * .getInstance(); you'll get Object
 * 
 * @author Nicolas Dular <nicolas.dulars@myxcode.at>
 */
public class SecureInstance implements Runnable {

	private static String securityKey = null;

	/**
	 * Set the Security Key for the Security Manager
	 * 
	 * @param secKey
	 *            Security Key
	 */
	public static void setSecurityKey(String secKey) {
		if (SecureInstance.securityKey == null) {
			SecureInstance.securityKey = secKey;
			RunMethod.setSecurityKey(SecureInstance.securityKey);
		}
	}

	private final String className;
	private long maxCPUTime = 1000;
	private long maxRealTime = 2000;
	private RunMethod method = null;

	private Thread ownThread = null;

	private Object returnValue = null;

	/**
	 * Constructor for SecureInstance with the default maximum of Time
	 * 
	 * @param className
	 *            Name of the Class you want to initialize
	 * 
	 * @return SecureInstance
	 */
	public SecureInstance(String className) throws Exception {
		this.className = className;
		// Start the Thread for creating an instance of the class
		startThread();
	}

	/**
	 * Constructor for SecureInstance
	 * 
	 * @param classname
	 *            Name of the Class you want to initialize
	 * @param maxRealTime
	 *            maximum of the Real Time in ms
	 * @param maxCPUTime
	 *            maximum of the CPU Time in ms
	 * 
	 * @return SecureInstance
	 */
	public SecureInstance(String classname, long maxRealTime, long maxCPUTime)
			throws Exception {
		className = classname;
		this.maxCPUTime = maxCPUTime;
		this.maxRealTime = maxRealTime;

		// Start the Thread for creating an instance of the class
		startThread();
	}

	/**
	 * Here you can call a Method of the Instance
	 * 
	 * @param maxRealTime
	 *            maximum of CPU Time ins ms
	 * @param maxCPUTime
	 *            maximum of Real Time ins ms
	 * @param methodName
	 *            Name of the Method
	 * @param parameter
	 *            all the parameters ...
	 */
	public Object callMethod(long maxRealTime, long maxCPUTime,
			String methodName, Object... parameter) throws Exception {
		method = new RunMethod(maxRealTime, maxCPUTime, methodName, this,
				parameter);
		return method.getReturnValue();
	}

	/**
	 * Here you can call a Method of the Instance with the default maximum of
	 * CPU and Real time
	 * 
	 * @param methodName
	 *            String Name of the Method
     */
	public Object callMethod(String methodName) throws Exception {
		method = new RunMethod(methodName, this);
		return method.getReturnValue();
	}

	/**
	 * Here you can call a Method of the Instance with the default maximum of
	 * CPU and Real time
	 * 
	 * @param methodName
	 *            String Name of the Method
	 * @param parameter
	 *            Object all the parameters ...
	 */
	public Object callMethod(String methodName, Object... parameter)
			throws Exception {
		method = new RunMethod(methodName, this, parameter);
		return method.getReturnValue();
	}

	/**
	 * Getter method for Classname
	 * 
	 * @return String Name of Class
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * Getter for the Instance of the Object
	 * 
	 * @return This is the real return of the Constructor, but have to Parse it
	 * @throws InterruptedException
	 */
	public Object getInstance() throws Exception {
		if (returnValue == null) {
			Debugger.getInstance().print(
					"Constructor of " + className + " is NULL");
			throw new Exception("Constructor of " + className + " given NULL");
		}
		return returnValue;
	}

	/**
	 * Here we start the Thread for the initialization of the Object
	 * 
	 * @Override
	 */
	@Override
	public void run() {
		WatchDog.getInstance().wakeUp();
		try {
			// this.returnValue = this.objectClass.newInstance();
			returnValue = Class.forName(className).newInstance();
		} catch (final Exception e) {
			System.out.println("YOU DID SOMETHING ILLEGAL - SHAME ON YOU!");
			// e.printStackTrace();
		}
	}

	/**
	 * Starts the Thread to initialize the new Object
	 */
	private void startThread() throws Exception {
		// Create a new Thread instance with a random ThreadGroup
		ownThread = new Thread(CodeSupSecurityManager.getInstance()
				.getThreadGroup(SecureInstance.securityKey), this, className);

		// Add the Thread to the WatchDog to observe it
		WatchDog.getInstance().setThread(ownThread, maxRealTime, maxCPUTime);

		ownThread.start();
		ownThread.join();

		// Wake up the Watchdog, if the thread was faster then the time it got
		WatchDog.getInstance().interrupt();
	}
}
