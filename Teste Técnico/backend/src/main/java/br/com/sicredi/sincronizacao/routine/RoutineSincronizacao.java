package br.com.sicredi.sincronizacao.routine;

import br.com.sicredi.sincronizacao.service.SincronizacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RoutineSincronizacao {

    @Autowired
    private SincronizacaoService sincronizacaoService;

    @Scheduled(cron = "0 0/30 * * * *")
    public void executar() {
        sincronizacaoService.syncAccounts();
    }
}
