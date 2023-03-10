package vanille.vocabe.payload;

import lombok.Builder;
import lombok.Data;
import vanille.vocabe.entity.User;
import vanille.vocabe.entity.Vocabulary;

import javax.validation.constraints.NotBlank;

public class VocaDTO {

    @Data
    public static class SaveForm {
        @NotBlank
        private String name;
        private String description;
        private User user;
        private boolean isPublic;

        public SaveForm() {}

        public SaveForm(String name, String description, boolean isPublic) {
            this.name = name;
            this.description = description;
            this.isPublic = isPublic;
        }
    }

    @Data
    public static class SearchForm {
        private Long voca;
        private User user;

        public SearchForm(Long voca, User user) {
            this.voca = voca;
            this.user = user;
        }
    }

    @Data
    public static class Detail {
        private Long id;
        private String name;
        private String description;
        private String username;
        private String createdAt;

        @Builder
        public Detail(Long id, String name, String description, String username, String createdAt) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.username = username;
            this.createdAt = createdAt;
        }

        public static Detail from(User user, Vocabulary voca) {
            return VocaDTO.Detail.builder()
                    .id(voca.getId())
                    .name(voca.getName())
                    .description(voca.getDescription())
                    .username(user.getUsername())
                    .createdAt(voca.getCreatedAt() == null ? null : voca.getCreatedAt().toString())
                    .build();
        }
    }

    @Data
    public static class Response {
        private Long id;
        private String name;
        private String description;

        @Builder
        public Response(Long id, String name, String description) {
            this.id = id;
            this.name = name;
            this.description = description;
        }

        public static Response from(Vocabulary voca) {
            return Response.builder()
                    .id(voca.getId())
                    .name(voca.getName())
                    .description(voca.getDescription())
                    .build();
        }

    }
}
