package control.reflection;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReflectionMiscellaneous {
	// Library useful for getting all getters or setters of an Object of any class using reflection
	private static Logger logger = Logger.getLogger(ReflectionMiscellaneous.class.getName());
	
	private ReflectionMiscellaneous() {}
		
	public static Method getGetter(String attrName, Object obj) throws NoSuchGetterException {
		try{
			return getGetterOrSetter("get", attrName, obj);
		}
		catch(NoSuchMethodException e) {
			try {
				return getGetterOrSetter("is", attrName, obj);
			} catch (NoSuchMethodException eNested) {
				throw new NoSuchGetterException(attrName); 
			}
		}
	}
	
	public static Method getSetter(String attrName, Object obj) throws NoSuchSetterException {
		try{
			return getGetterOrSetter("set", attrName, obj);
		}
		catch(NoSuchMethodException e) {
			throw new NoSuchSetterException(attrName);
		}
	}
	
	private static Method getGetterOrSetter(String getOrSet, String attrName, Object obj) throws NoSuchMethodException {
		// gets getter or setter of field attrName. Method must be named after getter/setter conventions
		Method[] methods = obj.getClass().getMethods();
		String setAttrName = "Searching method " + getOrSet + " for: " + attrName;
		logger.log(Level.INFO, setAttrName);
		for (int j = 0; j < methods.length; j ++) {
			if ((methods[j].getName().contains(getOrSet)
					&& methods[j].getName().substring(3).compareTo(attrName.substring(0, 1).toUpperCase() + attrName.substring(1)) == 0
					&& !methods[j].isSynthetic()) || (methods[j].getName().contains(getOrSet)
							&& methods[j].getName().substring(2).compareTo(attrName.substring(0, 1).toUpperCase() + attrName.substring(1)) == 0
							&& !methods[j].isSynthetic())
					) {
				logger.log(Level.INFO, methods[j].getName());
				return methods[j];
			}
		}
		throw new NoSuchMethodException();
	}
}
