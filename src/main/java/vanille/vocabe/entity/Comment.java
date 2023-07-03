package vanille.vocabe.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Builder
@Getter
@ToString(callSuper = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Comment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    private int liked;
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public Comment() {}

    @Builder
    public Comment(Long id, String content, int liked, Post post) {
        this.id = id;
        this.content = content;
        this.liked = liked;
        this.post = post;
    }
}
