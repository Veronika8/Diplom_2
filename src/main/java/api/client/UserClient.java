package api.client;

import api.model.User;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserClient {

    @Step("Send to post /api/auth/register")
    public Response sendPostRequestRegister(User user){
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(user)
                        .when()
                        .post("/api/auth/register");
        return response;
    }

    @Step("Send to delete /api/auth/user")
    public Response sendDeleteRequestUser(String accessToken) {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .auth().oauth2(accessToken)
                        .when()
                        .delete("/api/auth/user");
        return response;
    }

    @Step("Send to post /api/auth/login")
    public Response sendPostRequestLogin(User user) {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(user)
                        .when()
                        .post("/api/auth/login");
        return response;
    }

    @Step("Send to patch /api/auth/user")
    public Response sendPatchRequestUser(User user, String accessToken) {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .auth().oauth2(accessToken)
                        .body(user)
                        .when()
                        .patch("/api/auth/user");
        return response;
    }

    @Step("Send to patch /api/auth/user")
    public Response sendPatchRequestUserWithoutToken(User user) {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(user)
                        .when()
                        .patch("/api/auth/user");
        return response;
    }
}
