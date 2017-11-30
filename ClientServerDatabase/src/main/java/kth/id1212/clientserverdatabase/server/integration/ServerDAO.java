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
import kth.id1212.clientserverdatabase.server.model.FileDTO;

/**
 *
 * @author Jonas
 */
public class ServerDAO {
    private static final String ACCOUNT_TABLE_NAME = "ACCOUNT";
    private static final String CATALOG_TABLE_NAME = "CATALOG";
    
    private static final String FILENAME_COLUMN_NAME = "FILENAME";
    private static final String SIZE_COLUMN_NAME = "SIZE";
    private static final String OWNER_COLUMN_NAME = "OWNER";
    private static final String ACCESS_COLUMN_NAME = "ACCESS";
    private static final String PERMISSIONS_COLUMN_NAME = "PERMISSIONS";
    private static final String NOTIFY_COLUMN_NAME = "NOTIFY";
    
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
    
    private PreparedStatement selectFilesStatement;
    private PreparedStatement registerFileStatement;
    private PreparedStatement getFileDataStatement;
    private PreparedStatement removeFileStatement;
    
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
        if(!tableExists(connection, ACCOUNT_TABLE_NAME)){
            statement.executeUpdate("CREATE TABLE "+ACCOUNT_TABLE_NAME+" ("+USERNAME_COLUMN_NAME+" VARCHAR(32) PRIMARY KEY, " + PASSWORD_COLUMN_NAME + " VARCHAR(32))");
        }
        if(!tableExists(connection, CATALOG_TABLE_NAME)){
            statement.executeUpdate("CREATE TABLE "+CATALOG_TABLE_NAME+" ("+FILENAME_COLUMN_NAME+" VARCHAR(32) PRIMARY KEY, " + SIZE_COLUMN_NAME + " INT, "
                   + OWNER_COLUMN_NAME +" VARCHAR(32), " + ACCESS_COLUMN_NAME + " VARCHAR(32), " + PERMISSIONS_COLUMN_NAME + " VARCHAR(32), " + NOTIFY_COLUMN_NAME + " INT)");
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
    
    private boolean tableExists(Connection connection, String tablename) throws SQLException{
        int nameColumnIndex = 3;
        DatabaseMetaData data = connection.getMetaData();
        try(ResultSet rs = data.getTables(null, null, null, null)){
            for(; rs.next();){
                if(rs.getString(nameColumnIndex).equalsIgnoreCase(tablename)){
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
            }
        } catch (SQLException se){
            throw new RuntimeException("Failed to list users", se);
        }
        return usernames;
    }
    
    public List<FileDTO> listFiles() throws SQLException{
        List<FileDTO> files = new ArrayList<>();
        try(ResultSet file = selectFilesStatement.executeQuery()){
            while(file.next()){
                files.add(new FileDTO(file.getString(1), file.getInt(2), file.getString(3), file.getString(4), file.getString(5), file.getInt(6)));
            }
        } catch (SQLException se){
            throw new RuntimeException("Failed to list files", se);
        }
        return files;
    }
    
    public void addFile(FileDTO file){
        try{
            registerFileStatement.setString(1, file.getName());
            registerFileStatement.setInt(2, file.getSize());
            registerFileStatement.setString(3, file.getOwner());
            registerFileStatement.setString(4, file.getAccess());
            registerFileStatement.setString(5, file.getPermissions());
            registerFileStatement.setInt(6, file.getNotifyId());
            registerFileStatement.executeUpdate();
        } catch(SQLException se){
            throw new RuntimeException("failed to register file", se);
        }
    }
    
    public void removeFile(FileDTO file){
        try{
            removeFileStatement.setString(1, file.getName());
            removeFileStatement.executeUpdate();
        } catch(SQLException se){
            throw new RuntimeException("filed to remove file", se);
        }
    }
    
    public FileDTO getFile(String filename){
        try{
            List<FileDTO> files = listFiles();
            for(FileDTO file : files){
                if(file.getName().equals(filename)){
                    return file;
                }
            }
            return null;
        } catch(SQLException se){
            throw new RuntimeException("failed to get file");
        }
    }
    
    public boolean fileExists(String filename){
        try{
           List<FileDTO> files = listFiles();
           for(FileDTO tempfile : files){
               if(tempfile.getName().equalsIgnoreCase(filename)){
                   return true;
               }
           }
           return false;
        } catch (SQLException se){
            throw new RuntimeException("Error in fileExists of ServerDAO", se);
        }
    }
    
    //Here are all the SQL database methods such as creating tables and such.
    private void prepareStatements(Connection connection) throws SQLException{
        selectUsersStatement = connection.prepareStatement("SELECT * FROM " + ACCOUNT_TABLE_NAME);
        registerUserStatement = connection.prepareStatement("INSERT INTO " + ACCOUNT_TABLE_NAME + " VALUES (?, ?)");
        removeUserStatement = connection.prepareStatement("DELETE FROM " + ACCOUNT_TABLE_NAME + " WHERE username=?");
        getCredentialsStatement = connection.prepareStatement("SELECT " + PASSWORD_COLUMN_NAME + " FROM " + ACCOUNT_TABLE_NAME + " WHERE " + USERNAME_COLUMN_NAME + "=?");
        
        selectFilesStatement = connection.prepareStatement("SELECT * FROM " + CATALOG_TABLE_NAME);
        registerFileStatement = connection.prepareStatement("INSERT INTO " + CATALOG_TABLE_NAME + " VALUES (?, ?, ?, ?, ?, ?)");
        removeFileStatement = connection.prepareStatement("DELETE FROM " + CATALOG_TABLE_NAME + " WHERE filename=?");
        getFileDataStatement = connection.prepareStatement("SELECT * FROM " + CATALOG_TABLE_NAME + " WHERE " + FILENAME_COLUMN_NAME + "=?");
    }
}
