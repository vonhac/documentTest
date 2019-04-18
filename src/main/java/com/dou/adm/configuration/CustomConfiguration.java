package com.dou.adm.configuration;

import com.dou.document.ftp.ClientInfo;
import com.dou.document.ftp.ServerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableConfigurationProperties(ServerProperties.class)
public class CustomConfiguration {

    @Autowired
    private ServerProperties ftpServerInfo;

    /**
     * Register a ClientInfo bean for Spring container from loaded properties
     * @return ClientInfo
     */
    @Bean
    public ClientInfo createClientInfoBean() {
        if (ftpServerInfo.isValidInfo()) {
            return new ClientInfo(
                    ftpServerInfo.getAddress().getIp(),
                    ftpServerInfo.getAddress().getPort(),
                    ftpServerInfo.getAuth().getUsername(),
                    ftpServerInfo.getAuth().getPassword(),
                    ftpServerInfo.getOption().getRetryTimes(),
                    ftpServerInfo.getOption().getWaiting()
            );
        }
        throw new RuntimeException("Can not load FTP configurations.");
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "OPTIONS", "DELETE", "PUT", "PATCH")
                        .allowedHeaders("X-Requested-With", "Origin", "Content-Type", "Accept", "Authorization")
                        .allowCredentials(false)
                        .maxAge(3600)
                ;
            }
        };
    }
}
