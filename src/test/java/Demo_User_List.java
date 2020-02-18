import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.http.ContentType;

import org.json.simple.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import io.restassured.response.Response;
import java.util.List;


import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

public class Demo_User_List {
    @BeforeClass
    void setup(){
        RestAssured.baseURI = "http://localhost:3000";
    }

    //Show all users info
    @Test
    void getMethod(){
        given().log().all()
                .when().get("/users")
                .then().log().all();
    }

    //Query user's info by id
    @Test
    void queryUser(){
    Response response = given().log().all()
                                .when().get("/users")
                                .then().extract().response();
    List<Integer> result = response.path("findAll { it.id == 5}");
    System.out.println(result);
    }

    //Get method + Data provider
    @DataProvider(name="user")
    Object[][] user(){
        return new Object[][]{
          new Object[] {5,"user-name5"}
        };
    }

    @Test(dataProvider = "user")
    void showUserProfile(Integer id, String name){
        given().log().all()
                .pathParam("id", id)
                .when().get("/users/{id}")
                .then().log().all()
                .assertThat().body("name", is(name));
    }

    //Create new user
    @Test
    void postMethod(){
        Response allUsers = given().log().all()
                .when().get("/users")
                .then().extract().response();
        int max = (Integer) allUsers.path("id.max()");

        JSONObject request = new JSONObject();
        request.put("id", (max+1));
        request.put("name", "TienTHB");
        request.put("age", 170);
        request.put("address", "Phan Thiet city");
        request.put("employeeID", 609);

        System.out.println(request.toJSONString());


        given()
                .config(RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                .body(request.toString())
                .contentType("application/json")
                .post("/users")
                .then().log().ifError();

//        Response response = given().when().get("/users").then().extract().response();
//        List<String> result = response.path("findAll {it -> it.id == 110}");
//        System.out.println(result);
    }

}
