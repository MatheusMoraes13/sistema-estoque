package com.moraes_dev.sistema_estoque.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class IpMdcFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            // Pega o IP real (já considerando o forward-headers configurado no application.properties)
            String userIp = httpRequest.getRemoteAddr();
            // Coloca o IP no contexto do Logback com a chave "clientIp"
            MDC.put("clientIp", userIp);
        }

        try {
            chain.doFilter(request, response);
        } finally {
            MDC.remove("clientIp"); // Limpa após a requisição para evitar vazamento de contexto em threads reutilizadas
        }
    }
}