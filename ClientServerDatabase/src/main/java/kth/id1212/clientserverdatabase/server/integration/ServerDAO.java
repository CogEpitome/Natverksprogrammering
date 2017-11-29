/*
 * Database Access Object
 */
package kth.id1212.clientserverdatabase.server.integration;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jonas
 */
public class ServerDAO {
    private static final String TABLE_NAME = "tablename";
    private static final String COLUMN_NAME = "";
    private static final String USER_COLUMN_NAME = "username";
    
    private static final String MYSQL_USERNAME = "user";
    private static final String MYSQL_PASSWORD = "password";
    private static final String DERBY_USERNAME = "usr";
    private static final String DERBY_PASSWORD = "pwd";
    
    private PreparedStatement selectUsersStatement;
    
    public ServerDAO(String dbtype, String source) throws RuntimeException{
        try{
            Connection connection = createSource(dbtype, source);
            prepareStatements(connection);
        } catch (ClassNotFoundException | SQLException e){
            throw new RuntimeException("Couldn't connect to datasource", e);
        }
    }
    
    private Connection createSource(String type, String source) throws ClassNotFoundException, SQLException{
        Connection connection = connectDB(type, source);
        Statement statement = connection.createStatement();
        if(!tableExists(connection)){
            statement.executeUpdate("CREATE TABLE "+TABLE_NAME+" ("+USER_COLUMN_NAME+" VARCHAR(32) PRIMARY KEY)");
        }
        return connection;
    }
    
    private Connection connectDB(String type, String source) throws ClassNotFoundException, SQLException {
        if(type.equalsIgnoreCase("derby")){
            Class.forName("org.apache.derby.jdbc.ClientXADataSource");
            return DriverManager.getConnection("jdbc:derby://localhost:1527/" + source, DERBY_USERNAME, DERBY_PASSWORD);
        } else
        if(type.equalsIgnoreCase("mysql")){
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://localhost_3306/" + source, MYSQL_USERNAME, MYSQL_PASSWORD);
        } else {
            throw new RuntimeException("Unsupported dbms, supported types are: derby, and mysql.");
        }
    }
    
    private boolean tableExists(Connection connection) throws SQLException{
        int nameColumnIndex = 3;
        DatabaseMetaData data = connection.getMetaData();
        try(ResultSet rs = data.getTables(null, null, null, null)){
            for(; rs.next();){
                if(rs.getString(nameColumnIndex).equalsIgnoreCase(TABLE_NAME)){
                    return true;
                }
            }
            return false;
        } 
    }
    
    public List<String> listUsers() throws SQLException{
        List<String> usernames = new ArrayList<>();
        try(ResultSet users = selectUsersStatement.executeQuery()){
            while(users.next()){
                usernames.add(users.getString(1));
                System.out.println("name "+users.getString(1));
            }
        } catch (SQLException se){
            throw new RuntimeException("Failed to list users", se);
        }
        return usernames;
    }
    
    //Here are all the SQL database methods such as creating tables and such.
    private void prepareStatements(Connection connection) throws SQLException{
        selectUsersStatement = connection.prepareStatement("SELECT * FROM " + TABLE_NAME);
    }
}
