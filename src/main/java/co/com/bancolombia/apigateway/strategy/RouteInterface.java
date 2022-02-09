package co.com.bancolombia.apigateway.strategy;

import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;

import javax.sql.rowset.Predicate;
import java.util.function.Function;

public interface RouteInterface {

    Function<PredicateSpec, Buildable<Route>> execute();
}
