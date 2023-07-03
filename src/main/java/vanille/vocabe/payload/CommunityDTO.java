package vanille.vocabe.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vanille.vocabe.entity.Community;
import vanille.vocabe.entity.User;

import javax.validation.constraints.NotBlank;

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
    public static class Response {
        private Long id;
        private String name;
        private String description;
        private boolean open;
        private String totalNumber;
        private Long createdBy;
        private String createdAt;

        public static Response from(Community c) {
            int joinedMember = c.getCommunityUsers().size();
            return Response.builder()
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
    public static class ExpelleeForm {
        private Long requestId;
        private Long expelleeId;
        private Long communityId;
    }
}
