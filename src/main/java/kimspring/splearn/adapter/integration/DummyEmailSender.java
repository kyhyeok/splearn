package kimspring.splearn.adapter.integration;

import kimspring.splearn.application.required.EmailSender;
import kimspring.splearn.domain.Email;
import org.springframework.stereotype.Component;

@Component
public class DummyEmailSender implements EmailSender {
    @Override
    public void send(Email email, String subject, String body) {
        System.out.println("DummyEmailSender.send email: " + email);
    }
}
