package myeclipse.day16.jdbcframework;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;

@SuppressWarnings("static-access")
public class JdbcFramework {

	//使用dbcp创建数据库连接池
	private static DataSource ds = null;
	static {
		
		try{
			InputStream in = JdbcFramework.class.getClassLoader().getResourceAsStream("dbcpconfig.properties");
			Properties prop = new Properties();
			prop.load(in);
			
			BasicDataSourceFactory factory = new BasicDataSourceFactory();
			ds = factory.createDataSource(prop);
			
		} catch(Exception e){
			throw new ExceptionInInitializerError(e);
		}
	}
	
	public static Connection getConnection() throws SQLException{
		
		return ds.getConnection();
	}
	
	public static void release(Connection conn,Statement st,ResultSet rs){
		
		if (rs != null){
			try{
				rs.close();
			} catch(SQLException e){
				rs = null;
			}
		}
		if (st != null){
			try{
				st.close();
			} catch(SQLException e){
				st = null;
			}
		}
		if (conn != null){
			try{
				conn.close();
			} catch(SQLException e){
				conn = null;
			}
		}
		
	}
	
	/*
	 * 提供数据库insert,delete,update操作
	 */
	public static int update(String sql) throws SQLException{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			//通过数据库连接池获取连接
			conn = ds.getConnection();
			//预编译sql语句
			ps = conn.prepareStatement(sql);
			int status = ps.executeUpdate();
			return status;
		} finally{
			release(conn,ps,rs);
		}
		
	}
	
	/*重载update()*/
	public static int update(String sql,Object[] param) throws SQLException{
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			//通过数据库连接池获取连接
			conn = ds.getConnection();
			//预编译sql语句
			ps = conn.prepareStatement(sql);
			for (int i = 0;i < param.length; i++){
				ps.setObject(i+1,param[i]);
			}
			int status = ps.executeUpdate();
			return status;
		} finally{
			release(conn,ps,rs);
		}
		
	}
	
	public static Object query(String sql,ResultSetHandler rsh) throws SQLException{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			conn = ds.getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			//结果集交给handler处理
			return rsh.handler(rs);
		} finally{
			release(conn,ps,rs);
		}
	}
	
	/*重载query()*/
	public static Object query(String sql,Object[] param,ResultSetHandler rsh) throws SQLException{
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			conn = ds.getConnection();
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < param.length; i++){
				ps.setObject(i+1, param[i]);
			}
			rs = ps.executeQuery();
			//结果集交给handler处理
			return rsh.handler(rs);
		} finally{
			release(conn,ps,rs);
		}
	}
}
