package com.dou.adm.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Tu.Tran on 2018.09.21.
 */
@WebFilter(urlPatterns = "/api/*")
public class JwtAuthFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthFilter.class);

    public static final String JWT_USER = "jwtUser";

    private JwtProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            if(tokenProvider == null) {
                ServletContext servletContext = request.getServletContext();
                WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
                tokenProvider = webApplicationContext.getBean(JwtProvider.class);
            }

            String CORSAllowHeaders = request.getHeader("Access-Control-Request-Headers");
            if (CORSAllowHeaders == null) {
                String jwt = request.getHeader("Authorization");

                if (tokenProvider.validateToken(jwt)) {
                    JwtUser jwtUser = tokenProvider.getJwtUser(jwt);
                    request.setAttribute(JWT_USER, jwtUser);
                } else {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "UNAUTHORIZED");
                    return;
                }
            }
        } catch (Exception ex) {
            LOGGER.error("Could not set user authentication in security context", ex);
            response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, "NOT_ACCEPTABLE");
            return;
        }
        filterChain.doFilter(request, response);
    }
}
