package vanille.vocabe.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@ToString
@Entity
public class CommunityUser {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "community_id")
    private Community community;

    public User getUser() {
        return user;
    }

    public Community getCommunity() {
        return community;
    }


    protected CommunityUser() {
    }

    @Builder
    public CommunityUser(Long id, User user, Community community) {
        this.id = id;
        this.user = user;
        this.community = community;
    }
}
