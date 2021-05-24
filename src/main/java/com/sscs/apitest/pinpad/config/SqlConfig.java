package com.sscs.apitest.pinpad.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;

@Configuration
@Getter
@Setter
@ToString
@PropertySource("classpath:sql.properties")
public class SqlConfig {

    @Autowired
    private Environment env;

    public String getSql(String sqlName){
        return env.getProperty("sql."+sqlName);
    }

}
