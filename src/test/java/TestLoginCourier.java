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
import static org.hamcrest.Matchers.notNullValue;


public class TestLoginCourier {
    Courier courier;

    @Before
    public void createCourierInit() {
        RestAssured.baseURI = Configuration.URL_QA_SCOOTER;
        courier = new Courier();
    }

    @Test
    @DisplayName("Тест на успешную авторизацию курьера")
    @Description("Успешная авторизация курьера возможно при использовании валидных требуемых параметров")
    public void testCanLogInWithProperRequiredFields() {
        if (CourierAPI.createCourier(courier).getBody().asString().contains("ok")) {
            courier.setInApp(true);
        }
        Response responseLogin = CourierAPI.loginCourier(courier);
        responseLogin.then().assertThat()
                .statusCode(SC_OK)
                .and()
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Тест на неуспешную авторизацию курьера без логина")
    @Description("Неуспешная авторизация курьера без использования логина. Ошибка: 400 Bad request")
    public void testCannotGetInAppWithoutLogin() {
        if (CourierAPI.createCourier(courier).getBody().asString().contains("ok")) {
            courier.setInApp(true);
        }
        String loginKeeper = courier.getLogin();
        courier.setLogin("");
        Response responseLogin = CourierAPI.loginCourier(courier);
        responseLogin.then().assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
        courier.setLogin(loginKeeper);
    }

    @Test
    @DisplayName("Тест на неуспешную авторизацию курьера без пароля")
    @Description("Неуспешная авторизация курьера без использования пароля. Ошибка: 400 Bad request")
    public void testCannotGetInAppWithoutPassword() {
        if (CourierAPI.createCourier(courier).getBody().asString().contains("ok")) {
            courier.setInApp(true);
        }
        String pwdKeeper = courier.getPassword();
        courier.setPassword("");
        Response responseLogin = CourierAPI.loginCourier(courier);
        responseLogin.then().assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("message", equalTo("Недостаточно данных для входа"));
        courier.setPassword(pwdKeeper);
    }

    @Test
    @DisplayName("Тест на неуспешную авторизацию курьера с невалидным логином")
    @Description("Неуспешная авторизация курьера с невалидным логином. Ошибка: 404 Not found")
    public void testCannotGetInAppWithWrongLogin() {
        if (CourierAPI.createCourier(courier).getBody().asString().contains("ok")) {
            courier.setInApp(true);
        }
        String loginKeeper = courier.getLogin();
        courier.setLogin("WrongLogin");
        Response responseLogin = CourierAPI.loginCourier(courier);
        responseLogin.then().assertThat()
                .statusCode(SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
        courier.setLogin(loginKeeper);
    }

    @Test
    @DisplayName("Тест на неуспешную авторизацию курьера с невалидным паролем")
    @Description("Неуспешная авторизация курьера с невалидным паролем. Ошибка: 404 Not found")
    public void testCannotGetInAppWithWrongPassword() {
        if (CourierAPI.createCourier(courier).getBody().asString().contains("ok")) {
            courier.setInApp(true);
        }
        String pwdKeeper = courier.getPassword();
        courier.setPassword("WrongPassword");
        Response responseLogin = CourierAPI.loginCourier(courier);
        responseLogin.then().assertThat()
                .statusCode(SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
        courier.setPassword(pwdKeeper);
    }

    @Test
    @DisplayName("Тест на ошибку авторизации несуществующего курьера")
    @Description("Неуспешная авторизация курьера с невалидными логином и паролем. Ошибка: 404 Not found")
    public void testNonExistentCourierCannotLogIn() {
        if (CourierAPI.createCourier(courier).getBody().asString().contains("ok")) {
            courier.setInApp(true);
        }
        String pwdKeeper = courier.getPassword();
        String loginKeeper = courier.getLogin();
        courier.setPassword("WrongPassword");
        courier.setLogin("WrongLogin");
        Response responseLogin = CourierAPI.loginCourier(courier);
        responseLogin.then().assertThat()
                .statusCode(SC_NOT_FOUND)
                .and()
                .body("message", equalTo("Учетная запись не найдена"));
        courier.setPassword(pwdKeeper);
        courier.setLogin(loginKeeper);
    }

    @After
    @Description("Удаление уже существующего курьера")
    public void deleteTestCourierIfExist() {
        if (courier.isInApp()) {
            CourierAPI.loginCourier(courier);
            CourierAPI.deleteCourier(courier);
        }
    }


}
