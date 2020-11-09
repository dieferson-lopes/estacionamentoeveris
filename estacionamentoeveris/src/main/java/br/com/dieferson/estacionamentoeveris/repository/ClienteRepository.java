package br.com.dieferson.estacionamentoeveris.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.dieferson.estacionamentoeveris.model.Cliente;
import br.com.dieferson.estacionamentoeveris.model.Veiculo;

/**
 * @author dsantolo
 */
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

	/**
	 * Busca cliente pelo cpf, caso já exista retorna o existente e caso não exista
	 * retorna nulo
	 * 
	 * @return cliente
	 */
	@Query("SELECT c FROM Cliente c WHERE c.cpf = ?1")
	public Cliente getByCpf(String cpf);

}
