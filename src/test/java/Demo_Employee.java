import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class Demo_Employee {
    @BeforeTest
    void setup(){
        RestAssured.baseURI = "http://dummy.restapiexample.com/";
        //RestAssured.basePath = "/api/v1/employees";
    }

    //GET Method + JSONPath example 01
    @Test
    void getMethod01(){
        Response response =
                given().get("/api/v1/employees")
                        .then()
                        .log().all()
                        .extract().response();

//        List<String> employee = response.path("data.employee_name");
//        System.out.println(employee);
    }

    //GET Method + JSONPath validate response elements directly
    @Test
    void getMethod02(){
        String[] checkList = {"Tiger Nixon", "Garrett Winters", "Ashton Cox", "Cedric Kelly", "Airi Satou"};

        given().log().all()
                .get("/api/v1/employees")
                .then()
//                .log().all()
                .assertThat().body("data.employee_name", hasItem(in(checkList)));

    }

    //GET Method + handling validate response elements indirectly
    @Test
    void getMethod03(){
        String[] checkList = {"Tiger Nixon", "Garrett Winters", "Ashton Cox", "Cedric Kelly", "Airi Satou"};

        Response response =
                given().get("/api/v1/employees")
                        .then()
//                        .log().all()
                        .extract().response();

        List<String> employeeList = response.path("data.employee_name");
        assertThat(employeeList, hasItems(checkList));

    }

    //GET Method + Data Provider
    @DataProvider(name="employee")
    Object[][] employee(){
        return new Object[][]{
               new Object[]  {"Garrett Winters"}
               };
    }

    @Test(dataProvider = "employee")
    void showEmployeeProfile(String name){
       given().log().all()
                .contentType("application/json")
                .when().get("/api/v1/employees")
                .then().log().ifError()
                .assertThat().body("data[1].employee_name", is(name));

    }

}
