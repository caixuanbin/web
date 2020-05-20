package cn.sessiontech.xcx.utils;

import cn.sessiontech.xcx.common.QueryObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

@Slf4j
public class ReflectUtils {

	/**
	 * 获取对象泛型参数类型
	 * @param obj
	 * @return Class<?>
	 */
	public static Class<?> getGenericParamType(Object obj) {
		//目的：得到实际类型参数
		//得到当前运行对象
		Class<?> clazz = obj.getClass();
		//得到当前对象父类的参数化类型,一般使用type子接口ParameterizedType
		Type type = clazz.getGenericSuperclass();
		ParameterizedType ptype=(ParameterizedType)type;
		//得到实际类型参数
		Type[] types = ptype.getActualTypeArguments();
		Class<?> clazzParameter=(Class<?>)types[0];
		return clazzParameter;
	}
	
	/**
	 * 获取类属性
	 * @param clazz
	 * @param name
	 * @return Field
	 */
	public static Field getFileByName(Class<?> clazz, String name) {
		Field field = ReflectionUtils.findField(clazz,name);
		return field;
	}

	
	/**
	 * 获取属性值
	 * @param field 属性
	 * @param obj 对象
	 * @return
	 */
	public static Object getValue(Field field ,Object obj) {
        try {
        	//设置对象的访问权限，保证对private的属性的访问
        	field.setAccessible(true);
			return field.get(obj);
		} catch (Exception e) {
			log.error("",e);
		}
        return null;
	}
	
	/**
	 * 设置属性值
	 * @param field 属性
	 * @param obj 对象
	 * @param value 属性值
	 */
	public static void setValue(Field field ,Object obj,Object value) {
		try {
			//设置对象的访问权限，保证对private的属性的访问
			field.setAccessible(true);
			field.set(obj, value);
		} catch (Exception e) {
			log.error("",e);
		}
	}
	
	/**
	 * 设置属性值
	 * @param fieldName 属性名称
	 * @param obj 对象
	 * @param value 属性值
	 */
	public static void setValue(String fieldName ,Object obj,Object value) {
		if(obj==null) {
			return;
		}
		try {
			Field field = ReflectUtils.getFileByName(obj.getClass(), fieldName);
			if(field!=null) {
				ReflectUtils.setValue(field, obj, value);
			}
		} catch (Exception e) {
			log.error("",e);
		}
	}

	/**
	 * 将对象不为空单独部分封装到map返回
	 * @param o
	 * @return
	 */
	public static Map<String, String[]> allNotEmptyValue(Object o){
		Map<String, String[]> params = new HashMap<String, String[]>();
		Class tempClass = o.getClass();
		try{
			while(tempClass!=null){
				for(Field field:tempClass.getDeclaredFields()){
					field.setAccessible(true);//把私有属性公有化
					Object object = field.get(o);
					String fileName = field.getName();
					if(!ObjectUtils.isEmpty(object)){
						if(!Objects.equals(fileName,"serialVersionUID")&&!Objects.equals(fileName,"currentPage")
								&&!Objects.equals(fileName,"pageSize")){
							params.put(fileName,new String[]{String.valueOf(object)});
						}
					}
				}
				tempClass = tempClass.getSuperclass();
			}

		}catch (Exception e){
			e.printStackTrace();
		}
		return params;
	}
	/**
	 *
	 *  针对JPA 封装的根据传入的实体将不为空的属性封装成JPA查询分页查询所需要的集合
	 */
	public static List<QueryObject<String,Object>> getJpaNotEmptyValue(Object o){
		List<QueryObject<String,Object>> lists = new ArrayList<>();
		Class tempClass = o.getClass();
		try{
			while(tempClass!=null){
				for(Field field:tempClass.getDeclaredFields()){
					//把私有属性公有化
					field.setAccessible(true);
					Object object = field.get(o);
					String fileName = field.getName();
					if(!ObjectUtils.isEmpty(object)){
						if(!Objects.equals(fileName,"serialVersionUID")&&!Objects.equals(fileName,"currentPage")
								&&!Objects.equals(fileName,"pageSize")&&!Objects.equals(fileName,"beginTime")
								&&!Objects.equals(fileName,"endTime")){
							lists.add(new QueryObject<>(fileName, object));
						}
					}
				}
				tempClass = tempClass.getSuperclass();
			}

		}catch (Exception e){
			e.printStackTrace();
		}
		return lists;
	}
	
}
