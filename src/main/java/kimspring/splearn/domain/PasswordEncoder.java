package kimspring.splearn.domain;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public interface PasswordEncoder {
    String encode(String password);

    boolean matches(String password, String passwordHash);
}
