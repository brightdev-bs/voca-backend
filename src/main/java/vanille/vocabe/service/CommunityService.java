package vanille.vocabe.service;

import org.springframework.transaction.annotation.Transactional;
import vanille.vocabe.entity.Community;
import vanille.vocabe.entity.User;

import javax.mail.AuthenticationFailedException;
import java.util.List;

import static vanille.vocabe.payload.CommunityDTO.*;

public interface CommunityService {


    @Transactional
    Community saveCommunity(CommunityForm form) throws AuthenticationFailedException;

    @Transactional
    void joinRequest(Long communityId, User user);

    @Transactional
    void applyToCommunity(Long userId, Long communityId);

    List<HomeResponse> getCommunities();
}
