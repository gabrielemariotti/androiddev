/*******************************************************************************
 * Copyright 2013 Gabriele Mariotti
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package it.gmariotti.android.example.parser.json;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;

public class JsonHelper {

	private static String TAG = "JsonHelper";

	// -------------------------------------------------------------------------------------------------------------------------
	// Parsing JsonObject
	// -------------------------------------------------------------------------------------------------------------------------

	public static MyObject parse(String jsonString) {

		MyObject myobj = null;

		if (jsonString != null) {
			try {
				JSONObject jsonObject = new JSONObject(jsonString);

				myobj = new MyObject();

				myobj.setId(jsonObject.getInt("id"));
				myobj.setName(jsonObject.getString("name"));

				List<MyObjectMessage> messages = new ArrayList<MyObjectMessage>();

				JSONArray msgs = (JSONArray) jsonObject.get("listMessages");
				for (int i = 0; i < msgs.length(); i++) {
					JSONObject json_message = msgs.getJSONObject(i);
					if (json_message != null) {
						MyObjectMessage objMsg = new MyObjectMessage();
						objMsg.setId_message(json_message.getInt("id_message"));
						objMsg.setValue(json_message.getInt("value"));
						objMsg.setText(json_message.getString("text"));
						messages.add(objMsg);
					}
				}

				myobj.setListMessages(messages);

			} catch (JSONException je) {
				Log.e(TAG, "error while parsing", je);
			}
		}

		return myobj;

	}

	public static MyObject parseGson(String jsonString) {

		MyObject myobj = null;

		if (jsonString != null) {

			Gson gson = new Gson();
			myobj = gson.fromJson(jsonString, MyObject.class);
		}

		return myobj;
	}

	// -------------------------------------------------------------------------------------------------------------------------
	// Create JsonObject
	// -------------------------------------------------------------------------------------------------------------------------

	/**
	 * Output:
	 * {"id":10,"listMessages":[
	 *        {"value":1,"text":"value","id_message":0},
	 *        {"value":11,"text":"value","id_message":1},
	 *        {"value":21,"text":"value","id_message":2},
	 *        {"value":31,"text":"value","id_message":3},
	 *        {"value":41,"text":"value","id_message":4}
	 *        ]
	 * ,"name":"myname"}
	 * 
	 * @return
	 */
	public static JSONObject write() {

		MyObject myObj = createMyObject();
		if (myObj != null) {

			try {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("id", myObj.getId());
				jsonObject.put("name", myObj.getName());
				JSONArray jsonArrayMessages = new JSONArray();

				List<MyObjectMessage> messages = myObj.getListMessages();

				if (messages != null) {
					for (MyObjectMessage objMsg : messages) {
						JSONObject jsonMessage = new JSONObject();
						jsonMessage.put("id_message", objMsg.getId_message());
						jsonMessage.put("value", objMsg.getValue());
						jsonMessage.put("text", objMsg.getText());
						jsonArrayMessages.put(jsonMessage);
					}
				}

				jsonObject.put("listMessages", jsonArrayMessages);

				return jsonObject;

			} catch (JSONException je) {
				Log.e(TAG, "error in Write", je);
			}
		}

		return null;
	}

	/**
	 * Output:
	 * 
	 * @return
	 */
	public static String writeGson() {

		MyObject myObj = createMyObject();
		if (myObj != null) {

			Gson gson = new Gson();
			String objJson = gson.toJson(myObj);

			return objJson;

		}

		return null;
	}

	// -------------------------------------------------------------------------------------------------------------------------
	// Create Java Object
	// -------------------------------------------------------------------------------------------------------------------------

	public static MyObject createMyObject() {

		MyObject myObj = new MyObject();
		myObj.setId(10);
		myObj.setName("myname");

		List<MyObjectMessage> messages = new ArrayList<MyObjectMessage>();

		for (int i = 0; i < 5; i++) {
			MyObjectMessage objMessage = new MyObjectMessage();
			objMessage.setId_message(i);
			objMessage.setValue(i * 10 + 1);
			objMessage.setText("value");

			messages.add(objMessage);
		}

		myObj.setListMessages(messages);

		return myObj;
	}

	// -------------------------------------------------------------------------------------------------------------------------
	// Model
	// -------------------------------------------------------------------------------------------------------------------------

	public static class MyObject {

		private int id;
		private String name;
		private List<MyObjectMessage> listMessages;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public List<MyObjectMessage> getListMessages() {
			return listMessages;
		}

		public void setListMessages(List<MyObjectMessage> listMessages) {
			this.listMessages = listMessages;
		}

		@Override
		public String toString() {
			String newLine = "\n";
			StringBuffer sb = new StringBuffer();
			sb.append("id=" + id);
			sb.append(newLine);
			sb.append("name=" + name);
			sb.append(newLine);
			List<MyObjectMessage> messages = getListMessages();

			if (messages != null) {
				sb.append("Messages=" + messages.size());
				for (MyObjectMessage mymes : messages) {
					sb.append(newLine);
					sb.append(mymes.toString());
				}
			}

			return sb.toString();
		}

	}

	public static class MyObjectMessage {

		private int id_message;
		private int value;
		private String text;

		public int getId_message() {
			return id_message;
		}

		public void setId_message(int id_message) {
			this.id_message = id_message;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		@Override
		public String toString() {
			StringBuffer sb = new StringBuffer();
			String newLine = "\n";

			sb.append("  id_message=" + getId_message());
			sb.append(newLine);
			sb.append("  value=" + getValue());
			sb.append(newLine);
			sb.append("  text=" + getText());
			return sb.toString();
		}

	}

}
