package com.sscs.apitest.pinpad.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.concurrent.*;

@Configuration
@Getter
@Setter
@ToString
@PropertySources({
        @PropertySource("classpath:pinpad.properties"),
        //@PropertySource("classpath:bar.properties")
})
public class PinpadConfig {

    @Autowired
    private Environment env;

    public String getTermKey(String term){
        return env.getProperty("pinpad."+term+".termkey");
    }

}
