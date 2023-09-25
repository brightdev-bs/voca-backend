package vanille.vocabe.global.oepnFeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import vanille.vocabe.global.config.authConfig.GoogleConfig;
import vanille.vocabe.global.response.common.ApiResponse;
import vanille.vocabe.payload.UserDTO;
import vanille.vocabe.payload.UserDTO.GoogleUser;

import javax.validation.constraints.NotNull;

@FeignClient(name = "google", url = "https://www.googleapis.com", configuration = GoogleConfig.class)
public interface GoogleLogin {

    @GetMapping("/userinfo/v2/me")
    GoogleUser googleLogin(@RequestHeader("Authorization") String token);
}
