package userManagement;

import core.StatusCode;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import pojo.CityRequest;
import pojo.PostRequestBodyComplexJSON;
import pojo.PostRequestBodyRegres;
import pojo.PostRequestBodyWithList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.testng.Assert.assertEquals;

public class PostUser {

    private static FileInputStream fileInputStream;

    private static FileInputStream fileInputStreamMethod(String requestBodyFileName) {
        try {
            fileInputStream = new FileInputStream(
                    new File(System.getProperty("user.dir") + "/resources/testData/" + requestBodyFileName));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return fileInputStream;
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
    public void validatePostWithString() {

        Response response = given()
                .header("Content-Type", "application/json")
                .body("{\"name\":\"morpheus\",\"job\":\"leader\"}")
                .when()
                .post("https://reqres.in/api/users");
        assertEquals(response.getStatusCode(), StatusCode.CREATED.code);
        System.out.println(response.getBody().asString());
    }

    @Test
    public void validatePostByPassingJsonFile() {

        Response response = given()
                .header("Content-Type", "application/json")
                .body(fileInputStreamMethod("postRequestBody.json"))
                .when()
                .post("https://reqres.in/api/users");
        assertEquals(response.getStatusCode(), StatusCode.CREATED.code);
        System.out.println(response.getBody().asString());
    }

    @Test
    public void validatePostWithPojo() {

        PostRequestBodyRegres postRequestBodyRegres = new PostRequestBodyRegres();
        postRequestBodyRegres.setName("Tom");
        postRequestBodyRegres.setName("Engineer");

        Response response = given()
                .header("Content-Type", "application/json")
                .body(postRequestBodyRegres)
                .when()
                .post("https://reqres.in/api/users");
        assertEquals(response.getStatusCode(), StatusCode.CREATED.code);
        System.out.println(response.getBody().asString());
    }

    @Test
    public void validatePostWithPojoList() {

        PostRequestBodyWithList postRequestBodyWithList = new PostRequestBodyWithList();
        postRequestBodyWithList.setName("Tom");
        postRequestBodyWithList.setName("Engineer");
        postRequestBodyWithList.setLanguages(new ArrayList<>(Arrays.asList("Java", "Python", "Javascript")));

        Response response = given()
                .header("Content-Type", "application/json")
                .body(postRequestBodyWithList)
                .when()
                .post("https://reqres.in/api/users");
        assertEquals(response.getStatusCode(), StatusCode.CREATED.code);
        System.out.println(response.getBody().asString());
    }

    @Test
    public void validatePutWithPojo() {
        String name = "Jamie";
        String job = "Nurse";

        PostRequestBodyRegres putRequestBodyRegres = new PostRequestBodyRegres();
        putRequestBodyRegres.setName(name);
        putRequestBodyRegres.setJob(job);

        Response response = given()
                .header("Content-Type", "application/json")
                .body(putRequestBodyRegres)
                .when()
                .put("https://reqres.in/api/users/1");

        assertEquals(response.getStatusCode(), StatusCode.SUCCESS.code);
        PostRequestBodyRegres responseBody = response.as(PostRequestBodyRegres.class);
        assertEquals(responseBody.getName(), name);
        assertEquals(responseBody.getJob(), job);

        System.out.println(response.getBody().asString());
    }

    @Test(description = "Validating post request response by using POJO object")
    public void validatePostWithPojoObject() {

        CityRequest cityRequest1 = new CityRequest();
        cityRequest1.setCity("SF");
        cityRequest1.setTemp("15");

        CityRequest cityRequest2 = new CityRequest();
        cityRequest2.setCity("LA");
        cityRequest2.setTemp("20");

        List<CityRequest> cityRequest = new ArrayList<>();
        cityRequest.add(cityRequest1);
        cityRequest.add(cityRequest2);

        PostRequestBodyComplexJSON postRequestBody = new PostRequestBodyComplexJSON();
        postRequestBody.setName("Tom");
        postRequestBody.setName("Engineer");
        postRequestBody.setLanguages(new ArrayList<>(Arrays.asList("Java", "Python", "Javascript")));
        postRequestBody.setCityRequestBody(cityRequest);

        Response response = given()
                .header("Content-Type", "application/json")
                .body(postRequestBody)
                .when()
                .post("https://reqres.in/api/users");
        assertEquals(response.getStatusCode(), StatusCode.CREATED.code);

        PostRequestBodyComplexJSON responseBody = response.as(PostRequestBodyComplexJSON.class);
        assertEquals(responseBody.getCityRequestBody().get(0).getCity(), "SF");
        assertEquals(responseBody.getCityRequestBody().get(0).getTemp(), "15");

        System.out.println(response.getBody().asString());
    }
}
