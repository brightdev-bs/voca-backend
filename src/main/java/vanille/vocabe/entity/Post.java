package vanille.vocabe.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.List;


@Getter
@ToString(callSuper = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Post extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "community_id")
    private Community community;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

    public Post() {}

    @Builder
    public Post(Long id, String content, Community community, List<Comment> comments) {
        this.id = id;
        this.content = content;
        this.community = community;
        this.comments = comments;
    }
}
