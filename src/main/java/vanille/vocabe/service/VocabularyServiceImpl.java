package vanille.vocabe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vanille.vocabe.entity.*;
import vanille.vocabe.global.constants.ErrorCode;
import vanille.vocabe.global.exception.DuplicatedEntityException;
import vanille.vocabe.global.exception.NotFoundException;
import vanille.vocabe.global.util.JwtTokenUtils;
import vanille.vocabe.payload.VocaDTO;
import vanille.vocabe.payload.WordDTO;
import vanille.vocabe.repository.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class VocabularyServiceImpl implements VocabularyService {


    @Value("${jwt.secret-key}")
    private String secretKey;

    private final UserRepository userRepository;
    private final VocabularyRepository vocabularyRepository;
    private final VocabularyQuerydslRepository vocabularyQuerydslRepository;
    private final UserVocabularyRepository userVocabularyRepository;
    private final WordRepository wordRepository;
    private final UserCacheRepository userCacheRepository;

    @Override
    public List<VocaDTO.PopularVocabulary> findPublicVocabulariesForHome(int page) {
        List<Vocabulary> vocabularies = vocabularyQuerydslRepository.findPublicVocabulariesLimitFive(page);
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

        UserVocabulary userVocabulary = UserVocabulary.of(user, voca, VocabularyType.CREATED);
        userVocabularyRepository.save(userVocabulary);
        user.getVocabularies().add(userVocabulary);
        userCacheRepository.setUser(user);

        return VocaDTO.Detail.from(user, voca);
    }

    @Override
    public VocaDTO.VocaWordResponse findAllWordsByVocabularies(Pageable pageable, Long voca, HttpServletRequest request) {
        Vocabulary vocabulary = vocabularyRepository.findById(voca)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_VOCABULARY));
        Page<Word> words = wordRepository.findALLByVocabularyId(pageable, vocabulary.getId());
        List<WordDTO.WordDetail> wordList = words.stream().map(WordDTO.WordDetail::from).collect(Collectors.toList());

        String token = request.getHeader("Authorization");
        boolean liked = false;
        if (token != null && !token.isEmpty()) {
            String username = JwtTokenUtils.getUsername(token, secretKey);
            User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER));
            liked = checkIfTheUserAlreadyLikedVoca(user, vocabulary);
        }

        return VocaDTO.VocaWordResponse.of(wordList, words.getTotalPages(), vocabulary.getName(), liked);
    }

    private boolean checkIfTheUserAlreadyLikedVoca(User user, Vocabulary voca) {

        Optional<UserVocabulary> _result = userVocabularyRepository.findUserVocabularyByUserAndVocabulary(user, voca);
        return _result.isPresent();

    }

    // Todo : 테스트 작성 해야 함.
    @Transactional
    @Override
    public void likeVocabulary(User user, Long id) {
        Vocabulary vocabulary = vocabularyRepository.findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_VOCABULARY));
        Optional<UserVocabulary> optionalUserVoca = userVocabularyRepository.findUserVocabularyByUserAndVocabulary(user, vocabulary);
        if(optionalUserVoca.isPresent()) {
            throw new DuplicatedEntityException(ErrorCode.DUPLICATED_VOCABULARY);
        }
        UserVocabulary userVocabulary = userVocabularyRepository.save(UserVocabulary.of(user, vocabulary, VocabularyType.LIKED));
        user.getVocabularies().add(userVocabulary);
        userCacheRepository.setUser(user);
        vocabulary.increaseLiked();
    }

    @Override
    public List<VocaDTO.Response> findAllVocabulariesByKeyword(String keyword) {
        List<Vocabulary> vocabulariesByKeyword = vocabularyQuerydslRepository.findVocabulariesByKeyword(keyword);
        return vocabulariesByKeyword.stream().map(VocaDTO.Response::from).collect(Collectors.toList());
    }
}
