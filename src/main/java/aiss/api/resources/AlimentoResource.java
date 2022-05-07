package aiss.api.resources;

import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.jboss.resteasy.spi.BadRequestException;
import org.jboss.resteasy.spi.NotFoundException;

import aiss.api.resources.methods.AlimentoMethodsDelete;
import aiss.api.resources.methods.AlimentoMethodsGet;
import aiss.api.resources.methods.AlimentoMethodsPost;
import aiss.api.resources.methods.ResponsePost;
import aiss.model.Alergeno;
import aiss.model.Alimento;
import aiss.model.Categoria;
import aiss.model.Plato;
import aiss.model.Temporada;
import aiss.model.TipoAlimento;
import aiss.model.repository.DietaRepository;
import aiss.model.repository.MapDietaRepository;

@Path("/alimentos")
public class AlimentoResource {
	
	private static AlimentoResource _instance=null;
	DietaRepository repository;
	
	public AlimentoResource() {
		repository=MapDietaRepository.getInstance();

	}
	public static AlimentoResource getInstance()
	{
		if(_instance==null)
				_instance=new AlimentoResource();
		return _instance;
	}
	
	@GET
	@Produces("application/json")
	public Collection<Alimento> getAll(@QueryParam("limit") Integer limit,
			@QueryParam("offset") Integer offset,
			@QueryParam("s") String caracteres, 
			@QueryParam("temporada") String temporada,
			@QueryParam("tipo") String tipo,
			@QueryParam("categoria") String categoria){
		
		Collection<Alimento> alimentos= this.repository.getAllAlimentos();
		
		if( limit!=null || offset!=null ) {
			alimentos = AlimentoMethodsGet.getPaginaciónAlimentos(limit, offset, alimentos);
		}
		if(caracteres != null) {
			alimentos= AlimentoMethodsGet.getAlimentoPorCaracter(caracteres, alimentos);
		}
		if(temporada != null) {
			alimentos=AlimentoMethodsGet.getAlimentosPorTemporada(temporada, alimentos);
		}
		if(tipo != null) {
			alimentos=AlimentoMethodsGet.getAlimentosPorTipo(tipo, alimentos);
		}
		if(categoria != null) {
			alimentos=AlimentoMethodsGet.getAlimentosPorCategoria(categoria, alimentos);
		}
		
		return alimentos;
	}
	
	@GET
	@Path("/{id}")
	@Produces("application/json")
	public Alimento getAlimento(@PathParam("id") String alimentoId)
	{
		Alimento alimento = repository.getAlimento(alimentoId);
		if(alimento == null) throw new NotFoundException("el alimento con ID: " + alimentoId + " no existe");
		return alimento;
	}

	@GET
	@Path("/tipos")
	@Produces("application/json")
	public Collection<TipoAlimento> getTipoAlimento(){
		return Arrays.asList(TipoAlimento.values());
	}
	
	@GET
	@Path("/alergenos")
	@Produces("application/json")
	public Collection<Alergeno> getListOfAler(){
		return Arrays.asList(Alergeno.values());
	}
	
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response addAlimento(@Context UriInfo uriInfo, Alimento alimento) {
		
		AlimentoMethodsPost.checkAlimentoConsumed(alimento);
		repository.addAlimento(alimento);
		
		return ResponsePost.getResponseAlimento(uriInfo, alimento, "getAlimento", repository, this);
	}
	
	@DELETE
	@Path("/{id}")
	public Response removeAlimento(@PathParam("id") String id) {
		Alimento alimento=repository.getAlimento(id);
		if (alimento == null) {
			throw new NotFoundException("El alimento con ID: " + id + " no existe");
		}
		
		AlimentoMethodsDelete.checkAlimentoNoPerteneceAPlato(alimento, repository);
		
		repository.deleteAlimento(id);
		
		return Response.noContent().build();
	}
}
