package org.example;

import java.util.List;

public interface HistoricoConsultas {
    void adicionarConsulta(String consulta);
    List<String> exibirHistorico();
}
