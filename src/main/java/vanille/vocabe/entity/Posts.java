package vanille.vocabe.entity;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;


@Getter
@ToString(callSuper = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Posts extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    private String content;
    private String contentRaw;
    private String writer;
    private Long relatedPost;

    @ManyToOne
    @JoinColumn(name = "community_id")
    private Community community;
}
