package br.com.dieferson.estacionamentoeveris.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.dieferson.estacionamentoeveris.execption.ValidacaoException;
import br.com.dieferson.estacionamentoeveris.model.Cliente;
import br.com.dieferson.estacionamentoeveris.model.Ticket;
import br.com.dieferson.estacionamentoeveris.model.Veiculo;
import br.com.dieferson.estacionamentoeveris.repository.ClienteRepository;
import br.com.dieferson.estacionamentoeveris.repository.TicketRepository;
import br.com.dieferson.estacionamentoeveris.repository.VeiculoRepository;

/**
 * @author dsantolo
 */
@Service
public class TicketService {

	@Autowired
	private TicketRepository ticketRepository;

	@Autowired
	private VeiculoRepository veiculoRepository;

	@Autowired
	private ClienteRepository clienteRepository;

	/**
	 * Construtor usado nos testes 
	 * Recebe as instancias das classes dependentes como
	 * Mock
	 */
	public TicketService(TicketRepository ticketRepository, VeiculoRepository veiculoRepository,
			ClienteRepository clienteRepository) {
		super();
		this.ticketRepository = ticketRepository;
		this.veiculoRepository = veiculoRepository;
		this.clienteRepository = clienteRepository;
	}

	/**
	 * Recebe novo ticket para cadastro Valida ticket, se ok cadastra Cadastra
	 * cliente, veículo e ticket
	 * 
	 * @return ticket cadastrado
	 */
	public Ticket cadastrarNovoTicket(Ticket ticketParaCadastrar) throws ValidacaoException {
		validarTicket(ticketParaCadastrar);

		Cliente clienteParaSalvar = ticketParaCadastrar.getCliente();
		Cliente clienteExistente = clienteRepository.getByCpf(clienteParaSalvar.getCpf());
		if (clienteExistente != null) {
			clienteParaSalvar = clienteExistente;
		}
		Cliente clienteSalvo = clienteRepository.save(clienteParaSalvar);
		ticketParaCadastrar.setCliente(clienteSalvo);

		Veiculo veiculoParaSalvar = ticketParaCadastrar.getVeiculo();
		Veiculo veiculoExistente = veiculoRepository.getByPlaca(veiculoParaSalvar.getPlaca());
		if (veiculoExistente != null) {
			veiculoParaSalvar = veiculoExistente;
		}
		veiculoParaSalvar.setCliente(clienteSalvo);
		Veiculo veiculo = veiculoRepository.save(veiculoParaSalvar);
		ticketParaCadastrar.setVeiculo(veiculo);

		ticketParaCadastrar.setHoraEntrada(LocalDateTime.now());
		ticketParaCadastrar.setAtivo(true);
		return ticketRepository.save(ticketParaCadastrar);
	}

	/**
	 * Verificar quantidade de vagas abertas Verifica se o veículo já está
	 * estacionado
	 */
	public void validarTicket(Ticket validar) throws ValidacaoException {
		int qtTicketsAbertos = ticketRepository.getQuantidadeTicketsAtivos();
		if (qtTicketsAbertos >= 10) {
			throw new ValidacaoException("O estacionamento está cheio!");
		}
		int qtTicketAtivo = ticketRepository.existsByVeiculo(validar.getVeiculo().getPlaca());
		if (qtTicketAtivo > 0) {
			throw new ValidacaoException("Veículo já está estacionado!");
		}
	}

	/**
	 * Recebe ticket existente para encerramento Inativa o ticket Preenche horário
	 * de saída Preenche valor a pagar
	 * 
	 * @return ticket encerrado com valor a pagar
	 */
	public Ticket saidaTicket(Ticket saida) {
		// proteção para evitar valores nulos
		Optional<Ticket> ticketExistente = ticketRepository.findById(saida.getId());
		saida = ticketExistente.get();

		saida.setAtivo(false);
		saida.setHoraSaida(LocalDateTime.now());

		double valor = calcularTotal(saida.getHoraSaida(), saida.getHoraEntrada());
		saida.setValorTotal(valor);
		return ticketRepository.save(saida);

	}

	/**
	 * Recebe data/hora de entrada e saída Calcula diferença em horas entre entrada
	 * e saída
	 * 
	 * @return valor a pagar
	 */
	public double calcularTotal(LocalDateTime saida, LocalDateTime entrada) {
		long totalHoras = Duration.between(entrada, saida).toHours() + 1;
		return totalHoras * 2;
	}

}
