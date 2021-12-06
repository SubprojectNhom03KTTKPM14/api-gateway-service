package comjava.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@RefreshScope
@Component
public class AuthenticationFilter implements GatewayFilter {

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();

		List<String> tokens = request.getHeaders().getOrEmpty("Authorization");

		if (tokens == null || tokens.size() == 0)
			return this.onError(exchange, "Authorization header is invalid", HttpStatus.UNAUTHORIZED);

		String token = tokens.get(0);
		Integer userId = jwtTokenProvider.getUserIdFromJWT(token);
		jwtTokenProvider.getUserRoleFromJWT(token);
		this.populateRequestWithHeaders(exchange, userId);

		return chain.filter(exchange);
	}

	private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(httpStatus);
		return response.setComplete();
	}

	private void populateRequestWithHeaders(ServerWebExchange exchange, Integer userId) {

		exchange.getRequest().mutate().header("userId", String.valueOf(userId)).build();
	}
}