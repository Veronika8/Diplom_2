import api.client.UserClient;
import api.model.User;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class ChangeUserTest extends BaseTest {
    CreateData createData = new CreateData();
    String email = createData.createEmail();
    String password= createData.createPassword();
    String name= createData.createName();

    UserClient userClient = new UserClient();
    User user = new User(email,password,name);
    User userWithChangeData = new User(email+"123",password+"123",name+"123");

    String accessToken;

    @Test
    public void checkChangeWithAuth() {
        Response response = userClient.sendPostRequestRegister(user);
        response.then().assertThat().statusCode(200)
                .body("success",equalTo(true));

        accessToken=response.then().extract().body().path("accessToken");

        Response response1 = userClient.sendPatchRequestUser(userWithChangeData,accessToken.replace("Bearer ",""));
        response1.then().assertThat().statusCode(200)
                .body("success",equalTo(true))
                .body("user.email",equalTo(email+ "123"))
                .body("user.name",equalTo(name+ "123"));
    }

    @Test
    public void checkChangeWithoutAuth() {
        Response response = userClient.sendPostRequestRegister(user);
        response.then().assertThat().statusCode(200)
                .body("success",equalTo(true));

        accessToken=response.then().extract().body().path("accessToken");

        Response response1 = userClient.sendPatchRequestUserWithoutToken(userWithChangeData);
        response1.then().assertThat().statusCode(401)
                .body("success",equalTo(false))
                .body("message",equalTo("You should be authorised"));
    }

    @After
    public void deleteDate() {
        userClient.sendDeleteRequestUser(accessToken.replace("Bearer ",""));
    }
}
