package tech.cognity.apipedidos.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import tech.cognity.apipedidos.entity.Articulo;
import tech.cognity.apipedidos.exception.GeneralServiceException;
import tech.cognity.apipedidos.exception.NoDataFoundException;
import tech.cognity.apipedidos.exception.ValidateServiceException;
import tech.cognity.apipedidos.repository.ArticuloRepository;
import tech.cognity.apipedidos.service.ArticuloService;
import tech.cognity.apipedidos.validator.ArticuloValidator;

@Service
@Slf4j
public class ArticuloServiceImpl implements ArticuloService{
	@Autowired
	private ArticuloRepository repository;
	
	@Override
	@Transactional(readOnly = true)
	public List<Articulo> findAll(Pageable page) {
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
	public List<Articulo> findByNombre(String nombre, Pageable page) {
		try {
			return repository.findByNombreContaining(nombre, page);
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
	public Articulo findById(int id) {
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
	public Articulo update(Articulo articulo) {
		try {
			ArticuloValidator.save(articulo);
			Articulo registroD = repository.findByNombre(articulo.getNombre());
			if(registroD !=null && registroD.getId()!= articulo.getId()) {
				throw new ValidateServiceException("Ya existe un registro con el nombre "+articulo.getNombre());
			}
			Articulo registro=repository.findById(articulo.getId()).orElseThrow(()-> new NoDataFoundException("No existe el registro con ese Id"));
			registro.setNombre(articulo.getNombre());
			registro.setPrecio(articulo.getPrecio());
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
	public Articulo save(Articulo articulo) {
		try {
			ArticuloValidator.save(articulo);
			if(repository.findByNombre(articulo.getNombre())!=null) {
				throw new ValidateServiceException("Ya existe un registro con el nombre "+articulo.getNombre());
			}
			articulo.setActivo(true);
			Articulo registro =repository.save(articulo);
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
			Articulo registro=repository.findById(id).orElseThrow(()->new NoDataFoundException("No existe un registro con ese Id"));
			repository.delete(registro);
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new GeneralServiceException(e.getMessage());
		}		
	}

}
