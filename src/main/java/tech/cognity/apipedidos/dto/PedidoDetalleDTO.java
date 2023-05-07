package tech.cognity.apipedidos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PedidoDetalleDTO {
	private Long id;
	private Double cantidad;
	private Double precio;
	private ArticuloDTO articulo;
	private Double subtotal;
}
