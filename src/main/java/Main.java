import security.*;

/*
 * Some tests for the Gamboo-Java-Security
 */
public class Main {

	public static void main(String[] arg) {
		
		// Set the SecurityManager and register every classes with the security-key
		System.setSecurityManager(CodeSupSecurityManager.getInstance()); 
	    String key = CodeSupSecurityManager.getInstance().getSecurityKey();
		
		CodeSupSecurityManager.getInstance().allowThread(key,Thread.currentThread());
		SecureInstance.setSecurityKey(key);
		WatchDog.setSecurityKey(key);
		
		// run the evil code
		SecureInstance instance = null;
		try {
			instance = new SecureInstance("Evil.MyCode");
			
			if(instance.getInstance() != null){
				System.out.println("----------------------------------------------------------------\n\nstarting with some tests");
				
				// loops
				System.out.println("\n\nTrying an endless loop");
				
					instance.callMethod("endlessLoop");
					System.out.println("test passed\n\n\n");
				
				// files
				System.out.println("Trying a file-connection");
				try{
					instance.callMethod("fileConnection");
					System.out.println("test passed\n\n\n");
				}catch(Exception e){ System.out.println(e); }
				
				// sockets
				System.out.println("Trying a socket-connection");
				try{
					instance.callMethod("socketConnection");
					System.out.println("test passed\n\n\n");
				}catch(Exception e){ System.out.println(e); }
                
				// overflow 
				/*
				System.out.println("Trying an overflow");
				try{
					instance.callMethod("overflow");
					System.out.println("test passed\n\n\n");
				}catch(Exception e){ System.out.println(e); }
				*/
				
			}
		
		} catch (Exception e) {
			
		}
		

		// Close the WatchDog at least
		WatchDog.getInstance().close(key);	
	}

}
