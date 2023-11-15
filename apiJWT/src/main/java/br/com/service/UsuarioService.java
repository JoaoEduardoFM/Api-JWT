package br.com.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.config.security.JwtUtil;
import br.com.model.dto.CredenciaisDto;
import br.com.model.dto.SessaoDto;

@Service
public class UsuarioService {
	@Autowired
	private JwtUtil jwtUtil;
	
	@Value("${custom.login}")
    private String loginConfig;

    @Value("${custom.senha}")
    private String senhaConfig;

	public SessaoDto autenticar(CredenciaisDto credenciais) {

		if (credenciais.getLogin().equals(loginConfig) && credenciais.getSenha().equals(senhaConfig)) {

			SessaoDto sessao = new SessaoDto();

			sessao.setToken("Bearer " + jwtUtil.generateToken(credenciais.getLogin()));
			sessao.setDataInicio(new Date(System.currentTimeMillis()));
			sessao.setDataFim(new Date(System.currentTimeMillis() + jwtUtil.getExpiration()));

			return sessao;
		} else {
			return null;
		}
	}
}
