package co.com.bancolombia.apigateway.strategy;

import co.com.bancolombia.apigateway.filter.RechargeFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import java.util.function.Function;

public class RechargeAdapter implements RouteInterface {

    RechargeFilter rechargeFilter = new RechargeFilter();

    @Override
    public Function<PredicateSpec, Buildable<Route>> execute() {
        return r -> r.path("/gateway/recharge")
                .and().readBody(String.class, s -> true)
                .filters(f -> f.filter(rechargeFilter.apply(new Object())).rewritePath("/gateway/recharge", "/api/path"))
                .uri("http://localhost:8080");
    }


}
