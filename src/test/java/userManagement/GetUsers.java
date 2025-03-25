package userManagement;

import core.StatusCode;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.JSONReader;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.testng.Assert.assertEquals;

public class GetUsers {

    @Test
    public void getUsersData(){
        given().
                when().
                get("https://reqres.in/api/users?page=2").
                then().assertThat().statusCode(200);
    }

    @Test
    public void validateGetUserResponseBody(){
        RestAssured.baseURI = "https://reqres.in";
        given().when().get("/api/users?page=2").then().assertThat().statusCode(200).
                body("support.url", equalTo("https://contentcaddy.io?utm_source=reqres&utm_medium=json&utm_campaign=referral"));
    }

    @Test
    public void validateGetUserResponseBodyUsingResponseInterface(){
        RestAssured.baseURI = "https://reqres.in";
        Response response = given().log().all().when().get("/api/users?page=2").then().extract().response();
        System.out.println("Response is: " + response.asString());
        List<String> idEmail = response.jsonPath().getList("data.email");
        System.out.println(idEmail);
        assertThat(response.jsonPath().getList("data.email"), hasItems("michael.lawson@reqres.in"));
        assertThat(response.jsonPath().getList("data.email"), hasSize(6));
    }

    @Test
    public void validateGetUserResponseHamcrestMatchers(){
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
        Response response = given().log().all().when().get("/comments?postId=1").then().extract().response();
        System.out.println("Response is: " + response.asString());

        List<String> expectedEmail = Arrays.asList("Eliseo@gardner.biz", "Jayne_Kuhic@sydney.com", "Nikita@garfield.biz", "Lew@alysha.tv", "Hayden@althea.biz");
        assertThat(response.jsonPath().getList("email"), contains(expectedEmail.toArray(new String[0])));
    }

    @Test
    public void validateGetUserResponseForFirstUser(){

        RestAssured.baseURI = "https://reqres.in";
        Response response = given().log().all().when().get("/api/users?page=2").then().extract().response();
        System.out.println("Response is: " + response.asString());

        response.then().body("data[0].id", equalTo(7));
        response.then().body("data[0].email", equalTo("michael.lawson@reqres.in"));
        response.then().body("data[0].first_name", is("Michael"));
        response.then().body("data[0].last_name", is("Lawson"));
        response.then().body("data[0].avatar", is("https://reqres.in/img/faces/7-image.jpg"));
    }

    @Test
    public void singleQueryParam(){

        RestAssured.baseURI = "https://reqres.in";
        Response response = given().
                queryParam("page", "2").
                when().
                get("/api/users?page=2");

        System.out.println("Response is: " + response.asString());

        response.then().body("data[0].id", equalTo(7));
        response.then().body("data[0].email", equalTo("michael.lawson@reqres.in"));
        response.then().body("data[0].first_name", is("Michael"));
        response.then().body("data[0].last_name", is("Lawson"));
        response.then().body("data[0].avatar", is("https://reqres.in/img/faces/7-image.jpg"));
    }

    @Test
    public void testCreateUserWithFormParam() {

        RestAssured.baseURI = "https://reqres.in";
        Response response = given()
                .contentType("application/x-www-form-urlencoded")
                .formParam("name", "Jaell Ledford")
                .formParam("job", "Developer")
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .extract()
                .response();

        response.then().body("name", equalTo("Jaell Ledford"));
        response.then().body("job", equalTo("Developer"));
    }

    @Test
    public void testGetUsersWithHeader() {

        RestAssured.baseURI = "https://reqres.in";
        Response response = given()
                .header("Content-type","application/json")
                .when()
                .get("/api/users?page=2")
                .then()
                .statusCode(200)
                .log()
                .all()
                .extract()
                .response();
    }

    @Test
    public void getHeadersFromResponse() {

        RestAssured.baseURI = "https://reqres.in";
        Response response = given()
                .header("Content-type","application/json")
                .when()
                .get("/api/users?page=2");

        Headers headers = response.getHeaders();
        System.out.println("Response headers :: ");
        for(Header h : headers){
            System.out.println(h.getName() + " : " + h.getValue());
            if(h.getName().equals("Server") ){
                assertThat(h.getValue(), equalTo("cloudflare"));
            }
        }
    }

    @Test
    public void fetchCookies() {

        RestAssured.baseURI = "https://reqres.in";
        Response response = given()
                .header("Content-type","application/json")
                .when()
                .get("/api/users?page=2");

        Map<String, String> cookies = response.getCookies();

        for(Map.Entry<String, String> entry : cookies.entrySet()){
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }

    @Test
    public void deleteUser() {

        RestAssured.baseURI = "https://reqres.in";
        Response response = given().when().delete(JSONReader.get("deleteUser"));

        assertEquals(response.getStatusCode(), StatusCode.NO_CONTENT.code);
    }

}
