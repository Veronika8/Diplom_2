import api.client.OrderClient;
import api.client.UserClient;
import api.model.Order;
import api.model.User;
import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CreateOrderTest extends BaseTest {
    String email = "test2343267232992@test.ru";
    String password="123734348922";
    String name="nametest";

    UserClient userClient = new UserClient();
    OrderClient orderClient=new OrderClient();
    User user = new User(email,password,name);

    @Test
    public void createOrderWithAuth() {
        Response response = userClient.sendPostRequestRegister(user);
        response.then().assertThat().statusCode(200)
                .body("success",equalTo(true));

        String accessToken=response.then().extract().body().path("accessToken");

        Response response1=orderClient.sendGetRequestIngredients();
        String idIngredients = response1.then().extract().body().path("data[0]._id");

        Order order = new Order(idIngredients);
        Response response2 = orderClient.sendPostRequestCreateOrderWithAuth(order,accessToken.replace("Bearer ",""));
        response2.then().assertThat().statusCode(200)
                        .body("success",equalTo(true))
                                .body("order.ingredients[0]._id",equalTo(idIngredients))
                                        .body("order.owner.email",equalTo(email));

        userClient.sendDeleteRequestUser(accessToken.replace("Bearer ",""));
    }

    @Test
    public void createOrderWithoutAuth() {

        Response response=orderClient.sendGetRequestIngredients();
        String idIngredients = response.then().extract().body().path("data[0]._id");

        Order order = new Order(idIngredients);
        Response response1 = orderClient.sendPostRequestCreateOrderWithoutAuth(order);
        response1.then().assertThat().statusCode(200)
                .body("success",equalTo(true))
                .body("order.number",notNullValue());

    }

    @Test
    public void createOrderWithoutInredients() {
        Order order = new Order();
        Response response=orderClient.sendPostRequestCreateOrderWithoutAuth(order);
        response.then().assertThat().statusCode(400)
                .body("success",equalTo(false))
                .body("message",equalTo("Ingredient ids must be provided"));
    }

    @Test
    public void creteOrderWithInvalidHash() {
        Response response=orderClient.sendGetRequestIngredients();
        String idIngredients = response.then().extract().body().path("data[0]._id");

        Order order = new Order(idIngredients+"123");
        Response response1 = orderClient.sendPostRequestCreateOrderWithoutAuth(order);
        response1.then().assertThat().statusCode(500);
    }
}