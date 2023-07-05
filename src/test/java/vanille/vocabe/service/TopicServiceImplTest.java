package vanille.vocabe.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import vanille.vocabe.entity.Community;
import vanille.vocabe.entity.Topic;
import vanille.vocabe.global.config.TestConfig;
import vanille.vocabe.repository.CommunityRepository;
import vanille.vocabe.repository.TopicRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static vanille.vocabe.payload.TopicDTO.*;

@Import(TestConfig.class)
@ExtendWith(MockitoExtension.class)
class TopicServiceImplTest {

    @Mock
    TopicRepository topicRepository;

    @Mock
    CommunityRepository communityRepository;

    @InjectMocks
    TopicServiceImpl topicService;

    @DisplayName("Topic 생성")
    @Test
    void createTopic() {
        Community community = mock(Community.class);
        given(communityRepository.findById(any(Long.class))).willReturn(Optional.of(community));

        topicService.createTopic(mock(TopicForm.class));

        then(topicRepository).should().save(any(Topic.class));
    }
}