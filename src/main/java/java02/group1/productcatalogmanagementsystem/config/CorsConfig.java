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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // Set CORS headers
        setCorsHeaders(response, request);

        // Giải quyết request OPTIONS 
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void setCorsHeaders(HttpServletResponse res, HttpServletRequest req) {
        String origin = req.getHeader("Origin");

        // Cho phép localhost và Vercel frontend
        boolean allowed = origin != null && (
                origin.equals("http://localhost:5173") ||
                origin.equals("http://127.0.0.1:5173") ||
                origin.equals("https://productcatalog-pf31yzavo-minh-chaus-projects-11c47b5c.vercel.app") ||
                origin.endsWith(".vercel.app")
        );

        if (allowed) {
            res.setHeader("Access-Control-Allow-Origin", origin);
            res.setHeader("Access-Control-Allow-Credentials", "true");
        } else {
            res.setHeader("Access-Control-Allow-Origin", "null");
        }

        res.setHeader("Vary", "Origin");
        res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, PATCH, OPTIONS");

        //  Thêm toàn bộ header FE thực sự gửi (bao gồm ngrok-skip-browser-warning)
        res.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, ngrok-skip-browser-warning");

        // FE nếu cần đọc Authorization từ response thì expose:
        res.setHeader("Access-Control-Expose-Headers", "Authorization");

        // Cache preflight 1 ngày
        res.setHeader("Access-Control-Max-Age", "86400");
    }
}
