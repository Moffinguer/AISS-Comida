package aiss.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.jboss.resteasy.spi.BadRequestException;

@JsonSerialize(include = Inclusion.NON_NULL)
public class Ingrediente {
	private Alimento alimento;
	private Double cantidad;
	public Ingrediente() {}
	public Ingrediente(Alimento alimento, Double cantidad) {
		super();
		if(cantidad.compareTo(0.) <= 0) throw new BadRequestException("El alimento " + alimento.getNombre() + "posee cantidad negativa");
		this.alimento = alimento;
		this.cantidad = cantidad;
	}
	public Alimento getAlimento() {
		return alimento;
	}
	public void setAlimento(Alimento alimento) {
		this.alimento = alimento;
	}
	public Double getCantidad() {
		return cantidad;
	}
	public void setCantidad(Double cantidad) {
		if(cantidad.compareTo(0.) <= 0) throw new BadRequestException("El alimento " + alimento.getNombre() + "posee cantidad negativa");
		this.cantidad = cantidad;
	}
	
}
