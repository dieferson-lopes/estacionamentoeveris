package br.com.dieferson.estacionamentoeveris.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.dieferson.estacionamentoeveris.model.Cliente;
import br.com.dieferson.estacionamentoeveris.model.Ticket;
import br.com.dieferson.estacionamentoeveris.model.TipoVeiculo;
import br.com.dieferson.estacionamentoeveris.model.Veiculo;
import br.com.dieferson.estacionamentoeveris.repository.ClienteRepository;
import br.com.dieferson.estacionamentoeveris.repository.TicketRepository;
import br.com.dieferson.estacionamentoeveris.repository.VeiculoRepository;

/**
 * @author dsantolo
 */
@SpringBootTest
class TicketServiceTest {

	/**
	 * Criação de repository's tipo mock para testes
	 */
	private static TicketRepository ticketRepositoryMock = mock(TicketRepository.class);

	private static VeiculoRepository veiculoRepositoryMock = mock(VeiculoRepository.class);

	private static ClienteRepository clienteRepositoryMock = mock(ClienteRepository.class);

	private static TicketService ticketService;

	/**
	 * Executa antes de cada teste criando uma nova instância de TicketService
	 */
	@BeforeAll
	public static void setUp() {
		ticketService = new TicketService(ticketRepositoryMock, veiculoRepositoryMock, clienteRepositoryMock);
	}

	/**
	 * Testa cadastro do ticket com estacionamento ok
	 */
	@Test
	public void testaCadastraNovoTicket() throws Exception {
		Ticket ticketParaTestar = preparaTesteBasico(5, 0);
		Ticket ticketSalvo = ticketService.cadastrarNovoTicket(ticketParaTestar);

		verify(clienteRepositoryMock, times(1)).save(ticketParaTestar.getCliente());
		verify(veiculoRepositoryMock, times(1)).save(ticketParaTestar.getVeiculo());

		assertNotNull(ticketSalvo.getHoraEntrada());
		assertTrue(ticketSalvo.isAtivo());
	}

	/**
	 * Testa cadastro do ticket com estacionamento cheio
	 */
	@Test
	public void testaCadastraNovoTicket_estacionamentoCheio() throws Exception {
		Ticket ticketParaTestar = preparaTesteBasico(11, 0);
		Assertions.assertThrows(Exception.class, () -> {
			ticketService.cadastrarNovoTicket(ticketParaTestar);
		});
	}

	/**
	 * Testa cadastro do ticket com veículo já estacionado
	 */
	@Test
	public void testaCadastraNovoTicket_veiculoEstacionado() throws Exception {
		Ticket ticketParaTestar = preparaTesteBasico(5, 1);
		Assertions.assertThrows(Exception.class, () -> {
			ticketService.cadastrarNovoTicket(ticketParaTestar);
		});
	}

	/**
	 * Testa saída do veículo e o valor a ser pago
	 */
	@Test
	public void testaSaidaTicket() {
		LocalDateTime horaEntrada = LocalDateTime.now();
		Ticket ticketParaTestar = getTicket();
		ticketParaTestar.setHoraEntrada(horaEntrada);
		when(ticketRepositoryMock.findById(ticketParaTestar.getId())).thenReturn(Optional.of(ticketParaTestar));
		when(ticketRepositoryMock.save(ticketParaTestar)).thenReturn(ticketParaTestar);

		Ticket ticketSalvo = ticketService.saidaTicket(ticketParaTestar);

		assertFalse(ticketSalvo.isAtivo());
		assertNotNull(ticketSalvo.getHoraSaida());
		assertEquals(2, ticketSalvo.getValorTotal());

	}

	/**
	 * Prepara mocks básicos do cadastro de ticket return ticketParaTestar
	 */
	public Ticket preparaTesteBasico(int qtAtivos, int qtAtivosPorVeiculo) {
		Ticket ticketParaTestar = getTicket();

		when(ticketRepositoryMock.getQuantidadeTicketsAtivos()).thenReturn(qtAtivos);
		when(ticketRepositoryMock.existsByVeiculo(ticketParaTestar.getVeiculo().getPlaca()))
				.thenReturn(qtAtivosPorVeiculo);
		when(clienteRepositoryMock.getByCpf(ticketParaTestar.getCliente().getCpf()))
				.thenReturn(ticketParaTestar.getCliente());
		when(clienteRepositoryMock.save(ticketParaTestar.getCliente())).thenReturn(ticketParaTestar.getCliente());
		when(veiculoRepositoryMock.getByPlaca(ticketParaTestar.getVeiculo().getPlaca()))
				.thenReturn(ticketParaTestar.getVeiculo());
		when(veiculoRepositoryMock.save(ticketParaTestar.getVeiculo())).thenReturn(ticketParaTestar.getVeiculo());
		when(ticketRepositoryMock.save(ticketParaTestar)).thenReturn(ticketParaTestar);

		return ticketParaTestar;
	}

	/**
	 * Cria ticket fake
	 */
	public Ticket getTicket() {
		Cliente clienteParaTestar = new Cliente();
		clienteParaTestar.setCpf("98765432101");
		clienteParaTestar.setEmail("abc@abc.com");
		clienteParaTestar.setId(1L);
		clienteParaTestar.setNome("Dieff");
		clienteParaTestar.setTelefone("99887766");

		Veiculo veiculoParaTestar = new Veiculo();
		veiculoParaTestar.setCliente(clienteParaTestar);
		veiculoParaTestar.setId(1L);
		veiculoParaTestar.setPlaca("ABC0123");
		veiculoParaTestar.setVeiculo(TipoVeiculo.MOTO);

		Ticket ticketParaTestar = new Ticket();
		ticketParaTestar.setCliente(clienteParaTestar);
		ticketParaTestar.setVeiculo(veiculoParaTestar);

		return ticketParaTestar;
	}

}
