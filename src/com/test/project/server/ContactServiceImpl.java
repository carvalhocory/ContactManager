/*
 * Cory Carvalho
 * Contact Management Project
 * ContactServiceImpl: This class allows the client to communicate
 * and make things happen with the server involving fetching and updating information.
 */

package com.test.project.server;

import com.test.project.ContactDatabase;
import com.test.project.client.ContactService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class ContactServiceImpl extends RemoteServiceServlet implements ContactService {

	public String listContacts() throws IllegalArgumentException {
		String jsonList = ContactDatabase.contactList();
		if(jsonList.equals(null)) {
			throw new IllegalArgumentException("Empty List");
		}
		return jsonList;
	}
	
	public String updateContacts(String entry) throws IllegalArgumentException {
		String message = "";
		ContactDatabase.updateDatabase(entry);
		return message;
	}
	
	public String removeContact(String id) throws IllegalArgumentException {
		String message = "";
		ContactDatabase.removeContact(id);
		return message;
	}
}
