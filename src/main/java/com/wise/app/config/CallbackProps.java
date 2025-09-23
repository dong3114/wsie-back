// com.wise.app.config.CallbackProps
package com.wise.app.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Getter; import lombok.Setter;

@Getter @Setter
@Component
@ConfigurationProperties(prefix = "wsie.callback")
public class CallbackProps {
    private String apiKey;
}
