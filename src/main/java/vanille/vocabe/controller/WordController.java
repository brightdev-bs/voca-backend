package vanille.vocabe.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import vanille.vocabe.entity.User;
import vanille.vocabe.entity.Word;
import vanille.vocabe.global.response.common.ApiResponse;
import vanille.vocabe.payload.WordDTO;
import vanille.vocabe.service.WordService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class WordController {

    private final WordService wordService;

    @GetMapping("/words")
    public ApiResponse getWords(@Valid WordDTO.Request request, @AuthenticationPrincipal User user) {
        request.setUser(user);
        log.info("request = {}", request);
        List<Word> words = wordService.findWordsWithDate(request);
        return ApiResponse.of(HttpStatus.OK.toString(), WordDTO.WordsResponse.from(user, words));
    }

    @PostMapping("/words")
    public ApiResponse saveWord(@RequestBody @Valid WordDTO.NewWord request, @AuthenticationPrincipal User user) throws IllegalAccessException {
        request.setUser(user);
        Word word = wordService.saveWord(request);
        return ApiResponse.of(HttpStatus.OK.toString(), WordDTO.WordDetail.from(word));
    }

    @PatchMapping("/words/{id}")
    public ApiResponse changeCheck(@PathVariable Long id) {
        Word word = wordService.changeCheck(id);
        return ApiResponse.of(HttpStatus.OK.toString(), WordDTO.WordDetail.from(word));
    }
}
