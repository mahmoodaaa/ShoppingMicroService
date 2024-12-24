package com.microservice.api_gateway.routes;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoutesConfig {

@Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder){
                 return  builder.routes()
                                 .route(p->p.path("/doctor/**")
                                 .uri("lb://DOCTOR-SERVICE"))
                                 .route(p->p.path("/patients/**")
                                 .uri("lb://PATIENT-SERVICE"))
                                 .route(p->p.path("/payments/**")
                                 .uri("lb://PAYMENT-SERVICE"))
                       
                                 .route(p->p.path("/api/items/**")
                                 .uri("lb://ITEM-SERVICE"))
                                 .route(p->p.path("/api/carts/**")
                                 .uri("lb://CART-SERVICE"))
                                 .build();
}
}
