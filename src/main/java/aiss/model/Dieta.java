package aiss.model;

import java.util.ArrayList;
import java.util.List;

public class Dieta {
	
	//ATRIBUTOS
	private String id;
	private String nombre;
	private String descripcion; //opcional
	private List<Plato> platos;
	private TipoDieta tipo; //opcional
	
	//CONSTRUCTORES
	public Dieta () {}
	
	public Dieta (String nombre) {
		this.nombre = nombre;
	}
	
	public Dieta (String id, String nombre) {
		this.id = id;
		this.nombre = nombre;
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
		platos.add(plato);
	}
			
	public void deletePlato (String id) {
		for (int i = 0; i < this.platos.size(); i++) {
			if (this.platos.get(i).getId() == id.trim()) {
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
			if (this.platos.get(i).getId() == id.trim()) {
				plato = this.platos.get(i);
				break;
			}
		}
		return plato;
	}
}
