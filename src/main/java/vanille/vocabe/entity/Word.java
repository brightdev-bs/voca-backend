package vanille.vocabe.entity;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@ToString(callSuper = true)
@Entity
public class Word extends BaseEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String word;
    private String definition;
    private String note;
    private boolean checked;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ToString.Exclude
    @OneToOne
    @JoinColumn(name = "vocabulary_id")
    private Vocabulary vocabulary;

    protected Word() {}

    protected Word(String word, String definition, String note, User user, Vocabulary vocabulary) {
        this.word = word;
        this.definition = definition;
        this.note = note;
        this.user = user;
        this.vocabulary = vocabulary;
    }

    public static Word of(String word, String definition, String note, User user, Vocabulary vocabulary) {
        return new Word(word, definition, note, user, vocabulary);
    }

    public void changeCheckStatus() {
        this.checked = !this.checked;
    }
}
