package vanille.vocabe.service;

import vanille.vocabe.entity.User;
import vanille.vocabe.entity.Word;
import vanille.vocabe.payload.WordDTO;

import java.util.List;

public interface WordService {
    List<Word> findWordsWithDate(WordDTO.Request request);

    Word saveWord(WordDTO.NewWord request);

    Word changeCheck(Long id);

    List<String> findPriorStudyRecords(User user);
}
