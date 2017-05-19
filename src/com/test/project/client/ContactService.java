/*
 * Cory Carvalho
 * Contact Management Project
 * ContactService
 */

package com.test.project.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("greet")
public interface ContactService extends RemoteService {
	String listContacts();
	String updateContacts(String entry) throws IllegalArgumentException;
	String removeContact(String id) throws IllegalArgumentException;
}
