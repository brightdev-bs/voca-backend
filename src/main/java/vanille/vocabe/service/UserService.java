package vanille.vocabe.service;

import org.springframework.transaction.annotation.Transactional;
import vanille.vocabe.entity.User;
import vanille.vocabe.payload.UserDTO;

public interface UserService {
    @Transactional
    User saveUser(UserDTO.SignForm form);

    User login(UserDTO.loginForm form);
}
