import api.client.OrderClient;
import api.client.UserClient;
import api.model.Order;
import api.model.User;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class GetOrdersUserTest extends BaseTest {
     CreateData createData = new CreateData();

    UserClient userClient = new UserClient();
    OrderClient orderClient=new OrderClient();
    User user = new User(createData.createEmail(),createData.createPassword(),createData.createName());

    String accessToken;

    @Test
    public void checkGetOrdersUserWithAuth() {
        Response response = userClient.sendPostRequestRegister(user);
        response.then().assertThat().statusCode(200)
                .body("success",equalTo(true));

        accessToken=response.then().extract().body().path("accessToken");

        Response response1=orderClient.sendGetRequestIngredients();
        String idIngredients = response1.then().extract().body().path("data[0]._id");

        Order order = new Order(idIngredients);

        Response response2 = orderClient.sendPostRequestCreateOrderWithAuth(order,accessToken.replace("Bearer ",""));
        response2.then().assertThat().statusCode(200);
        int number = response2.then().extract().body().path("order.number");

        Response response3=orderClient.sendGetRequestOrdersUserWithAuth(accessToken.replace("Bearer ",""));
        response3.then().assertThat().statusCode(200)
                .body("success",equalTo(true))
                        .body("orders[0].number",equalTo(number));
    }

    @Test
    public void checkGetOrdersUserWithoutAuth() {
        accessToken="";
        Response response = orderClient.sendGetRequestOrdersUserWithoutAuth();
        response.then().assertThat().statusCode(401)
                .body("success",equalTo(false))
                .body("message",equalTo("You should be authorised"));
    }

    @After
    public void deleteDate() {
        userClient.sendDeleteRequestUser(accessToken.replace("Bearer ",""));
    }
}
