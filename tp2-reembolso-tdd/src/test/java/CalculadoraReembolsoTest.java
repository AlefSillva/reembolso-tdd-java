import org.example.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculadoraReembolsoTest {
    //-------------------------Variáveis---------------------------
    private CalculadoraReembolso reembolso;
    private Paciente paciente;

    //---------------------------Dummy-----------------------------
    static class PacienteDummy extends Paciente {
    }

    //-----------------------Interface Fake------------------------
    static class HistoricoConsultasFake implements HistoricoConsultas {
        private List<String> consultas = new ArrayList<>();

        @Override
        public void adicionarConsulta(String consulta) {
            consultas.add(consulta);
        }

        @Override
        public List<String> exibirHistorico() {
            return consultas;
        }
    }

    //---------------------------Stubs----------------------------
    static class PlanoSaudeStub50 implements PlanoSaude {
        @Override
        public double getPercentualCobertura() {
            return 50.0;
        }
    }

    static class PlanoSaudeStub80 implements PlanoSaude {
        @Override
        public double getPercentualCobertura() {
            return 80.0;
        }
    }

    //-----------------------------Spy-----------------------------
    static class AuditoriaSpy implements Auditoria {
        private boolean metodoChamado = false;

        @Override
        public void registrarConsulta(String consulta) {
            metodoChamado = true;
        }

        public boolean metodoFoiChamado() {
            return metodoChamado;
        }

    }


    @BeforeEach
    public void init() {
        reembolso = new CalculadoraReembolso();
        paciente = new PacienteDummy();
    }

    //---------------------------Testes----------------------------
    @DisplayName("calcular reembolso com base em um valor fixo")
    @Test
    public void deveRetornarReembolsoBaseadoNoValorFixo() {
        double percentCobertura = 70.0;
        double valorReembolso = reembolso.calcularReembolso(200.0, percentCobertura, paciente);
        assertEquals(140.0, valorReembolso);
    }

    @DisplayName("Teste em que o valor da consulta é igual a 0")
    @Test
    public void deveRetornarReembolsoZeroParaValorZero() {
        double valorReembolso = reembolso.calcularReembolso(0.0, 60.0, paciente);
        assertEquals(0.0, valorReembolso);
    }

    @DisplayName("Teste em que o percentual de cobertura é igual a 0")
    @Test
    public void deveRetornarReembolsoZeroParaPercentualZero() {
        double valorReembolso = reembolso.calcularReembolso(100.0, 0.0, paciente);
        assertEquals(0.0, valorReembolso);
    }

    @DisplayName("Teste em que o percentual de cobertura é igual a 100")
    @Test
    public void deveRetornarReembolsoIgualAoValorParaPercentualCem() {
        double valorReembolso = reembolso.calcularReembolso(150.0, 100.0, paciente);
        assertEquals(150.0, valorReembolso);
    }

    //-------------------Testes com Stubs do plano de saúde-------------------
    @DisplayName("Teste com plano de saúde com 50% de cobertura")
    @Test
    public void deveRetornarReembolsoComPlanoSaude50() {
        PlanoSaude plano = new PlanoSaudeStub50();
        double valorReembolso = reembolso.calcularReembolso(200.0, plano.getPercentualCobertura(), paciente);
        assertEquals(100.0, valorReembolso);
    }

    @DisplayName("Teste com plano de saúde com 80% de cobertura")
    @Test
    public void deveRetornarReembolsoComPlanoSaude80() {
        PlanoSaude plano = new PlanoSaudeStub80();
        double valorReembolso = reembolso.calcularReembolso(200.0, plano.getPercentualCobertura(), paciente);
        assertEquals(160.0, valorReembolso);
    }
}
