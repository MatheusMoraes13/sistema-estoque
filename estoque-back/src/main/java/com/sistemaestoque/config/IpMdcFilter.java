package com.sistemaestoque.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class IpMdcFilter implements Filter {

    private static final String MDC_KEY = "clientIp";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            if (request instanceof HttpServletRequest) {
                HttpServletRequest req = (HttpServletRequest) request;
                // Como server.forward-headers-strategy=native está ativo, 
                // getRemoteAddr() já deve conter o IP real do cliente.
                String ip = req.getRemoteAddr();
                MDC.put(MDC_KEY, ip);
            }
            chain.doFilter(request, response);
        } finally {
            MDC.remove(MDC_KEY);
        }
    }
}