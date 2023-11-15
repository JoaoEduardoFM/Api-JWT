package br.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.model.dto.CredenciaisDto;
import br.com.model.dto.SessaoDto;
import br.com.model.response.ResponseRest;
import br.com.model.response.ResponseRest.messageType;
import br.com.service.UsuarioService;
import io.swagger.annotations.Api;

@RestController
@RequestMapping("/login")
@Api(value = "Login", tags = "Login", description = " Retorna o token ao realizar o login.")
public class LoginController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping()
    public ResponseEntity<?> autenticar(@RequestBody CredenciaisDto credenciais) {
        try {
        	SessaoDto sessao =  usuarioService.autenticar(credenciais);
        	if(sessao != null) {
            return ResponseEntity.ok().body(sessao);
        	} else {
        		ResponseRest response = new ResponseRest();
        		response.setMessage("Login não autorizado. Verifique suas credenciais");
        		response.setType(messageType.ATENCAO);
        		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        	}

        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
