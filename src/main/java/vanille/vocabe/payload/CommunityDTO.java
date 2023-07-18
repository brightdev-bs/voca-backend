package vanille.vocabe.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vanille.vocabe.entity.Community;
import vanille.vocabe.entity.Post;
import vanille.vocabe.entity.User;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static vanille.vocabe.payload.PostDTO.PostDetail;

public class CommunityDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommunityForm {
        @NotBlank
        private String name;
        private String description;
        private boolean isPublic;
        private int total;
        private User user;

        public Community toEntity() {
            return Community.builder()
                    .name(this.getName())
                    .description(this.getDescription())
                    .open(this.isPublic())
                    .totalMember(this.getTotal())
                    .build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HomeResponse {
        private Long id;
        private String name;
        private String description;
        private boolean open;
        private String totalNumber;
        private Long createdBy;
        private String createdAt;

        public static HomeResponse from(Community c) {
            int joinedMember = c.getCommunityUsers().size();
            return HomeResponse.builder()
                    .id(c.getId())
                    .name(c.getName())
                    .description(c.getDescription())
                    .open(c.isOpen())
                    .totalNumber(joinedMember + " / " + c.getTotalMember())
                    .createdBy(c.getCreatedBy())
                    .createdAt(c.getCreatedAt().toString())
                    .build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommunityDetail {
        private Long id;
        private String name;
        private String description;
        private boolean open;
        private int totalMember;
        private Long createdBy;
        private List<PostDetail> posts = new ArrayList<>();
        private List<Long> joinedMembers = new ArrayList<>();

        public static CommunityDetail from(Community c, List<Post> posts) {
            List<Long> joinedUsers = c.getCommunityUsers().stream().map(cu -> cu.getUser().getId()).collect(Collectors.toList());

            return CommunityDetail.builder()
                    .id(c.getId())
                    .name(c.getName())
                    .description(c.getDescription())
                    .open(c.isOpen())
                    .joinedMembers(joinedUsers)
                    .totalMember(c.getTotalMember())
                    .posts(posts.stream().map(PostDetail::from).collect(Collectors.toList()))
                    .createdBy(c.getCreatedBy())
                    .build();
        }

    }


    @Data
    @Builder
    public static class ExpelleeForm {
        private Long requestId;
        private Long expelleeId;
        private Long communityId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinForm {
        private User user;
        private String content;
        private Long communityId;
    }
}
