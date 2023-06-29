package vanille.vocabe.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vanille.vocabe.entity.Community;
import vanille.vocabe.payload.CommunityDTO;
import vanille.vocabe.repository.CommunityRepository;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommunityServiceImpl implements CommunityService {

    private final CommunityRepository communityRepository;


    @Override
    public Community saveCommunity(CommunityDTO.CommunityForm form) {
        return communityRepository.save(form.toEntity());
    }


}
