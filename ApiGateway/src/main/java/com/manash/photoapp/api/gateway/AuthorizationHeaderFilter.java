package com.manash.photoapp.api.gateway;

import com.netflix.discovery.converters.Auto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.util.Base64;

@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config>
{
	public AuthorizationHeaderFilter(){
		super(Config.class);
	}

	@Autowired
	Environment env;

	@Override
	public GatewayFilter apply(Config config)
	{
		return (exchange, chain) ->{
			String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

			// Check if Header is Present
			if (authHeader == null || !authHeader.startsWith("Bearer ")) {
				return onError(exchange, "No Authorization header", HttpStatus.UNAUTHORIZED);
			}

			// Extract Token and Validate
			String token = authHeader.replace("Bearer ", "");
			if (!isValidToken(token)) {
				return onError(exchange, "Invalid JWT Token", HttpStatus.UNAUTHORIZED);
			}
			exchange.getRequest().mutate().header(HttpHeaders.AUTHORIZATION, authHeader).build();
			// Continue to Next Filter
			System.out.println("Authorization header: " + authHeader);
			return chain.filter(exchange);
		};
	}

	public static class Config {
		// Configuration properties, if needed
	}
	private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus){

		ServerHttpResponse response = exchange.getResponse();

		response.setStatusCode(httpStatus);

		return response.setComplete();

	}
	private boolean isValidToken(String token) {

		String tokenSecret = env.getProperty("token.secret");
		byte[] secretKeyBytes = Base64.getEncoder().encode(tokenSecret.getBytes());
		SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyBytes);


		try {
			Claims claims = Jwts.parser()
				.setSigningKey(secretKey)
				.build().parseClaimsJws(token).getBody();
			return claims.getSubject() != null;
		} catch (Exception e) {
			return false;
		}
	}
}
