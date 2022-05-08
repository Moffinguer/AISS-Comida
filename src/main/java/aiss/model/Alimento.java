package aiss.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.jboss.resteasy.spi.BadRequestException;

@JsonSerialize(include = Inclusion.NON_NULL)
public class Alimento {
	
	//ATRIBUTOS
	private String id;
	private String nombre;
	private Double calorias;
	private Categoria categoria;
	private TipoAlimento tipo;
	private Alergeno alergeno; 
	private Temporada temporada; 
	
	//CONSTRUCTORES
	public Alimento () {}
	
	public Alimento (String nombre) {
		this.nombre = nombre;
	}
	public Alimento(String nombre, Double calorias, Categoria categoria, TipoAlimento tipo,
			Alergeno alergeno, Temporada temporada) {
		if(calorias.compareTo(0.) <= 0) throw new BadRequestException("El alimento " + nombre + "posee calorías negativas");
		this.nombre = nombre;
		this.calorias = calorias;
		this.categoria = categoria;
		this.tipo = tipo;
		this.alergeno = alergeno;
		this.temporada = temporada;
	}

	//GETTERS Y SETTERS
	public String getId() {
		return this.id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Double getCalorias() {
		return calorias;
	}

	public void setCalorias(Double calorias) {
		if(calorias.compareTo(0.) <= 0) throw new BadRequestException("El alimento " + nombre + "posee calorías negativas");
		this.calorias = calorias;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public TipoAlimento getTipo() {
		return tipo;
	}

	public void setTipo(TipoAlimento tipo) {
		this.tipo = tipo;
	}

	public Alergeno getAlergeno() {
		return alergeno;
	}

	public void setAlergeno(Alergeno alergeno) {
		this.alergeno = alergeno;
	}

	public Temporada getTemporada() {
		return temporada;
	}

	public void setTemporada(Temporada temporada) {
		this.temporada = temporada;
	}
	
}
