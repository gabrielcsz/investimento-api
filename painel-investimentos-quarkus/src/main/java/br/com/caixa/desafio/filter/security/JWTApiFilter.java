package br.com.caixa.desafio.filter.security;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;

import java.io.IOException;

public class JWTApiFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        if(requestContext.getUriInfo() != null &&(requestContext.getUriInfo().getPath().equals("/swagger.json") ||
            requestContext.getUriInfo().getPath().equals("/admin") ||
            requestContext.getUriInfo().getPath().equals("/q/dev-ui") ||
            requestContext.getUriInfo().getPath().equals("/swagger-ui") ||
            requestContext.getUriInfo().getPath().equals("/health")||
            requestContext.getUriInfo().getPath().equals("/metrics")
        )){return;}
    }
}
