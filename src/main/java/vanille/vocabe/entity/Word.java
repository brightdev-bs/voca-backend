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

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private User user;

    protected Word() {}

    protected Word(String word, String definition, String note, User user) {
        this.word = word;
        this.definition = definition;
        this.note = note;
        this.user = user;
    }

    public static Word of(String word, String definition, String note, User user) {
        return new Word(word, definition, note, user);
    }

    public void changeCheckStatus() {
        this.checked = !this.checked;
    }
}
