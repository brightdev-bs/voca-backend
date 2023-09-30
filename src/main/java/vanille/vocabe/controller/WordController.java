package vanille.vocabe.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import vanille.vocabe.entity.User;
import vanille.vocabe.entity.Word;
import vanille.vocabe.global.constants.Constants;
import vanille.vocabe.global.response.common.ApiResponse;
import vanille.vocabe.payload.WordDTO;
import vanille.vocabe.service.UserWordService;
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
    public ApiResponse getWords(
            @PageableDefault Pageable pageable,
            @Valid WordDTO.Request request,
            @AuthenticationPrincipal User user) {
        request.setUser(user);
        Page<Word> words = wordService.findWordsWithDate(pageable, request);
        return ApiResponse.of(HttpStatus.OK.toString(), WordDTO.WordsResponse.from(words));
    }

    @PostMapping("/words")
    public ApiResponse saveWord(@RequestBody @Valid WordDTO.NewWord request, @AuthenticationPrincipal User user) throws IllegalAccessException {
        request.setUser(user);
        Word word = wordService.saveWord(request);
        return ApiResponse.of(HttpStatus.OK.toString(), WordDTO.WordDetail.from(word));
    }

    @PutMapping("/words/{id}")
    public ApiResponse changeWord(@PathVariable Long id, @RequestBody @Valid WordDTO.EditWord request, @AuthenticationPrincipal User user) throws IllegalAccessException {
        request.setUser(user);
        Word word = wordService.updateWord(request);
        return ApiResponse.of(HttpStatus.OK.toString(), WordDTO.WordDetail.from(word));
    }

    @PatchMapping("/words/{id}")
    public ApiResponse changeCheck(@PathVariable Long id, @AuthenticationPrincipal User user) {
        Word word = wordService.changeCheck(id, user);
        return ApiResponse.of(HttpStatus.OK.toString(), WordDTO.WordDetail.from(word));
    }

    @DeleteMapping("/words/{id}")
    public ApiResponse deleteWord(@PathVariable Long id, @AuthenticationPrincipal User user) throws IllegalAccessException {
        wordService.deleteWord(id, user);
        return ApiResponse.of(HttpStatus.OK.toString(), Constants.SUCCESS);
    }
}
