package vanille.vocabe.fixture;

import vanille.vocabe.entity.User;
import vanille.vocabe.entity.Word;

public class WordFixture {

    public static Word get(User user) {
        return Word.of("mango", "망고", "mango is delicious", user);
    }
}
