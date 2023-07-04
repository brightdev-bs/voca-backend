package vanille.vocabe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Comment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    @ManyToOne
    @JoinColumn(name = "post_id")
    @JsonIgnore
    private Post post;
    private String writer;

    public Comment() {}

    @Builder
    public Comment(Long id, String content, Post post, String writer) {
        this.id = id;
        this.content = content;
        this.post = post;
        this.writer = writer;
    }
}
