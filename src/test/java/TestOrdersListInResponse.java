import example.Configuration;
import example.Data;
import example.OrderAPI;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class TestOrdersListInResponse {

    @Before
    public void createCourierInit() {
        RestAssured.baseURI = Configuration.URL_QA_SCOOTER;

    }
    @Test
    public void TestOrdersListInResponse(){
        Response response = given()
                .header(Data.REQUEST_HEADER)
                .and()
                .get(OrderAPI.getOrdersListAPIPath);
        response.then().assertThat().body("orders",notNullValue());
    }

}
