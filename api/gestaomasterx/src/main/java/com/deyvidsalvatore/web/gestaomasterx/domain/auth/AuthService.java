package com.deyvidsalvatore.web.gestaomasterx.domain.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.deyvidsalvatore.web.gestaomasterx.domain.usuario.Usuario;
import com.deyvidsalvatore.web.gestaomasterx.domain.usuario.UsuarioRepository;

@Service
public class AuthService implements UserDetailsService {
	
	private final UsuarioRepository usuarioRepository;

	public AuthService(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));

		return usuario;
	}

}
