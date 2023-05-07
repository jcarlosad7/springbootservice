package tech.cognity.apipedidos.converter;

//import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import tech.cognity.apipedidos.dto.PedidoDTO;
import tech.cognity.apipedidos.dto.PedidoDetalleDTO;
import tech.cognity.apipedidos.entity.Pedido;
import tech.cognity.apipedidos.entity.PedidoDetalle;

@Component
public class PedidoConverter extends AbstractConverter<Pedido, PedidoDTO> {
	//private static final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss");
	private ArticuloConverter articuloConverter=new ArticuloConverter();
	
	@Override
	public PedidoDTO fromEntity(Pedido entity) {
		if (entity ==null) return null;
		List<PedidoDetalleDTO> detalles = fromIngresoDetalleEntity(entity.getDetalles());
		
		return PedidoDTO.builder()
				.id(entity.getId())
				.detalles(detalles)				
				.fecha(entity.getFecha())
				.numero(entity.getNumero())
				.total(entity.getTotal())
				.estado(entity.getEstado())
				.build();
	}

	@Override
	public Pedido fromDTO(PedidoDTO dto) {
		if (dto==null) return null;
		List<PedidoDetalle> detalles = fromPedidoDetalleDTO(dto.getDetalles());
		return Pedido.builder()
				.id(dto.getId())
				.fecha(dto.getFecha())
				.numero(dto.getNumero())
				.detalles(detalles)
				.total(dto.getTotal())
				.build();
	}
	
	private List<PedidoDetalleDTO> fromIngresoDetalleEntity(List<PedidoDetalle> detalles) {
		if(detalles ==null) return null;
		return detalles.stream().map(detalle -> {
			return PedidoDetalleDTO.builder()
					.id(detalle.getId())
					.articulo(articuloConverter.fromEntity(detalle.getArticulo()))
					.cantidad(detalle.getCantidad())
					.precio(detalle.getPrecio())
					.subtotal(detalle.getSubtotal())
					.build();
		})
		.collect(Collectors.toList());
	}
	
	private List<PedidoDetalle> fromPedidoDetalleDTO(List<PedidoDetalleDTO> detalles) {
		if(detalles ==null) return null;
		return detalles.stream().map(detalle -> {
			return PedidoDetalle.builder()
					.id(detalle.getId())
					.articulo(articuloConverter.fromDTO(detalle.getArticulo()))
					.cantidad(detalle.getCantidad())
					.precio(detalle.getPrecio())
					.subtotal(detalle.getSubtotal())
					.build();
		})
		.collect(Collectors.toList());
	}
}
