package jsonPlaceholderEndpoint;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

public class GetUsers {

    @Test
    public void validateGetUserResponseHamcrestMatchers(){
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
        Response response = given().log().all().when().get("/comments?postId=1").then().extract().response();
        System.out.println("Response is: " + response.asString());

        List<String> expectedEmail = Arrays.asList("Eliseo@gardner.biz", "Jayne_Kuhic@sydney.com", "Nikita@garfield.biz", "Lew@alysha.tv", "Hayden@althea.biz");
        assertThat(response.jsonPath().getList("email"), contains(expectedEmail.toArray(new String[0])));
    }
}
