package br.com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ConfigurableWebApplicationContext;

import br.com.model.response.ResponseRest;
import br.com.model.response.ResponseRest.messageType;

@Service
public class DesligarService {

	@Autowired
	ConfigurableWebApplicationContext context;

	public ResponseEntity<ResponseRest> shutdown() {
		ResponseRest response = new ResponseRest();
		TaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();

		taskExecutor.execute(() -> {
			response.setMessage("Aplicação desligada com sucesso.");
			response.setType(messageType.SUCESSO);
			context.close();
			System.exit(0);

		});
		return new ResponseEntity<ResponseRest>(response, HttpStatus.BAD_REQUEST);
	}
}
