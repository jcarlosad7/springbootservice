package tech.cognity.apipedidos.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import tech.cognity.apipedidos.dto.LoginRequestDTO;
import tech.cognity.apipedidos.dto.LoginResponseDTO;
import tech.cognity.apipedidos.entity.Usuario;

public interface UsuarioService {
	public List<Usuario> findAll(Pageable page);
	public List<Usuario> findByEmail(String email, Pageable page);
	public Usuario findById(int id);
	public Usuario update(Usuario usuario);
	public Usuario save(Usuario usuario);
	public void delete(int id);
	public LoginResponseDTO login(LoginRequestDTO request);
}
