package vanille.vocabe.entity;

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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "vocabulary_id")
    private Vocabulary vocabulary;

    protected UserVocabulary(){}


    protected UserVocabulary(User user, Vocabulary vocabulary) {
        this.user = user;
        this.vocabulary = vocabulary;
    }


    public static UserVocabulary of(User user, Vocabulary vocabulary) {
        return new UserVocabulary(user, vocabulary);
    }
}
