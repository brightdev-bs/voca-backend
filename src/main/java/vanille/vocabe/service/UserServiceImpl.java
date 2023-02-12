package vanille.vocabe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vanille.vocabe.entity.User;
import vanille.vocabe.global.constants.ErrorCode;
import vanille.vocabe.global.exception.DuplicatedEntityException;
import vanille.vocabe.global.exception.InvalidPasswordException;
import vanille.vocabe.global.exception.NotFoundException;
import vanille.vocabe.global.exception.UnverifiedEmailException;
import vanille.vocabe.payload.UserDTO;
import vanille.vocabe.repository.UserRepository;
import vanille.vocabe.service.email.EmailService;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final EmailService emailService;

    @Transactional
    @Override
    public User saveUser(UserDTO.SignForm form) {

        Optional<User> optionalUser = userRepository.findByEmail(form.getEmail());

        User user;
        if(optionalUser.isPresent()) {
            user = optionalUser.get();
            if(user.isVerified()) {
                throw new DuplicatedEntityException(ErrorCode.DUPLICATED_EMAIL);
            }
        } else {
            user = User.from(form);
            userRepository.save(user);
        }

        emailService.sendConfirmEmail(form.getEmail());
        return user;
    }

    @Override
    public User login(UserDTO.loginForm form) {
        User user = userRepository.findByEmail(form.getEmail()).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER));

        if(!user.getPassword().equals(form.getPassword())) {
            throw new InvalidPasswordException(ErrorCode.INVALID_PASSWORD);
        }

        if(!user.isVerified()) {
            throw new UnverifiedEmailException(ErrorCode.UNVERIFIED_USER);
        }

        return user;
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER));
    }


}
