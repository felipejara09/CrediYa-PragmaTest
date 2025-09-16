package co.com.bancolombia.api.security;

<<<<<<< HEAD
=======
<<<<<<< HEAD
>>>>>>> 64ae74362c4ef6e5c96ad00eb9499a158b0963b0
import co.com.bancolombia.api.error.ForbiddenException;
import co.com.bancolombia.api.error.UnauthorizedException;
import co.com.bancolombia.model.auth.gateways.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
<<<<<<< HEAD
=======
=======
import co.com.bancolombia.model.auth.gateways.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
>>>>>>> e9327b0a8449ea6154e02b3317113961689f247a
>>>>>>> 64ae74362c4ef6e5c96ad00eb9499a158b0963b0
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Set;

<<<<<<< HEAD
=======
<<<<<<< HEAD
>>>>>>> 64ae74362c4ef6e5c96ad00eb9499a158b0963b0
@Slf4j
@Component
@RequiredArgsConstructor
public class RequireAuth {
    private final TokenProvider tokenProvider;

<<<<<<< HEAD
=======
=======
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
>>>>>>> e9327b0a8449ea6154e02b3317113961689f247a
>>>>>>> 64ae74362c4ef6e5c96ad00eb9499a158b0963b0
    public HandlerFilterFunction<ServerResponse, ServerResponse> requireRoles(Long... allowed) {
        Set<Long> allowedRoles = Set.of(allowed);
        return (request, next) -> {
            String header = request.headers().firstHeader(HttpHeaders.AUTHORIZATION);
            if (header == null || !header.startsWith("Bearer ")) {
<<<<<<< HEAD
=======
<<<<<<< HEAD
>>>>>>> 64ae74362c4ef6e5c96ad00eb9499a158b0963b0
                log.debug("[auth] missing/invalid Authorization header");
                return Mono.error(new UnauthorizedException(
                        "Missing or invalid Authorization header (expected Bearer token)."));
            }

<<<<<<< HEAD
=======
=======
                return ServerResponse.status(401).build();
            }
>>>>>>> e9327b0a8449ea6154e02b3317113961689f247a
>>>>>>> 64ae74362c4ef6e5c96ad00eb9499a158b0963b0
            final Map<String, Object> claims;
            try {
                claims = tokenProvider.verify(header.substring(7));
            } catch (Exception e) {
<<<<<<< HEAD
=======
<<<<<<< HEAD
>>>>>>> 64ae74362c4ef6e5c96ad00eb9499a158b0963b0
                log.debug("[auth] invalid token: {}", e.getMessage());
                return Mono.error(new UnauthorizedException("Invalid or expired token."));
            }

            Long role = asLong(claims.get("role"));
            if (role == null || !allowedRoles.contains(role)) {
                log.debug("[auth] forbidden role={}, allowed={}", role, allowedRoles);
                return Mono.error(new ForbiddenException(
                        "You are not allowed to perform this action. Required roles: ADMIN (1) or ADVISOR (2)."
                                + (role != null ? " Your role: " + role + "." : "")));
            }

            return next.handle(request)
                    .contextWrite(ctx -> ctx.put("auth.claims", claims));
<<<<<<< HEAD
=======
=======
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
>>>>>>> e9327b0a8449ea6154e02b3317113961689f247a
>>>>>>> 64ae74362c4ef6e5c96ad00eb9499a158b0963b0
        };
    }

    private Long asLong(Object v) {
        if (v instanceof Long l) return l;
        if (v instanceof Integer i) return i.longValue();
        if (v instanceof String s) try { return Long.parseLong(s); } catch (NumberFormatException ignored) {}
        return null;
    }
<<<<<<< HEAD
}
=======
<<<<<<< HEAD
}
=======
}
>>>>>>> e9327b0a8449ea6154e02b3317113961689f247a
>>>>>>> 64ae74362c4ef6e5c96ad00eb9499a158b0963b0
