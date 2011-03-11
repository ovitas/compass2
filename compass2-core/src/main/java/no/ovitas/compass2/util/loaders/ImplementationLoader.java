/**
 * 
 */
package no.ovitas.compass2.util.loaders;

import no.ovitas.compass2.config.settings.Implementation;
import no.ovitas.compass2.config.settings.ImplementationReflection;
import no.ovitas.compass2.config.settings.ImplementationSpring;

/**
 * @class ImplementationLoader
 * @project compass2-core
 * @author Milan Gyalai (gyalai@mail.thot-soft.com) 
 * @version 
 * @date 2010.09.09.
 * 
 */
public class ImplementationLoader {

	public static <T> T loadInstance(Implementation impl, Class<T> classOject) {
		switch (impl.getImplementationType()) {
		case SPRING:
			ImplementationSpring implementationSpring = (ImplementationSpring)impl;
			return SpringLoader.loadBeanInstance(implementationSpring.getBeanName(), implementationSpring.getContextFiles().getFileNameArray(), classOject);

		case REFLECTION:
			ImplementationReflection implementationReflection = (ImplementationReflection)impl;
			return ReflectionLoader.loadObjectInstance(implementationReflection.getClassName(), classOject);
		}
		return null;
	}
}
