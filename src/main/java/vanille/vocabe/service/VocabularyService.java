package vanille.vocabe.service;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import vanille.vocabe.entity.User;
import vanille.vocabe.payload.VocaDTO;
import vanille.vocabe.payload.WordDTO;

import java.util.List;

public interface VocabularyService {
    List<VocaDTO.Response> findAllVocabularies(User user);

    @Transactional
    VocaDTO.Detail saveVocabulary(VocaDTO.SaveForm form);

    WordDTO.WordsResponse findAllWordsByVocabularies(Pageable pageable, VocaDTO.SearchForm form) throws IllegalAccessException;
}
