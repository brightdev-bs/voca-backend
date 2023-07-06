package vanille.vocabe.service;

import org.springframework.transaction.annotation.Transactional;
import vanille.vocabe.entity.Community;
import vanille.vocabe.payload.ApplicantDTO;

import javax.mail.AuthenticationFailedException;
import java.util.List;

import static vanille.vocabe.payload.CommunityDTO.*;

public interface CommunityService {


    CommunityDetail getCommunityDetails(Long communityId);

    @Transactional
    Community saveCommunity(CommunityForm form) throws AuthenticationFailedException;

    @Transactional
    void joinRequest(JoinForm form);

    @Transactional
    void applyToCommunity(Long userId, Long communityId);

    @Transactional
    void expelUser(ExpelleeForm form);

    List<HomeResponse> getCommunities();

    List<Community> getCommunities(String name);


    void responseForApplicant(ApplicantDTO.ApplicantDetail form);
}
