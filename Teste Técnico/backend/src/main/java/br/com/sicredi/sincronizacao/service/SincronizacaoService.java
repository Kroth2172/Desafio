package br.com.sicredi.sincronizacao.service;

import br.com.sicredi.sincronizacao.dto.ContaDTO;
import br.com.sicredi.sincronizacao.timer.MeasuredExecutionTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


@Service
@RequiredArgsConstructor
@Slf4j
public class SincronizacaoService {

  @Autowired
  BancoCentralService bancoCentralService;

  @MeasuredExecutionTime
  public void syncAccounts() {
    // 007 aqui, trocar as coisas e deixar tudo aqui mesmo

    // Ler do cvs
    ArrayList<ContaDTO> resultado = ListarContas();

    // Grava o resultado no arquivo CSV
    gravarContas(resultado);
  }

  // Método para listar as contas do arquivo CSV
  public ArrayList<ContaDTO> ListarContas() {

    // Caminho do arquivo leitura
    // String arquivoLeitura = "./teste/dados/DATA.csv";
    String arquivoLeitura = "c:\\desafio\\dados\\DATA.csv";

    ArrayList<ContaDTO> lista = new ArrayList<>();

    try {
      // Abrir o leitor para ler o arquivo
      BufferedReader leitor = new BufferedReader(new FileReader(arquivoLeitura));
      String linha;
      boolean primeiraLinha = true;

      while ( (linha = leitor.readLine()) != null ) {
        // Ignora a primeira linha
        if (primeiraLinha) {
          primeiraLinha = false;
          continue;
        }
        // Adiciona na lista
        lista.add(validaCampos(linha));
      }

      leitor.close();

    } catch(IOException e) {
      e.printStackTrace();
    }

    return lista;
  }

  public ContaDTO validaCampos(String linha) {
    // Dividir a linha em partes usando o ponto e vírgula como separador
    String[] partes = linha.split(",");

    // Agencia
    String agencia = partes[0];
    // Conta
    String conta = partes[1];
    // Saldo
    Double saldo = Double.parseDouble(partes[2]);
    // Status
    String status = "";

    // Criar o objeto conta agencia;conta;saldo;status
    ContaDTO contaDTO = new ContaDTO(agencia, conta, saldo, status);

    // Validações sobre os campos antes de enviar para o banco central
    if ("".equalsIgnoreCase(agencia) && agencia.isEmpty()) {
      status = "Erro - Agência está vazio!";
    } else if ("".equalsIgnoreCase(conta) && conta.isEmpty()) {
      status = "Erro - Conta está vazio!";
    } else if (saldo < 0) {
      status = "Erro - Saldo está vazio!";
    } else {
      // Se os campos não estão vazios, é feita a sincronização com o banco central e atualizado o status
      bancoCentralService.atualizaConta(contaDTO);
      status = "Sucesso - Atualizado com sucesso!";
    }

    // Atualizo o status
    contaDTO.setStatus(status);

    // Imprimir informações da conta
    System.out.println("agencia: " + agencia + " - conta: " + conta + " - saldo: " + saldo);

    return contaDTO;
  }

  // Método para gravar o resultado da sincronização com o banco central no arquivo CSV
  public void gravarContas(ArrayList<ContaDTO> contas) {
    try {
      // Caminho do arquivo escrita
      String arquivoResultado = "c:\\desafio\\dados\\Resultado.csv";

      // Abre o escritor para adicionar dados ao arquivo
      FileWriter escritor = new FileWriter(arquivoResultado, StandardCharsets.ISO_8859_1, false);

      escritor.write("agencia;conta;saldo;status\n");
      escritor.flush();

      for (ContaDTO conta: contas) {
        // Escrever os dados da conta
        escritor.write(conta.getAgencia() + ";" + conta.getConta() + ";" + conta.getSaldo() + ";" + conta.getStatus() + "\n");

        // Escrever todos os dados do buffer no arquivo imediatamente
        escritor.flush();
      }

      // Fecha o recurso de escrita
      escritor.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
