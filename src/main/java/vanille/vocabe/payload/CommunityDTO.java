package vanille.vocabe.payload;

import lombok.Builder;
import lombok.Data;
import vanille.vocabe.entity.Community;
import vanille.vocabe.entity.User;

public class CommunityDTO {

    @Data
    @Builder
    public static class CommunityForm {
        private String name;
        private String description;
        private boolean open;
        private int totalNumber;
        private User user;

        public Community toEntity() {
            return Community.builder()
                    .name(this.getName())
                    .description(this.getDescription())
                    .open(this.isOpen())
                    .totalMember(this.getTotalNumber())
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
