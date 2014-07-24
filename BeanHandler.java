package myeclipse.day16.jdbcframework;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;


public class BeanHandler implements ResultSetHandler {

	private Class<?> clazz;
	public BeanHandler(Class<?> clazz){
		this.clazz = clazz;
	}
	
	
	@Override
	public Object handler(ResultSet rs) {
		
		try {
			if (!rs.next()){
				return null;
			}
			ResultSetMetaData metadata = rs.getMetaData();
			Object bean = clazz.newInstance();
			//获得结果集的列数
			int columnCount = metadata.getColumnCount();
			for (int i = 0; i < columnCount; i++){
				//通过列数获取到列名
				String columnName = metadata.getColumnName(i + 1);
				//获取到列的数据
				Object columnData = rs.getObject(i + 1);
				//反射出相对应表中数据的bean上的字段
				Field field = clazz.getDeclaredField(columnName);
				field.setAccessible(true);
				field.set(bean, columnData);
			}
			
			return bean;
		} catch (Exception e) {
			
			throw new ExceptionInInitializerError(e);
		} 
	}

}
