package vanille.vocabe.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import vanille.vocabe.entity.User;
import vanille.vocabe.entity.UserVocabulary;
import vanille.vocabe.entity.Vocabulary;
import vanille.vocabe.fixture.UserFixture;
import vanille.vocabe.fixture.VocabularyFixture;
import vanille.vocabe.payload.VocaDTO;
import vanille.vocabe.repository.UserCacheRepository;
import vanille.vocabe.repository.UserRepository;
import vanille.vocabe.repository.UserVocabularyRepository;
import vanille.vocabe.repository.VocabularyRepository;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class VocabularyServiceImplTest {

    @Mock
    private VocabularyRepository vocabularyRepository;

    @Mock
    private UserVocabularyRepository userVocabularyRepository;

    @Mock
    private UserCacheRepository userCacheRepository;

    @InjectMocks
    private VocabularyServiceImpl vocabularyService;


    @DisplayName("[성공] 단어장 생성")
    @ParameterizedTest
    @MethodSource("saveForm")
    void saveVoca(VocaDTO.SaveForm saveForm) {
        User user = UserFixture.getVerifiedUser();
        saveForm.setUser(user);

        VocaDTO.Detail detail = vocabularyService.saveVocabulary(saveForm);

        then(vocabularyRepository).should().save(any(Vocabulary.class));
        then(userVocabularyRepository).should().save(any(UserVocabulary.class));
        Assertions.assertEquals(saveForm.isPublicFlag(), detail.isPublic());
    }
    static Stream<VocaDTO.SaveForm> saveForm() {
        return Stream.of(
                new VocaDTO.SaveForm("테스트 단어장 1", null, true),
                new VocaDTO.SaveForm("테스트 단어장 2", null, false)
        );
    }

    @DisplayName("[성공] 단어장 리스트 검색")
    @Test
    void findAllVocabularies() {
        User user = UserFixture.getVerifiedUser();
        List<UserVocabulary> vocabularies = user.getVocabularies();
        vocabularies.add(UserVocabulary.of(user, VocabularyFixture.getVocabularyFixture("test")));
        vocabularies.add(UserVocabulary.of(user, VocabularyFixture.getVocabularyFixture("test2")));
        vocabularies.add(UserVocabulary.of(user, VocabularyFixture.getVocabularyFixture("test3")));

        User user2 = UserFixture.getVerifiedUser("teseter2");
        List<UserVocabulary> vocabularies2 = user2.getVocabularies();
        vocabularies2.add(UserVocabulary.of(user2, VocabularyFixture.getVocabularyFixture("test222")));

        List<VocaDTO.Response> allVocabularies = vocabularyService.findAllVocabularies(user);
        Assertions.assertEquals(3, allVocabularies.size());
    }

}