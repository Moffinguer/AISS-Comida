package aiss.api.resources.methods;

import java.net.URI;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.ResponseBuilder;

import aiss.model.Alimento;
import aiss.model.Dieta;
import aiss.model.Plato;
import aiss.model.repository.DietaRepository;

public class ResponsePost {

	public static <T> Response getResponseAlimento(UriInfo uriInfo, Alimento alimento, String method,
			DietaRepository repository, T clase) {
		UriBuilder ub = uriInfo.getAbsolutePathBuilder().path(clase.getClass(), method);
		URI uri = ub.build(alimento.getId());
		ResponseBuilder respb = Response.created(uri);
		respb.entity(alimento);
		return respb.build();
	}
	
	public static <T> Response getResponsePlato(UriInfo uriInfo, Plato plato, String method,
			DietaRepository repository, T clase) {
		UriBuilder ub = uriInfo.getAbsolutePathBuilder().path(clase.getClass(), method);
		URI uri = ub.build(plato.getId());
		ResponseBuilder respb = Response.created(uri);
		respb.entity(plato);
		return respb.build();
	}
	
	public static <T> Response getResponseDieta(UriInfo uriInfo, Dieta dieta, String method,
			DietaRepository repository, T clase) {
		UriBuilder ub = uriInfo.getAbsolutePathBuilder().path(clase.getClass(), method);
		URI uri = ub.build(dieta.getId());
		ResponseBuilder respb = Response.created(uri);
		respb.entity(dieta);
		return respb.build();
	}
}
