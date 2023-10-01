package vanille.vocabe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import vanille.vocabe.payload.UserDTO;
import vanille.vocabe.payload.WordDTO;
import vanille.vocabe.repository.VocabularyRepository;
import vanille.vocabe.repository.WordQuerydslRepository;
import vanille.vocabe.repository.WordRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class WordServiceImpl implements WordService {

    private final WordRepository wordRepository;
    private final WordQuerydslRepository wordQuerydslRepository;
    private final VocabularyRepository vocabularyRepository;
    @Override
    public Page<Word> findWordsWithDate(final Pageable pageable, final WordDTO.Request request) {
        LocalDate date = LocalDate.parse(request.getDate());
        return wordRepository.findByUserAndCreatedAtAndDeleted(pageable, request.getUser(), date, false);
    }

    @Transactional
    @Override
    public Word saveWord(WordDTO.NewWord request) throws IllegalAccessException {

        Vocabulary vocabulary = null;
        if(request.getVocaId() != null) {
            vocabulary = vocabularyRepository.findById(request.getVocaId())
                    .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_VOCABULARY));
        }

        User user = request.getUser();
        checkEditorableUser(vocabulary, user);

        Word word = Word.of(request.getWord(), request.getDefinition(), request.getNote(), user, vocabulary, request.getDate());
        return wordRepository.save(word);
    }

    @Transactional
    @Override
    public Word updateWord(WordDTO.EditWord request) throws IllegalAccessException {
        // 단어가 있는지 확인
        Word word = wordRepository.findById(request.getId()).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_WORD));
        log.info("word = {}", word);

        Vocabulary vocabulary = null;
        if(request.getVocaId() != null) {
            vocabulary = vocabularyRepository.findById(request.getVocaId())
                    .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_VOCABULARY));
        }

        User user = request.getUser();
        checkEditorableUser(vocabulary, user);

        word.update(request, vocabulary);
        return word;
    }

    private void checkEditorableUser(Vocabulary vocabulary, User user) throws IllegalAccessException {
        if(vocabulary != null && !vocabulary.getCreatedBy().equals(user.getId())) {
            throw new IllegalAccessException(ErrorCode.NO_AUTHORITY.getMessage());
        }
    }

    @Transactional
    @Override
    public Word changeStudiedFlag(Long id, User user) {
        Word word = wordRepository.findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_WORD));
        word.changeStudyFlag();
        return word;
    }

    @Override
    public UserDTO.UserDetailWithStudyRecords findPriorStudyRecords(User user) {
        LocalDateTime joinDate = user.getCreatedAt();
        LocalDateTime date = LocalDateTime.now();
        List<String> studiedDates = wordQuerydslRepository.findByUserAndCreatedAtBetweenAndGroupBy(
                user,
                LocalDate.of(joinDate.getYear(), joinDate.getMonth(), 1),
                LocalDate.of(date.getYear(), date.getMonth(), date.toLocalDate().lengthOfMonth())
        );

        List<UserVocabulary> vocabularies = user.getVocabularies();
        List<Vocabulary> vocaList = new ArrayList<>();
        for (UserVocabulary uv : vocabularies) {
            vocaList.add(uv.getVocabulary());
        }

        return UserDTO.UserDetailWithStudyRecords.from(user, studiedDates, vocaList);
    }

    @Transactional
    @Override
    public void deleteWord(Long id, User user) throws IllegalAccessException {
        Word word = wordRepository.findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_WORD));
        if (!word.getCreatedBy().equals(user.getId())) {
            throw new IllegalAccessException(ErrorCode.NO_AUTHORITY.getMessage());
        }
        wordRepository.delete(word);
    }
}
