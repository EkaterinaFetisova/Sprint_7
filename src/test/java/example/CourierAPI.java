package example;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierAPI {

    public static final String newCourierAPIPath = "/api/v1/courier";
    public static final String loginCourierAPIPath = "/api/v1/courier/login";
    public static final String deleteCourierAPIPath = "/api/v1/courier/";

    @Step("Создание нового курьера")
    public static Response createCourier(Courier courier) {
        Response responseCreate = given()
                .header(Data.REQUEST_HEADER)
                .and()
                .body(courier)
                .when()
                .post(newCourierAPIPath);
        return responseCreate;
    }

    @Step("Авторизация логина и получение ID")
    @Description("Курьер авторизуется и получает ID курьера")
    public static Response loginCourier(Courier courier) {
        Response responseLogin =
                given()
                        .header(Data.REQUEST_HEADER)
                        .and()
                        .body(courier)
                        .when()
                        .post(loginCourierAPIPath);
        return responseLogin;
    }

    @Step("Удаление курьера")
    public static void deleteCourier(Courier courier) {
        try {
            Response responseLogin = loginCourier(courier);
            int courierId = responseLogin.jsonPath().getInt("id");
            given()
                    .header(Data.REQUEST_HEADER)
                    .when()
                    .delete(deleteCourierAPIPath + courierId);

        } catch (NullPointerException e) {
            System.out.println("Курьер не был создан, его невозможно удалить!");
        }

    }
}
