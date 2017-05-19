/*
 * Cory Carvalho
 * Contact Management Project
 * ContactManager: This is the main part of the client where calls to
 * the server are made, and UI elements are generated to display/manipulate information.
 */

package com.test.project.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ContactManager implements EntryPoint {

	 //Create a remote service proxy to talk to the server-side Greeting service.
	private final ContactServiceAsync contactService = GWT.create(ContactService.class);
	
	//Vertical panel, with the label/text holders for formatting, and our data table.
	final VerticalPanel mainHolder = new VerticalPanel();
	final HorizontalPanel firstHolder = new HorizontalPanel();
	final HorizontalPanel secondHolder = new HorizontalPanel();
	FlexTable contactTable = new FlexTable();
	
	//Buttons for sending/listing data.
	final Button listButton = new Button("List Entries");
	final Button sendButton = new Button("Update/Add Entry");
	
	//Text boxes for adding/updating contacts.
	final TextBox nameText = new TextBox();
	final TextBox emailText = new TextBox();
	final TextBox phoneText = new TextBox();
	final TextBox streetText = new TextBox();
	final TextBox cityText = new TextBox();
	final TextBox stateText = new TextBox();
	final TextBox zipText = new TextBox();
	
	//Current status of updating, if inputs are good etc.
	final Label status = new Label("Status");
	
	//JSON value names to avoid excessive duplicate code.
	final String[] contactAttributes = {"name", "email", "phone", 
			"street", "city", "state", "zip"};
	
	//Entry point method, loads the UI and event handlers for it.
	public void onModuleLoad() {
		
		//Add column identifiers.
		dataTools.clearTable(contactTable);
		
		//Adjust size of UI elements for consistency.
		nameText.setPixelSize(100, 20);
		emailText.setPixelSize(120, 20);
		phoneText.setPixelSize(80, 20);
		streetText.setPixelSize(120, 20);
		cityText.setPixelSize(100, 20);
		stateText.setPixelSize(40, 20);
		zipText.setPixelSize(60, 20);
		
		//First segment of text boxes.
		firstHolder.add(new Label("Name: "));
		firstHolder.add(nameText);
		firstHolder.add(new Label("Email: "));
		firstHolder.add(emailText);
		firstHolder.add(new Label("Phone: "));
		firstHolder.add(phoneText);
		
		//Second segment of text boxes.
		secondHolder.add(new Label("Street: "));
		secondHolder.add(streetText);
		secondHolder.add(new Label("City: "));
		secondHolder.add(cityText);
		secondHolder.add(new Label("State: "));
		secondHolder.add(stateText);		
		secondHolder.add(new Label("Zip: "));
		secondHolder.add(zipText);
		
		//controlsHolder.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		mainHolder.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		//Add buttons and table to vertical panel.
		mainHolder.add(listButton);
		mainHolder.add(firstHolder);
		mainHolder.add(secondHolder);
		mainHolder.add(sendButton);
		mainHolder.add(new Label("-----------------------------------------------"));
		mainHolder.add(status);
		mainHolder.add(new Label("-----------------------------------------------"));
		mainHolder.add(contactTable);

		//Add vertical panel with table and buttons.
		RootPanel.get("mainHolderContainer").add(mainHolder);
		
		//Handler for listing contacts.
		listButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				listButton.setEnabled(false);
				listContacts();
			}
		});
		
		//Handler for updating contacts.
		sendButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				sendButton.setEnabled(false);
				String[] entry = {nameText.getText(), emailText.getText(), 
						phoneText.getText(), streetText.getText(), cityText.getText(), 
						stateText.getText(), zipText.getText()};
				if(dataTools.goodEntry(entry) == true) {
					updateContacts("new");
				} else {
					status.setText("Entry isn't valid to send.");
					sendButton.setEnabled(true);
				}
			}
		});
	}
	
	//Removes the contact record in accordance with it's button.
	private void removeRequest(String id) {
		JSONObject jsonObject = new JSONObject();
        
		jsonObject.put("id", new JSONString(id));

        String entry = jsonObject.toString();
        
        contactService.removeContact(entry, new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				status.setText("Could not remove contact.");
				listButton.setEnabled(true);
			}
			
			public void onSuccess(String result) {
				status.setText("Contact successfully removed.");
				listContacts();
				}
        });
	}
	
	//Takes the user's current entered values, sends them as a JSON.
	private void updateContacts(String id) {
		JSONObject jsonObject = new JSONObject();
        
		jsonObject.put("id", new JSONString("0"));
		jsonObject.put("name", new JSONString(nameText.getText()));
		jsonObject.put("email", new JSONString(emailText.getText()));
		jsonObject.put("phone", new JSONString(phoneText.getText()));
		jsonObject.put("street", new JSONString(streetText.getText()));
		jsonObject.put("city", new JSONString(cityText.getText()));
		jsonObject.put("state", new JSONString(stateText.getText()));
		jsonObject.put("zip", new JSONString(zipText.getText()));
		
		if(!id.equals("new")) {
			jsonObject.put("id", new JSONString(id));
		}
		
        String entry = jsonObject.toString();
        
        contactService.updateContacts(entry, new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				status.setText("Some error occured trying to send the contact.");
				sendButton.setEnabled(true);
			}
			
			public void onSuccess(String result) {
				status.setText("Successfully retrieved contact list.");
				listContacts();
				sendButton.setEnabled(true);
			}
        });
	}
	
	private void listContacts() {
		//Clear out previous table.
		mainHolder.remove(contactTable);
		contactTable = new FlexTable();
		dataTools.clearTable(contactTable);
		mainHolder.add(contactTable);
		
		contactService.listContacts(new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				status.setText("There are no contacts to list.");
				listButton.setEnabled(true);
			}

			public void onSuccess(String result) {
				JSONObject jsonObject;
				JSONValue jsonValue;
				JSONArray jsonArray;
				JSONString jsonString;
				
				//Take JSON string from DB and make it usable.
				jsonValue = JSONParser.parseStrict(result);
				jsonArray = jsonValue.isArray();
				int rows = jsonArray.size();
				
				//Nested for loop for listing all columns of all rows.
				for(int i = 0; i < rows; i++) {
					jsonValue = jsonArray.get(i);
					jsonObject = jsonValue.isObject();
					
					jsonValue = jsonObject.get("id");
					jsonString = jsonValue.isString();
					final String id = jsonString.stringValue();
					
					for(int c = 0; c < contactAttributes.length; c++) {
						jsonValue = jsonObject.get(contactAttributes[c]);
						jsonString = jsonValue.isString();
						String value = jsonString.stringValue();
						contactTable.setText(i + 1, c, value);
					}
					
					//Respective update button for each row's contents.
					Button updateButton = new Button("Update");
					updateButton.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							updateContacts(id);
						}
					});
					contactTable.setWidget(i + 1, 7, updateButton);
					
					//Respective remove button for each row's contents.
					Button removeButton = new Button("Remove");
					removeButton.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							removeRequest(id);
						}
					});
					contactTable.setWidget(i + 1, 8, removeButton);
				}
				listButton.setEnabled(true);
			}
		});
	}
}
