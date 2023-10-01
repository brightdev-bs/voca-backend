package vanille.vocabe.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import vanille.vocabe.entity.User;
import vanille.vocabe.entity.Word;
import vanille.vocabe.payload.UserDTO;
import vanille.vocabe.payload.WordDTO;

public interface WordService {
    Page<Word> findWordsWithDate(Pageable pageable, WordDTO.Request request);

    Word saveWord(WordDTO.NewWord request) throws IllegalAccessException;

    @Transactional
    Word updateWord(WordDTO.EditWord request) throws IllegalAccessException;


    Word changeStudiedFlag(Long id, User user);

    UserDTO.UserDetailWithStudyRecords findPriorStudyRecords(User user);

    @Transactional
    void deleteWord(Long id, User user) throws IllegalAccessException;
}
