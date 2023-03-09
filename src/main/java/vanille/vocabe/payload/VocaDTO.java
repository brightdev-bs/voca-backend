package vanille.vocabe.payload;

import lombok.Builder;
import lombok.Data;
import vanille.vocabe.entity.User;
import vanille.vocabe.entity.Vocabulary;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class VocaDTO {

    @Data
    public static class SaveForm {
        @NotBlank
        private String name;
        private String description;
        private User user;
        @NotBlank
        private boolean isPublic;

        public SaveForm() {}

        public SaveForm(String name, String description, boolean isPublic) {
            this.name = name;
            this.description = description;
            this.isPublic = isPublic;
        }
    }

    @Data
    public static class Detail {
        private String name;
        private String description;
        private String username;
        private String createdAt;

        @Builder
        public Detail(String name, String description, String username, String createdAt) {
            this.name = name;
            this.description = description;
            this.username = username;
            this.createdAt = createdAt;
        }

        public static Detail from(User user, Vocabulary voca) {
            return VocaDTO.Detail.builder()
                    .name(voca.getName())
                    .description(voca.getDescription())
                    .username(user.getUsername())
                    .createdAt(voca.getCreatedAt() == null ? null : voca.getCreatedAt().toString())
                    .build();
        }
    }

    @Data
    public static class Response {
        private String name;
        private String description;

        @Builder
        public Response(String name, String description) {
            this.name = name;
            this.description = description;
        }

        public static Response from(Vocabulary voca) {
            return Response.builder()
                    .name(voca.getName())
                    .description(voca.getDescription())
                    .build();
        }

    }
}
