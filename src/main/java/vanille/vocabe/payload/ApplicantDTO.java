package vanille.vocabe.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vanille.vocabe.entity.Applicant;
import vanille.vocabe.entity.User;
import vanille.vocabe.repository.ApplicantRepository;

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

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApplicantResponse {
        private Long id;
        private String name;
        private String content;

        public static ApplicantResponse from(Applicant applicant) {
            return ApplicantResponse.builder()
                    .id(applicant.getId())
                    .name(applicant.getUser().getUsername())
                    .content(applicant.getMotive())
                    .build();
        }
    }
}
