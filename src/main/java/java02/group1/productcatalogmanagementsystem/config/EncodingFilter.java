package java02.group1.productcatalogmanagementsystem.config;


import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE) // đảm bảo chạy đầu tiên trong chuỗi filter
public class EncodingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // Thiết lập encoding UTF-8 cho toàn bộ request/response
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // Với response, đảm bảo content-type trả về cũng là UTF-8
        if (response instanceof HttpServletResponse res) {
            res.setHeader("Content-Type", "application/json; charset=UTF-8");
        }

        // Tiếp tục luồng filter
        chain.doFilter(request, response);
    }
}
