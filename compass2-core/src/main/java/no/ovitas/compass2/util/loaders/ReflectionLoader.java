/**
 * 
 */
package no.ovitas.compass2.util.loaders;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @class ReflectionLoader
 * @project compass2-core
 * @author Milan Gyalai (gyalai@mail.thot-soft.com)
 * @version
 * @date 2010.09.08.
 * 
 */
public class ReflectionLoader {

	private static Log log = LogFactory.getLog(ReflectionLoader.class);

	@SuppressWarnings("unchecked")
	public static <T> T loadObjectInstance(String className, Class<T> classOject) {

		T obj = null;

		try {
			Object newInstance = Class.forName(className).newInstance();

			if (classOject.isInstance(newInstance)) {
				obj = (T) newInstance;
			}

		} catch (ClassNotFoundException e) {
			log.error(
					"Error occured when try to load class: " + className
							+ ", with reflection loader! Message is: "
							+ e.getMessage(), e);
		} catch (InstantiationException e) {
			log.error(
					"Error occured when try to load class: " + className
							+ ", with reflection loader! Message is: "
							+ e.getMessage(), e);
		} catch (IllegalAccessException e) {
			log.error(
					"Error occured when try to load class: " + className
							+ ", with reflection loader! Message is: "
							+ e.getMessage(), e);
		}

		return obj;
	}
}
