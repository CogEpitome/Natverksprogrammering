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
import kth.id1212.clientserverdatabase.common.Account;

/**
 *
 * @author Jonas
 */
public class ServerDAO {
    private static final String TABLE_NAME = "ACCOUNT";
    private static final String COLUMN_NAME = "";
    private static final String USERNAME_COLUMN_NAME = "USERNAME";
    private static final String PASSWORD_COLUMN_NAME = "PASSWORD";
    
    private static final String MYSQL_USERNAME = "user";
    private static final String MYSQL_PASSWORD = "password";
    private static final String DERBY_USERNAME = "usr";
    private static final String DERBY_PASSWORD = "pwd";
    
    private PreparedStatement selectUsersStatement;
    private PreparedStatement registerUserStatement;
    private PreparedStatement getCredentialsStatement;
    private PreparedStatement removeUserStatement;
    
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
            statement.executeUpdate("CREATE TABLE "+TABLE_NAME+" ("+USERNAME_COLUMN_NAME+" VARCHAR(32) PRIMARY KEY, " + PASSWORD_COLUMN_NAME + " VARCHAR(32))");
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
    
    public boolean userExists(String username) throws RuntimeException{
        try{
           List<String> users = listUsers();
           for(String user : users){
               if(user.equalsIgnoreCase(username)){
                   return true;
               }
           }
           return false;
        } catch (SQLException se){
            throw new RuntimeException("Error in userExists of ServerDAO", se);
        }
    }
    
    public boolean credentialsMatch(String username, String password) throws RuntimeException{
        try{
            getCredentialsStatement.setString(1, username);
            ResultSet matchingPasswords = getCredentialsStatement.executeQuery();
            if(matchingPasswords.next()){
                if(matchingPasswords.getString(1).equals(password)){
                    return true;
                }
            }
            return false;
        } catch (Exception e){
            throw new RuntimeException("whopsee", e);
        }
    }
    
    public void register(Account account) throws RuntimeException{
        try{            
            registerUserStatement.setString(1, account.getUsername());
            registerUserStatement.setString(2, account.getPassword());
            registerUserStatement.executeUpdate();
        } catch(SQLException e){
            throw new RuntimeException("Failed to register user", e);
        } catch(RuntimeException re){
            throw new RuntimeException("userExists failed!", re);
        }
    }
    
    public void remove(String username){
        try{
            removeUserStatement.setString(1, username);
            removeUserStatement.executeUpdate();
        } catch (SQLException se){
            System.out.println("Failed to remove user");
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
        registerUserStatement = connection.prepareStatement("INSERT INTO " + TABLE_NAME + " VALUES (?, ?)");
        removeUserStatement = connection.prepareStatement("DELETE FROM " + TABLE_NAME + " WHERE username=?");
        getCredentialsStatement = connection.prepareStatement("SELECT " + PASSWORD_COLUMN_NAME + " FROM " + TABLE_NAME + " WHERE " + USERNAME_COLUMN_NAME + "=?");
    }
}
