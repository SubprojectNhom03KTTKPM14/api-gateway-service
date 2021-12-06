package comjava.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Autowired
    private AuthenticationFilter filter;
    
    @Autowired
    private AdminAuthenticationFilter adminFilter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
        		 .route("user-service", r -> r.path("/user-service/auth/**")
                         .uri("lb://user-service/auth"))
                .route("user-service", r -> r.path("/user-service/users/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://user-service/users"))
                .route("user-service", r -> r.path("/user-service/admin/**")
                        .filters(f -> f.filter(adminFilter))
                        .uri("lb://user-service/admin"))
                
                .route("product-service", r -> r.path("/product-service/admin/**")
                        .filters(f -> f.filter(adminFilter))
                        .uri("lb://product-service/admin"))
                .route("product-service", r -> r.path("/product-service/products/**")
                        .uri("lb://product-service/products"))
                .route("product-service", r -> r.path("/product-service/categories/**")
                        .uri("lb://product-service/categories"))
              
                .route("order-service", r -> r.path("/order-service/admin/**")
                        .filters(f -> f.filter(adminFilter))
                        .uri("lb://order-service/admin"))
                .route("order-service", r -> r.path("/order-service/orders/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://order-service/orders"))
                .build();
    }

}
