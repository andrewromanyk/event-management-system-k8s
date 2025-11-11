package ua.edu.ukma.event_management_system_gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.AfterFilterFunctions.rewriteLocationResponseHeader;
import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.preserveHostHeader;
import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;

@Configuration
public class GatewayConfig {

    @Value("${gateway.inner.ip.views}")
    private String viewsServiceUrl;

    @Bean
    public RouterFunction<ServerResponse> gatewayRoutes() {
        return route("building-service")
                .route(path("/api/building/**"), http())
                .before(uri("http://localhost:8082"))
                .before(preserveHostHeader())
                .after(rewriteLocationResponseHeader())
                .build()

                .and(route("else")
                        .route(path("/api/**"), http())
                        .before(uri("http://localhost:8081"))
                        .before(preserveHostHeader())
                        .after(rewriteLocationResponseHeader())
                        .build())

                .and(route("views")
                        .route(path("/**"), http())
                        .before(uri(viewsServiceUrl))
                        .before(preserveHostHeader())
                        .after(rewriteLocationResponseHeader())
                        .build());
    }
}
