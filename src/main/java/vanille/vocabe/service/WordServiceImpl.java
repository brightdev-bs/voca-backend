package vanille.vocabe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vanille.vocabe.entity.User;
import vanille.vocabe.entity.UserVocabulary;
import vanille.vocabe.entity.Vocabulary;
import vanille.vocabe.entity.Word;
import vanille.vocabe.global.constants.ErrorCode;
import vanille.vocabe.global.exception.NotFoundException;
import vanille.vocabe.global.util.DateFormatter;
import vanille.vocabe.payload.UserDTO;
import vanille.vocabe.payload.WordDTO;
import vanille.vocabe.repository.VocabularyRepository;
import vanille.vocabe.repository.WordQuerydslRepository;
import vanille.vocabe.repository.WordRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
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
        LocalDate date = LocalDate.parse(request.getDate());
        return wordRepository.findByUserAndCreatedAt(request.getUser(), date);
    }

    @Override
    public Word saveWord(WordDTO.NewWord request) throws IllegalAccessException {

        Vocabulary vocabulary = null;
        if(request.getVocaId() != null) {
            vocabulary = vocabularyRepository.findById(request.getVocaId())
                    .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_VOCABULARY));
        }

        User user = request.getUser();
        if(vocabulary != null && !vocabulary.getCreatedBy().equals(user.getId())) {
            throw new IllegalAccessException(ErrorCode.NO_AUTHORITY.getMessage());
        }

        Word word = Word.of(request.getWord(), request.getDefinition(), request.getNote(), user, vocabulary, request.getDate());
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
}
