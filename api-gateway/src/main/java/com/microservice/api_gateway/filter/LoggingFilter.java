package com.microservice.api_gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.http.HttpRequest;
import java.util.logging.Logger;

@Component
public class LoggingFilter implements GlobalFilter {


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        Logger logger =  Logger.getLogger(LoggingFilter.class.getName());
        logger.info("Request coming.....");
        ServerHttpRequest request = exchange.getRequest();
        String url = request.getURI().toString();

        logger.info("URL : " + url);
        return chain.filter(exchange);
    }


    }

