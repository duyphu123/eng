package com.smartviet.base.salary;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

@Slf4j
@EnableCaching
@EnableConfigurationProperties
@SpringBootApplication(
        exclude = {
                RedisAutoConfiguration.class,
                RedisRepositoriesAutoConfiguration.class
        }
)
@EnableFeignClients(basePackages = "com.smartviet.base.salary.services.iservice")
public class Application {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(Application.class);
        application.addInitializers(new DotenvInitializer());
        application.run(args);
        log.info("Application started successfully");
    }

    public static class DotenvInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            ConfigurableEnvironment environment = applicationContext.getEnvironment();
            String env = environment.getProperty("spring.profiles.name");
            String folder = environment.getProperty("spring.environment.active.folder");
            String file = environment.getProperty("spring.environment.active.file");
            log.info("Service version {}, code {} provided by {}",
                    environment.getProperty("spring.application.version"),
                    environment.getProperty("spring.application.code"),
                    environment.getProperty("spring.application.provider"));
            log.info("Configured environment: {}", env);
            log.info("Configured folder: {}", folder);
            log.info("Configured file: {}", file);
            if (StringUtils.isBlank(folder) || StringUtils.isBlank(file)) {
                throw new IllegalStateException("Environment folder or file is not configured properly");
            }
            Dotenv dotenv = Dotenv.configure()
                    .directory(folder)
                    .filename(file)
                    .load();
            dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
        }
    }

}
