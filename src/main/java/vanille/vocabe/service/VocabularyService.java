package vanille.vocabe.service;

import org.springframework.transaction.annotation.Transactional;
import vanille.vocabe.entity.User;
import vanille.vocabe.payload.VocaDTO;

import java.util.List;

public interface VocabularyService {
    List<VocaDTO.Response> findAllVocabularies(User user);

    @Transactional
    VocaDTO.Detail saveVocabulary(VocaDTO.SaveForm form);
}
