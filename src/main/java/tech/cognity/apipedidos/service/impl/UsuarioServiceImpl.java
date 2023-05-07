package tech.cognity.apipedidos.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.qos.logback.core.pattern.Converter;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import tech.cognity.apipedidos.converter.UsuarioConverter;
import tech.cognity.apipedidos.dto.LoginRequestDTO;
import tech.cognity.apipedidos.dto.LoginResponseDTO;
import tech.cognity.apipedidos.entity.Usuario;
import tech.cognity.apipedidos.exception.GeneralServiceException;
import tech.cognity.apipedidos.exception.NoDataFoundException;
import tech.cognity.apipedidos.exception.ValidateServiceException;
import tech.cognity.apipedidos.repository.UsuarioRepository;
import tech.cognity.apipedidos.security.JwtService;
import tech.cognity.apipedidos.service.UsuarioService;
import tech.cognity.apipedidos.validator.UsuarioValidator;

@Service
@Slf4j
public class UsuarioServiceImpl implements UsuarioService {
	@Autowired
	UsuarioRepository repository;
	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private UsuarioConverter converter;
	
	@Override
	@Transactional(readOnly = true)
	public List<Usuario> findAll(Pageable page) {
		try {
			return repository.findAll(page).toList();
		} catch(ValidateServiceException | NoDataFoundException e) {
			log.info(e.getMessage());
			throw e;
		}catch (Exception e) {
			log.error(e.getMessage());
			throw new GeneralServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<Usuario> findByEmail(String nombre, Pageable page) {
		try {
			return repository.findByEmailContaining(nombre, page);
		} catch(ValidateServiceException | NoDataFoundException e) {
			log.info(e.getMessage());
			throw e;
		}catch (Exception e) {
			log.error(e.getMessage());
			throw new GeneralServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Usuario findById(int id) {
		try {
			return repository.findById(id).orElseThrow(()->new NoDataFoundException("No existe el registro "+id));
		} catch(ValidateServiceException | NoDataFoundException e) {
			log.info(e.getMessage());
			throw e;
		}catch (Exception e) {
			log.error(e.getMessage());
			throw new GeneralServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Usuario update(Usuario usuario) {
		try {
			UsuarioValidator.save(usuario);
			Usuario registroD = repository.findByEmail(usuario.getEmail()).orElseThrow(()-> new NoDataFoundException("No existe el usuario"));
			if(registroD !=null && registroD.getId()!= usuario.getId()) {
				throw new ValidateServiceException("Ya existe un registro con el email "+usuario.getEmail());
			}
			Usuario registro=repository.findById(usuario.getId()).orElseThrow(()-> new NoDataFoundException("No existe el registro con ese Id"));
			registro.setEmail(usuario.getEmail());
			String passEncode=encoder.encode(usuario.getPassword());
			registro.setPassword(passEncode);
			registro.setRol(usuario.getRol());
			repository.save(registro);
			return registro;
		} catch(ValidateServiceException | NoDataFoundException e) {
			log.info(e.getMessage());
			throw e;
		}catch (Exception e) {
			log.error(e.getMessage());
			throw new GeneralServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Usuario save(Usuario usuario) {
		try {
			UsuarioValidator.save(usuario);
			Optional<Usuario> reg=repository.findByEmail(usuario.getEmail());
			if(reg.isPresent()) {
				throw new ValidateServiceException("Ya existe un registro con el email "+usuario.getEmail());
			}
			String passEncode=encoder.encode(usuario.getPassword());
			usuario.setPassword(passEncode);
			usuario.setActivo(true);
			Usuario registro =repository.save(usuario);
			return registro;
		} catch(ValidateServiceException | NoDataFoundException e) {
			log.info(e.getMessage());
			throw e;
		}catch (Exception e) {
			log.error(e.getMessage());
			throw new GeneralServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public void delete(int id) {
		try {
			Usuario registro=repository.findById(id).orElseThrow(()->new NoDataFoundException("No existe un registro con ese Id"));
			repository.delete(registro);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new GeneralServiceException(e.getMessage());
		}
	}
	
	@Override
	@Transactional
	public LoginResponseDTO login(LoginRequestDTO request) {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
					);
			var usuario=repository.findByEmail(request.getEmail()).orElseThrow(()-> new NoDataFoundException("No existe el usuario"));
			var jwtToken=jwtService.generateToken(usuario);
			return new LoginResponseDTO(converter.fromEntity(usuario), jwtToken);
		} catch (JwtException e) {
			log.info(e.getMessage());
			throw new ValidateServiceException(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new ValidateServiceException(e.getMessage());
		}
	}

}
