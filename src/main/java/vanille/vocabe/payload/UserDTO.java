package vanille.vocabe.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vanille.vocabe.entity.User;

public class UserDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignForm {
        private String username;
        private String email;
        private String password;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class loginForm {
        private String email;
        private String password;
    }

    @Data
    @Builder
    public static class UserResponse {
        private String username;
        private String token;

        public static UserResponse from(User user, String token) {
            return UserResponse.builder()
                    .username(user.getUsername())
                    .token(token)
                    .build();
        }
    }
}
