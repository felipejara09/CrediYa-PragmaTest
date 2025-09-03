package co.com.bancolombia.api.security;

import co.com.bancolombia.model.auth.gateways.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class RequireAuth {

    private final TokenProvider tokenProvider;

    /** Requiere token válido. */
    public HandlerFilterFunction<ServerResponse, ServerResponse> requireAuthenticated() {
        return (request, next) -> {
            String header = request.headers().firstHeader(HttpHeaders.AUTHORIZATION);
            if (header == null || !header.startsWith("Bearer ")) {
                return ServerResponse.status(401).build();
            }
            final Map<String, Object> claims;
            try {
                claims = tokenProvider.verify(header.substring(7));
            } catch (Exception e) {
                return ServerResponse.status(401).build();
            }
            ServerRequest withClaims = ServerRequest.from(request)
                    .attribute("auth.claims", claims)
                    .build();
            return next.handle(withClaims);
        };
    }

    /** Requiere token válido y que el claim "role" esté en los permitidos. */
    public HandlerFilterFunction<ServerResponse, ServerResponse> requireRoles(Long... allowed) {
        Set<Long> allowedRoles = Set.of(allowed);
        return (request, next) -> {
            String header = request.headers().firstHeader(HttpHeaders.AUTHORIZATION);
            if (header == null || !header.startsWith("Bearer ")) {
                return ServerResponse.status(401).build();
            }
            final Map<String, Object> claims;
            try {
                claims = tokenProvider.verify(header.substring(7));
            } catch (Exception e) {
                return ServerResponse.status(401).build();
            }
            Long role = asLong(claims.get("role"));
            if (!allowedRoles.isEmpty() && (role == null || !allowedRoles.contains(role))) {
                return ServerResponse.status(403).build();
            }
            ServerRequest withClaims = ServerRequest.from(request)
                    .attribute("auth.claims", claims)
                    .build();
            return next.handle(withClaims);
        };
    }

    private Long asLong(Object v) {
        if (v instanceof Long l) return l;
        if (v instanceof Integer i) return i.longValue();
        if (v instanceof String s) try { return Long.parseLong(s); } catch (NumberFormatException ignored) {}
        return null;
    }
}