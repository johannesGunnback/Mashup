package se.jg.mashup.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import static java.util.Arrays.asList;

@Configuration
public class RestConfiguration {

    @Bean
    public RestTemplate createRestTemplate(ObjectMapper objectMapper) {
        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        restTemplate.setMessageConverters(asList(createMessageConverter(objectMapper), new ResourceHttpMessageConverter()));
        restTemplate.getInterceptors().add((httpRequest, bytes, clientHttpRequestExecution) -> {
            HttpHeaders headers = httpRequest.getHeaders();
            headers.add(HttpHeaders.USER_AGENT, "Mashup/0.0.1 (johannes.gunnback@gmail.com)");
            return clientHttpRequestExecution.execute(httpRequest, bytes);
        });
        return restTemplate;
    }

    private MappingJackson2HttpMessageConverter createMessageConverter(ObjectMapper objectMapper) {
        final MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        messageConverter.setObjectMapper(objectMapper);
        return messageConverter;
    }
}
