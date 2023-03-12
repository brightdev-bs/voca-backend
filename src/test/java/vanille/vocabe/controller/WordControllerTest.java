package vanille.vocabe.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import vanille.vocabe.entity.User;
import vanille.vocabe.fixture.UserFixture;
import vanille.vocabe.fixture.WordFixture;
import vanille.vocabe.global.util.DateFormatter;
import vanille.vocabe.payload.WordDTO;
import vanille.vocabe.repository.UserRepository;
import vanille.vocabe.repository.WordRepository;

import javax.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static vanille.vocabe.constants.TestConstants.BEARER_TOKEN;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class WordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WordRepository wordRepository;

    private User user;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();

        user = UserFixture.getVerifiedUser();
        userRepository.save(user);

        wordRepository.save(WordFixture.get(user));
        wordRepository.save(WordFixture.get(user));
        wordRepository.save(WordFixture.get(user));
    }

    @DisplayName("[성공] 새로운 단어 추가 성공")
    @Test
    void saveWord() throws Exception {
        WordDTO.NewWord request = WordDTO.NewWord.builder()
                .word("test")
                .definition("test입니다.")
                .build();

        mockMvc.perform(post("/api/v1/words")
                        .header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(jsonPath("statusCode").value(HttpStatus.OK.toString()));
    }

    @DisplayName("[실패] 새로운 단어 추가 실패 - 파라미터")
    @ParameterizedTest
    @MethodSource("wordForm")
    void saveWordFailWithParam(String word, String definition) throws Exception {

        WordDTO.NewWord request = WordDTO.NewWord.builder()
                .word(word)
                .definition(definition)
                .build();

        mockMvc.perform(post("/api/v1/words")
                        .header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(jsonPath("statusCode").value(HttpStatus.BAD_REQUEST.toString()))
                .andDo(print());
    }
    private static Stream<Arguments> wordForm() {
        return Stream.of(
                Arguments.of(null, "testDefinition"),
                Arguments.of("test", null)
        );
    }

    @DisplayName("[성공] 단어 조회 (날짜 기준)")
    @Test
    void getWordsWithDate() throws Exception {
        WordDTO.Request request = new WordDTO.Request();
        request.setDate(LocalDateTime.now().toString());

        mockMvc.perform(get("/api/v1/words?date=" + LocalDate.now() + " 00:00:00")
                        .header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
                )
                .andExpect(jsonPath("$.data.words", hasSize(3)))
                .andDo(print());
    }

}