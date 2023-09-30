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
import vanille.vocabe.global.exception.DuplicatedEntityException;
import vanille.vocabe.global.exception.NotFoundException;
import vanille.vocabe.payload.VocaDTO;
import vanille.vocabe.payload.WordDTO;
import vanille.vocabe.repository.UserVocabularyRepository;
import vanille.vocabe.repository.VocabularyQuerydslRepository;
import vanille.vocabe.repository.VocabularyRepository;
import vanille.vocabe.repository.WordRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class VocabularyServiceImpl implements VocabularyService {

    private final VocabularyRepository vocabularyRepository;
    private final VocabularyQuerydslRepository vocabularyQuerydslRepository;
    private final UserVocabularyRepository userVocabularyRepository;
    private final WordRepository wordRepository;

    @Override
    public List<VocaDTO.PopularVocabulary> findPublicVocabulariesForHome() {
        List<Vocabulary> vocabularies = vocabularyQuerydslRepository.findPublicVocabulariesLimitFive();
        return vocabularies.stream().map(VocaDTO.PopularVocabulary::from).collect(Collectors.toList());
    }

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
        Vocabulary voca = Vocabulary.of(form.getName(), form.getDescription(), form.isPublicFlag());
        vocabularyRepository.save(voca);

        UserVocabulary userVocabulary = UserVocabulary.of(user, voca);
        userVocabularyRepository.save(userVocabulary);

        return VocaDTO.Detail.from(user, voca);
    }

    @Override
    public VocaDTO.VocaWordResponse findAllWordsByVocabularies(Pageable pageable, Long id) throws IllegalAccessException {
        Vocabulary vocabulary = vocabularyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_VOCABULARY));
        Page<Word> words = wordRepository.findALLByVocabularyId(pageable, vocabulary.getId());
        List<WordDTO.WordDetail> wordList = words.stream().map(w -> WordDTO.WordDetail.from(w)).collect(Collectors.toList());
        return VocaDTO.VocaWordResponse.of(wordList, words.getTotalPages());
    }

    // Todo : 테스트 작성 해야 함.
    @Transactional
    @Override
    public void addPublicVocabulary(User user, Long id) {
        Vocabulary vocabulary = vocabularyRepository.findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_VOCABULARY));

        Optional<UserVocabulary> optionalUserVoca = userVocabularyRepository.findUserVocabularyByUserAndVocabulary(user, vocabulary);
        if(optionalUserVoca.isPresent()) {
            throw new DuplicatedEntityException(ErrorCode.DUPLICATED_VOCABULARY);
        }
        userVocabularyRepository.save(UserVocabulary.of(user, vocabulary));
        vocabulary.increaseLiked();
    }
}
