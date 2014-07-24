package myeclipse.day16.jdbcframework;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

public class BeanListHandler implements ResultSetHandler {

	private Class<?> clazz;
	private List<Object> list;
	
	public BeanListHandler(Class<?> clazz){
		this.clazz = clazz;
	}
	
	
	@Override
	public List<Object> handler(ResultSet rs) {
		list = new ArrayList<Object>();
		
		try {
			while(rs.next()){
				Object bean = clazz.newInstance();
				ResultSetMetaData metadata = rs.getMetaData();
				int columnCount = metadata.getColumnCount();
				for (int i = 0; i < columnCount; i++){
					String columnName = metadata.getColumnName(i + 1);
					Object columnData = rs.getObject(columnName);
					Field field = clazz.getDeclaredField(columnName);
					field.setAccessible(true);
					field.set(bean, columnData);
				}				
				list.add(bean);
			}	
			return list.size() > 0?list:null;
		} catch (Exception e) {

			throw new ExceptionInInitializerError(e);
		}	
	}

}
