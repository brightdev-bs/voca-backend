package vanille.vocabe.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import vanille.vocabe.entity.User;
import vanille.vocabe.entity.Word;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class WordDTO {

    @Data
    public static class Request {
        @NotBlank
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private String date;
        private User user;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WordDetail {

        private Long id;
        private String word;
        private String definition;
        private String note;
        private boolean checked;

        public static WordDetail from(Word word) {
            return WordDetail.builder()
                    .id(word.getId())
                    .word(word.getWord())
                    .definition(word.getDefinition())
                    .note(word.getNote())
                    .checked(word.isChecked())
                    .build();
        }
    }

    @Data
    public static class WordsResponse {

        private UserDTO.UserDetail user;
        private List<WordDetail> words;

        protected WordsResponse() {}

        private WordsResponse(UserDTO.UserDetail user, List<WordDetail> words) {
            this.user = user;
            this.words = words;
        }

        public static WordsResponse from(User user, List<Word> words) {
            List<WordDetail> detailInfos = words.stream().map(WordDetail::from).collect(Collectors.toList());
            return new WordsResponse(UserDTO.UserDetail.from(user), detailInfos);
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NewWord {
        @NotBlank
        private String word;
        @NotBlank
        private String definition;
        private String note;
        private User user;
    }
}
