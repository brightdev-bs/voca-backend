package vanille.vocabe.service;

import org.springframework.transaction.annotation.Transactional;
import vanille.vocabe.payload.TopicDTO;

public interface TopicService {
    @Transactional
    void createTopic(TopicDTO.TopicForm form);
}
