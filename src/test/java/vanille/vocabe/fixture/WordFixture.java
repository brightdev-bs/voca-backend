package vanille.vocabe.fixture;

import vanille.vocabe.entity.User;
import vanille.vocabe.entity.Word;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class WordFixture {

    public static Word get(User user) {
        return Word.of("mango", "망고", "mango is delicious", user, null, String.valueOf(LocalDate.now()));
    }

}
