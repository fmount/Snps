package org.osgi.snps.base.daoservice;

import java.sql.DriverManager;
import java.sql.SQLException;
import com.mysql.jdbc.Connection;

/*import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException; */



      
public class MySqlDAOFactory extends DataManagerDaoFactory {
      
		static String dbName = "SensorDB";
		static String userName = "root";
		static String password = "root";

        public static final String DRIVER="com.mysql.jdbc.Driver";
        final static String DB_URL = "jdbc:mysql://127.0.0.1:3306/"+dbName+"?user="+userName+"&password="+password;
        public static Connection con;
        
        // method to create MySql connections
        public static Connection createConnection() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
              Class.forName(DRIVER).newInstance();
              //String driverName = Driver.class.getName();
              //Class.forName(driverName);
              con = (Connection) DriverManager.getConnection(DB_URL);
              return con;
        }
        
        public DataManager getDataManager(){
                try{
                      con = createConnection();
                      return new DataManager(con);
                }catch(Exception e){
                      e.printStackTrace();
                      return null;
                }
        }

}
