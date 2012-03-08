package security;

import java.lang.reflect.Method;

/**
 * With this Class it's possible to run every method in a Thread.
 * It's possible to give every method a maximum on cpu and real time.
 * 
 * 
 * @author Nicolas Dular <nicolas.dulars@myxcode.at>
 */
class RunMethod implements Runnable {

	private static String securityKey = null;

	/**
	 * Set the Security Key for the Security Manager
	 * 
	 * @param secKey
	 *            String Security Key
	 */
	public static void setSecurityKey(String secKey) {
		if (RunMethod.securityKey == null) {
			RunMethod.securityKey = secKey;
		}
	}

	private Boolean found = true;
	private long maxCPUTime = 1000;
	private long maxRealTime = 2000;
	private Method method = null;

	private String methodName = null;
	private Thread ownThread = null;
	private Object[] parameters;
	Class<?>[] parameterTypes = null;
	private Object returnValue = null;

	private SecureInstance secureInstance = null;

	/** 
     * Constructor for RunMethod
     * 
     * @param maxRealTime  maximum of Real Time in ms
     * @param methodName  maximum of CPU Time in ms
     * @param secureInstance  Name of the Method
     * @param parameters array of all parameters
     */
	RunMethod(long maxRealTime,long maxCPUTime,String methodName, SecureInstance secureInstance,Object[]  parameters) throws Exception{
		this.maxRealTime = maxRealTime;
		this.maxCPUTime = maxCPUTime;
		this.parameters = parameters;
		this.secureInstance = secureInstance;
		this.methodName = methodName;
		
		startThread();
	}
	
	/** 
     * Constructor for RunMethod with default maximum of Real and CPU Time
     * 
     * @param methodName Name of the Method
     * @param secureInstance SecureInstance
     * @param parameters Object[] array of all parameters
     */
	RunMethod(String method, SecureInstance secureInstance,Object[]  parameters) throws Exception{
		this.parameters = parameters;
		this.secureInstance = secureInstance;
		this.methodName = method;
	
		startThread();
	}
	
	/** 
     * Constructor for RunMethod with default maximum of Real and CPU Time
     * 
     * @param methodName Name of the Method
     * @param secureInstance SecureInstance
     */
	RunMethod(String method, SecureInstance secureInstance) throws Exception{
		this.secureInstance = secureInstance;
		this.methodName = method;
		startThread();
	}

	/**
	 * We wait until the Thread stopped, then we return the Object
	 * 
	 * @exception Exception
	 *                a Exception if the return value is NULL
	 * 
	 * @return Object return value of the Method
	 */
	public Object getReturnValue() throws Exception {
		return returnValue;
	}

	/**
	 * Here we start the Thread of the Method
	 * 
	 * @Override
	 */
	@Override
	public void run() {
		// Start the WatchDog
		WatchDog.getInstance().wakeUp();
		try {
			method = secureInstance.getInstance().getClass()
					.getMethod(methodName, parameterTypes);
			returnValue = (method.invoke(secureInstance.getInstance(),
					parameters));
		} catch (final Exception e) {
			e.printStackTrace();
			Debugger.getInstance().print("Something went wrong with method: " + methodName);
		}
	}

	/**
	 * Here we start the Thread for the method First we try to find the method
	 * for the reflection
	 * 
	 * @bug By searching for the right method we have to support primitive data
	 *      types
	 * 
	 * @Override
	 */
	private void startThread() throws Exception {

		if (secureInstance.getInstance() != null) {

			// Initialize a new Thread with a random ThreadGroup and a new Name
			ownThread = new Thread(CodeSupSecurityManager.getInstance()
					.getThreadGroup(securityKey), this,
					secureInstance.getClassName() + "." + methodName);

			// Observe the Thread in the WatchDog
			WatchDog.getInstance()
					.setThread(ownThread, maxRealTime, maxCPUTime);

			// Search for the right Method in the Class
			for (final Method method : secureInstance.getInstance().getClass()
					.getMethods()) {

				// if it isn't the right we go straight forward
				if ((!method.getName().equals(methodName))) {
					continue;
				}

				// we found the right method, now search for the right
				// parameters
				parameterTypes = method.getParameterTypes();

				for (int i = 0; i < parameterTypes.length; i++) {

					if (!parameterTypes[i].getClass().isAssignableFrom(
							parameters[i].getClass())) {
						found = false;
						break;
					}
				}
			}

			if (found == true || parameterTypes != null) {
				ownThread.start();
				ownThread.join();
				// Tell the WatchDog that the thread finished already
				WatchDog.getInstance().interrupt();
			}
		}
	}

}