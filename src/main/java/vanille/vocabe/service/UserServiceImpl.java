package vanille.vocabe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vanille.vocabe.entity.EmailToken;
import vanille.vocabe.entity.User;
import vanille.vocabe.global.constants.ErrorCode;
import vanille.vocabe.global.exception.DuplicatedEntityException;
import vanille.vocabe.global.exception.InvalidPasswordException;
import vanille.vocabe.global.exception.NotFoundException;
import vanille.vocabe.global.exception.UnverifiedException;
import vanille.vocabe.payload.UserDTO;
import vanille.vocabe.repository.UserRepository;
import vanille.vocabe.service.email.EmailService;
import vanille.vocabe.service.email.EmailTokenService;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final EmailTokenService emailTokenService;

    @Transactional
    @Override
    public User saveUser(UserDTO.SignForm form) {

        User user = userRepository.findByEmail(form.getEmail()).orElseGet(() -> null);

        if(user != null) {
            if(user.isVerified()) {
                throw new DuplicatedEntityException(ErrorCode.DUPLICATED_USER);
            }

            checkIfNameIsDuplicated(form);

            user.changeUsernameAndPassword(form.getUsername(), encodePassword(form.getPassword()));
        } else {
            checkIfNameIsDuplicated(form);
            user = User.from(form);
        }

        userRepository.save(user);

        emailService.sendSignUpConfirmEmail(form.getEmail());
        return user;
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private void checkIfNameIsDuplicated(UserDTO.SignForm form) {
        Optional<User> byUsername = userRepository.findByUsername(form.getUsername());
        if(byUsername.isPresent()) {
            throw new DuplicatedEntityException(ErrorCode.DUPLICATED_USERNAME);
        }
    }

    @Override
    public User login(UserDTO.LoginForm form) {
        User user = userRepository.findByEmail(form.getEmail()).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER));

        if(!user.isVerified()) {
            throw new UnverifiedException(ErrorCode.UNVERIFIED_USER);
        }

        String password = user.getPassword();
        if (!passwordEncoder.matches(form.getPassword(), password)) {
            throw new InvalidPasswordException(ErrorCode.INVALID_PASSWORD);
        }

        return user;
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsernameAndVerifiedTrue(username).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER));
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER));
    }

    @Transactional
    @Override
    public boolean changeUserPassword(UserDTO.PasswordForm form) {
        EmailToken emailToken = emailTokenService.findByToken(form.getToken())
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_TOKEN));

        if (emailTokenService.validateToken(emailToken)) {

            if(form.getPassword().equals(form.getPassword2())) {
                User user = userRepository.findByEmail(emailToken.getEmail())
                        .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER));
                user.changePassword(encodePassword(form.getPassword()));
                userRepository.save(user);
                return true;
            } else {
                throw new InvalidPasswordException(ErrorCode.INVALID_PASSWORD);
            }
        }

        return false;
    }


}
