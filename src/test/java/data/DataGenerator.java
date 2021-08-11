package data;

import com.github.javafaker.Faker;
import lombok.Data;

import java.util.Locale;

@Data

public class DataGenerator {
    private static final Faker faker = new Faker(new Locale("en"));

    public static String getRandomLogin() {
        String login = faker.cat().name();
        return login;
    }

    public static String getRandomPassword() {
            String password = faker.bothify("#??#?#");
            return password;
    }

    public static String getRandomAuthCode(){
        String authCode = faker.numerify("######");
        return authCode;
    }

    public static String getRandomUserId(){
//        В идеале получить из таблицы последний созданный ID в расшифрованном виде и прибавить к нему единицу.
        String userId  = faker.numerify("######");
        return userId;
    }
}
