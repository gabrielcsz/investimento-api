package br.com.caixa.desafio.exceptions;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class DataSourceMapper implements ExceptionMapper<DatasourceException> {
    @Override
    public Response toResponse(DatasourceException exception) {
        return Response.status(exception.getStatus())
                .entity(new ErrorResponse(exception.getTitle(), exception.getStatus(), exception.getDetail()))
                .build();
    }
}
