/**
*	Copyright (C) 2011 gamboo.at
*	
*	Permission is hereby granted, free of charge, to any person obtaining a copy of
*	this software and associated documentation files (the "Software"), to deal in
*	the Software without restriction, including without limitation the rights to
*	use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
*	of the Software, and to permit persons to whom the Software is furnished to do
*	so, subject to the following conditions:
*	
*	The above copyright notice and this permission notice shall be included in all
*	copies or substantial portions of the Software.
*	
*	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
*	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
*	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
*	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
*	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
*	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
*	SOFTWARE.
*/

import security.*;

/*
 * Some tests for the Gamboo-Java-Security
 */
public class Main {

	public static void main(String[] arg) {
		
		// Set the SecurityManager and register every classes with the security-key
		System.setSecurityManager(GambooSecurityManager.getInstance()); 
	    String key = GambooSecurityManager.getInstance().getSecurityKey();
		
		GambooSecurityManager.getInstance().allowThread(key,Thread.currentThread());
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
