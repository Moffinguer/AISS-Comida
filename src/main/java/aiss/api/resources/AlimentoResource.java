package aiss.api.resources;

import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
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
			@QueryParam("offset") Integer offset, @QueryParam("s") String caracteres){
		
		Collection<Alimento> alimentos= this.repository.getAllAlimentos();
		if(caracteres != null) {
			getAlimentoPorCaracter(caracteres, alimentos);
		}
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
	private void getAlimentoPorCaracter(String caracteres, Collection<Alimento> alimentos)
	{
		if (caracteres.charAt(0)== 'X' ) {
			alimentos = alimentos.stream().filter(x->x.getNombre().startsWith(caracteres.substring(1, caracteres.length()))).collect(Collectors.toList());
		}
		else if (caracteres.charAt(0)== '-' ) {
			alimentos = alimentos.stream().filter(x->x.getNombre().endsWith(caracteres.substring(1, caracteres.length()))).collect(Collectors.toList());
		}
		else {
			alimentos = alimentos.stream().filter(x->x.getNombre().contains(caracteres.substring(1, caracteres.length()))).collect(Collectors.toList());
		}
		
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
	
	@GET
	@Produces("application/json")
	public Collection<Alimento> getAlimentosPorTemporada(@QueryParam("temporada") String temporada) {
		Collection<Alimento> alimentos = this.repository.getAllAlimentos();
		Collection<Alimento> res = new ArrayList<>();
		
		if(Arrays.asList(Temporada.values()).stream().map(v -> v.toString().toUpperCase()).
				anyMatch(v -> v.equals(temporada.toUpperCase()))) {
			
			res = alimentos.stream().filter(a -> a.getTemporada().toString().toUpperCase().equals(temporada.toUpperCase())).Collect(Collectors.toList());
			
		} else{
			throw new BadRequestException("Temporada no v�lida")
	    }
		
		return res;
	}
	
	@GET
	@Produces("application/json")
	public Collection<Alimento> getAlimentosPorTipo(@QueryParam("tipo") String tipo) {
		Collection<Alimento> alimentos = this.repository.getAllAlimentos();
		Collection<Alimento> res= new ArrayList<>();
		
		if(Arrays.asList(TipoAlimento.values()).stream().map(v -> v.toString().toUpperCase()).
				anyMatch(v -> v.equals(tipo.toUpperCase()))) {
			
			res = alimentos.stream().filter(a -> a.getTipo().toString().toUpperCase().equals(tipo.toUpperCase())).Collect(Collectors.toList());
			
		} else{
			throw new BadRequestException("Tipo de alimento no v�lido")
	    }
		
		return res;
	}
	
	@GET
	@Produces("application/json")
	public Collection<Alimento> getAlimentosPorCategoria(@QueryParam("categoria") String categoria) {
		Collection<Alimento> alimentos = this.repository.getAllAlimentos();
		Collection<Alimento> res= new ArrayList<>();
		
		if(Arrays.asList(Categoria.values()).stream().map(v -> v.toString().toUpperCase()).
				anyMatch(v -> v.equals(categoria.toUpperCase()))) {
			
			res = alimentos.stream().filter(a -> a.getCategoria().toString().toUpperCase().equals(categoria.toUpperCase())).Collect(Collectors.toList());
			
		} else{
			throw new BadRequestException("Categor�a no v�lida")
	    }
		
		return res;
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
	
	@DELETE
	@Path("/{id}")
	public Response removeAlimento(@PathParam("id") String id) {
		Alimento alimento=repository.getAlimento(id);
		if (alimento == null) {
			throw new NotFoundException("El alimento con ID: " + id + " no existe");
			}
		else {
			for (Plato plato : repository.getAllPlatos()) {
				if (plato.getAlimentos().stream().anyMatch(x -> x.getAlimento().equals(alimento))) {
					throw new BadRequestException("No puede eliminar el alimenta, ya que pertenece a algún plato");
				}
			}
		}
			repository.deleteAlimento(id);
		
		return Response.noContent().build();
	}
}
