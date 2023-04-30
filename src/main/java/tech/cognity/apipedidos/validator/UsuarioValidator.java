package tech.cognity.apipedidos.validator;

import tech.cognity.apipedidos.entity.Usuario;
import tech.cognity.apipedidos.exception.ValidateServiceException;

public class UsuarioValidator {
	public static void save(Usuario usuario) {
		if(usuario.getEmail()==null) {
			throw new ValidateServiceException("El email es requerido");
		}
		if(usuario.getEmail().length()<=0) {
			throw new ValidateServiceException("El email es requerido");
		}
		if(usuario.getPassword()==null) {
			throw new ValidateServiceException("El password es requerido");
		}
		if(usuario.getPassword().length()<=0) {
			throw new ValidateServiceException("El password es requerido");
		}
		if(usuario.getRol()==null) {
			throw new ValidateServiceException("El rol es requerido");
		}
	}
}
