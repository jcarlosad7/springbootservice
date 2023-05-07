package tech.cognity.apipedidos.dto;

import java.time.LocalDateTime;
import java.util.List;

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
public class PedidoDTO {
	private Long id;
	private LocalDateTime fecha;
	private String numero;
	private List<PedidoDetalleDTO> detalles;
	private Double total;
	private String estado;
}
