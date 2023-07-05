package vanille.vocabe.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vanille.vocabe.entity.Community;
import vanille.vocabe.entity.Topic;

import javax.validation.constraints.NotNull;

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
}
