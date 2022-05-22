package aiss.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import aiss.api.resources.IngredienteResource;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"id",
"nombre",
"calorias",
"precio"
})
@Generated("jsonschema2pojo")
public class IngredienteExterno {

@JsonProperty("id")
private String id;
@JsonProperty("nombre")
private String nombre;
@JsonProperty("calorias")
private Double calorias100gr;
@JsonProperty("precio")
private Double precio;
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

@JsonProperty("calorias")
public Double getCalorias100gr() {
return calorias100gr;
}

@JsonProperty("calorias")
public void setCalorias100gr(Double calorias100gr) {
this.calorias100gr = calorias100gr;
}
@JsonProperty("precio")
public Double getPrecio() {
return precio;
}

@JsonProperty("precio")
public void setPrecio(Double precio) {
this.precio = precio;
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
