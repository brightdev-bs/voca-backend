package vanille.vocabe.service.email;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

@RequiredArgsConstructor
@Service
public class EmailServiceImpl implements EmailService {

    private final EmailTokenService emailTokenService;
    private final UserRepository userRepository;
    private final EmailSender emailSender;
    private final String SIGN_UP_MAIL_SUBJECT = "회원가입 이메일 인증";
    private final String PASSWORD_FIND_SUBJECT = "패스워드 찾기";
    @Value("${front-server}")
    public String FRONT_SERVER;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void sendSignUpConfirmEmail(String email) {
        EmailToken emailToken = emailTokenService.createEmailToken(email);
        sendEmail(emailToken, SIGN_UP_MAIL_SUBJECT, "/email?token=");
    }

    /**
     *
     * @param emailToken
     * @param subject
     * @param path 쿼리 파라미터의 경우 '?[name]='추가할 것. /example?token=
     */
    private void sendEmail(EmailToken emailToken, String subject, String path) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(emailToken.getEmail());
        mailMessage.setSubject(subject);
        mailMessage.setText(FRONT_SERVER + path + emailToken.getToken());
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

    @Override
    public void sendPasswordFindEmail(String email) {
        EmailToken emailToken = emailTokenService.createEmailToken(email);
        sendEmail(emailToken, PASSWORD_FIND_SUBJECT, "/password/options?token=" + emailToken.getToken());
    }

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
