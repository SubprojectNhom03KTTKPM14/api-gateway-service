package comjava.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class JwtTokenProvider {

	@Value("${TOKEN_EXPIRED}")
	private long TOKEN_EXPIRED;

	@Value("${TOKEN_SECRET}")
	private String TOKEN_SECRET;

	public Integer getUserIdFromJWT(String token) {

		Claims claims = Jwts.parser().setSigningKey(TOKEN_SECRET).parseClaimsJws(token).getBody();
		return Integer.valueOf(claims.getSubject());
	}

	public String getUserRoleFromJWT(String token) {

		Claims claims = Jwts.parser().setSigningKey(TOKEN_SECRET).parseClaimsJws(token).getBody();
		return claims.get("role", String.class);
	}
}
