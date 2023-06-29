package vanille.vocabe.service;

import org.springframework.transaction.annotation.Transactional;
import vanille.vocabe.entity.Community;
import vanille.vocabe.entity.User;
import vanille.vocabe.payload.CommunityDTO;
import vanille.vocabe.payload.UserDTO;

import static vanille.vocabe.payload.CommunityDTO.*;

public interface CommunityService {

    @Transactional
    Community saveCommunity(CommunityForm form);
}
