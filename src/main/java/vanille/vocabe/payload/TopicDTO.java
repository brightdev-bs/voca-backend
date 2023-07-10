package vanille.vocabe.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vanille.vocabe.entity.Community;
import vanille.vocabe.entity.Topic;

import javax.validation.constraints.NotNull;
import java.util.List;

import static vanille.vocabe.payload.PostDTO.*;

public class TopicDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopicForm {
        String content;
        Long communityId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopicDetail {
        Long id;
        String content;
        String createdAt;
        Long createdBy;

        public static TopicDetail from(Topic topic) {
            return TopicDetail.builder()
                    .id(topic.getId())
                    .content(topic.getContent())
                    .createdAt(topic.getCreatedAt() == null ? null : topic.getCreatedAt().toString().substring(0, 10))
                    .createdBy(topic.getCreatedBy())
                    .build();
        }

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopicMain {
        private Long topicId;
        private String topic;
        private List<PostDetail> posts;

        public static TopicMain from(Topic topic, List<PostDetail> posts) {
            return TopicMain.builder()
                    .topicId(topic.getId())
                    .topic(topic.getContent())
                    .posts(posts)
                    .build();
        }

    }
}
