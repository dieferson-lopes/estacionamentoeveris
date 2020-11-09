package br.com.dieferson.estacionamentoeveris.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.dieferson.estacionamentoeveris.model.Veiculo;

/**
 * @author dsantolo
 */
public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {

	/**
	 * Busca veiculo pela placa, caso já exista retorna o existente e caso não
	 * exista retorna nulo
	 * 
	 * @return veiculo
	 */
	@Query("SELECT v FROM Veiculo v WHERE v.placa = ?1")
	public Veiculo getByPlaca(String placa);
}
