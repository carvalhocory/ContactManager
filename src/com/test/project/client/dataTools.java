/*
 * Cory Carvalho
 * Contact Management Project
 * Data Tools: The focus of this class is to clean up the main entry
 * of the web app, by moving large functions that do not serve direct
 * purpose in client/server interactions.
 */

package com.test.project.client;

import com.google.gwt.user.client.ui.FlexTable;

public class dataTools {
		
		//In addition to resetting the table, this re-supplies the header.
		public static FlexTable clearTable(FlexTable table) {	
			table.setText(0, 0, "Name");
			table.setText(0, 1, "Email");
			table.setText(0, 2, "Telephone");
			table.setText(0, 3, "Street");
			table.setText(0, 4, "City");
			table.setText(0, 5, "State");
			table.setText(0, 6, "Zip");
			table.setText(0, 7, "Update");
			table.setText(0, 8, "Remove");
			return table;
		}
		
		/*Checks to see if any entry values are blank,
		 doesn't currently check for specifics.*/
		public static boolean goodEntry(String[] entry) {
			boolean good = true;
			if(entry[0].equals("")) {
				good = false;
			}
			
			if(entry[1].equals("")) {
				good = false;
			}
			
			if(entry[2].equals("")) {
				good = false;
			}
			
			if(entry[3].equals("")) {
				good = false;
			}
			
			if(entry[4].equals("")) {
				good = false;
			}
			
			if(entry[5].equals("")) {
				good = false;
			}
			
			if(entry[6].equals("")) {
				good = false;
			}
			return good;
		}
}
