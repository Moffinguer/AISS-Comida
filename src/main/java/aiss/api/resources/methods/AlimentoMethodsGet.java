package aiss.api.resources.methods;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.jboss.resteasy.spi.BadRequestException;

import aiss.model.Alimento;
import aiss.model.Categoria;
import aiss.model.Temporada;
import aiss.model.TipoAlimento;

public class AlimentoMethodsGet {

	public static Collection<Alimento> getAlimentoPorCaracter(String caracteres, Collection<Alimento> alimentos) {
		if (caracteres.charAt(0) == 'X') {
			alimentos = alimentos.stream()
					.filter(x -> x.getNombre().startsWith(caracteres.substring(1, caracteres.length())))
					.collect(Collectors.toList());
		} else if (caracteres.charAt(0) == '-') {
			alimentos = alimentos.stream()
					.filter(x -> x.getNombre().endsWith(caracteres.substring(1, caracteres.length())))
					.collect(Collectors.toList());
		} else {
			alimentos = alimentos.stream().filter(x -> x.getNombre().contains(caracteres)).collect(Collectors.toList());
		}

		return alimentos;

	}

	public static Collection<Alimento> getAlimentosPorTemporada(String temporada, Collection<Alimento> alimentos) {
		boolean checkTemporada = Arrays.asList(Temporada.values()).stream().map(v -> v.toString().toUpperCase())
				.anyMatch(v -> v.equals(temporada.toUpperCase()));
		if (checkTemporada) {

			alimentos = alimentos.stream()
					.filter(a -> a.getTemporada().toString().toUpperCase().equals(temporada.toUpperCase()))
					.collect(Collectors.toList());

		} else {
			throw new BadRequestException("Temporada no válida");
		}

		return alimentos;
	}

	public static Collection<Alimento> getAlimentosPorTipo(String tipo, Collection<Alimento> alimentos) {
		boolean checkTipo = Arrays.asList(TipoAlimento.values()).stream().map(v -> v.toString().toUpperCase())
				.anyMatch(v -> v.equals(tipo.toUpperCase()));
		if (checkTipo) {

			alimentos = alimentos.stream().filter(a -> a.getTipo().toString().toUpperCase().equals(tipo.toUpperCase()))
					.collect(Collectors.toList());

		} else {
			throw new BadRequestException("Tipo de alimento no válido");
		}

		return alimentos;

	}

	public static Collection<Alimento> getAlimentosPorCategoria(String categoria, Collection<Alimento> alimentos) {

		if (Arrays.asList(Categoria.values()).stream().map(v -> v.toString().toUpperCase())
				.anyMatch(v -> v.equals(categoria.toUpperCase()))) {

			alimentos = alimentos.stream()
					.filter(a -> a.getCategoria().toString().toUpperCase().equals(categoria.toUpperCase()))
					.collect(Collectors.toList());

		} else {
			throw new BadRequestException("Categoría no válida");
		}

		return alimentos;

	}

	public static Collection<Alimento> getPaginaciónAlimentos(Integer limit, Integer offset,
			Collection<Alimento> alimentos) {
		try {
			if (offset == null) {
				alimentos = alimentos.stream().collect(Collectors.toList()).subList(0, limit);
			} else if (limit == null) {
				alimentos = alimentos.stream().collect(Collectors.toList()).subList(offset, alimentos.size());
			} else {
				alimentos = alimentos.stream().collect(Collectors.toList()).subList(offset, offset + limit);
			}
		} catch (Exception e) {
			throw new BadRequestException("Error en el limite o en el offset");

		}

		return alimentos;
	}
}
