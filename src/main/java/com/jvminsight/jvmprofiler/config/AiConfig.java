package com.jvminsight.jvmprofiler.config;

import com.zhipu.oapi.ClientV4;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @PACKAGE_NAME: com.huaweicloud.jvmprofiler.config
 * @NAME: AiConfig
 * @USER: tangxiang
 * @DATE: 2024/7/28
 * @PROJECT_NAME: HuaweiCloud-JVM-Profiler
 * @DESCRIPTION: 智能化平台，将质谱的APi注册为Bean
 **/
@Configuration
@ConfigurationProperties(prefix = "ai")
@Data
public class AiConfig {

    private String apiKey;

    @Bean
    public ClientV4 getClientV4() {
        return new ClientV4.Builder(apiKey).build();
    }
}
