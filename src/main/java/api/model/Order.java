package api.model;

import groovy.transform.ASTTest;

import java.util.Arrays;
import java.util.List;

public class Order {

    private List<String> ingredients;

    public Order(String ingredients){
        this.ingredients= Arrays.asList(ingredients);
    }

    public Order() {}

    public List<String> getIngredients() {
        return ingredients;
    }

}
