package co.com.bancolombia.apigateway;

import co.com.bancolombia.apigateway.strategy.RouteStrategyFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SpringCloudGateway {

    @Bean
    public RouteLocator customRouterLocator(RouteLocatorBuilder builder) {

        RouteStrategyFactory routeStrategyFactory = new RouteStrategyFactory();

        // Se adiccionan todas la rutas
        return builder.routes()
                .route(routeStrategyFactory.getStrategy("recharge").execute())
                .route(routeStrategyFactory.getStrategy("pay").execute())
                .build();
    }

}
