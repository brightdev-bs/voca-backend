package vanille.vocabe.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vanille.vocabe.entity.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ApplicantDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApplicantDetail {
        @NotNull
        private Long applicantId;
        private Long communityId;
        @NotBlank
        private boolean accept;

        private User user;
    }
}
