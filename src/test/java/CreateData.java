import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;

import java.util.Locale;

public class CreateData {

    public String createEmail() {
        FakeValuesService fakeValuesService = new FakeValuesService(
                new Locale("en-GB"), new RandomService());
        String email = fakeValuesService.bothify("????????#####@mail.ru");
        return email;
    }

    public String createPassword() {
        FakeValuesService fakeValuesService = new FakeValuesService(
                new Locale("en-GB"), new RandomService());
        String password = fakeValuesService.bothify("????????#####");
        return password;
    }

    public String createName() {
        FakeValuesService fakeValuesService = new FakeValuesService(
                new Locale("en-GB"), new RandomService());
        String name = fakeValuesService.bothify("????????");
        return name;
    }
}
