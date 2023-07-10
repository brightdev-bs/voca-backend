package vanille.vocabe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vanille.vocabe.entity.Community;
import vanille.vocabe.entity.Topic;
import vanille.vocabe.global.constants.ErrorCode;
import vanille.vocabe.global.exception.NotFoundException;
import vanille.vocabe.payload.PostDTO;
import vanille.vocabe.payload.TopicDTO;
import vanille.vocabe.repository.CommunityRepository;
import vanille.vocabe.repository.TopicRepository;

import java.util.List;

import static vanille.vocabe.payload.TopicDTO.*;
import static vanille.vocabe.payload.TopicDTO.TopicForm;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;

    private final CommunityRepository communityRepository;


    @Transactional
    @Override
    public void createTopic(TopicForm form) {

        Community community = communityRepository.findById(form.getCommunityId()).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_COMMUNITY));
        Topic topic = Topic.builder()
                .content(form.getContent())
                .community(community)
                .build();
        topicRepository.save(topic);
    }

    @Override
    public TopicMain getTopicMain(Long topicId, List<PostDTO.PostDetail> posts) {
        Topic topic = topicRepository.findById(topicId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_TOPIC));
        return TopicMain.from(topic, posts);
    }
}

