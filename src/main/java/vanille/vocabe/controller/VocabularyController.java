package vanille.vocabe.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import vanille.vocabe.entity.User;
import vanille.vocabe.global.response.common.ApiResponse;
import vanille.vocabe.payload.VocaDTO;
import vanille.vocabe.payload.WordDTO;
import vanille.vocabe.service.VocabularyService;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class VocabularyController {

    private final VocabularyService vocabularyService;

    @PostMapping("/v1/voca")
    public ApiResponse saveVoca(@RequestBody @Valid VocaDTO.SaveForm request, @AuthenticationPrincipal User user) {
        request.setUser(user);
        VocaDTO.Detail detail = vocabularyService.saveVocabulary(request);
        return ApiResponse.of(HttpStatus.CREATED.toString(), detail);
    }

    @GetMapping("/v1/voca")
    public ApiResponse getVocabularies(@AuthenticationPrincipal User user) {
        List<VocaDTO.Response> allVocabularies = vocabularyService.findAllVocabularies(user);
        return ApiResponse.of(HttpStatus.OK.toString(), allVocabularies);
    }

    @GetMapping("/v1/voca/words")
    public ApiResponse getWordsByVoca(
            @PageableDefault Pageable pageable,
            @Valid VocaDTO.SearchForm request,
            @AuthenticationPrincipal User user
    ) throws IllegalAccessException {
        request.setUser(user);
        WordDTO.WordsResponse response = vocabularyService.findAllWordsByVocabularies(pageable, request);
        return ApiResponse.of(HttpStatus.OK.toString(), response);
    }
}
