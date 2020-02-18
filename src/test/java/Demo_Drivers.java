import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class Demo_Drivers {

    @BeforeClass (alwaysRun = true)
    void setup(){
        //Base URI can pointed to anywhere of site, depends on testing purpose.
        RestAssured.baseURI = "http://ergast.com";
    }

    //GET Method
    @Test
    void TestGetResponse(){
        given().log().all()
                .get("/api/f1/drivers.json")
                .then().log().all()
                .statusCode(200);
    }

    //Data Provider for next test script
    @DataProvider(name = "driver")
    Object[][] driver() {
        return new Object[][]{
                new Object[]{"max_verstappen", 200, 6000L, "Verstappen"},
                new Object[]{"hamilton", 200, 6000L, "Hamilton"}
        };
    }


    //GET Method + JSON Path
    @Test(dataProvider = "driver")
    void ShowDriverInfo(String driverName, int responseCode, long responseTime, String familyName){

        given().log().all()
                .pathParam("driver_name", driverName)
        .when()
                .get("/api/f1/drivers/{driver_name}.json")
        .then().log().all()
                .statusCode(responseCode)
                .time(lessThan(responseTime))
                .assertThat().body("MRData.DriverTable.Drivers[0].familyName", is(familyName));
    }


}
