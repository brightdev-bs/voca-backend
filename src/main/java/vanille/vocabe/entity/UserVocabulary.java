package vanille.vocabe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Getter
@ToString
@Entity
public class UserVocabulary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "vocabulary_id")
    private Vocabulary vocabulary;

    @Enumerated(value = EnumType.STRING)
    VocabularyType type = VocabularyType.CREATED;

    protected UserVocabulary(){}


    protected UserVocabulary(User user, Vocabulary vocabulary, VocabularyType type) {
        this.user = user;
        this.vocabulary = vocabulary;
        this.type = type;
    }


    public static UserVocabulary of(User user, Vocabulary vocabulary, VocabularyType type) {
        return new UserVocabulary(user, vocabulary, type);
    }
}
