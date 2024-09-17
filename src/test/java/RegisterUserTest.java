import api.client.UserClient;
import api.model.User;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class RegisterUserTest extends BaseTest {

    CreateData createData = new CreateData();

    UserClient userClient = new UserClient();
    User user = new User(createData.createEmail(),createData.createPassword(),createData.createName());
    User userWithoutName = new User(createData.createEmail(), createData.createPassword());

    String accessToken;

    @Test
    public void checkRegisterUserTest() {
        Response response = userClient.sendPostRequestRegister(user);
        response.then().assertThat().statusCode(200)
                .body("success",equalTo(true));

        accessToken=response.then().extract().body().path("accessToken");
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

        accessToken=response.then().extract().body().path("accessToken");
    }

    @Test
    public void checkSendRegisterRequestWithoutNameTest() {
        accessToken="";
        Response response = userClient.sendPostRequestRegister(userWithoutName);
        response.then().assertThat().statusCode(403)
                .body("success",equalTo(false))
                .body("message",equalTo("Email, password and name are required fields"));

    }

    @After
    public void deleteDate() {
        userClient.sendDeleteRequestUser(accessToken.replace("Bearer ",""));
    }
}
