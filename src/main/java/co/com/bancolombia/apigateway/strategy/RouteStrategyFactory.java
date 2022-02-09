package co.com.bancolombia.apigateway.strategy;

import java.util.HashMap;
import java.util.Map;

public class RouteStrategyFactory {

    private Map<String, RouteInterface> strategies;

    public RouteStrategyFactory() {
        strategies = new HashMap<>();
        initStrategies();
    }

    private void initStrategies() {
        strategies.put("recharge", new RechargeAdapter());
        strategies.put("pay", new RechargeAdapter());

    }

    public RouteInterface getStrategy(String type){
        return strategies.get(type);
    }
}
