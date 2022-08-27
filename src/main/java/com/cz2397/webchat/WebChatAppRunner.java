package com.cz2397.webchat;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.InetAddress;
import java.net.UnknownHostException;


@EnableScheduling
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class, SecurityAutoConfiguration.class})
@MapperScan("com.cz2397.webchat.dao")
public class WebChatAppRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebChatAppRunner.class);

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext application= SpringApplication.run(WebChatAppRunner.class, args);
        Environment env = application.getEnvironment();
        LOGGER.info("\n[----------------------------------------------------------]\n\t" +
                        "Welcome to WebChat!\thttp://{}:{}" +
                        "\n[----------------------------------------------------------",
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"));
    }
}

