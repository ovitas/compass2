/**
 * 
 */
package no.ovitas.compass2.util.loaders;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @class SpringLoader
 * @project compass2-core
 * @author Milan Gyalai (gyalai@mail.thot-soft.com)
 * @version
 * @date 2010.09.08.
 * 
 */
public class SpringLoader {

	private static Log log = LogFactory.getLog(SpringLoader.class);

	@SuppressWarnings("unchecked")
	public static <T> T loadBeanInstance(String beanName,
			String[] contextFiles, Class<T> classOject) {
	
		T obj = null;
		
		try {

		Object newInstance = new ClassPathXmlApplicationContext(contextFiles)
				.getBean(beanName);

		if (classOject.isInstance(newInstance)) {
			obj = (T) newInstance;
		}
		} catch (BeansException e) {
			log.error(
					"Error occured when try to load class: " + beanName
							+ ", with sprig loader! Message is: "
							+ e.getMessage(), e);
		}

		return obj;
	}
}
