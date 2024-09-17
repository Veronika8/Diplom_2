import api.client.UserClient;
import api.model.User;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class LoginUserTest extends BaseTest {
    CreateData createData = new CreateData();
    String email = createData.createEmail();
    String password= createData.createPassword();
    String name= createData.createName();

    UserClient userClient = new UserClient();
    User user = new User(email,password, name);
    User userWithoutName = new User(email,password);

    User userWithInvalidPasswordAndEmail = new User(createData.createEmail(),createData.createPassword());
    String accessToken;

    @Test
    public void checkLoginExistingUserTest() {
        Response response = userClient.sendPostRequestRegister(user);
        response.then().assertThat().statusCode(200)
                .body("success",equalTo(true));

        Response response1=userClient.sendPostRequestLogin(userWithoutName);
        response1.then().assertThat().statusCode(200)
                .body("success",equalTo(true));

        accessToken=response1.then().extract().body().path("accessToken");
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

        accessToken=response.then().extract().body().path("accessToken");
    }

    @After
    public void deleteDate() {
        userClient.sendDeleteRequestUser(accessToken.replace("Bearer ",""));
    }
}
