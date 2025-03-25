package userManagement;

import core.BaseTest;
import core.StatusCode;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import utils.ExtentReport;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class DeleteUser extends BaseTest {

    @Test
    public void deleteUser() {

        ExtentReport.extentlog =
                ExtentReport.extentreport.
                        startTest("deleteUser", "Validate 204 Status Code for delete request");

        RestAssured.baseURI = "https://reqres.in";
        Response response = given().when().delete("/api/users/2");

        assertEquals(response.getStatusCode(), StatusCode.NO_CONTENT.code);
    }
}
