package com.example.RestfulCRUD;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RestfulCrudApplicationTests {
	public static Response response;
	public static String jsonAsString;
	@BeforeClass
	public static void setup() {
		String port = System.getProperty("server.port");
		if (port == null) {
			RestAssured.port = Integer.valueOf(8080);
		}
		else{
			RestAssured.port = Integer.valueOf(port);
		}
		String basePath = System.getProperty("server.base");
		if(basePath==null){
			basePath = "/rest/";
		}
		RestAssured.basePath = basePath;

		String baseHost = System.getProperty("server.host");
		if(baseHost==null){
			baseHost = "http://localhost";
		}
		RestAssured.baseURI = baseHost;
	}

	@Test
	public void pingServerTest() {
		given()
		.when()
		.get("/hello")
		.then()
		//.body(containsString("Restful API using Spring"));
		.body(containsString("Hello world"));
	}
	@Test
	public void getListUserTest(){
		JsonPath data = given()
				.when()
				.get("/users")
				.then()
				.statusCode(200)
				.extract().jsonPath();
		assertThat(data.getList("name")).contains("Daren");
		assertThat(data.getList("name")).contains("Larry");
	}

	@Test
	public void getUserByNameTest(){
		given()
		.contentType(ContentType.JSON)
		.pathParam("name", "Daren")
		.when()
		.get("/usersbyname/{name}")
		.then()
		.statusCode(200)
		.body(containsString("Daren"))
		.body(containsString("20"));
	}

	@Test
	public void createUserTest(){
		Map<String, String> user = new HashMap<>();
		user.put("id", "3");
		user.put("name", "Ben");
		user.put("age", "22");
		user.put("description", "A description.");

		given()
		.contentType("application/json")
		.body(user)
		.when().post("/adduser")
		.then()
		.statusCode(200);
	}
	
	@Test
	public void deleteUserTest(){
		given()
		.pathParam("id", 3)
		.when()
		.delete("/delete/{id}")
		.then()
		.statusCode(200);
	}
}
