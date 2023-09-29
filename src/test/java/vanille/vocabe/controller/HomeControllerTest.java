package vanille.vocabe.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@Transactional
@AutoConfigureMockMvc
@SpringBootTest
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;


    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    @DisplayName("메인 페이지에서 가장 유명한 단어장 5개를 볼 수 있다.")
    @Test
    void getPopularVocabularies() throws Exception {
        mockMvc.perform(get("/api/v1/home"))
                .andDo(print())
                .andExpect(jsonPath("statusCode").value(HttpStatus.OK.toString()))
                .andExpect(jsonPath("$.data.size()").value(5))
                .andExpect(jsonPath("$.data.[0].id").hasJsonPath())
                .andExpect(jsonPath("$.data.[0].name").hasJsonPath())
                .andExpect(jsonPath("$.data.[0].description").hasJsonPath())
                .andExpect(jsonPath("$.data.[0].liked").hasJsonPath())
                .andExpect(jsonPath("$.data.[0].createdBy").hasJsonPath());
    }
}
