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

    @Id @GeneratedValue
    private Long id;

    private String content;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "community_id")
    private Community community;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @NotNull
    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;


    public Post() {}

    @Builder
    public Post(Long id, String content, Community community, List<Comment> comments, Topic topic) {
        this.id = id;
        this.content = content;
        this.community = community;
        this.comments = comments == null ? new ArrayList<>() : comments;
        this.topic = topic;
    }
}
