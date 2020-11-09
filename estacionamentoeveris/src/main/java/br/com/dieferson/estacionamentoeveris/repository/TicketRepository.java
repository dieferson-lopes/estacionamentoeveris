package br.com.dieferson.estacionamentoeveris.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.dieferson.estacionamentoeveris.model.Ticket;

/**
 * @author dsantolo
 */
public interface TicketRepository extends JpaRepository<Ticket, Long> {
	/**
	 * @return quantidade de tickets ativos
	 */
	@Query("SELECT count(*) FROM Ticket t WHERE t.ativo = true")
	public int getQuantidadeTicketsAtivos();
	/**
	 * @return verifica se a placa esta ativa no estacionamento, ou seja, veículo estacionado
	 */
	@Query("SELECT count(*) FROM Ticket t WHERE t.ativo = true AND t.veiculo.placa = ?1 ")
	public int existsByVeiculo(String placa);
	/**
	 * @return verifica tickets ativos
	 */
	@Query("SELECT t FROM Ticket t WHERE t.ativo = true")
	public List<Ticket> getTicketsAtivos();

}
