/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dal.sql;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import javax.sql.DataSource;

/**
 *
 * @author GamerGruft
 */
public class DataSourceSingleton {
    
    private static DataSource instance;
    
    private static final String SERVER_NAME = "localhost";
    private static final String DATABASE_NAME = "MovieDB";
    private static final String USER = "sa";
    private static final String PASSWORD = "SQL";
    
    private DataSourceSingleton() {}

    public static DataSource getInstance() {
        if (instance==null) {
            instance=createInstance();
        }
        return instance;
    }
    
    private static DataSource createInstance() {
        SQLServerDataSource dataSource=new SQLServerDataSource();
        
        dataSource.setUser(USER);
        dataSource.setPassword(PASSWORD);
        dataSource.setServerName(SERVER_NAME);
        dataSource.setDatabaseName(DATABASE_NAME);
        
        return dataSource;
    }
}
