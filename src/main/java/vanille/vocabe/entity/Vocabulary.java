package vanille.vocabe.entity;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@Entity
public class Vocabulary extends BaseEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private int liked;

    private boolean isPublic;

    @OneToMany(mappedBy = "vocabulary")
    private List<UserVocabulary> userVocabularies;

    public Vocabulary() {}

    protected Vocabulary(String name, String description, boolean isPublic) {
        this.name = name;
        this.description = description;
        this.liked = 0;
        this.userVocabularies = new ArrayList<>();
        this.isPublic = isPublic;
    }

    public static Vocabulary of(String name, String description, boolean isPublic) {
        return new Vocabulary(name, description, isPublic);
    }

    public void setIdForTest(Long id) {
        this.id = id;
    }

}
