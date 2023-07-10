package vanille.vocabe.service;

import org.springframework.transaction.annotation.Transactional;
import vanille.vocabe.payload.TopicDTO;

import java.util.List;

import static vanille.vocabe.payload.PostDTO.*;

public interface TopicService {
    @Transactional
    void createTopic(TopicDTO.TopicForm form);

    TopicDTO.TopicMain getTopicMain(Long topicId, List<PostDetail> posts);
}
