package ua.edu.ukma.event_management_system_gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.function.Function;

import static org.springframework.cloud.gateway.server.mvc.filter.AfterFilterFunctions.rewriteLocationResponseHeader;
import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.*;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;

@Configuration
public class GatewayConfig {

    @Value("${gateway.inner.ip.views}")
    private String viewsServiceUrl;

    @Value("${gateway.inner.ip.main}")
    private String mainServiceUrl;

    @Value("${gateway.inner.ip.building}")
    private String buildingServiceUrl;

    @Value("${gateway.inner.ip.admin}")
    private String adminPanelUrl;

    @Bean
    public RouterFunction<ServerResponse> gatewayRoutes() {
        return route("building-service")
                .route(path("/api/building/**"), http())
                .before(uri(buildingServiceUrl))
                .before(preserveHostHeader())
                .after(rewriteLocationResponseHeader())
                .build()

                .and(route("main-service")
                        .route(path("/api/**"), http())
                        .before(uri(mainServiceUrl))
                        .before(preserveHostHeader())
                        .after(rewriteLocationResponseHeader())
                        .build())

                .and(route("admin-panel")
                        .route(path("/admin/**"), http())
                        .before(uri(adminPanelUrl))
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
