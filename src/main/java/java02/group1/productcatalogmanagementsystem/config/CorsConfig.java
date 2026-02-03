package java02.group1.productcatalogmanagementsystem.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsConfig extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        setCorsHeaders(response, request);

        // Preflight request
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void setCorsHeaders(HttpServletResponse res, HttpServletRequest req) {
        String origin = req.getHeader("Origin");

        // Không phải CORS request thì bỏ qua
        if (origin == null) return;

        // Whitelist origin
        boolean allowed =
                origin.equals("http://localhost:5173") ||
                origin.equals("http://127.0.0.1:5173") ||
                origin.endsWith(".vercel.app") ||
                origin.endsWith(".railway.app");

        // Không allow thì không set CORS header
        if (!allowed) return;

        res.setHeader("Access-Control-Allow-Origin", origin);
        res.setHeader("Access-Control-Allow-Credentials", "true");
        res.setHeader("Vary", "Origin");

        res.setHeader(
                "Access-Control-Allow-Methods",
                "GET, POST, PUT, DELETE, PATCH, OPTIONS"
        );

        // Echo lại headers mà browser yêu cầu (tránh thiếu accept, authorization,…)
        String requestHeaders = req.getHeader("Access-Control-Request-Headers");
        if (requestHeaders != null && !requestHeaders.isBlank()) {
            res.setHeader("Access-Control-Allow-Headers", requestHeaders);
        } else {
            res.setHeader(
                    "Access-Control-Allow-Headers",
                    "Content-Type, Authorization, Accept"
            );
        }

        // Cho FE đọc Authorization từ response nếu cần
        res.setHeader("Access-Control-Expose-Headers", "Authorization");

        // Cache preflight 1 ngày
        res.setHeader("Access-Control-Max-Age", "86400");
    }
}
