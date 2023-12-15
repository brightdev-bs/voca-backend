package vanille.vocabe.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vanille.vocabe.entity.User;
import vanille.vocabe.entity.Vocabulary;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

public class VocaDTO {

    @Data
    public static class SaveForm {
        @NotBlank
        private String name;
        private String description;
        private User user;
        private boolean publicFlag;

        public SaveForm() {}

        public SaveForm(String name, String description, boolean isPublic) {
            this.name = name;
            this.description = description;
            this.publicFlag = isPublic;
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
        private boolean isPublic;

        @Builder
        public Detail(Long id, String name, String description, String username, String createdAt, boolean isPublic) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.username = username;
            this.createdAt = createdAt;
            this.isPublic = isPublic;
        }

        public static Detail from(User user, Vocabulary voca) {
            return Detail.builder()
                    .id(voca.getId())
                    .name(voca.getName())
                    .description(voca.getDescription())
                    .username(user.getUsername())
                    .createdAt(voca.getCreatedAt() == null ? null : voca.getCreatedAt().toString())
                    .isPublic(voca.isPublic())
                    .build();
        }
    }

    @Data
    public static class PopularVocabulary {
        private Long id;
        private String name;
        private String description;
        private int liked;
        private Long createdBy;

        @Builder
        public PopularVocabulary(Long id, String name, String description, Long createdBy, int liked) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.liked = liked;
            this.createdBy = createdBy;
        }

        public static PopularVocabulary from(Vocabulary voca) {
            return PopularVocabulary.builder()
                    .id(voca.getId())
                    .name(voca.getName())
                    .description(voca.getDescription())
                    .liked(voca.getLiked())
                    .createdBy(voca.getCreatedBy())
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

    @Data
    @NoArgsConstructor
    public static class VocaWordResponse {
        private List<WordDTO.WordDetail> words = new ArrayList<>();
        private int totalPage;
        private String title;
        private boolean liked;

        public VocaWordResponse(List<WordDTO.WordDetail> words, int totalPage, String title, boolean liked) {
            this.words = words;
            this.totalPage = totalPage;
            this.title = title;
            this.liked = liked;
        }

        public static VocaWordResponse of(List<WordDTO.WordDetail> list, int totalPage, String name, boolean liked) {
            return new VocaDTO.VocaWordResponse(list, totalPage, name, liked);
        }

    }
}
