package vanille.vocabe.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import vanille.vocabe.entity.User;
import vanille.vocabe.global.constants.Constants;
import vanille.vocabe.global.response.common.ApiResponse;
import vanille.vocabe.payload.VocaDTO;
import vanille.vocabe.service.VocabularyService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
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
    public ApiResponse getMyVocabularies(@AuthenticationPrincipal User user) {
        List<VocaDTO.MyVocaResponse> allVocabularies = vocabularyService.findAllMyVocabularies(user);
        return ApiResponse.of(HttpStatus.OK.toString(), allVocabularies);
    }

    @GetMapping("/v1/voca/{voca}")
    public ApiResponse getWordsByVoca(
            @PageableDefault Pageable pageable,
            @PathVariable Long voca,
            HttpServletRequest request
    ) throws IllegalAccessException {
        VocaDTO.VocaWordResponse response = vocabularyService.findAllWordsByVocabularies(pageable, voca, request);
        log.info("{}", response);
        return ApiResponse.of(HttpStatus.OK.toString(), response);
    }

    @PostMapping("/v1/voca/{id}/liked")
    public ApiResponse addLike(@PathVariable Long id, @AuthenticationPrincipal User user) {
        vocabularyService.likeVocabulary(user, id);
        return ApiResponse.of(HttpStatus.OK.toString(), Constants.SUCCESS);
    }

    @GetMapping("/v1/voca/search")
    public ApiResponse getVocabularyByKeyword(@RequestParam String keyword) {
        List<VocaDTO.Response> allVocabularies = vocabularyService.findAllVocabulariesByKeyword(keyword);
        return ApiResponse.of(HttpStatus.OK.toString(), allVocabularies);
    }

}
