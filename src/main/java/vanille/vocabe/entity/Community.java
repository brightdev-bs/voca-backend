package vanille.vocabe.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@ToString(callSuper = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Community extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    private String description;
    private int totalMember;
    private boolean open;
    @ToString.Exclude
    @OneToMany(mappedBy = "community")
    private List<CommunityUser> communityUsers = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "community")
    private List<Topic> topics = new ArrayList<>();

    protected Community() {}

    @Builder
    public Community(Long id, String name, String description, int totalMember, boolean open, List<CommunityUser> communityUsers, List<Topic> topics) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.totalMember = totalMember;
        this.open = open;
        this.communityUsers = communityUsers == null ? new ArrayList<>() : communityUsers;
        this.topics = topics == null ? new ArrayList<>() : topics;
    }
}
