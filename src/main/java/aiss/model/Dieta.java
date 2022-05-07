package aiss.model;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

@JsonSerialize(include = Inclusion.NON_NULL)
public class Dieta {
	
	//ATRIBUTOS
	private String id;
	private String nombre;
	private String descripcion; 
	private List<Plato> platos;
	private TipoDieta tipo;
	
	//CONSTRUCTORES
	public Dieta () {}
	
	public Dieta (String nombre) {
		this.nombre = nombre;
	}
	public Dieta (String nombre, String descripcion, List<Plato> platos, TipoDieta tipo) {
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.platos = platos;
		this.tipo = tipo;
	}
	public String getId() {
		return id;
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

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public List<Plato> getPlatos() {
		return platos;
	}

	public void setPlatos(List<Plato> platos) {
		this.platos = platos;
	}

	public TipoDieta getTipo() {
		return tipo;
	}

	public void setTipo(TipoDieta tipo) {
		this.tipo = tipo;
	}
	
	//OTROS METODOS
	public void addPlato (Plato plato) {
		if (this.platos == null) {
			this.platos = new ArrayList<Plato>();
		}
		if(!platos.contains(plato)) {
			platos.add(plato);
		}
	}
			
	public void deletePlato (String id) {
		for (int i = 0; i < this.platos.size(); i++) {
			if (this.platos.get(i).getId().equals(id.trim())) {
				this.platos.remove(i);
				break;
			}
		}
	}
			
	public void deletePlato (Plato plato) {
		this.platos.remove(plato);
	}
	
	public Plato getPlato(String id) {
		Plato plato = null;
		for (int i = 0; i < this.platos.size(); i++) {
			if (this.platos.get(i).getId().equals(id.trim())) {
				plato = this.platos.get(i);
				break;
			}
		}
		return plato;
	}
	
}
