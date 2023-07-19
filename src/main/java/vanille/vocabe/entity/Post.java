package vanille.vocabe.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


@Getter
@ToString(callSuper = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Post extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private String writer;

    @ManyToOne
    @JoinColumn(name = "community_id")
    private Community community;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    public Post() {}

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    @Builder
    public Post(Long id, String content, String writer, Community community, List<Comment> comments) {
        this.id = id;
        this.content = content;
        this.writer = writer;
        this.community = community;
        this.comments = comments == null ? new ArrayList<>() : comments;
    }
}
