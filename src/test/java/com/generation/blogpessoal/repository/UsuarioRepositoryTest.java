package com.generation.blogpessoal.repository;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;


import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.generation.blogpessoal.model.Usuario;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioRepositoryTest {
    
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@BeforeAll
	void start(){

		usuarioRepository.deleteAll();


	usuarioRepository.save(new Usuario(0L, "João da Silva","https://i.imgur.com/FETvs2O.jpg","13465278","joao@email.com.br"));
	
	usuarioRepository.save(new Usuario(0L, "Manuela da Silva","https://i.imgur.com/NtyGneo.jpg","13465278","manuela@email.com.br"));
	
	usuarioRepository.save(new Usuario(0L, "Adriana da Silva","https://i.imgur.com/mB3VM2N.jpg","13465278","adriana@email.com.br"));

    usuarioRepository.save(new Usuario(0L, "Paulo Antunes", "https://i.imgur.com/JR7kUFU.jpg","13465278","paulo@email.com.br"));
	
		}

	@Test
	@DisplayName("Retorna 1 usuario")
	public void deveRetornarUmUsuario() {

		Optional<Usuario> usuario = usuarioRepository.findByUsuario("joao@email.com.br");

		assertTrue(usuario.get().getUsuario().equals("joao@email.com.br"));
	}

	
		
	}
	
	
