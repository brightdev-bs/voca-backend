package vanille.vocabe.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
class WordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("[성공] 새로운 단어 추가 성공")
    @Test
    void saveWord() {
//        mockMvc.perform()
    }

    @DisplayName("[실패] 새로운 단어 추가 실패 - 파라미터")
    @Test
    void saveWordFailWithParam() {

    }
}