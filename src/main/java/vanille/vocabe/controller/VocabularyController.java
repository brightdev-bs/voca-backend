package vanille.vocabe.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import vanille.vocabe.entity.User;
import vanille.vocabe.global.response.common.ApiResponse;
import vanille.vocabe.payload.VocaDTO;
import vanille.vocabe.service.VocabularyService;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api")
@RequiredArgsConstructor
@Controller
public class VocabularyController {

    private final VocabularyService vocabularyService;

    @PostMapping("/v1/voca")
    public ApiResponse saveVoca(@RequestBody @Valid VocaDTO.SaveForm request, @AuthenticationPrincipal User user) {
        request.setUser(user);
        VocaDTO.Detail detail = vocabularyService.saveVocabulary(request);
        return ApiResponse.of(HttpStatus.CREATED.toString(), detail);
    }

    @GetMapping("/v1/voca")
    public ApiResponse getVocabularies(User user) {
        List<VocaDTO.Response> allVocabularies = vocabularyService.findAllVocabularies(user);
        return ApiResponse.of(HttpStatus.OK.toString(), allVocabularies);
    }
}
