package vanille.vocabe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;
import vanille.vocabe.entity.User;
import vanille.vocabe.entity.Vocabulary;
import vanille.vocabe.entity.Word;
import vanille.vocabe.global.constants.ErrorCode;
import vanille.vocabe.global.exception.NotFoundException;
import vanille.vocabe.global.util.DateFormatter;
import vanille.vocabe.payload.WordDTO;
import vanille.vocabe.repository.VocabularyRepository;
import vanille.vocabe.repository.WordQuerydslRepository;
import vanille.vocabe.repository.WordRepository;

import javax.naming.AuthenticationException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class WordServiceImpl implements WordService {

    private final WordRepository wordRepository;
    private final WordQuerydslRepository wordQuerydslRepository;
    private final VocabularyRepository vocabularyRepository;

    @Override
    public List<Word> findWordsWithDate(WordDTO.Request request) {

        log.info("time = {}", request.getDate());

        LocalDate now = DateFormatter.from(request.getDate()).toLocalDate();

        LocalDateTime start = LocalDateTime.of(now, LocalTime.of(0, 0, 0));
        LocalDateTime end = LocalDateTime.of(now, LocalTime.of(23, 59, 59));

        return wordRepository.findByUserAndCreatedAtBetween(request.getUser(), start, end);
    }

    @Override
    public Word saveWord(WordDTO.NewWord request) throws IllegalAccessException {
        Optional<Vocabulary> ofVoca = vocabularyRepository.findByName(request.getVocabularyName());
        Vocabulary vocabulary = null;
        if(ofVoca.isPresent()) {
            vocabulary = ofVoca.get();
        }

        User user = request.getUser();
        if(vocabulary != null && !vocabulary.getCreatedBy().equals(user.getId())) {
            throw new IllegalAccessException(ErrorCode.NO_AUTHORITY.getMessage());
        }

        Word word = Word.of(request.getWord(), request.getDefinition(), request.getNote(), user, vocabulary);
        return wordRepository.save(word);
    }

    @Override
    public Word changeCheck(Long id) {
        Word word = wordRepository.findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_WORD));
        word.changeCheckStatus();
        wordRepository.save(word);
        return word;
    }

    @Override
    public List<String> findPriorStudyRecords(User user) {
        LocalDateTime joinDate = user.getCreatedAt();
        LocalDateTime date = LocalDateTime.now();
        return wordQuerydslRepository.findByUserAndCreatedAtBetweenAndGroupBy(
                user,
                LocalDateTime.of(joinDate.getYear(), joinDate.getMonth(), 1, 0,0,0),
                LocalDateTime.of(date.getYear(), date.getMonth(), date.toLocalDate().lengthOfMonth(), 23, 59, 59)
        );
    }
}
