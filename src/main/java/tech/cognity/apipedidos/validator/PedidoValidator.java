package tech.cognity.apipedidos.validator;

import tech.cognity.apipedidos.entity.Pedido;
import tech.cognity.apipedidos.exception.ValidateServiceException;

public class PedidoValidator {
	public static void save(Pedido pedido) {
		if(pedido.getDetalles() == null) {
			throw new ValidateServiceException("Los detalles son requeridos");
		}
		if(pedido.getDetalles().isEmpty()) {
			throw new ValidateServiceException("Los detalles son requeridos");
		}
	}
}
