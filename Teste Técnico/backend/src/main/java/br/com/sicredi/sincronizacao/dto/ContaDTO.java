package br.com.sicredi.sincronizacao.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ContaDTO {

    private String agencia;
    private String conta;
    private Double saldo;
    private String status;

}
