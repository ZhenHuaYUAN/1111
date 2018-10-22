package af.util;

import java.lang.reflect.Method;
import java.util.Map;

/*
 * 支持 byte, short, int ,long, float, double, boolean
 *     Byte, Short, Integer, Long, Float, Double, Boolean
 *     String的自动映射
 * 除此之外的类型，请手动调用 setter, 例如Date类型，应在map之后再自己另外处理
 */
public class AfReflect2
{
	public static Object map(Class clazz, Map<String, Object> attrs) throws Exception
	{
		Object obj = clazz.newInstance();		
		map(obj, clazz, attrs);
		return obj;
	}
	
	public static Object map(Object obj, Class clazz, Map<String, Object> attrs) throws Exception
	{
		Method[] methods = clazz.getMethods();
		for(String key : attrs.keySet())
		{
			Object value = attrs.get(key);
			if (value == null)
			{
				continue;
			}
			
			// 标准化setter的名字，例如 username -> setUsername
			char firstChar = Character.toUpperCase(key.charAt(0));
			StringBuffer strbuf = new StringBuffer("set" + key);
			strbuf.setCharAt(3, firstChar);

			// 找到对应的Method
			String methodName = strbuf.toString();
			Method method = null;
			for (Method m : methods)
			{
				if (m.getName().equals(methodName))
				{
					method = m;
					break;
				}
			}

			if (method != null)
			{
				// 查看参数类型
				Class[] pt = method.getParameterTypes();

				// 执行setter
				Object p0 = AfReflect2.createObjectByType(pt[0], value);
				Object args[] = { p0 };
				try{
					if(p0 != null) method.invoke(obj, args);
				}
				catch(IllegalArgumentException e)
				{
					throw new Exception("IllegalArgumentException: need " + pt[0].getName() + ",but " + value.getClass().getCanonicalName());
				}
			}
		}	
		
		return obj;
	}
	
	// 根据参数类型，构造一个对象
	public static Object createObjectByType(Class clazz, Object value)
	{		
		// System.out.println("参数类型: need " + clazz.getName() + ",but " + value.getClass().getName());

		Class c2 = value.getClass();
		if(clazz.equals( c2 )) return value; // 类型相同
		
		if(clazz.equals( int.class) || clazz.equals(Integer.class))
		{
			if( c2.equals(int.class) || c2.equals(Integer.class)) return value;
			else return Integer.valueOf(value.toString());
		}
		if(clazz.equals( long.class) || clazz.equals(Long.class))
		{
			if( c2.equals(long.class) || c2.equals(Long.class)) return value;
			else return Long.valueOf(value.toString());
		}
		if(clazz.equals( short.class) || clazz.equals(Short.class))
		{
			if( c2.equals(short.class) || c2.equals(Short.class)) return value;
			else return Short.valueOf(value.toString());
		}
		if(clazz.equals( byte.class) || clazz.equals(Byte.class))
		{
			if( c2.equals(byte.class) || c2.equals(Byte.class)) return value;
			else return Integer.valueOf(value.toString());
		}
		if(clazz.equals( boolean.class) || clazz.equals(Boolean.class))
		{
			if( c2.equals(boolean.class) || c2.equals(Boolean.class)) return value;
			else return Boolean.valueOf(value.toString());
		}
		if(clazz.equals( float.class) || clazz.equals(Float.class))
		{
			if( c2.equals(float.class) || c2.equals(Float.class)) return value;
			else return Float.valueOf(value.toString());
		}
		if(clazz.equals( double.class) || clazz.equals(double.class))
		{
			if( c2.equals(Double.class) || c2.equals(Double.class)) return value;
			else return Double.valueOf(value.toString());
		}
		if(clazz.equals(String.class))
		{
			return value.toString();
		}
		return null; // return null说明类型不匹配, 不应执行方法
		
//		String t = clazz.getName();
//		if(t.equals("int") || t.equals("java.lang.Integer"))
//		{
//			return Integer.valueOf(value.toString());
//		}
//		if(t.equals("byte") || t.equals("java.lang.Byte"))
//		{
//			return Byte.valueOf(value.toString());
//		}
//		if(t.equals("short") || t.equals("java.lang.Short"))
//		{
//			return Short.valueOf(value.toString());
//		}
//		if(t.equals("long") || t.equals("java.lang.Long"))
//		{
//			return Long.valueOf(value.toString());
//		}
//		if(t.equals("boolean") || t.equals("java.lang.Boolean"))
//		{
//			return Boolean.valueOf(value.toString());
//		}
//		if(t.equals("java.lang.String"))
//		{
//			return value;
//		}
//		if(t.equals("java.util.Date"))
//		{
//			System.out.println(value.getClass().getName());
//			String s = value.toString();
//			return Date.valueOf(value.toString());
//		}
		
	}	
	
}
