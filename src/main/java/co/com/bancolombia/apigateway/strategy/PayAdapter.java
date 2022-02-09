package co.com.bancolombia.apigateway.strategy;

import org.json.JSONObject;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.function.Function;

public class PayAdapter implements RouteInterface {
    @Override
    public Function<PredicateSpec, Buildable<Route>> execute() {
        return r -> r.path("/gateway/recharge")
                .and().readBody(String.class, s -> true)
                .filters(f -> f.filter((exchange, chain) -> {
                    //Que tareas hacer antes de redirigir
                    ServerHttpRequest request = exchange.getRequest();
                    HttpHeaders requestHeader = request.getHeaders();

                    //Validate Header
                    if (!requestHeader.containsKey("token")) {
                        return onError(exchange, "No Token Header", HttpStatus.BAD_REQUEST);
                    }

                    //Validate Body
                    String cacheBody = exchange.getAttribute("cachedRequestBodyObject");
                    JSONObject jsonObject = new JSONObject(cacheBody);

                    if (jsonObject.isNull("accountNumber")) {
                        return onError(exchange, "No body data (AccountNumber)", HttpStatus.BAD_REQUEST);
                    }

                    return chain.filter(exchange.mutate().build());
                }).rewritePath("/gateway/pay", "/api/pay"))
                .uri("http://localhost:8090");
    }

    public Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        exchange.getResponse().setStatusCode(httpStatus);
        byte[] bytes = err.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        return exchange.getResponse().writeWith(Flux.just(buffer));
    }
}
