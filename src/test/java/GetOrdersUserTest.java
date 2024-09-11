import api.client.OrderClient;
import api.client.UserClient;
import api.model.Order;
import api.model.User;
import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class GetOrdersUserTest extends BaseTest {
    String email = "test2312322@test.ru";
    String password="1237343489232422";
    String name="nametest";

    UserClient userClient = new UserClient();
    OrderClient orderClient=new OrderClient();
    User user = new User(email,password,name);

    @Test
    public void checkGetOrdersUserWithAuth() {
        Response response = userClient.sendPostRequestRegister(user);
        response.then().assertThat().statusCode(200)
                .body("success",equalTo(true));

        String accessToken=response.then().extract().body().path("accessToken");

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


        userClient.sendDeleteRequestUser(accessToken.replace("Bearer ",""));
    }

    @Test
    public void checkGetOrdersUserWithoutAuth() {
        Response response = orderClient.sendGetRequestOrdersUserWithoutAuth();
        response.then().assertThat().statusCode(401)
                .body("success",equalTo(false))
                .body("message",equalTo("You should be authorised"));
    }
}
