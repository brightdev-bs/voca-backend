package vanille.vocabe.service.email;

public interface EmailService {
    void sendSignUpConfirmEmail(String email);

    boolean verifyEmail(String token);

    void sendPasswordFindEmail(String email);
}
