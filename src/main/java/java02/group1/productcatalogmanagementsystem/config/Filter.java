package java02.group1.productcatalogmanagementsystem.config;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java02.group1.productcatalogmanagementsystem.entity.Account;
import java02.group1.productcatalogmanagementsystem.exception.exception.AuthenticationException;
import java02.group1.productcatalogmanagementsystem.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.BeanRegistrarDslMarker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;

@Component
public class Filter extends OncePerRequestFilter {


    private final TokenService tokenService;
    private final HandlerExceptionResolver resolver;

    public Filter(
            TokenService tokenService,
            @Qualifier("handlerExceptionResolver")
            HandlerExceptionResolver resolver
    ) {
        this.tokenService = tokenService;
        this.resolver = resolver;
    }

    private static final List<String> PUBLIC_API = List.of(
            "GET:/swagger-ui/**",
            "GET:/v3/api-docs/**",
            "GET:/swagger-resources/**",

            "POST:/api/accounts/register",
            "POST:/api/accounts/login"
    );

    private boolean isPublicAPI(String uri, String method) {
        AntPathMatcher matcher = new AntPathMatcher();

        return PUBLIC_API.stream().anyMatch(pattern -> {
            String[] parts = pattern.split(":", 2);
            if (parts.length != 2) return false;

            return parts[0].equalsIgnoreCase(method)
                    && matcher.match(parts[1], uri);
        });
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String uri = request.getRequestURI();
        String method = request.getMethod();

        // 1. Public API → cho đi thẳng
        if (isPublicAPI(uri, method)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Lấy token
        String token = getToken(request);
        if (token == null) {
            resolver.resolveException(
                    request, response, null,
                    new AuthenticationException("Missing Authorization token")
            );
            return;
        }

        try {
            // 3. Verify token
            Account account = tokenService.extractToken(token);
            if (account == null) {
                throw new AuthenticationException("Account not found");
            }

            // 4. Set SecurityContext
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            account,
                            null,
                            account.getAuthorities()
                    );

            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 5. Cho request đi tiếp
            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            resolver.resolveException(
                    request, response, null,
                    new AuthenticationException("Token expired")
            );
        } catch (MalformedJwtException e) {
            resolver.resolveException(
                    request, response, null,
                    new AuthenticationException("Invalid token")
            );
        } catch (Exception e) {
            resolver.resolveException(
                    request, response, null,
                    new AuthenticationException("Authentication failed")
            );
        }
    }

    private String getToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.substring(7);
    }
}
