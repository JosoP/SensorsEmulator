package sk.uniza.fri.health;

import com.codahale.metrics.health.HealthCheck;

public class ApiHealthCheck extends HealthCheck {
    private String apiAddress;

    public ApiHealthCheck(String apiAddress) {
        this.apiAddress = apiAddress;
    }

    @Override
    protected Result check() throws Exception {
        if(!apiAddress.contains(".")){
            if(!apiAddress.equals("localhost")){
                return Result.unhealthy("Bad format of API address");
            }
        }
        return Result.healthy();
    }
}
