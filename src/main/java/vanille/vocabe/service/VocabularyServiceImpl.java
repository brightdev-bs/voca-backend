package vanille.vocabe.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vanille.vocabe.entity.User;
import vanille.vocabe.entity.UserVocabulary;
import vanille.vocabe.entity.Vocabulary;
import vanille.vocabe.entity.Word;
import vanille.vocabe.global.constants.ErrorCode;
import vanille.vocabe.global.exception.NotFoundException;
import vanille.vocabe.payload.VocaDTO;
import vanille.vocabe.payload.WordDTO;
import vanille.vocabe.repository.UserVocabularyRepository;
import vanille.vocabe.repository.VocabularyRepository;
import vanille.vocabe.repository.WordRepository;

import java.util.List;
import java.util.stream.Collectors;


@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class VocabularyServiceImpl implements VocabularyService {

    private final VocabularyRepository vocabularyRepository;
    private final UserVocabularyRepository userVocabularyRepository;
    private final WordRepository wordRepository;

    @Override
    public List<VocaDTO.Response> findAllVocabularies(User user) {
        List<UserVocabulary> userVocabularies = user.getVocabularies();
        return userVocabularies.stream()
                .map(uv -> VocaDTO.Response.from(uv.getVocabulary()))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public VocaDTO.Detail saveVocabulary(VocaDTO.SaveForm form) {
        User user = form.getUser();
        Vocabulary voca = Vocabulary.of(form.getName(), form.getDescription(), form.isPublic());
        vocabularyRepository.save(voca);

        UserVocabulary userVocabulary = UserVocabulary.of(user, voca);
        userVocabularyRepository.save(userVocabulary);

        return VocaDTO.Detail.from(user, voca);
    }

    @Override
    public WordDTO.WordsResponse findAllWordsByVocabularies(Pageable pageable, VocaDTO.SearchForm form) throws IllegalAccessException {
        Vocabulary vocabulary = vocabularyRepository.findById(form.getVoca())
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_VOCABULARY));

        User user = form.getUser();
        if(!vocabulary.getCreatedBy().equals(user.getId())) {
            throw new IllegalAccessException(ErrorCode.NO_AUTHORITY.getMessage());
        }

        Page<Word> words = wordRepository.findALLByVocabularyId(pageable, vocabulary.getId());
        return WordDTO.WordsResponse.from(user, words);
    }

}
