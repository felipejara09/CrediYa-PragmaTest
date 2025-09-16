package co.com.bancolombia.api.security;

import co.com.bancolombia.api.error.ForbiddenException;
import co.com.bancolombia.api.error.UnauthorizedException;
import co.com.bancolombia.model.auth.gateways.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequireAuth {
    private final TokenProvider tokenProvider;

    public HandlerFilterFunction<ServerResponse, ServerResponse> requireRoles(Long... allowed) {
        Set<Long> allowedRoles = Set.of(allowed);
        return (request, next) -> {
            String header = request.headers().firstHeader(HttpHeaders.AUTHORIZATION);
            if (header == null || !header.startsWith("Bearer ")) {
                log.debug("[auth] missing/invalid Authorization header");
                return Mono.error(new UnauthorizedException(
                        "Missing or invalid Authorization header (expected Bearer token)."));
            }

            final Map<String, Object> claims;
            try {
                claims = tokenProvider.verify(header.substring(7));
            } catch (Exception e) {
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
        };
    }

    private Long asLong(Object v) {
        if (v instanceof Long l) return l;
        if (v instanceof Integer i) return i.longValue();
        if (v instanceof String s) try {
            return Long.parseLong(s);
        } catch (NumberFormatException ignored) {
        }
        return null;
    }
}
