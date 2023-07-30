package vanille.vocabe.entity;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import vanille.vocabe.global.util.DateFormatter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@ToString(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Word {

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

    private LocalDate createdAt;

    @CreatedBy
    @Column(updatable = false)
    private Long createdBy;

    protected Word() {}

    protected Word(String word, String definition, String note, User user, Vocabulary vocabulary, String date) {
        this.word = word;
        this.definition = definition;
        this.note = note;
        this.user = user;
        this.vocabulary = vocabulary;
        this.createdAt = LocalDate.parse(date);
    }

    public static Word of(String word, String definition, String note, User user, Vocabulary vocabulary, String date) {
        return new Word(word, definition, note, user, vocabulary, date);
    }

    public void changeCheckStatus() {
        this.checked = !this.checked;
    }
}
