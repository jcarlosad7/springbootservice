package tech.cognity.apipedidos.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import tech.cognity.apipedidos.entity.Articulo;

public interface ArticuloService {
	public List<Articulo> findAll(Pageable page);
	public List<Articulo> findByNombre(String nombre, Pageable page);
	public Articulo findById(int id);
	public Articulo update(Articulo articulo);
	public Articulo save(Articulo articulo);
	public void delete(int id);
}
