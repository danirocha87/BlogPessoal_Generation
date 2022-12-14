package com.generation.blogpessoal.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.blogpessoal.model.Tema;
import com.generation.blogpessoal.repository.TemaRepository;

@RestController 
//O RestController permite lidar com todas as APIs REST, como solicitações GET , POST , Delete, PUT . 

@RequestMapping("/temas") 
// é o endpoint

@CrossOrigin(origins = "*", allowedHeaders = "*")
//Informar a um navegador que um aplicativo Web seja executado em uma domínio

public class TemaController {
	
	@Autowired
	private TemaRepository temaRepository;
	
	@GetMapping
	public ResponseEntity<List<Tema>> getAll(){
		return ResponseEntity.ok(temaRepository.findAll());
	}
	
	//Procurando pelo id 
	@GetMapping("/{id}")
	public ResponseEntity<Tema> getById(@PathVariable long id)
	//Esta anotação insere o valor enviado no endereço do endpoint, na Variável de Caminho
	{
		return temaRepository.findById(id).map(resposta -> ResponseEntity.ok(resposta)) // retorna o HTTP Status OK🡪200
				// Se o objeto da classe tema for encontrado o metodo map vai retornar o metodo findById(id)
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build()); // retorna o HTTP Status NOT FOUND 🡪 404
		//O método build() constrói a Resposta com o HTTP Status retornado
		
	}
	//Procurar pelo nome da descrição
	@GetMapping("descricao/{descricao}")
	public ResponseEntity<List<Tema>> getbyTitle(@PathVariable String descricao){
		return ResponseEntity.ok(temaRepository
				.findAllByDescricaoContainingIgnoreCase(descricao));
	}
	
	//Inserir
	@PostMapping
	public ResponseEntity<Tema> post(@Valid @RequestBody Tema tema){
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(temaRepository.save(tema));
	}
	
	//Alterar
	@PutMapping
	public ResponseEntity<Tema> put(@Valid @RequestBody Tema tema){
		return temaRepository.findById(tema.getId())
				.map(resposta -> ResponseEntity.status(HttpStatus.OK)
						.body(temaRepository.save(tema)))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	
	
	//Deletar
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}") // Deletar pelo id
	public void delete(@PathVariable Long id) { 
		Optional<Tema> tema = temaRepository.findById(id);
		
		if(tema.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		
		temaRepository.deleteById(id);
	}
	
	
	
}