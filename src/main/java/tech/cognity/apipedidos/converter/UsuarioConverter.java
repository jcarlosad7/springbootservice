package tech.cognity.apipedidos.converter;

import org.springframework.stereotype.Component;

import tech.cognity.apipedidos.dto.UsuarioRequestDTO;
import tech.cognity.apipedidos.dto.UsuarioResponseDTO;
import tech.cognity.apipedidos.entity.Rol;
import tech.cognity.apipedidos.entity.Usuario;

@Component
public class UsuarioConverter extends AbstractConverter<Usuario, UsuarioResponseDTO>{

	@Override
	public UsuarioResponseDTO fromEntity(Usuario entity) {
		if (entity==null)return null;
		return UsuarioResponseDTO.builder()
				.id(entity.getId())
				.email(entity.getEmail())
				.activo(entity.getActivo())
				.rol(entity.getRol().name())
				.build();
	}

	@Override
	public Usuario fromDTO(UsuarioResponseDTO dto) {
		if(dto==null)return null;
		Rol rol=Rol.valueOf(dto.getRol().toUpperCase());
		return Usuario.builder()
				.id(dto.getId())
				.email(dto.getEmail())
				.activo(dto.isActivo())
				.rol(rol)
				.build();
	}
	
	public Usuario registro(UsuarioRequestDTO dto) {
		if (dto==null) return null;
		Rol rol=Rol.valueOf(dto.getRol().toUpperCase());
		return Usuario.builder()
				.id(dto.getId())
				.email(dto.getEmail())
				.password(dto.getPassword())
				.rol(rol)
				.build();
	}
}
