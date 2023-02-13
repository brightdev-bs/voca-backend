package vanille.vocabe.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vanille.vocabe.entity.Word;
import vanille.vocabe.global.constants.ErrorCode;
import vanille.vocabe.global.exception.NotFoundException;
import vanille.vocabe.payload.WordDTO;
import vanille.vocabe.repository.WordRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class WordServiceImpl implements WordService {

    private final WordRepository wordRepository;

    @Override
    public List<Word> findWordsWithDate(WordDTO.Request request) {
        LocalDateTime start = LocalDateTime.of(request.getDate().toLocalDate(), LocalTime.of(0, 0, 0));
        LocalDateTime end = LocalDateTime.of(request.getDate().toLocalDate(), LocalTime.of(23, 59, 59));

        return wordRepository.findByUserAndCreatedAtBetween(request.getUser(), start, end);
    }

    @Override
    public Word saveWord(WordDTO.NewWord request) {
        Word word = Word.of(request.getWord(), request.getDefinition(), request.getNote(), request.getUser());
        return wordRepository.save(word);
    }

    @Override
    public Word changeCheck(Long id) {
        Word word = wordRepository.findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_WORD));
        word.changeCheckStatus();
        return word;
    }
}
