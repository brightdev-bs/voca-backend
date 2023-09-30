package vanille.vocabe.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.domain.Page;
import vanille.vocabe.entity.User;
import vanille.vocabe.entity.Word;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

public class WordDTO {

    @Data
    @Builder
    public static class Request {
        @JsonFormat(pattern = "yyyy-MM-dd")
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
        private Long vocabularyId;

        public static WordDetail from(Word word) {
            Long vocaId = null;
            if(word.getVocabulary() != null) vocaId = word.getVocabulary().getId();
            return WordDetail.builder()
                    .id(word.getId())
                    .word(word.getWord())
                    .definition(word.getDefinition())
                    .note(word.getNote())
                    .vocabularyId(vocaId)
                    .build();
        }
    }

    @Data
    public static class WordsResponse {

        private UserDTO.UserDetail user;
        private List<WordDetail> words;
        private int totalPage;

        protected WordsResponse() {}

        private WordsResponse(List<WordDetail> words, int totalPage) {
            this.words = words;
            this.totalPage = totalPage;
        }

        public static WordsResponse from(Page<Word> words) {
            List<WordDetail> detailInfos = words.stream().map(WordDetail::from).collect(Collectors.toList());
            return new WordsResponse(detailInfos, words.getTotalPages());
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
        private Long vocaId;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private String date;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EditWord {
        private Long id;
        @NotBlank
        private String word;
        @NotBlank
        private String definition;
        private String note;
        private User user;
        private Long vocaId;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private String date;
    }
}
