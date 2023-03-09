package vanille.vocabe.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vanille.vocabe.entity.User;
import vanille.vocabe.entity.UserVocabulary;
import vanille.vocabe.entity.Vocabulary;
import vanille.vocabe.payload.VocaDTO;
import vanille.vocabe.repository.UserVocabularyRepository;
import vanille.vocabe.repository.VocabularyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class VocabularyServiceImpl implements VocabularyService {

    private final VocabularyRepository vocabularyRepository;
    private final UserVocabularyRepository userVocabularyRepository;

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

}
