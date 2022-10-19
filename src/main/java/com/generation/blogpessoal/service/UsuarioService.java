package com.generation.blogpessoal.service;

import java.nio.charset.Charset;
import java.util.Optional;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.model.UsuarioLogin;
import com.generation.blogpessoal.repository.UsuarioRepository;
	
	@Service
	public class UsuarioService {
	
	@Autowired //uso para estanciar 
	private UsuarioRepository usuarioRepository;
	
	//uso o optional porque ele pode ter valor ou não 
	public Optional<Usuario> cadastrarUsuario(Usuario usuario) {   //esse isPresent vai validar como verdadeiro ou falso
		//este metodo verifica se ja existe esse usuario
		if (usuarioRepository.findByUsuario(usuario.getUsuario()).isPresent())
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário já existe!", null);
			usuario.setSenha(criptografarSenha(usuario.getSenha()));
			return Optional.of(usuarioRepository.save(usuario));
	}
	
	public Optional<Usuario> atualizarUsuario(Usuario usuario) {
			//este metodo verifica se o ID do usuario existe
		if (usuarioRepository.findById(usuario.getId()).isPresent()) {
			Optional<Usuario> buscaUsuario = usuarioRepository.
			findByUsuario(usuario.getUsuario());//verifica o usuario
				if (buscaUsuario.isPresent()) {//se o usuario estiver presente precisa fazer uma segunda validação
					if (buscaUsuario.get().getId() != usuario.getId())//compara se é igual o ID
						throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Usuário já existe!", null);
			}
			usuario.setSenha(criptografarSenha(usuario.getSenha()));
				return Optional.of(usuarioRepository.save(usuario));
			}
		
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Usuário não encontrado!", null);
			}
	
	public Optional<UsuarioLogin> logarUsuario(
			Optional<UsuarioLogin> usuarioLogin) {
			Optional<Usuario> usuario = usuarioRepository.findByUsuario(usuarioLogin.get().getUsuario());
			
			if (usuario.isPresent()) {
				if (compararSenhas(usuarioLogin.get().getSenha(),
					usuario.get().getSenha())) {
					usuarioLogin.get().setId(usuario.get().getId());
					usuarioLogin.get().setNome(usuario.get().getNome());
					usuarioLogin.get().setFoto(usuario.get().getFoto());
					usuarioLogin.get().setToken(
					gerarBasicToken(usuarioLogin.get().getUsuario(),
					usuarioLogin.get().getSenha()));
					usuarioLogin.get().setSenha(usuario.get().getSenha());
			return usuarioLogin;
				}
			}
			throw new ResponseStatusException(
			HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos!", null);
			}
			
			//este metodo vai criar um objeto que recebera uma nova senha criptografada
			//senha encoder recebera a senha criptografada(a criptografia ocorre no enconder)
			//e retornara a senha criptografada
			private String criptografarSenha(String senha) {
				BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
				String senhaEncoder = encoder.encode(senha);
				return senhaEncoder;
			}
			
			//Compara a senha digitada com a senha criptografada armazenada no banco de dados.
			private boolean compararSenhas(String senhaDigitada,String senhaBanco){
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			return encoder.matches(senhaDigitada, senhaBanco);
			}
			//este gera o token para o usuario, ele pega e-mail,senha,mas algumas coisas e gera o 
			//token para o usuario . 
			private String gerarBasicToken(String email, String password) {
			String estrutura = email + ":" + password;
			byte[] estruturaBase64 = Base64.encodeBase64(estrutura.getBytes(Charset.forName("US-ASCII")));
					return "Basic " + new String(estruturaBase64);
						//este Basic precisa ter um espaço antes de fechar as ""
			}
	}
			
