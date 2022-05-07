package aiss.api.resources.methods;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.jboss.resteasy.spi.BadRequestException;

import aiss.model.Plato;
import aiss.model.Temporada;
import aiss.model.TipoDieta;
import aiss.model.repository.DietaRepository;

public class PlatoMethodsGet {

	public static List<Comparator> getOrderPlato(String sort) {
		List<Comparator> ordenamiento = new LinkedList<>();
		if (sort != null) {
			for (String elemento : Arrays.asList(sort.split(","))) {
				String atributo = elemento.substring(1);
				String orden = elemento.substring(0, 1);
				checkRestrictions(atributo, orden);
				takeOptions(ordenamiento, atributo, orden);
			}
		}
		return ordenamiento;
	}

	public static void checkRestrictions(String parameter, String ordering) {
		if (!parameter.equalsIgnoreCase("nombre") && !parameter.equalsIgnoreCase("calorias")) {
			throw new BadRequestException(
					"Query \'" + parameter + "\', solo admite los valores \'nombre\' o \'calorias\'");
		}
		if (!ordering.equals("X") && !ordering.equals("-")) {
			throw new BadRequestException(
					"Solo se admiten los simbolos \'X\' y \'-\' pero se ha usado \'" + ordering + "\'");
		}
	}

	public static void takeOptions(List<Comparator> options, String parameter, String ordering) {
		/*
		 * Para poder filtrarlo por cada uno, como hay 3 posibilidades (que se filtre
		 * por los 2 campos, por 1, o por ninguno)
		 */
		if (parameter.equalsIgnoreCase("nombre")) {
			if (ordering.equals("X")) {
				options.add(Comparator.comparing(Plato::getNombre).reversed());
			} else {
				options.add(Comparator.comparing(Plato::getNombre));
			}
		} else {
			if (ordering.equals("X")) {
				options.add(Comparator.comparing(Plato::getCalorias).reversed());
			} else {
				options.add(Comparator.comparing(Plato::getCalorias));
			}
		}
	}

	public static Collection<Plato> setOrder(List<Comparator> ordenamiento, Collection<Plato> platos) {
		if (!ordenamiento.isEmpty()) {
			if (ordenamiento.size() == 1) {
				platos.stream().sorted(ordenamiento.get(0));
			} else {
				platos.stream().sorted(ordenamiento.get(0).thenComparing(ordenamiento.get(1)));
			}
		}
		return platos;
	}

	public static Collection<Plato> getPlatoPorCaracter(Collection<Plato> platos, String caracteres) {
		/*
		 * Devuelve los platos que empiezan(X), terminan(-) o contienen un caracter
		 * especificado
		 */

		if (caracteres.charAt(0) == 'X') {
			platos = platos.stream().filter(x -> x.getNombre().startsWith(caracteres.substring(1, caracteres.length())))
					.collect(Collectors.toList());
		} else if (caracteres.charAt(0) == '-') {
			platos = platos.stream().filter(x -> x.getNombre().endsWith(caracteres.substring(1, caracteres.length())))
					.collect(Collectors.toList());
		} else {
			platos = platos.stream().filter(x -> x.getNombre().contains(caracteres.substring(1, caracteres.length())))
					.collect(Collectors.toList());
		}

		return platos;
	}

	public static Collection<Plato> getPlatosPorTemporada(String temporada, Collection<Plato> platos) {
		boolean checkTemporadaValida = Arrays.asList(Temporada.values()).stream().map(v -> v.toString().toUpperCase())
				.anyMatch(v -> v.equals(temporada.toUpperCase()));
		if (checkTemporadaValida) {
			platos = platos.stream()
					.filter(p -> p.getTemporada().toString().toUpperCase().equals(temporada.toUpperCase()))
					.collect(Collectors.toList());

		} else {
			throw new BadRequestException("Temporada no válida");
		}

		return platos;
	}

	public static Collection<Plato> getPlatosPorCA(String ca, Collection<Plato> platos) {

		platos = platos.stream().filter(p -> p.getCAOrigen().toUpperCase().equals(ca.toUpperCase()))
				.collect(Collectors.toList());

		return platos;

	}

	public static Collection<Plato> getPlatosPorTipoDieta(String tipoDieta, Collection<Plato> platos,
			DietaRepository repository) {
		boolean checkDietaValida = Arrays.asList(TipoDieta.values()).stream().map(v -> v.toString().toUpperCase())
				.anyMatch(v -> v.equals(tipoDieta.toUpperCase()));
		if (checkDietaValida) {

			platos = repository.getAllDietas().stream()
					.filter(d -> d.getTipo().toString().toUpperCase().equals(tipoDieta.toUpperCase()))
					.flatMap(d -> d.getPlatos().stream()).collect(Collectors.toSet());

		} else {
			throw new BadRequestException("Tipo de dieta no válida");
		}

		return platos;
	}
}
