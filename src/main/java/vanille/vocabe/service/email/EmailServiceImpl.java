package vanille.vocabe.service.email;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vanille.vocabe.entity.EmailToken;
import vanille.vocabe.entity.User;
import vanille.vocabe.global.constants.ErrorCode;
import vanille.vocabe.global.exception.NotFoundException;
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

    @Transactional
    @Override
    public boolean verifyEmail(String tokenId) {
        EmailToken emailToken = emailTokenService
                .findByIdAndExpirationDateAfterAndExpired(tokenId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_EMAIL_TOKEN));
        emailToken.setExpired();

        Optional<User> findUser = userRepository.findByEmail(emailToken.getEmail());

        if(findUser.isPresent()) {
            User user = findUser.get();
            user.setVerified();
            return true;
        } else {
            throw new NotFoundException(ErrorCode.NOT_FOUND_USER);
        }
    }
}
