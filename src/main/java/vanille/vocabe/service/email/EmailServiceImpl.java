package vanille.vocabe.service.email;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import vanille.vocabe.entity.EmailToken;
import vanille.vocabe.entity.User;
import vanille.vocabe.repository.UserRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class EmailServiceImpl implements EmailService{

    private final EmailTokenService emailTokenService;
    private final UserRepository userRepository;

    @Override
    public void sendConfirmEmail(String email) {
        emailTokenService.createEmailToken(email);
    }

    @Override
    public boolean verifyEmail(String token) throws Exception {
        EmailToken findEmailToken = emailTokenService.findByIdAndExpirationDateAfterAndExpired(token);

        // Todo : 이메일 성공 인증 로직
        Optional<User> findUser = userRepository.findByEmailAndVerifiedTrue(findEmailToken.getEmail());
        findEmailToken.setExpired();

        if(findUser.isPresent()) {
            User user = findUser.get();
            user.setVerified();
            return true;
        } else {
            throw new Exception("error");
        }
    }
}
