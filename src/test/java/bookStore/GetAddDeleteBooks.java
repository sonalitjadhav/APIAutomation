package bookStore;

import core.StatusCode;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class GetAddDeleteBooks {
    public String getAuthorizationToken(){

        RestAssured.baseURI = "https://bookstore.toolsqa.com";
        Response response = given()
                .header("Content-Type", "application/json")
                .body("{\n  \"userName\": \"sonalij\",\n  \"password\": \"#Sonalij11\"\n}")
                .when()
                .post("/Account/v1/GenerateToken");

        System.out.println("Response is: " + response.asString());
        return response.jsonPath().get("token");
    }

    @Test
    public void verifyGetBooks(){

        RestAssured.baseURI = "https://bookstore.toolsqa.com";
        Response response = given()
                .header("Content-Type", "application/json")
                .when()
                .get("/BookStore/v1/Books");

        System.out.println("Response is: " + response.asString());
    }

    @Test
    public void verifyBookStoreAddBooks() {
        String authToken = getAuthorizationToken();

        System.out.println("Auth Token is " + authToken);
        RestAssured.baseURI = "https://bookstore.toolsqa.com";
        Response response = given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + authToken)
                .body("{\"userId\":\"ef99d1af-fa4c-4d54-b08b-793e14b2623d\",\"collectionOfIsbns\":[{\"isbn\":\"9781491904244\"}]}")
                .when()
                .post("/BookStore/v1/Books");
        System.out.println("Status code is " + response.getStatusCode());
        System.out.println("Response is: " + response.asString());
        assertEquals(response.getStatusCode(), StatusCode.CREATED.code);
    }

}
