package vanille.vocabe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.redis.core.RedisTemplate;

import javax.persistence.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @JsonIgnore
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

    public void increaseLiked() {
        this.liked = this.liked + 1;
    }



}
