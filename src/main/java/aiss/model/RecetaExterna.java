
package aiss.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "nombre",
    "tiempo",
    "precio",
    "calorias",
    "personas",
    "categoria",
    "descripcion"
})
@Generated("jsonschema2pojo")
public class RecetaExterna {

    @JsonProperty("id")
    private String id;
    @JsonProperty("nombre")
    private String nombre;
    @JsonProperty("tiempo")
    private String tiempo;
    @JsonProperty("precio")
    private String precio;
    @JsonProperty("calorias")
    private String calorias;
    @JsonProperty("personas")
    private String personas;
    @JsonProperty("categoria")
    private String categoria;
    @JsonProperty("descripcion")
    private String descripcion;
    @JsonProperty("ingredientes")
    private List<IngredienteExterno> ingredientes = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("nombre")
    public String getNombre() {
        return nombre;
    }

    @JsonProperty("nombre")
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @JsonProperty("tiempo")
    public String getTiempo() {
        return tiempo;
    }

    @JsonProperty("tiempo")
    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    @JsonProperty("precio")
    public String getPrecio() {
        return precio;
    }

    @JsonProperty("precio")
    public void setPrecio(String precio) {
        this.precio = precio;
    }

    @JsonProperty("calorias")
    public String getCalorias() {
        return calorias;
    }

    @JsonProperty("calorias")
    public void setCalorias(String calorias) {
        this.calorias = calorias;
    }

    @JsonProperty("personas")
    public String getPersonas() {
        return personas;
    }

    @JsonProperty("personas")
    public void setPersonas(String personas) {
        this.personas = personas;
    }

    @JsonProperty("categoria")
    public String getCategoria() {
        return categoria;
    }

    @JsonProperty("categoria")
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    @JsonProperty("descripcion")
    public String getDescripcion() {
        return descripcion;
    }

    @JsonProperty("descripcion")
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    @JsonProperty("ingredientes")
    public List<IngredienteExterno> getIngredientes() {
    	return this.ingredientes;
    }
    @JsonProperty("ingredientes")
    public void setIngredientes(List<IngredienteExterno> ingredientesExternos) {
    	this.ingredientes = ingredientesExternos;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}