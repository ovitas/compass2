/**
 * 
 */
package no.ovitas.compass2.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author magyar
 *
 */
public class CompassUtil {
	
	private static ApplicationContext applicationContext = null;
	
	public static ApplicationContext getApplicationContext(){
	  if(applicationContext == null){
		  applicationContext = new ClassPathXmlApplicationContext(
			        new String[] {"applicationContext.xml", "applicationContext-dao.xml","applicationContext-service.xml"});		  
		  
	  }
	  return applicationContext;
		
	}

}
