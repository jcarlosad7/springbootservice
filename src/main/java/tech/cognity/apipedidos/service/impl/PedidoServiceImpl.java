package tech.cognity.apipedidos.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import tech.cognity.apipedidos.entity.Articulo;
import tech.cognity.apipedidos.entity.Pedido;
import tech.cognity.apipedidos.entity.PedidoDetalle;
import tech.cognity.apipedidos.exception.GeneralServiceException;
import tech.cognity.apipedidos.exception.NoDataFoundException;
import tech.cognity.apipedidos.exception.ValidateServiceException;
import tech.cognity.apipedidos.repository.ArticuloRepository;
import tech.cognity.apipedidos.repository.PedidoDetalleRepository;
import tech.cognity.apipedidos.repository.PedidoRepository;
import tech.cognity.apipedidos.service.PedidoService;
import tech.cognity.apipedidos.validator.PedidoValidator;

@Slf4j
@Service
public class PedidoServiceImpl implements PedidoService {
	@Autowired
	private PedidoRepository repository;
	
	@Autowired
	private PedidoDetalleRepository repositoryDetalle;
	
	@Autowired
	private ArticuloRepository repositoryArticulo;
	
	
	@Override
	@Transactional(readOnly=true)
	public List<Pedido> findAll(Pageable page) {
		try {
			return repository.findAll(page).toList();
		} catch (NoDataFoundException e) {
			log.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new GeneralServiceException(e.getMessage(), e);
		}
	}

	@Override
	@Transactional(readOnly=true)
	public List<Pedido> findByNumeroContainig(String numero, Pageable page) {
		try {
			return repository.findByNumeroContaining(numero,page);
		} catch (NoDataFoundException e) {
			log.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new GeneralServiceException(e.getMessage(), e);
		}
	}

	@Override
	@Transactional(readOnly=true)
	public Pedido findById(Long id) {
		try {
			Pedido registro = repository.findById(id).
					orElseThrow(() -> new NoDataFoundException("No existe el registro"));
			return registro;
		}catch(NoDataFoundException e) {
			log.info(e.getMessage(),e);
			throw e;
		}catch(Exception e) {
			log.error(e.getMessage());
			throw new GeneralServiceException(e.getMessage(),e);
		}
	}

	@Override
	@Transactional
	public Pedido update(Pedido pedido) {
		try {
			PedidoValidator.save(pedido);
			pedido.getDetalles().forEach(detalle-> detalle.setPedido(pedido));
			Pedido registro = repository.findById(pedido.getId()).
					orElseThrow(() -> new NoDataFoundException("No existe el pedido"));
			//Eliminamos los detalles
			List<PedidoDetalle> deletedDetalles=registro.getDetalles();
			deletedDetalles.removeAll(pedido.getDetalles());
			repositoryDetalle.deleteAll(deletedDetalles);
			
			registro.setNumero(pedido.getNumero());
			//Asignamos el pedido_id
			pedido.getDetalles().forEach(detalle-> detalle.setPedido(pedido));
			//Calculamos el total
			double total=0;
			for(PedidoDetalle detalle : pedido.getDetalles()) {
				Articulo articulo = repositoryArticulo.findById(detalle.getArticulo().getId())
					.orElseThrow(() -> new NoDataFoundException("No existe el artículo " + detalle.getArticulo().getId()));
				
				detalle.setPrecio(articulo.getPrecio());
				detalle.setSubtotal(articulo.getPrecio() * detalle.getCantidad());
				total += detalle.getSubtotal();
			}			
			registro.setDetalles(pedido.getDetalles());
			registro.setTotal(total);
			registro.setEstado("Modificado");			
			
			repository.save(registro);
			return registro;
		}catch(ValidateServiceException | NoDataFoundException e) {
			log.info(e.getMessage(),e);
			throw e;
		}catch(Exception e) {
			log.error(e.getMessage());
			throw new GeneralServiceException(e.getMessage(),e);
		}
	}

	@Override
	@Transactional
	public Pedido save(Pedido pedido) {
		try {
			PedidoValidator.save(pedido);
			pedido.setFecha(LocalDateTime.now());
			pedido.setEstado("Registrado");
			//Asignamos el pedido_id a cada detalle
			pedido.getDetalles().forEach(detalle-> detalle.setPedido(pedido));
			//Calculamos el total
			double total=0;
			for(PedidoDetalle detalle : pedido.getDetalles()) {
				Articulo articulo = repositoryArticulo.findById(detalle.getArticulo().getId())
					.orElseThrow(() -> new NoDataFoundException("No existe el artículo " + detalle.getArticulo().getId()));
				
				detalle.setPrecio(articulo.getPrecio());
				detalle.setSubtotal(articulo.getPrecio() * detalle.getCantidad());
				total += detalle.getSubtotal();
			}
			pedido.setTotal(total);
			Pedido nuevo = repository.save(pedido);
			return nuevo;
		}catch(ValidateServiceException | NoDataFoundException e) {
			log.info(e.getMessage(),e);
			throw e;
		}catch(Exception e) {
			log.error(e.getMessage());
			throw new GeneralServiceException(e.getMessage(),e);
		}
	}

	@Override
	@Transactional
	public void anular(Long id) {
		try {
			Pedido pedido = repository.findById(id)
					.orElseThrow(() -> new NoDataFoundException("No existe el pedido."));
			pedido.setEstado("Anulado");
			repository.save(pedido);
		} catch (NoDataFoundException e) {
			log.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new GeneralServiceException(e.getMessage(), e);
		}	
	}
}
