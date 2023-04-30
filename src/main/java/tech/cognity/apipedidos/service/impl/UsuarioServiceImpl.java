package tech.cognity.apipedidos.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import tech.cognity.apipedidos.entity.Usuario;
import tech.cognity.apipedidos.exception.GeneralServiceException;
import tech.cognity.apipedidos.exception.NoDataFoundException;
import tech.cognity.apipedidos.exception.ValidateServiceException;
import tech.cognity.apipedidos.repository.UsuarioRepository;
import tech.cognity.apipedidos.service.UsuarioService;
import tech.cognity.apipedidos.validator.UsuarioValidator;

@Service
@Slf4j
public class UsuarioServiceImpl implements UsuarioService {
	@Autowired
	UsuarioRepository repository;
	
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
			Usuario registroD = repository.findByEmail(usuario.getEmail());
			if(registroD !=null && registroD.getId()!= usuario.getId()) {
				throw new ValidateServiceException("Ya existe un registro con el email "+usuario.getEmail());
			}
			Usuario registro=repository.findById(usuario.getId()).orElseThrow(()-> new NoDataFoundException("No existe el registro con ese Id"));
			registro.setEmail(usuario.getEmail());
			registro.setPassword(usuario.getPassword());
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
			if(repository.findByEmail(usuario.getEmail())!=null) {
				throw new ValidateServiceException("Ya existe un registro con el email "+usuario.getEmail());
			}
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
	public void delete(int id) {
		try {
			Usuario registro=repository.findById(id).orElseThrow(()->new NoDataFoundException("No existe un registro con ese Id"));
			repository.delete(registro);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new GeneralServiceException(e.getMessage());
		}
	}

}
