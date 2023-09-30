package vanille.vocabe.service;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import vanille.vocabe.entity.User;
import vanille.vocabe.payload.VocaDTO;

import java.util.List;

public interface VocabularyService {

    List<VocaDTO.PopularVocabulary> findPublicVocabulariesForHome();

    List<VocaDTO.Response> findAllVocabularies(User user);

    @Transactional
    VocaDTO.Detail saveVocabulary(VocaDTO.SaveForm form);

    VocaDTO.VocaWordResponse findAllWordsByVocabularies(Pageable pageable, Long id) throws IllegalAccessException;

    void addPublicVocabulary(User user, Long id);
}
