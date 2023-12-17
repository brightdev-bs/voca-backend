package vanille.vocabe.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vanille.vocabe.entity.Vocabulary;
import vanille.vocabe.global.response.common.ApiResponse;
import vanille.vocabe.payload.VocaDTO;
import vanille.vocabe.service.VocabularyService;

import java.util.List;

@Slf4j
@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class HomeController {

    private final VocabularyService vocabularyService;

    @GetMapping("/v1/home")
    public ApiResponse home(@RequestParam(defaultValue = "0") int page) {
        List<VocaDTO.PopularVocabulary> response = vocabularyService.findPublicVocabulariesForHome(page);
        return ApiResponse.of(HttpStatus.OK.toString(), response);
    }
}
