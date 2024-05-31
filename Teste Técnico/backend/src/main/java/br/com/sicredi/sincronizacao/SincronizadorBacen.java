package br.com.sicredi.sincronizacao;

import br.com.sicredi.sincronizacao.dto.ContaDTO;
import br.com.sicredi.sincronizacao.service.SincronizacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class SincronizadorBacen {

	@Autowired
	SincronizacaoService sincronizacaoService;

	public static void main(String[] args) {
		SpringApplication.run(SincronizadorBacen.class, args);
	}


	@Component
	public class CommandLineAppStartupRunner implements CommandLineRunner {

		@Override
		public void run(String...args) throws Exception {

			sincronizacaoService.syncAccounts();

		}


	}

}
