package vanille.vocabe.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vanille.vocabe.global.constants.Constants;
import vanille.vocabe.global.response.common.ApiResponse;
import vanille.vocabe.payload.TopicDTO;
import vanille.vocabe.repository.TopicRepository;
import vanille.vocabe.service.TopicService;

import javax.validation.Valid;

import static vanille.vocabe.payload.TopicDTO.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class TopicController {

    private final TopicService topicService;

    @PostMapping("/community/{id}/topic")
    public ApiResponse createTopic(@PathVariable Long id, @RequestBody @Valid TopicForm form) {
        form.setCommunityId(id);
        topicService.createTopic(form);
        return ApiResponse.of(HttpStatus.CREATED.toString(), Constants.CREATED);
    }

}