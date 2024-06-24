import example.Configuration;
import example.Courier;
import example.CourierAPI;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;


public class TestCreateCourier {

    Courier courier;

    @Before
    public void createCourierInit() {
        RestAssured.baseURI = Configuration.URL_QA_SCOOTER;
        courier = new Courier();
    }

    @Test
    @DisplayName("Тест на создание нового курьера")
    @Description("Можно создать нового уникального курьера")
    public void testCanCreateNewCourier() {
        Response responseCreate = CourierAPI.createCourier(courier);
        if (responseCreate.getBody().asString().contains("ok")) {
            courier.setInApp(true);
        }
        responseCreate.then().assertThat()
                .statusCode(SC_CREATED)
                .and()
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Тест на создание двух одинаковых курьеров")
    @Description("Нельзя создать 2 одинаковых курьера")
    public void testCanNotCreateTwoIdenticalCouriers() {
        Response responseCreate = CourierAPI.createCourier(courier);
        if (responseCreate.getBody().asString().contains("ok")) {
            courier.setInApp(true);
        }
        responseCreate.then().assertThat()
                .statusCode(SC_CREATED)
                .and()
                .body("ok", equalTo(true));
        responseCreate = CourierAPI.createCourier(courier);
        responseCreate.then().assertThat()
                .statusCode(SC_CONFLICT)
                .and()
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));

    }

    @Test
    @DisplayName("Тест на создание двух одинаковых логинов")
    @Description("Нельзя создать два одинаковых логинов")
    public void testCanNotCreateTwoCouriersWithIdenticalLogins() {
        Response responseCreate = CourierAPI.createCourier(courier);
        if (responseCreate.getBody().asString().contains("ok")) {
            courier.setInApp(true);
        }
        responseCreate.then().assertThat()
                .statusCode(SC_CREATED)
                .and()
                .body("ok", equalTo(true));
        String pwdKeeper = courier.getPassword();
        courier.setPassword("newUnusedPwd");
        String firstNameKeeper = courier.getFirstName();
        courier.setFirstName("newUnusedName");
        responseCreate = CourierAPI.createCourier(courier);
        responseCreate.then().assertThat()
                .statusCode(SC_CONFLICT)
                .and()
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
        courier.setPassword(pwdKeeper);
        courier.setFirstName(firstNameKeeper);
    }

    @Test
    @DisplayName("Тест на создание курьера без логина")
    @Description("Нельзя создать курьера без логина ")
    public void testCanNotCreateCourierWithoutLogin() {

        String loginKeeper = courier.getLogin();
        courier.setLogin("");

        Response responseCreate = CourierAPI.createCourier(courier);
        if (responseCreate.getBody().asString().contains("ok")) {
            courier.setInApp(true);
        }
        responseCreate.then().assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
        courier.setLogin(loginKeeper);

    }

    @Test
    @DisplayName("Тест на создание курьера без пароля")
    @Description("Нельзя создать курьера без пароля")
    public void testCanNotCreateCourierWithoutPassword() {

        String pwdKeeper = courier.getPassword();
        courier.setPassword("");

        Response responseCreate = CourierAPI.createCourier(courier);
        if (responseCreate.getBody().asString().contains("ok")) {
            courier.setInApp(true);
        }
        responseCreate.then().assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
        courier.setLogin(pwdKeeper);
    }

    @After
    @Description("Удаление уже существующего курьера")
    public void deleteTestCourierIfExist() {
        if(courier.isInApp()) {
            CourierAPI.loginCourier(courier);
            CourierAPI.deleteCourier(courier);
        }
    }

}