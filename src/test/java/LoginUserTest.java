import api.client.UserClient;
import api.model.User;
import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class LoginUserTest extends BaseTest {

    String email = "test234123123@test.ru";
    String password="1237343489";
    String name="nametest";

    UserClient userClient = new UserClient();
    User user = new User(email,password,name);
    User userWithoutName = new User(email,password);
    User userWithInvalidPasswordAndEmail = new User(email+"123",password+"123");

    @Test
    public void checkLoginExistingUserTest() {
        Response response = userClient.sendPostRequestRegister(user);
        response.then().assertThat().statusCode(200)
                .body("success",equalTo(true));

        Response response1=userClient.sendPostRequestLogin(userWithoutName);
        response1.then().assertThat().statusCode(200)
                .body("success",equalTo(true));

        String accessToken=response1.then().extract().body().path("accessToken");
        userClient.sendDeleteRequestUser(accessToken.replace("Bearer ",""));
    }

    @Test
    public void checkLoginWithInvalidPasswordAndEmailTest() {
        Response response = userClient.sendPostRequestRegister(user);
        response.then().assertThat().statusCode(200)
                .body("success",equalTo(true));

        Response response1=userClient.sendPostRequestLogin(userWithInvalidPasswordAndEmail);
        response1.then().assertThat().statusCode(401)
                .body("success",equalTo(false))
                .body("message",equalTo("email or password are incorrect"));

        String accessToken=response.then().extract().body().path("accessToken");
        userClient.sendDeleteRequestUser(accessToken.replace("Bearer ",""));
    }
}
