package tech.cognity.apipedidos.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import tech.cognity.apipedidos.entity.Pedido;

public interface PedidoService {
	public List<Pedido> findAll(Pageable page);
	public List<Pedido> findByNumeroContainig(String numero,Pageable page);
	public Pedido findById(Long id);
	public Pedido update(Pedido pedido);
	public Pedido save(Pedido pedido);
	public void anular(Long id);
}
