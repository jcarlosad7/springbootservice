package tech.cognity.apipedidos.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="usuarios")
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name="email", length = 30,nullable=false)
	private String email;
	
	@Column(name="password", length = 150,nullable=false)
	private String password;
	
	@Column(name="activo",nullable=false)
	private Boolean activo;
	
	@Enumerated(EnumType.STRING)
	@Column(name="rol", length = 20,nullable=false)
	private Rol rol; 
	
}
