package security;

import java.io.FileDescriptor;
import java.net.InetAddress;
import java.security.Permission;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

/**
 * This is the own GambooSecurityManager that inherit for the Java
 * SecurityManager The job is to check if a Thread has got enough permissions
 * 
 * @author Nicolas Dular <nicolas.dular@myxcode.at>
 */
public class CodeSupSecurityManager extends SecurityManager {

	// Singelton 
	private static CodeSupSecurityManager instance;

	/**  allowedPermissions denies specific permissions */
	private final ArrayList<String> allowedPermissions = new ArrayList<String>();

	/** in applicationsThreads are all threads with enough permission */
	private final ArrayList<ThreadGroup> applicationThreads = new ArrayList<ThreadGroup>();

	private boolean permission = false;

	// some variables
	private Random random = null;
	private String securityKey = null;
	private String token = null;
	private String permissionRequest;
	
	/**
	 * Singelton Instance-Method
	 * 
	 * @return GambooSecurityManager
	 */
	public static CodeSupSecurityManager getInstance() {
		if (CodeSupSecurityManager.instance == null) {
			CodeSupSecurityManager.instance = new CodeSupSecurityManager();
		}
		return CodeSupSecurityManager.instance;
	}

	private CodeSupSecurityManager() {
		// permission specification
		allowedPermissions.add("suppressAccessChecks");
		allowedPermissions.add("accessClassInPackage.sun.reflect");
	}

	/**
	 * Checks if the calling thread has got enough permission.
	 * 
	 * @return boolean true = has permission, false = no permission
	 */
	private boolean accessOK() {
		permission = false;
	
		// if there is a thread
		if (applicationThreads.size() == 0) {
			permission = true;
		} else { // check if GambooSecurityManagers knows the thread
			for (final ThreadGroup tg : applicationThreads) {
				if (tg == Thread.currentThread().getThreadGroup()) {
					permission = true;
				}
			}
		}
		return permission;
	}
		
	void printIt(String s) throws SecurityException{
		permissionRequest = s + " for "  + Thread.currentThread().toString();
		Logger.getInstance().info("ASK PERMISSION " + permissionRequest);
		if (!accessOK()) {
			Logger.getInstance().error("PERMISSION DENIED " + permissionRequest);
			throw new SecurityException("PERMISSION DENIED" + permissionRequest);
		}
	}
	
	
	/**
	 * If a thread needs permission, it register the thread into the SecurityManager
	 * 
	 * @param key
	 *            The key to authenticate the class created the thread
	 * @param t
	 *            The thread that needs the permissions
	 */
	public void allowThread(String key, Thread t) {
		permissionRequest = "allowThread " +  t.toString();
		
		Logger.getInstance().info(permissionRequest);
		if (key.equals(securityKey)) {
			applicationThreads.add(t.getThreadGroup());
		} else {
			Logger.getInstance().error(permissionRequest);
		}
	}

	@Override
	public void checkAccept(String host, int port) {
		printIt("checkAccept(host,port)");
	}

	/**
	 * @Overwrited Methods for the SecurityManager
	 *             ------------------------------------------------------------
	 */
	@Override
	public void checkAccess(Thread thread) {
		printIt("checkAccess(thread)");
	}

	@Override
	public void checkAccess(ThreadGroup arg0) {
		printIt("checkAccess(ThreadGroup)");
	}

	@Override
	public void checkAwtEventQueueAccess() {
		printIt("checkAwtEventQueueAccess");
	}

	@Override
	public void checkConnect(String host, int port) {
		printIt("checkConnect(String host, int port)");
	}

	@Override
	public void checkConnect(String host, int port, Object executionContext) {
		printIt("checkConnect(String host, int port, Object executionContext)");
	}

	@Override
	public void checkCreateClassLoader() {
		//printIt("checkCreateClassLoader");
	}

	@Override
	public void checkDelete(String filename) {
		printIt("checkDelete(String filename)");
	}

	@Override
	public void checkExec(String command) {
		printIt("checkExec(String command)");
	}

	@Override
	public void checkExit(int status) {
		printIt("checkExit(int status)");
	}

	@Override
	public void checkLink(String library) {
		printIt("checkLink(String library)");
	}

	@Override
	public void checkListen(int port) {
		printIt("checkListen(int port)");
	}

	@Override
	public void checkMemberAccess(Class<?> arg0, int arg1) {
		//printIt("checkMemberAccess(Class<?> arg0, int arg1)");
	}

	@Override
	public void checkMulticast(InetAddress arg0) {
		printIt("checkMulticast(InetAddress arg0)");
	}

	@Override
	public void checkMulticast(InetAddress arg0, byte arg1) {
		printIt("checkMulticast(InetAddress arg0, byte arg1)");
	}

	@Override
	public void checkPackageDefinition(String packageName) {
		printIt("checkPackageDefinition(String packageName)");
	}

	@Override
	public void checkPermission(Permission perm) {
		//printIt("checkPermission(Permission perm)");
	}

	@Override
	public void checkPrintJobAccess() {
		printIt("checkPrintJobAccess()");
	}

	@Override
	public void checkPropertiesAccess() {
		printIt("checkPropertiesAccess()");
	}

	@Override
	public void checkPropertyAccess(String key) {
		printIt("checkPropertyAccess(String key)");
	}

	@Override
	public void checkRead(FileDescriptor filedescriptor) {
		printIt("checkRead(FileDescriptor filedescriptor)");
	}

	@Override
	public void checkRead(String filename) {
		//printIt("checkRead(String filename)");
	}

	@Override
	public void checkRead(String filename, Object executionContext) {
		printIt("checkRead(String filename, Object executionContext)");
	}

	@Override
	public void checkSecurityAccess(String arg0) {
		printIt("checkSecurityAccess(String arg0)");
	}

	@Override
	public void checkSetFactory() {
		printIt("checkSetFactory()");
	}

	@Override
	public void checkSystemClipboardAccess() {
		printIt("checkSystemClipboardAccess()");
	}

	@Override
	public void checkWrite(FileDescriptor filedescriptor) {
		printIt("checkWrite(FileDescriptor filedescriptor)");
	}

	@Override
	public void checkWrite(String filename) {
		printIt("checkWrite(String filename)");
	}

	/**
	 * Getter Method for Security Key. The method can be called only once.
	 * 
	 * @return generated key or null if already called
	 */
	public String getSecurityKey() {
		if (securityKey == null) {
			securityKey = keygen();
			return securityKey;
		}
		return null;
	}

	/**
	 * Creates a random ThreadGroup for the threads from the user code
	 * 
	 * @param key
	 *            with the security key the SecurityManager can authenticate the class
	 * 
	 * @return a random generated ThreadGroup
	 */
	public ThreadGroup getThreadGroup(String key) {
		if (key.equals(securityKey)) {
			final ThreadGroup threadGroup = new ThreadGroup(keygen());
			return threadGroup;
		} else {
			Logger.getInstance().debug(
					"No Permission to get a ThreadGroup "
							+ Thread.currentThread());
			return null;
		}
	}

	/**
	 * Generates a random key
	 * 
	 * @return generated key
	 */
	private String keygen() {
		random = new Random();
		token = Long.toString(Math.abs(random.nextLong()), 12);
		return token;
	}

}
