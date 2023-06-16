package vanille.vocabe.service.email;

public interface EmailService {
    void sendSignUpConfirmEmail(String email);

    boolean verifyEmail(String token) throws Exception;

}
