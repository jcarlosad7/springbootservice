package tech.cognity.apipedidos.validator;

import tech.cognity.apipedidos.entity.Articulo;
import tech.cognity.apipedidos.exception.ValidateServiceException;

public class ArticuloValidator {
	public static void save(Articulo articulo) {
		if(articulo.getNombre()==null) {
			throw new ValidateServiceException("El nombre es requerido");
		}
		if(articulo.getNombre().length()>100) {
			throw new ValidateServiceException("El nombre no puede tener más de 100 caracteres");
		}
		if(articulo.getPrecio()==null) {
			throw new ValidateServiceException("El precio es requerido");
		}
		if(articulo.getPrecio()<=0) {
			throw new ValidateServiceException("El precio es incorrecto");
		}
	}

}
