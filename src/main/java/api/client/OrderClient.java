package api.client;

import api.model.Order;
import api.model.User;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderClient {

    @Step("Send to get /api/ingredients")
    public Response sendGetRequestIngredients() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .when()
                        .get("/api/ingredients");
        return response;
    }

    @Step("Send to post /api/orders")
    public Response sendPostRequestCreateOrderWithAuth(Order order, String accessToken){
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .auth().oauth2(accessToken)
                        .body(order)
                        .when()
                        .post("/api/orders");
        return response;
    }

    @Step("Send to post /api/orders")
    public Response sendPostRequestCreateOrderWithoutAuth(Order order){
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(order)
                        .when()
                        .post("/api/orders");
        return response;
    }

    @Step("Send to get /api/orders")
    public Response sendGetRequestOrdersUserWithAuth(String accessToken) {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .auth().oauth2(accessToken)
                        .when()
                        .get("/api/orders");
        return response;
    }

    @Step("Send to get /api/orders")
    public Response sendGetRequestOrdersUserWithoutAuth() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .when()
                        .get("/api/orders");
        return response;
    }
}
