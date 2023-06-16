package vanille.vocabe.service.email;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import vanille.vocabe.entity.EmailToken;
import vanille.vocabe.entity.User;
import vanille.vocabe.global.constants.ErrorCode;
import vanille.vocabe.global.exception.ExpiredTokenException;
import vanille.vocabe.global.exception.InvalidVerificationCodeException;
import vanille.vocabe.global.exception.NotFoundException;
import vanille.vocabe.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static vanille.vocabe.global.constants.Constants.FRONT_SERVER;

@RequiredArgsConstructor
@Service
public class EmailServiceImpl implements EmailService {

    private final EmailTokenService emailTokenService;
    private final UserRepository userRepository;
    private final EmailSender emailSender;
    private final String SIGN_UP_MAIL_SUBJECT = "회원가입 이메일 인증";

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void sendSignUpConfirmEmail(String email) {
        EmailToken emailToken = emailTokenService.createEmailToken(email);
        sendEmail(emailToken, SIGN_UP_MAIL_SUBJECT);
    }

    private void sendEmail(EmailToken emailToken, String subject) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(emailToken.getEmail());
        mailMessage.setSubject(subject);
        mailMessage.setText(FRONT_SERVER + "/email?token=" + emailToken.getToken());
        emailSender.sendEmail(mailMessage);
    }

    @Transactional
    @Override
    public boolean verifyEmail(String tokenId) {
        if(!StringUtils.hasText(tokenId)) {
            throw new InvalidVerificationCodeException(ErrorCode.INVALID_VERIFICATION_CODE);
        }

        EmailToken emailToken = emailTokenService
                .findByToken(tokenId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_EMAIL_TOKEN));

        Optional<User> findUser = userRepository.findByEmail(emailToken.getEmail());
        if (emailToken.isExpired() || emailToken.getExpirationDate().isBefore(LocalDateTime.now())) {
            if(verifyUser(findUser)) {
                emailTokenService.createEmailToken(findUser.get().getEmail());
            }
            throw new ExpiredTokenException(ErrorCode.EXPIRED_TOKEN);
        }

        emailToken.setExpired(true);


        return verifyUser(findUser);
    }

//    @Override
//    public void sendPasswordFindEmail(String email) {
//        emailTokenService.createEmailToken(email);
//    }

    private static boolean verifyUser(Optional<User> findUser) {
        if(findUser.isPresent()) {
            User user = findUser.get();
            user.setVerified();
            return true;
        } else {
            throw new NotFoundException(ErrorCode.NOT_FOUND_USER);
        }
    }
}
