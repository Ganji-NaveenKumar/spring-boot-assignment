package com.naveen.api_gateway.filter;

import com.naveen.api_gateway.exception.MissingAuthHeaderClass;
import com.naveen.api_gateway.exception.UnAuthorizedUserClass;
import com.naveen.api_gateway.util.JwtUtil;
import jakarta.ws.rs.core.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;


@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;


    @Autowired
    private JwtUtil jwtUtil;


    public AuthenticationFilter(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new MissingAuthHeaderClass("Missing Auth Header Token");
                }
                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }
                try {
                  jwtUtil.validateToken(authHeader);
                } catch (Exception e) {
                    throw new UnAuthorizedUserClass("Unauthorized access to application");
                }
            }

            return chain.filter(exchange);
        });
    }

    public static  class Config{

    }
}
