/*
 * Cory Carvalho
 * Contact Management Project
 * ContactServiceAsync
 */

package com.test.project.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ContactServiceAsync {
	void listContacts(AsyncCallback<String> callback);
	void updateContacts(String input, AsyncCallback<String> callback) throws IllegalArgumentException;
	void removeContact(String input, AsyncCallback<String> callback) throws IllegalArgumentException;
}
