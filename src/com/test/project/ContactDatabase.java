/*
 * Cory Carvalho
 * Contact Management Project
 * ContactDatabase: This class creates our database, and has
 * the means to update and remove records from it.
 */

package com.test.project;

import java.io.StringWriter;
import java.sql.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONArray;

public class ContactDatabase {
	private static Connection c;
    private static Statement stmt;
    private static JSONParser parser = new JSONParser();
    
    private ContactDatabase() 
    {
        c = null;
        stmt = null;
    }
    
    //Used to make the initial database.
    public static void openDatabase() {
        
    	try {
            
        	Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:ContactDatabase.db");
            c.setAutoCommit(false);
            System.out.println("it works");
        
    	} catch ( ClassNotFoundException | SQLException e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }
    
    //Makes initial database with contacts table and ex. entry.
    public static void initialDatabase()
    {
      
    	try {
    		stmt = c.createStatement();

    		String sql = "CREATE TABLE CONTACTS " +
                     "(ID        INTEGER   PRIMARY KEY," +
                     " NAME      TEXT ," +
                     " EMAIL     TEXT," +
                     " TELEPHONE TEXT," +
                     " STREET    TEXT," +
                     " CITY      TEXT," +
                     " STATE     TEXT," +
                     " ZIP       TEXT)";  
    		stmt.executeUpdate(sql);

    		sql = "INSERT INTO CONTACTS (NAME, EMAIL, TELEPHONE, STREET, CITY, STATE, ZIP) " +
                             "VALUES ('Cory', 'carvalhocory@gmail.com', '5086495769', "
                             + "'213 chicago street', 'Fall River', 'MA', '02721');"; 
        
    		stmt.executeUpdate(sql);

    		c.commit();
    		
    		c.close();
    		stmt.close();
    	} catch ( SQLException e ) {
    		System.out.println("Database tables already created.");
    		return;
    	}
        	System.out.println("Table created successfully.");
    }
    
    //Gets the entire list of contacts from our database and returns them as a JSON string.
    @SuppressWarnings("unchecked")
	public static String contactList() {
    	
        String contactList = "";
        
        //JSON holders for our query result set.
        JSONArray list = new JSONArray();
        
        boolean empty = true;
        
        try {
            
        	//Get our connection to the database ready.
        	Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:ContactDatabase.db");
            c.setAutoCommit(false);

            //Create statement, send query acquiring the result set of all contacts.
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM CONTACTS;");
            while ( rs.next() ) {
            	JSONObject obj = new JSONObject();
            	String ID = rs.getString("ID");
            	obj.put("id", ID);
                String Name = rs.getString("NAME");
                obj.put("name", Name);
                String Email = rs.getString("EMAIL");
                obj.put("email", Email);
                String Phone = rs.getString("TELEPHONE");
                obj.put("phone", Phone);
                String Street = rs.getString("STREET");
                obj.put("street", Street);
                String City = rs.getString("CITY");
                obj.put("city", City);
                String State = rs.getString("STATE");
                obj.put("state", State);
                String Zip = rs.getString("ZIP");
                obj.put("zip", Zip);
                
                list.add(obj);
                
                if(empty == true) {
                	empty = false;
                }
            }
        
        //JSON to String.
        StringWriter out = new StringWriter();
        list.writeJSONString(out);
        contactList = out.toString();
        
        if(empty == true) {
        	contactList = null;
        }
        
        //Close connection.
        rs.close();
        stmt.close();
        c.close();
        
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
            System.out.println("List aquired.");
            return contactList;
    }
    
    public static void updateDatabase(String entry) {
       
    	try {
			Object temp = parser.parse(entry);
			JSONObject obj = (JSONObject)temp;
			Connection update = null;
			Statement uStmt = null;
			String sql = "";
			try {
	        	//Get our connection to the database ready.
	        	Class.forName("org.sqlite.JDBC");
	            update = DriverManager.getConnection("jdbc:sqlite:ContactDatabase.db");
	            update.setAutoCommit(false);
	            
	            uStmt = update.createStatement();
	            
	            //If it is a new contact it gets it's own row, if this is from
	            //an existing ID, it's row is updated.
	            if(obj.get("id").equals("0")) {
	            	sql = "INSERT INTO CONTACTS (NAME, EMAIL, TELEPHONE, STREET, CITY, STATE, ZIP) " +
	            			"VALUES ('" +  obj.get("name") + "', '" + obj.get("email") + "', '" 
	            			+ obj.get("phone") + "', '" + obj.get("street") + "', '" + obj.get("city") +
	            			"', '" + obj.get("state") + "', '" + obj.get("zip") + "');"; 
	            } else {
	            	sql = "UPDATE CONTACTS SET NAME = '" + obj.get("name") + "', EMAIL = '" + obj.get("email") + 
	            			"', TELEPHONE = '" + obj.get("phone") + "', STREET = '" + obj.get("street") + 
	            			"', CITY = '" + obj.get("city") + "', STATE = '" + obj.get("state") + "', ZIP = '" + 
	            			obj.get("zip") + "' WHERE ID = '" + obj.get("id") +"';"; 
	            }
	            
	            uStmt.executeUpdate(sql);

	            //Commit, Close connection.
	            update.commit();
	            uStmt.close();
	            update.close();
	        
	        } catch ( Exception e ) {
	            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	        }
		} catch (ParseException e) {
			e.printStackTrace();
		}
    }
    
    public static void removeContact(String id) {
    	
    	try {
			Object temp = parser.parse(id);
			JSONObject obj = (JSONObject)temp;
			Connection del = null;
			Statement rStmt = null;
			try {
	        	//Get our connection to the database ready.
	        	Class.forName("org.sqlite.JDBC");
	            del = DriverManager.getConnection("jdbc:sqlite:ContactDatabase.db");
	            del.setAutoCommit(false);
	            
	            rStmt = del.createStatement();
	            
	            //Delete from contacts using the given ID.
	            String sql = "DELETE FROM CONTACTS WHERE ID = " + obj.get("id") + ";";   
   
	            rStmt.execute(sql);

	            //Commit, Close connection.
	            del.commit();
	            rStmt.close();
	            del.close();
	        
	        } catch ( Exception e ) {
	            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	        }
		} catch (ParseException e) {
			e.printStackTrace();
		}
    }
}
