package tech.cognity.apipedidos.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="pedido_detalles")
public class PedidoDetalle {
	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JoinColumn(name="pedido_id",nullable=false)
	private Pedido pedido;
	
	@ManyToOne
	@JoinColumn(name="articulo_id",nullable=false)
	private Articulo articulo;
	@Column(name="cantidad",nullable=false)
	private Double cantidad;
	@Column(name="precio",nullable=false)
	private Double precio;
	@Column(name="subtotal",nullable=false)
	private Double subtotal;
}
