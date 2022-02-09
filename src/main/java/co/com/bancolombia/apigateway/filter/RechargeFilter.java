package co.com.bancolombia.apigateway.filter;


import co.com.bancolombia.apigateway.validar.ValidatePasswordCliente;
import org.json.JSONObject;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

public class RechargeFilter {

    ValidatePasswordCliente validatePasswordCliente  = new ValidatePasswordCliente();

    public GatewayFilter apply(Object config) {
        return ((exchange, chain) -> {
            //Que tareas hacer antes de redirigir
            ServerHttpRequest request = exchange.getRequest();
            HttpHeaders requestHeader = request.getHeaders();

            //Validate Header token
            if (!requestHeader.containsKey("token")) {
                return onError(exchange, "No Token Header", HttpStatus.BAD_REQUEST);
            }

            //Validate Header idPassword
            if (!requestHeader.containsKey("idPassword")) {
                return onError(exchange, "El idPassword Header", HttpStatus.BAD_REQUEST);
            } else {
                String idPassword = requestHeader.getFirst("idPassword");

                if (validatePasswordCliente.validatePassword(idPassword)){
                    return onError(exchange, "El cliente no tiene acceso", HttpStatus.BAD_REQUEST);
                }

            }


            //Validate Body
            String cacheBody = exchange.getAttribute("cachedRequestBodyObject");
            JSONObject jsonObject = new JSONObject(cacheBody);

            if (jsonObject.isNull("accountNumber")) {
                return onError(exchange, "No body data (AccountNumber)", HttpStatus.BAD_REQUEST);
            }

            return chain.filter(exchange.mutate().build());
        });
    }

    public Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        exchange.getResponse().setStatusCode(httpStatus);
        byte[] bytes = err.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        return exchange.getResponse().writeWith(Flux.just(buffer));
    }
}
