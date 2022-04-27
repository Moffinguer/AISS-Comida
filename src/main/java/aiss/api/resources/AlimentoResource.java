package aiss.api.resources;

import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
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

import aiss.model.Alergeno;
import aiss.model.Alimento;
import aiss.model.Categoria;
import aiss.model.Temporada;
import aiss.model.TipoAlimento;
import aiss.model.repository.DietaRepository;
import aiss.model.repository.MapDietaRepository;
import aiss.model.repository.MapPlaylistRepository;

@Path("/alimentos")
public class AlimentoResource {
	
	private static AlimentoResource _instance=null;
	DietaRepository repository;
	
	private AlimentoResource() {
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
			@QueryParam("offset") Integer offset){
		
		Collection<Alimento> alimentos= this.repository.getAllAlimentos();
		
		//Para la paginacion
		if( limit!=null || offset!=null ) {
			try {
				if(offset==null) {
					alimentos = alimentos.stream().collect(Collectors.toList())
							.subList(0, limit);
				} else if (limit==null) {
					alimentos = alimentos.stream().collect(Collectors.toList())
							.subList(offset, alimentos.size());
				} else {
					alimentos = alimentos.stream().collect(Collectors.toList())
							.subList(offset, offset+limit);
				}
			} catch (Exception e) {
				throw new BadRequestException("Error en el limite o en el offset");
				
			}
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
	
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response addAlimento(@Context UriInfo uriInfo, Alimento alimento) {
		if(alimento.getNombre() == null || alimento.getNombre().equals("")) throw new BadRequestException("El nombre del alimento no puede ser nulo");
		if(alimento.getCalorias() == null) throw new BadRequestException("Las calorías del alimento no puede ser nulo");
		if(alimento.getTipo() == null ) throw new BadRequestException("El alimento debe ser al menos de un tipo");
		if(alimento.getAlergeno() == null) throw new BadRequestException("El alimento debe tener al menos un alergeno");
		if(alimento.getCategoria() == null) throw new BadRequestException("El alimento deber pertenecer al menos a una categoría");
		Collection<TipoAlimento> tiposAlimentos=Arrays.asList(TipoAlimento.values());
		Collection<Alergeno> tiposAlergenos=Arrays.asList(Alergeno.values());
		Collection<Categoria> tiposCategorias=Arrays.asList(Categoria.values());
		Collection<Temporada> tiposTemporadas=Arrays.asList(Temporada.values());
		if(!tiposAlimentos.contains(alimento.getTipo())) {
			throw new BadRequestException("Tipo de alimento no válido");
		}
		if(!tiposAlergenos.contains(alimento.getAlergeno())) {
			throw new BadRequestException("Alergeno no válido");
		}
		if(!tiposCategorias.contains(alimento.getCategoria())) {
			throw new BadRequestException("Categoría no válida");
		}
		if(!tiposTemporadas.contains(alimento.getTemporada())) {
			throw new BadRequestException("Temporada no válida");
		}
		repository.addAlimento(alimento);
		UriBuilder ub = uriInfo.getAbsolutePathBuilder().path(this.getClass(), "get");
		URI uri = ub.build(alimento.getId());
		ResponseBuilder respb = Response.created(uri);
		respb.entity(alimento);
		return respb.build();
	}
}
