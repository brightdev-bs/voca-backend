package vanille.vocabe.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vanille.vocabe.entity.User;
import vanille.vocabe.entity.Vocabulary;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

public class UserDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignForm {
        @NotBlank
        private String username;
        @NotBlank
        private String email;
        @NotBlank
        @Size(min = 8)
        private String password;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginForm {
        private String email;
        private String password;
    }

    @Data
    @Builder
    public static class UserLoginResponse {
        private Long id;
        private String username;
        private String token;

        public static UserLoginResponse from(User user, String token) {
            return UserLoginResponse.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .token(token)
                    .build();
        }
    }

    @Data
    @Builder
    public static class UserDetail {
        private String username;
        private String email;
        private String role;

        public static UserDetail from(User user) {
            return UserDetail.builder()
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .role(user.getRole().toString())
                    .build();
        }
    }

    @Data
    public static class UserDetailWithStudyRecords {
        private UserDetail user;
        private List<VocaDTO.Response> vocabularyList;
        private List<String> dates;

        protected UserDetailWithStudyRecords(User user, List<String> dates, List<Vocabulary> vocabularyList) {
            this.user = UserDetail.from(user);
            this.dates = dates;
            this.vocabularyList = vocabularyList.stream().map(VocaDTO.Response::from).collect(Collectors.toList());
        }

        public static UserDetailWithStudyRecords from(User user, List<String> list, List<Vocabulary> vocabularyList) {
            return new UserDetailWithStudyRecords(user, list, vocabularyList);
        }
    }

    @Data
    public static class PasswordForm {
        @Size(min = 8)
        String password;
        @Size(min = 8)
        String password2;
        @NotBlank
        String token;

        public PasswordForm() {}

        @Builder
        public PasswordForm(String password, String password2, String token) {
            this.password = password;
            this.password2 = password2;
            this.token = token;
        }
    }

    @Data
    @NoArgsConstructor
    public static class GoogleUser {
        private String name;
        private String email;

        public GoogleUser(String name, String email) {
            this.name = name;
            this.email = email;
        }
    }
}
