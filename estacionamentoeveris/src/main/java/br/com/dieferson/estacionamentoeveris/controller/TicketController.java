package br.com.dieferson.estacionamentoeveris.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.dieferson.estacionamentoeveris.execption.Mensagem;
import br.com.dieferson.estacionamentoeveris.execption.ValidacaoException;
import br.com.dieferson.estacionamentoeveris.model.Ticket;
import br.com.dieferson.estacionamentoeveris.repository.TicketRepository;
import br.com.dieferson.estacionamentoeveris.service.TicketService;

/**
 * @author dsantolo
 */
@ControllerAdvice
@RestController
@RequestMapping("/ticket")
public class TicketController {

	@Autowired
	private TicketService ticketService;
	@Autowired
	private TicketRepository ticketRepository;

	/**
	 * @return todos os tickets do banco de dados
	 */
	@GetMapping
	public List<Ticket> listaTicket() {
		List<Ticket> tickets = ticketRepository.findAll();
		return tickets;
	}

	/**
	 * @return todos os tickets ativos do banco de dados
	 */
	@GetMapping(value = "/ativos")
	public List<Ticket> listaTicketAtivos() {
		List<Ticket> tickets = ticketRepository.getTicketsAtivos();
		return tickets;
	}

	/**
	 * cadastra novo ticket
	 * @return ticket cadastrado
	 */
	@PostMapping(value = "/entrada")
	public ResponseEntity<Ticket> criarNovoTicket(@RequestBody Ticket ticket) throws ValidacaoException {
		Ticket ticketSalvo = ticketService.cadastrarNovoTicket(ticket);

		return ResponseEntity.ok(ticketSalvo);
	}

	/**
	 * calcula e encerra ticket ativo
	 * @return ticket encerrado com valor calculado
	 */
	@PutMapping(value = "/saida")
	public ResponseEntity<Ticket> saidaTicket(@RequestBody Ticket ticket) {
		Ticket ticketAlterado = ticketService.saidaTicket(ticket);

		return ResponseEntity.ok(ticketAlterado);
	}
	/**
	 * Trata mensagem de erro
	 * @return mensagem de erro gerada durante a validação
	 */
	@ExceptionHandler(value = ValidacaoException.class)
	public ResponseEntity<Object> exception(ValidacaoException exception) {
		Mensagem msg = new Mensagem(null, exception.getMessage());
		return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Trata mensagem de erro
	 * @return mensagem de erro gerada quando acontece uma ConstraintViolationException
	 */
	@ExceptionHandler(value = ConstraintViolationException.class)
	public ResponseEntity<Object> exception(ConstraintViolationException exception) {
		String mensagem = "";
		String campoIncorreto = "";
		if(exception.getConstraintViolations() != null) {
			campoIncorreto = (String) exception.getConstraintViolations().stream().collect(Collectors.toList()).get(0).getPropertyPath().toString();
			mensagem = (String) exception.getConstraintViolations().stream().collect(Collectors.toList()).get(0).getMessageTemplate();
		}	
		Mensagem msg = new Mensagem(campoIncorreto, mensagem);
		return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Trata mensagem de erro
	 * @return mensagem de erro gerada quando acontece uma HttpMessageNotReadableException
	 */
	@ExceptionHandler(value = HttpMessageNotReadableException.class)
	public ResponseEntity<Object> exception(HttpMessageNotReadableException exception) {
		String mensagem = "Parâmetro inválido";
		Mensagem msg = new Mensagem(null, mensagem);
		return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
}
}
