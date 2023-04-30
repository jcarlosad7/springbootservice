package tech.cognity.apipedidos.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import tech.cognity.apipedidos.entity.Articulo;

public interface ArticuloRepository extends JpaRepository<Articulo, Integer>{
	List<Articulo> findByNombreContaining(String nombre, Pageable page);
	Articulo findByNombre(String nombre);
}
