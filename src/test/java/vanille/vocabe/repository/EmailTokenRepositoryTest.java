package vanille.vocabe.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import vanille.vocabe.entity.EmailToken;
import vanille.vocabe.global.config.jpaConfig.JpaConfig;

@DataJpaTest
@Import(JpaConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EmailTokenRepositoryTest {

    @Autowired
    private EmailTokenRepository emailTokenRepository;

    @DisplayName("[성공] EmailToken 찾아오기")
    @Test
    void getValidEmailToken() {
        String email = "vanille@gmail.com";
        EmailToken emailToken = EmailToken.createEmailToken(email);
        emailTokenRepository.saveAndFlush(emailToken);

        EmailToken findEmailToken = emailTokenRepository
                .findByToken(emailToken.getToken())
                .orElseThrow(() -> new IllegalArgumentException());
        Assertions.assertEquals(email, findEmailToken.getEmail());
        Assertions.assertEquals(emailToken.getId(), findEmailToken.getId());
    }

}