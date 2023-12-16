package vanille.vocabe.service;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import vanille.vocabe.entity.User;
import vanille.vocabe.payload.VocaDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface VocabularyService {

    List<VocaDTO.PopularVocabulary> findPublicVocabulariesForHome();

    List<VocaDTO.Response> findAllVocabularies(User user);

    @Transactional
    VocaDTO.Detail saveVocabulary(VocaDTO.SaveForm form);

    VocaDTO.VocaWordResponse findAllWordsByVocabularies(Pageable pageable, Long vocaId, HttpServletRequest request) throws IllegalAccessException;

    @Transactional
    void addPublicVocabulary(User user, Long id);

    List<VocaDTO.Response> findAllVocabulariesByKeyword(String keyword);
}
