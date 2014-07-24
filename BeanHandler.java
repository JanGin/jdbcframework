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
			//��ý����������
			int columnCount = metadata.getColumnCount();
			for (int i = 0; i < columnCount; i++){
				//ͨ��������ȡ������
				String columnName = metadata.getColumnName(i + 1);
				//��ȡ���е�����
				Object columnData = rs.getObject(i + 1);
				//��������Ӧ�������ݵ�bean�ϵ��ֶ�
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
