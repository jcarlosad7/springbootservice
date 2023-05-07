package tech.cognity.apipedidos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tech.cognity.apipedidos.converter.PedidoConverter;
import tech.cognity.apipedidos.dto.PedidoDTO;
import tech.cognity.apipedidos.entity.Pedido;
import tech.cognity.apipedidos.service.PedidoService;
import tech.cognity.apipedidos.util.WrapperResponse;

@RestController
@RequestMapping("/v1/pedidos")
public class PedidoController {
	@Autowired
	private PedidoService service;
	@Autowired
	private PedidoConverter converter;
	
	@GetMapping()
	public ResponseEntity<List<PedidoDTO>> findAll(
			@RequestParam(value = "numero", required = false, defaultValue = "") String numero,
			@RequestParam(value = "offset", required = false, defaultValue = "0") int pageNumber,
			@RequestParam(value = "limit", required = false, defaultValue = "5") int pageSize) {

		Pageable page = PageRequest.of(pageNumber, pageSize);
		List<Pedido> pedidos;
		if(numero==null) {
			pedidos = service.findAll(page);
		}else {
			pedidos=service.findByNumeroContainig(numero, page);
		}
		
		if(pedidos.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		List<PedidoDTO> pedidosDTO=converter.fromEntity(pedidos);
		return new WrapperResponse(true, "success", pedidosDTO).createResponse(HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<WrapperResponse<PedidoDTO>> findById(@PathVariable("id") Long id) {
		Pedido pedido = service.findById(id);
		PedidoDTO pedidoDTO=converter.fromEntity(pedido);
		return new WrapperResponse<PedidoDTO>(true,"success",pedidoDTO).createResponse(HttpStatus.OK);
	}
	
	@PostMapping()
	public ResponseEntity<PedidoDTO> create(@RequestBody PedidoDTO pedidoDTO) {
		Pedido registro = service.save(converter.fromDTO(pedidoDTO));
		PedidoDTO registroDTO=converter.fromEntity(registro);
		return new WrapperResponse(true, "success", registroDTO).createResponse(HttpStatus.CREATED);
	}
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<PedidoDTO> update(@PathVariable("id") int id,@RequestBody PedidoDTO pedidoDTO) {
		Pedido registro = service.update(converter.fromDTO(pedidoDTO));
		if(registro==null) {
			return ResponseEntity.notFound().build();
		}
		PedidoDTO registroDTO=converter.fromEntity(registro);
		return new WrapperResponse(true, "success", registroDTO).createResponse(HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<PedidoDTO> delete(@PathVariable("id") Long id) {
		service.anular(id);
		return new WrapperResponse(true, "success", null).createResponse(HttpStatus.OK);
	}
}
