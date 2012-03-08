package security;

import java.io.FileDescriptor;
import java.net.InetAddress;
import java.security.Permission;
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
	private final Vector<String> allowedPermissions = new Vector<String>();

	/** in applicationsThreads are all threads with enough permission */
	private final Vector<ThreadGroup> applicationThreads = new Vector<ThreadGroup>();

	private boolean permission = false;

	// some variables
	private Random random = null;
	private String securityKey = null;
	private String token = null;
	
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

	/**
	 * If a thread needs permission, it register the thread into the SecurityManager
	 * 
	 * @param key
	 *            The key to authenticate the class created the thread
	 * @param t
	 *            The thread that needs the permissions
	 */
	public void allowThread(String key, Thread t) {
		if (key.equals(securityKey)) {
			applicationThreads.add(t.getThreadGroup());
		} else {
			Debugger.getInstance().print(
					"Try to allow Thread for " + Thread.currentThread());
		}
	}

	@Override
	public void checkAccept(String host, int port) {
		Debugger.getInstance().print("checkAccept for " + Thread.currentThread());
		if (!accessOK()) {
			throw new SecurityException(
					"No Way! - YOU ARE NOT ALLOWED TO DO THIS!");
		}
	}

	/**
	 * @Overwrited Methods for the SecurityManager
	 *             ------------------------------------------------------------
	 */
	@Override
	public void checkAccess(Thread thread) {
		Debugger.getInstance().print("checkAccess Thread for " + Thread.currentThread());
		if (!accessOK()) {
			throw new SecurityException(
					"No Way! - YOU ARE NOT ALLOWED TO DO THIS!");
		}
	}

	@Override
	public void checkAccess(ThreadGroup arg0) {
		Debugger.getInstance().print("checkAccess ThreadGroup for " + Thread.currentThread());
		if (!accessOK()) {
			throw new SecurityException(
					"No Way! - YOU ARE NOT ALLOWED TO DO THIS!");
		}
	}

	@Override
	public void checkAwtEventQueueAccess() {
		Debugger.getInstance().print("checkAwtEventQueueAccess for " + Thread.currentThread());
		if (!accessOK()) {
			throw new SecurityException(
					"No Way! - YOU ARE NOT ALLOWED TO DO THIS!");
		}
	}

	@Override
	public void checkConnect(String host, int port) {
		Debugger.getInstance().print("checkConnect for " + Thread.currentThread());
		if (!accessOK()) {
			throw new SecurityException(
					"No Way! - YOU ARE NOT ALLOWED TO DO THIS!");
		}
	}

	@Override
	public void checkConnect(String host, int port, Object executionContext) {
		Debugger.getInstance().print("checkConnect for " + Thread.currentThread());
		if (!accessOK()) {
			throw new SecurityException(
					"No Way! - YOU ARE NOT ALLOWED TO DO THIS!");
		}
	}

	@Override
	public void checkCreateClassLoader() {
		Debugger.getInstance().print("checkCreateClassLoader for " + Thread.currentThread());
		if (!accessOK()) {
			// Debugger.getInstance().print("checkCreateClassLoader for " +
			// Thread.currentThread());
			// throw new
			// SecurityException("No Way! - YOU ARE NOT ALLOWED TO DO THIS!");
		}
	}

	@Override
	public void checkDelete(String filename) {
		Debugger.getInstance().print("checkDelete for " + Thread.currentThread());
		if (!accessOK()) {
			throw new SecurityException(
					"No Way! - YOU ARE NOT ALLOWED TO DO THIS!");
		}
	}

	@Override
	public void checkExec(String command) {
		Debugger.getInstance().print("checkExec for " + Thread.currentThread());
		if (!accessOK()) {
			throw new SecurityException(
					"No Way! - YOU ARE NOT ALLOWED TO DO THIS!");
		}
	}

	@Override
	public void checkExit(int status) {
		Debugger.getInstance().print("checkExit for " + Thread.currentThread());
		if (!accessOK()) {
			throw new SecurityException(
					"No Way! - YOU ARE NOT ALLOWED TO DO THIS!");
		}
	}

	@Override
	public void checkLink(String library) {
		Debugger.getInstance().print("checkLink for " + Thread.currentThread());
		if (!accessOK()) {
			throw new SecurityException(
					"No Way! - YOU ARE NOT ALLOWED TO DO THIS!");
		}
	}

	@Override
	public void checkListen(int port) {
		Debugger.getInstance().print("checkListen for " + Thread.currentThread());
		if (!accessOK()) {
			throw new SecurityException(
					"No Way! - YOU ARE NOT ALLOWED TO DO THIS!");
		}
	}

	@Override
	public void checkMemberAccess(Class<?> arg0, int arg1) {
		Debugger.getInstance().print("checkMemberAccess for " + Thread.currentThread());
		if (!accessOK()) {
			// throw new
			// SecurityException("No Way! - YOU ARE NOT ALLOWED TO DO THIS!");
		}
	}

	@Override
	public void checkMulticast(InetAddress arg0) {
		Debugger.getInstance().print("checkMulticast for " + Thread.currentThread());
		if (!accessOK()) {
			throw new SecurityException(
					"No Way! - YOU ARE NOT ALLOWED TO DO THIS!");
		}
	}

	@Override
	public void checkMulticast(InetAddress arg0, byte arg1) {
		Debugger.getInstance().print("checkMulticast for " + Thread.currentThread());
		if (!accessOK()) {
			throw new SecurityException(
					"No Way! - YOU ARE NOT ALLOWED TO DO THIS!");
		}
	}

	@Override
	public void checkPackageDefinition(String packageName) {
		Debugger.getInstance().print("checkPackageDefinition for " +
				Thread.currentThread());
		if (!accessOK()) {
			throw new SecurityException(
					"No Way! - YOU ARE NOT ALLOWED TO DO THIS!");
		}
	}

	@Override
	public void checkPermission(Permission perm) {
		Debugger.getInstance().print(perm.getName() + " for " + Thread.currentThread());
		if (!accessOK()) {
			if (!allowedPermissions.contains(perm.getName())) {
				throw new SecurityException(
						"No Way! - YOU ARE NOT ALLOWED TO DO THIS! "
								+ Thread.currentThread());
			}
		}

	}

	@Override
	public void checkPrintJobAccess() {
		Debugger.getInstance().print("checkAccess printJobAccess for "+ Thread.currentThread());
		if (!accessOK()) {
			throw new SecurityException(
					"No Way! - YOU ARE NOT ALLOWED TO DO THIS!");
		}
	}

	@Override
	public void checkPropertiesAccess() {
		Debugger.getInstance().print("checkAccess PropertiesAccess for "+ Thread.currentThread());
		if (!accessOK()) {
			throw new SecurityException(
					"No Way! - YOU ARE NOT ALLOWED TO DO THIS!");
		}
	}

	@Override
	public void checkPropertyAccess(String key) {
		Debugger.getInstance().print("checkPropertyAccess for " + Thread.currentThread());
		if (!accessOK()) {
			throw new SecurityException(
					"No Way! - YOU ARE NOT ALLOWED TO DO THIS!");
		}
	}

	@Override
	public void checkRead(FileDescriptor filedescriptor) {
		Debugger.getInstance().print("checkRead for " + Thread.currentThread());
		if (!accessOK()) {
			throw new SecurityException(
					"No Way! - YOU ARE NOT ALLOWED TO DO THIS!");
		}
	}

	@Override
	public void checkRead(String filename) {
		if (!accessOK()) {
			// throw new
			// SecurityException("No Way! - YOU ARE NOT ALLOWED TO DO THIS!");
		}
	}

	@Override
	public void checkRead(String filename, Object executionContext) {
		Debugger.getInstance().print("checkRead for " + Thread.currentThread());
		if (!accessOK()) {
			throw new SecurityException(
					"No Way! - YOU ARE NOT ALLOWED TO DO THIS!");
		}
	}

	@Override
	public void checkSecurityAccess(String arg0) {
		Debugger.getInstance().print("securityAccess for "+ Thread.currentThread());
		if (!accessOK()) {
			throw new SecurityException(
					"No Way! - YOU ARE NOT ALLOWED TO DO THIS!");
		}
	}

	@Override
	public void checkSetFactory() {
		Debugger.getInstance().print("checkAccess SetFactory for " + Thread.currentThread());
		if (!accessOK()) {
			throw new SecurityException(
					"No Way! - YOU ARE NOT ALLOWED TO DO THIS!");
		}
	}

	@Override
	public void checkSystemClipboardAccess() {
		Debugger.getInstance().print("SystemClipboardAccess for " + Thread.currentThread());
		if (!accessOK()) {
			throw new SecurityException(
					"No Way! - YOU ARE NOT ALLOWED TO DO THIS!");
		}
	}

	@Override
	public void checkWrite(FileDescriptor filedescriptor) {
		Debugger.getInstance().print("CheckWrite for " + Thread.currentThread());
		if (!accessOK()) {
			throw new SecurityException(
					"No Way! - YOU ARE NOT ALLOWED TO DO THIS!");
		}
	}

	@Override
	public void checkWrite(String filename) {
		Debugger.getInstance().print("checkWrite for " + Thread.currentThread());
		if (!accessOK()) {
			
			throw new SecurityException(
					"No Way! - YOU ARE NOT ALLOWED TO DO THIS!");
		}
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
			Debugger.getInstance().print(
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