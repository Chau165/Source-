package java02.group1.productcatalogmanagementsystem.config;

import com.cloudinary.Cloudinary;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Value("${cloudinary.cloud-name:${CLOUDINARY_CLOUD_NAME}}")
    private String cloudName;

    @Value("${cloudinary.api-key:${CLOUDINARY_API_KEY}}")
    private String apiKey;

    @Value("${cloudinary.api-secret:${CLOUDINARY_API_SECRET}}")
    private String apiSecret;

    @PostConstruct
    public void checkConfig() {
        System.out.println("Cloudinary cloud name = " + cloudName);
    }


    @Bean
    public Cloudinary cloudinary() {

        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);

        return new Cloudinary(config);
    }
}

