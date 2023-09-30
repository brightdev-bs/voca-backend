package vanille.vocabe.entity;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@ToString
@Entity
public class UserWord extends BaseEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "vocabulary_id")
    private Vocabulary vocabulary;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @OneToOne
    private Word word;
    @Column(columnDefinition="BOOLEAN DEFAULT false")
    private boolean studied;

    public UserWord() {}

    protected UserWord(User user, Vocabulary vocabulary, Word word) {
        this.user = user;
        this.vocabulary = vocabulary;
        this.word = word;
        this.studied = false;
    }

    public static UserWord of(User user, Vocabulary vocabulary, Word word) {
        return new UserWord(user, vocabulary, word);
    }

    public void studiedTrue() {
        this.studied = true;
    }

    public void studiedFalse() {
        this.studied = false;
    }
}
