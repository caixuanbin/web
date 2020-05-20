package cn.sessiontech.xcx.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author ljy
 * 
 */
public class ReqToModelHelper {
	
	protected static Log logger = LogFactory.getLog(ReqToModelHelper.class);

	public static void setFieldVal(Field field, Object req, Object model) {
		field.setAccessible(true);
		try {
			String fieldName = field.getName();
			String methodName = fieldName.substring(0, 1).toUpperCase()
					+ fieldName.substring(1);
			String type = field.getGenericType().toString(); // 获取属性的类型
			if (type.equals("class java.lang.Integer")) {
				Method m = req.getClass().getMethod("get" + methodName);
				Integer value = (Integer) m.invoke(req);
				if (value != null) {
					try {
						m = model.getClass().getMethod("set" + methodName,
								Integer.class);
						m.invoke(model, value);
					} catch (NoSuchMethodException e) {
						logger.warn("set" + methodName + "is not exist");
					}
				}
			}
			if (type.equals("class java.lang.Long")) {
				Method m = req.getClass().getMethod("get" + methodName);
				Long value = (Long) m.invoke(req);
				if (value != null) {
					try {
						m = model.getClass().getMethod("set" + methodName,
								Long.class);
						m.invoke(model, value);
					} catch (NoSuchMethodException e) {
						logger.warn("set" + methodName + "is not exist");
					}
				}
			}
			if (type.equals("class java.lang.String")) {
				Method m = req.getClass().getMethod("get" + methodName);
				String value = (String) m.invoke(req);
				if (value != null) {
					try {
						m = model.getClass().getMethod("set" + methodName,
								String.class);
						m.invoke(model, value);
					} catch (NoSuchMethodException e) {
						logger.warn("set" + methodName + "is not exist");
					}
				}
			}
			if (type.equals("class java.lang.Boolean")) {
				Method m = req.getClass().getMethod("get" + methodName);
				Boolean value = (Boolean) m.invoke(req);
				if (value != null) {
					try {
						m = model.getClass().getMethod("set" + methodName,
								Boolean.class);
						m.invoke(model, value);
					} catch (NoSuchMethodException e) {
						logger.warn("set" + methodName + "is not exist");
					}
				}
			}
			if (type.equals("class java.util.Date")) {
				Method m = req.getClass().getMethod("get" + methodName);
				Date value = (Date) m.invoke(req);
				if (value != null) {
					try {
						m = model.getClass().getMethod("set" + methodName,
								Date.class);
						m.invoke(model, value);
					} catch (NoSuchMethodException e) {
						logger.warn("set" + methodName + "is not exist");
					}
				}
			}
			if (type.equals("class java.lang.Float")) {
				Method m = req.getClass().getMethod("get" + methodName);
				Float value = (Float) m.invoke(req);
				if (value != null) {
					try {
						m = model.getClass().getMethod("set" + methodName,
								Float.class);
						m.invoke(model, value);
					} catch (NoSuchMethodException e) {
						logger.warn("set" + methodName + "is not exist");
					}
				}
			}
			if (type.equals("class java.lang.Short")) {
				Method m = req.getClass().getMethod("get" + methodName);
				Short value = (Short) m.invoke(req);
				if (value != null) {
					try {
						m = model.getClass().getMethod("set" + methodName,
								Short.class);
						m.invoke(model, value);
					} catch (NoSuchMethodException e) {
						logger.warn("set" + methodName + "is not exist");
					}
				}
			}
			if (type.equals("class java.lang.Double")) {
				Method m = req.getClass().getMethod("get" + methodName);
				Double value = (Double) m.invoke(req);
				if (value != null) {
					try {
						m = model.getClass().getMethod("set" + methodName,
								Double.class);
						m.invoke(model, value);
					} catch (NoSuchMethodException e) {
						logger.warn("set" + methodName + "is not exist");
					}
				}
			}
			if (type.equals("class java.math.BigDecimal")) {
				Method m = req.getClass().getMethod("get" + methodName);
				BigDecimal value = (BigDecimal) m.invoke(req);
				if (value != null) {
					try {
						m = model.getClass().getMethod("set" + methodName,
								BigDecimal.class);
						m.invoke(model, value);
					} catch (NoSuchMethodException e) {
						logger.warn("set" + methodName + "is not exist");
					}
				}
			}
			if (type.equals("class java.sql.Timestamp")) {
				Method m = req.getClass().getMethod("get" + methodName);
				Timestamp value = (Timestamp) m.invoke(req);
				if (value != null) {
					try {
						m = model.getClass().getMethod("set" + methodName,
								Timestamp.class);
						m.invoke(model, value);
					} catch (NoSuchMethodException e) {
						logger.warn("set" + methodName + "is not exist");
					}
				}
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
	}
	
	// 将参数值赋值给model
	public static void copyReqValueToModelDeeply(Object req, Object model) {
		HashSet<Field> fields = new HashSet<Field>();
		getSuperFields(req.getClass(), fields);
		for (Field field : fields) {
			setFieldVal(field, req, model);
		}
	}
	

	/**
	 * 获取父类的fields,一直到Object类为止
	 * @param clazz
	 * @param fields
	 */
	public static void getSuperFields(Class<?> clazz, Set<Field> fields){
	    if(fields == null) {return;}
	    
	    if(fields.size() == 0){
	        for(Field f : clazz.getDeclaredFields()){
	            fields.add(f);
	        }
	    }
	    
	    Class<?> superClazz = clazz.getSuperclass();
	    
	    if(superClazz != null && !superClazz.equals(Object.class)){
	        getSuperFields(superClazz, fields);
	    }
	    for(Field f : superClazz.getDeclaredFields()){
	        fields.add(f);
	    }
	}
	


}
