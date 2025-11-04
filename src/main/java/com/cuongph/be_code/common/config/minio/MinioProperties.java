package com.cuongph.be_code.common.config.minio;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {
    public String baseUrl;
    public String accessKey;
    public String secretKey;
    public String bucket;
}
