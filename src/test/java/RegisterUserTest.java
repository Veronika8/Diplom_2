import api.client.UserClient;
import api.model.User;
import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class RegisterUserTest extends BaseTest {

    String email = "test21213123@test.ru";
    String password="1237343489";
    String name="nametest";

    UserClient userClient = new UserClient();
    User user = new User(email,password,name);
    User userWithoutName = new User(email,password);

    @Test
    public void checkRegisterUserTest() {
        Response response = userClient.sendPostRequestRegister(user);
        response.then().assertThat().statusCode(200)
                .body("success",equalTo(true));

        String accessToken=response.then().extract().body().path("accessToken");
        userClient.sendDeleteRequestUser(accessToken.replace("Bearer ",""));
    }

    @Test
    public void checkSecondSendRegisterRequestTest() {
        Response response = userClient.sendPostRequestRegister(user);
        response.then().assertThat().statusCode(200)
                .body("success",equalTo(true));

        Response response1 = userClient.sendPostRequestRegister(user);
        response1.then().assertThat().statusCode(403)
                .body("success",equalTo(false))
                .body("message",equalTo("User already exists"));

        String accessToken=response.then().extract().body().path("accessToken");
        userClient.sendDeleteRequestUser(accessToken.replace("Bearer ",""));
    }

    @Test
    public void checkSendRegisterRequestWithoutNameTest() {
        Response response = userClient.sendPostRequestRegister(userWithoutName);
        response.then().assertThat().statusCode(403)
                .body("success",equalTo(false))
                .body("message",equalTo("Email, password and name are required fields"));

    }
}
