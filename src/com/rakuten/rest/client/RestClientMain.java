package com.rakuten.rest.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import com.rakuten.rest.client.http.RestManager;

public class RestClientMain {

	public static void main(String[] args) {
		objective1();
		System.out.println();
		objective2();
		System.out.println();
		objective3();
		System.out.println();
		objective4();
		System.out.println();
		objective5();
	}

	private static void objective1() {
		System.out.println("------OBJECTIVE 1 BEGINS -------------");
		Random random = new Random();
		int number = random.nextInt(10 + 1);

		String url = "https://jsonplaceholder.typicode.com/users/" + number;
		String respText = RestManager.callGet(url, "application/json");
		if (respText == null || respText.isEmpty()) {
			System.out.println("Objective1 : WARNING - No response received from server");
			return;
		}

		JSONObject respJson = new JSONObject(respText.toString());
		JSONObject usrAddr = (JSONObject) respJson.get("address");
		String uStreet = "", uSuite = "", uCity = "", uZip = "";
		if (usrAddr != null) {
			uStreet = usrAddr.getString("street");
			uSuite = usrAddr.getString("suite");
			uCity = usrAddr.getString("city");
			uZip = usrAddr.getString("zipcode");
		}

		System.out.println("User Id   : " + respJson.getDouble("id"));
		System.out.println("User Name : " + respJson.get("name"));
		System.out.println("Address   : " + uStreet + "," + uSuite + "," + uCity + "," + uZip);
		System.out.println("------OBJECTIVE 1 ENDS -------------");
	}

	private static void objective2() {
		System.out.println("------OBJECTIVE 2 BEGINS -------------");

		String url = "https://jsonplaceholder.typicode.com/users";
		String respText = RestManager.callGet(url, "application/json");
		if (respText == null || respText.isEmpty()) {
			System.out.println("Objective2 : WARNING - No response received from server");
			return;
		}
		JSONArray respJsonArr = new JSONArray(respText.toString());

		for (int i = 0; i < respJsonArr.length(); i++) {
			JSONObject jObj = (JSONObject) respJsonArr.get(i);
			Integer uId = (Integer) jObj.get("id");
			String uName = jObj.getString("name");
			JSONObject addrJson = (JSONObject) jObj.get("address");
			String geo = "";
			if (addrJson != null) {
				JSONObject geoJson = (JSONObject) addrJson.get("geo");
				if (geoJson != null) {
					geo = "lateral =" + geoJson.getString("lat");
					geo = geo + " and Longitude =" + geoJson.getString("lng");
				}
			}
			System.out.println("-----------------------------------");
			System.out.println("User Id   : " + jObj.getDouble("id"));
			System.out.println("User Name : " + jObj.get("name"));
			System.out.println("Location : " + geo);

		}
		System.out.println("------OBJECTIVE 2 ENDS -------------");

	}

	private static void objective3() {
		System.out.println("------OBJECTIVE 3 BEGINS -------------");
		// get all users
		String urlUsers = "https://jsonplaceholder.typicode.com/users";
		String users = RestManager.callGet(urlUsers, "application/json");
		if (users == null || users.isEmpty()) {
			System.out.println("Objective3 : WARNING - No response received from server");
			return;
		}
		JSONArray usrArr = new JSONArray(users);
		Map<Integer, Integer> UserComm = new HashMap<Integer, Integer>();
		Integer maxCnt = 0;
		Integer maxUid = 0;
		String maxEmail = "";
		for (int i = 0; i < usrArr.length(); i++) {
			JSONObject usrObj = (JSONObject) usrArr.get(i);
			if (usrObj != null) {
				Integer usrId = (Integer) usrObj.get("id");
				String email = usrObj.getString("email");

				// retrieve all posts for the particular user
				String urlPosts = "https://jsonplaceholder.typicode.com/posts?userId=" + usrId;
				String posts = RestManager.callGet(urlPosts, "application/json");
				if (posts != null) {
					JSONArray postsArr = new JSONArray(posts);
					String filter = "?";
					for (int j = 0; j < postsArr.length(); j++) {
						JSONObject pObj = postsArr.getJSONObject(j);
						Integer pId = (Integer) pObj.get("id");
						filter = filter + "postId=" + pId + "&&";
					}

					// retrieve count of comments for a post
					String urlComms = "https://jsonplaceholder.typicode.com/comments/"
							+ filter.substring(0, filter.length() - 2);
					String comments = RestManager.callGet(urlComms, "application/json");
					JSONArray commArr = new JSONArray(comments);
					Integer commCnt = 0;
					if (commArr != null) {
						commCnt = commArr.length();
					}
					// if 2 entries with same number are found, then
					// the first max number will be considered to be the highest
					if (maxCnt < commCnt) {
						maxCnt = commCnt;
						maxUid = usrId;
						maxEmail = email;
					}
					UserComm.put(usrId, commCnt);
				}
			}
		}
		System.out.println("UserId=" + maxUid + " has maximum (" + maxCnt + ") Comments");
		System.out.println("His/her email is " + maxEmail);

		System.out.println("------OBJECTIVE 3 ENDS -------------");
	}

	private static void objective4() {
		System.out.println("------OBJECTIVE 4 BEGINS -------------");
		String url = "https://jsonplaceholder.typicode.com/albums";
		String albums = RestManager.callGet(url, "application/json");
		if (albums == null || albums.isEmpty()) {
			System.out.println("Objective 4 : WARNING - No response received from server");
			return;
		}

		JSONArray albumArr = new JSONArray(albums.toString());
		for (int i = 0; i < albumArr.length(); i++) {
			JSONObject albObj = (JSONObject) albumArr.get(i);
			Integer id = (Integer) albObj.get("id");
			String title = albObj.getString("title");

			String photoUrl = "https://jsonplaceholder.typicode.com/photos?albumId=" + id;
			String photos = RestManager.callGet(photoUrl, "application/json");
			int photoCnt = 0;
			if (photos != null) {
				JSONArray photoArr = new JSONArray(photos.toString());
				photoCnt = photoArr.length();

				System.out.println("Album : '" + title + "' has " + photoCnt + " photos.");
			}
		}
		System.out.println("------OBJECTIVE 4 ENDS -------------");
	}

	private static void objective5() {
		System.out.println("------OBJECTIVE 5 BEGINS -------------");
		String url = "https://jsonplaceholder.typicode.com/todos/?completed=false";
		String todos = RestManager.callGet(url, "application/json");
		if (todos == null || todos.isEmpty()) {
			System.out.println("Objective5 : WARNING - No response received from server");
			return;
		}
		JSONArray todoArr = new JSONArray(todos.toString());
		int maxCount = 0;
		Integer maxUId = 0;
		Map<Integer, Integer> idTodos = new HashMap<Integer, Integer>();
		for (int i = 1; i < todoArr.length(); i++) {
			JSONObject todoObj = (JSONObject) todoArr.get(i);
			if (todoObj != null) {
				Integer uId = (Integer) todoObj.get("userId");
				Integer cnt = idTodos.get(uId);
				if (cnt == null)
					cnt = 1;
				else
					cnt++;
				if (maxCount < cnt) {
					maxCount = cnt;
					maxUId = uId;
				}
				idTodos.put(uId, cnt);

			}
		}
		String urlUsr = "https://jsonplaceholder.typicode.com/users/" + maxUId;
		String user = RestManager.callGet(urlUsr, "application/json");
		if (user == null || user.isEmpty()) {
			System.out.println("Objective5 : WARNING - No response received from server");
			return;
		}
		JSONObject userObj = new JSONObject(user);
		String name = userObj.getString("name");
		String email = userObj.getString("email");
		System.out.println("User:" + name + " has maximum (" + maxCount + ") pending tasks");
		System.out.println("His/her email is:" + email);
		System.out.println("------OBJECTIVE 5 ENDS -------------");

	}
}
