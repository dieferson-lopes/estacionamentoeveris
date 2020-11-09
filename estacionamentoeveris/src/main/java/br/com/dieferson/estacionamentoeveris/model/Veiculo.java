package br.com.dieferson.estacionamentoeveris.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author dsantolo
 */
@Entity
@Table(name = "TB_VEICULO", uniqueConstraints = { @UniqueConstraint(name = "unique_placa", columnNames = { "placa" }) })
public class Veiculo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "A placa é obrigatória")
	private String placa;

	@NotNull(message = "O tipo de veículo é obrigatório (MOTO/CARRO)")
	@Enumerated(EnumType.STRING)
	private TipoVeiculo veiculo;

	@ManyToOne
	@JoinColumn(name = "cliente_id")
	@NotNull(message = "O cliente é obrigatório") 
	private Cliente cliente;

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public TipoVeiculo getVeiculo() {
		return veiculo;
	}

	public void setVeiculo(TipoVeiculo veiculo) {
		this.veiculo = veiculo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
