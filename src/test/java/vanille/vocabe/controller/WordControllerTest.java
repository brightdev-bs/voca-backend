package vanille.vocabe.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
import vanille.vocabe.fixture.WordFixture;
import vanille.vocabe.global.constants.ErrorCode;
import vanille.vocabe.global.exception.NotFoundException;
import vanille.vocabe.payload.WordDTO;
import vanille.vocabe.repository.UserRepository;
import vanille.vocabe.repository.WordRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static vanille.vocabe.constants.TestConstants.BEARER_TOKEN;
import static vanille.vocabe.global.Constants.VERIFIED_USER_EMAIL;

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
    private WordRepository wordRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();

        user = userRepository.findByEmail(VERIFIED_USER_EMAIL).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER));
    }

    @DisplayName("[성공] 새로운 단어 추가 성공")
    @Test
    void saveWord() throws Exception {
        WordDTO.NewWord request = WordDTO.NewWord.builder()
                .word("test")
                .definition("test입니다.")
                .date(LocalDate.now().toString())
                .build();

        mockMvc.perform(post("/api/v1/words")
                        .header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
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
        wordRepository.save(WordFixture.get(user));
        wordRepository.save(WordFixture.get(user));
        wordRepository.save(WordFixture.get(user));

        mockMvc.perform(get("/api/v1/words?date="  + LocalDate.now())
                        .header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
                )
                .andDo(print())
                .andExpect(jsonPath("$.data.words", hasSize(3)));
    }

    @DisplayName("단어가 많으면 10개씩 가져온다.")
    @Test
    void pagingTest() throws Exception {
        initDummyWords(user);
        WordDTO.Request request = WordDTO.Request.builder()
                .date(String.valueOf(LocalDate.now()))
                .user(user)
                .build();

        mockMvc.perform(get("/api/v1/words?date=" + request.getDate())
                        .header(HttpHeaders.AUTHORIZATION, BEARER_TOKEN)
                ).andExpect(jsonPath("statusCode").value(HttpStatus.OK.toString()))
                .andExpect(jsonPath("$.data.words", hasSize(10)))
                .andExpect(jsonPath("$.data.totalPage").value(2))
                .andDo(print());

    }

    private void initDummyWords(User user) {
        for(int i = 0; i < 12; i++) {
            wordRepository.save(WordFixture.get(user));
        }
    }
}