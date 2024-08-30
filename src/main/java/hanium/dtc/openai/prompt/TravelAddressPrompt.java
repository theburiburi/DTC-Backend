package hanium.dtc.openai.prompt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "openai.travel-address-prompt")
public class TravelAddressPrompt {
    private List<String> roles;
    private List<String> contents;
}