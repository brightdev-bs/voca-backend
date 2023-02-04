package vanille.vocabe.service.email;

public interface EmailService {
    void sendConfirmEmail(String email);

    boolean verifyEmail(String token) throws Exception;

}
