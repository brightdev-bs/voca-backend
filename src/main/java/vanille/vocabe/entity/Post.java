package vanille.vocabe.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;


@Getter
@ToString(callSuper = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Post extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    private String content;
    private Long relatedPost;

    @ManyToOne
    @JoinColumn(name = "community_id")
    private Community community;

    @Builder
    public Post(Long id, String content, Long relatedPost, Community community) {
        this.id = id;
        this.content = content;
        this.relatedPost = relatedPost;
        this.community = community;
    }
}
