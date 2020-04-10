package se.jg.mashup.integration;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.web.client.RestTemplate;

public abstract class AbstractRestClient {

    // TODO should probably find a rate limiter not in beta. We will most of the time be saved by the cache this is used as "Belt and suspenders".
    private final RateLimiter rateLimiter;
    protected final RestTemplate restTemplate;
    protected final EndpointBuilder endpointBuilder;

    protected AbstractRestClient(RestTemplate restTemplate, EndpointBuilder endpointBuilder) {
        this.restTemplate = restTemplate;
        this.rateLimiter = RateLimiter.create(1);
        this.endpointBuilder = endpointBuilder;
    }

    protected void acquireRateLimit () {
        rateLimiter.acquire(1);
    }
}
