/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kth.id1212.clientserverdatabase;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Jonas
 */
public class DatabaseConnection {
    private static final String TABLE_NAME = "person";
    private PreparedStatement createPersonStatement;
    private PreparedStatement findAllPersonsStatement;
    private PreparedStatement deletePersonStatement;
    
    private void access(){
        try {
            Class.forName("org.apache.derby.jdbc.ClientXADataSource");
            Connection connection = DriverManager.getConnection("jdbc:derby://localhost:1527/HW4DB", "usr", "pwd");
            createTable(connection);
            prepareStatements(connection);
            createPersonStatement.setString(1,"stina");
            createPersonStatement.setString(2,"123456789");
            createPersonStatement.setInt(3,42);
            createPersonStatement.executeUpdate();
            createPersonStatement.setString(1,"olle");
            createPersonStatement.setString(2,"6969696969");
            createPersonStatement.setInt(3,15);
            createPersonStatement.executeUpdate();
            listAllRows(connection);
            deletePersonStatement.setString(1, "stina");
            deletePersonStatement.executeUpdate();
            listAllRows(connection);
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void createPerson(String name, String nr, int age)throws SQLException{
        createPersonStatement.setString(1,"stina");
        createPersonStatement.setString(2,"123456789");
        createPersonStatement.setInt(3,42);
        createPersonStatement.executeUpdate();
    }
    
    private void createTable(Connection connection) throws SQLException {
        if(!tableExists(connection)){
            Statement statement = connection.createStatement();
            statement.executeUpdate("create table " + TABLE_NAME + " (name varchar(32) primary key, phone varchar(12), age int)");
        }
    }

    private boolean tableExists(Connection connection) throws SQLException {
        DatabaseMetaData dbMetaData = connection.getMetaData();
        ResultSet metaDataTable = dbMetaData.getTables(null, null,null, null);
        while(metaDataTable.next()){
            String tableName = metaDataTable.getString(3);
            if(tableName.equalsIgnoreCase(TABLE_NAME)){
                return true;
            }
        }
        return false;
    }
    
    private void listAllRows(Connection connection) throws SQLException{
        ResultSet persons = findAllPersonsStatement.executeQuery();
        while(persons.next()){
            System.out.println("name "+persons.getString(1)+". phone: "+persons.getString(2)+". age: "+persons.getInt(3));
        }
    }
    
    private void prepareStatements(Connection connection) throws SQLException{
        createPersonStatement = connection.prepareStatement("INSERT INTO " + TABLE_NAME + " VALUES (?, ?, ?)");
        findAllPersonsStatement = connection.prepareStatement("SELECT * FROM " + TABLE_NAME);
        deletePersonStatement = connection.prepareStatement("DELETE FROM " + TABLE_NAME + " WHERE name = ?");
    }
    
    public static void main(String[]args){
        new DatabaseConnection().access();
    }
    
}
