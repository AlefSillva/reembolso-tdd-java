import org.example.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CalculadoraReembolsoTest {
    //-------------------------Variáveis---------------------------
    private CalculadoraReembolso reembolso;
    private Paciente paciente;
    private Consulta consulta;

    //--------------------------HELPERS----------------------------
    private Consulta criarConsultaPadrao() {
        return new Consulta(
                "Dr. Armênio",
                LocalDate.of(2025, 5, 19),
                200.0,
                paciente
        );
    }

    private boolean compararComMargemDeErro(double esperado, double atual) {
        return Math.abs(esperado - atual) <= 0.01;
    }

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

    //-----------------------------SPY-----------------------------
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

    //---------------------------Setup-----------------------------
    @BeforeEach
    public void init() {
        reembolso = new CalculadoraReembolso();
        paciente = new PacienteDummy();
        consulta = criarConsultaPadrao();
    }

    //---------------------------Testes----------------------------
    @DisplayName("calcular reembolso com base em um valor fixo")
    // Testa o cálculo do reembolso usando um valor e percentual fixos, validando o valor esperado.
    @Test
    public void deveRetornarReembolsoBaseadoNoValorFixo() {
        double percentCobertura = 70.0;
        double valorReembolso = reembolso.calcularReembolso(
                consulta.getValor(),
                percentCobertura,
                consulta.getPaciente()
        );
        assertTrue(compararComMargemDeErro(140.0, valorReembolso));
    }

    @DisplayName("Teste em que o valor da consulta é igual a 0")
    // Verifica que o reembolso é zero quando o valor da consulta é zero, independentemente da cobertura.
    @Test
    public void deveRetornarReembolsoZeroParaValorZero() {
        double valorReembolso = reembolso.calcularReembolso(
                0.0,
                60.0,
                paciente
        );
        assertTrue(compararComMargemDeErro(0.0, valorReembolso));
    }

    @DisplayName("Teste em que o percentual de cobertura é igual a 0")
    // Confirma que o reembolso é zero quando o percentual de cobertura é zero, mesmo com valor positivo.
    @Test
    public void deveRetornarReembolsoZeroParaPercentualZero() {
        double valorReembolso = reembolso.calcularReembolso(
                consulta.getValor(),
                0.0,
                consulta.getPaciente()
        );
        assertTrue(compararComMargemDeErro(0.0, valorReembolso));
    }

    @DisplayName("Teste em que o percentual de cobertura é igual a 100")
    // Testa se o reembolso é igual ao valor total da consulta quando a cobertura é 100%.
    @Test
    public void deveRetornarReembolsoIgualAoValorParaPercentualCem() {
        double valorReembolso = reembolso.calcularReembolso(
                consulta.getValor(),
                100.0,
                consulta.getPaciente()
        );

        assertTrue(compararComMargemDeErro(200.0, valorReembolso));
    }

    //-------------------Testes com Stubs do plano de saúde----------------
    @DisplayName("Teste com plano de saúde com 50% de cobertura")
    // Usa stub para simular plano de saúde com 50% de cobertura e testa cálculo do reembolso.
    @Test
    public void deveRetornarReembolsoComPlanoSaude50() {
        PlanoSaude plano = new PlanoSaudeStub50();
        double valorReembolso = reembolso.calcularReembolso(
                consulta.getValor(),
                plano.getPercentualCobertura(),
                consulta.getPaciente()
        );
        assertTrue(compararComMargemDeErro(100.0, valorReembolso));
    }

    @DisplayName("Teste com plano de saúde com 80% de cobertura")
    // Usa stub para simular plano de saúde com 80% de cobertura e verifica cálculo correto do reembolso.
    @Test
    public void deveRetornarReembolsoComPlanoSaude80() {
        PlanoSaude plano = new PlanoSaudeStub80();
        double valorReembolso = reembolso.calcularReembolso(
                consulta.getValor(),
                plano.getPercentualCobertura(),
                consulta.getPaciente()
        );
        assertTrue(compararComMargemDeErro(160.0, valorReembolso));
    }

    //-----------------------Testes com Auditoria-----------------------
    @DisplayName("Teste com auditoria que registra consulta")
    // Verifica se o método registrarConsulta da auditoria é chamado durante o cálculo do reembolso (Spy).
    @Test
    public void deveChamarAuditoriaAoCalcularReembolso() {
        AuditoriaSpy auditoriaSpy = new AuditoriaSpy();
        CalculadoraReembolso calc = new CalculadoraReembolso(auditoriaSpy, null,null);

        Paciente pacienteDummy = new PacienteDummy();
        double valorConsulta = 150.0;
        double percentualCobertura = 80.0;

        calc.calcularReembolso(valorConsulta, percentualCobertura, pacienteDummy);

        assertTrue(auditoriaSpy.metodoFoiChamado(), "Auditoria deve ser chamada após calcular reembolso");
    }

    //------------------Testes com MOCK Autorizador de Reembolso----------------
    @DisplayName("Deve lançar exceção quando reembolso não for autorizado")
    // Usa mock para simular autorização negada e valida que a exceção é lançada corretamente.
    @Test
    public void deveNegarReembolsoQuandoNaoAutorizado() {
        Paciente paciente = new Paciente();

        // Mock do autorizador de reembolso
        AutorizadorReembolso autorizadorMock = mock(AutorizadorReembolso.class);
        when(autorizadorMock.autorizar(any(Paciente.class))).thenReturn(false);

        CalculadoraReembolso calc = new CalculadoraReembolso(null, autorizadorMock, null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            calc.calcularReembolso(100.0, 50.0, paciente);
        });
        assertEquals("Reembolso não autorizado", exception.getMessage());

        verify(autorizadorMock).autorizar(paciente);
    }
    //------------------------------------------D-----------------------

    //-------------------------Regras de Teto--------------------------
    @DisplayName("Deve retornar reembolso máximo de 150.0")
    // Verifica a aplicação da regra de teto, limitando o reembolso máximo a 150,00.
    @Test
    public void deveRetornarReembolsoMaximo() {
        double percentualCobertura = 90.0;

        double valorReembolso = reembolso.calcularReembolso(
                consulta.getValor(),
                percentualCobertura,
                consulta.getPaciente()
        );

        assertTrue(
                compararComMargemDeErro(valorReembolso, 150.0),
                "O reembolso deve ser limitado ao teto de 150.0"
        );
    }

    //------------------------Integração com Vários Dublês--------------------
    @DisplayName("Teste de integração com dublês")
    /* Testa a integração entre cálculo de reembolso, auditoria, autorização e histórico de consultas,
     utilizando dublês para simular dependências.*/
    @Test
    public void deveRegistrarConsultaComHistorico() {
        PlanoSaude plano = new PlanoSaudeStub80();

        // Mock do autorizador de reembolso para simular autorização
        AutorizadorReembolso autorizadoMock = mock(AutorizadorReembolso.class);
        when(autorizadoMock.autorizar(any(Paciente.class))).thenReturn(true);

        // Spy da auditoria para verificar se o método foi chamado
        AuditoriaSpy auditoriaSpy = new AuditoriaSpy();

        // Instância do historico de consultas fake
        HistoricoConsultasFake historicoConsulta = new HistoricoConsultasFake();

        // Instância da calculadora de reembolso com os dublês
        CalculadoraReembolso calculadora = new CalculadoraReembolso(auditoriaSpy, autorizadoMock, historicoConsulta);

        double valorReembolso = calculadora.calcularReembolso(
                consulta.getValor(),
                plano.getPercentualCobertura(),
                consulta.getPaciente()
        );

        // Verifica se o autorizador foi chamado para autorizar o paciente
        verify(autorizadoMock).autorizar(consulta.getPaciente());

        // Verifica se o método de auditoria foi chamado
        assertTrue(auditoriaSpy.metodoFoiChamado(),
                "A auditoria deve registrar a consulta");

        // Verifica se o reembolso foi limitado ao teto de 150.0
        assertTrue(compararComMargemDeErro(150.0, valorReembolso),
                "O reembolso deve ser limitado ao teto de 150.0");

        String esperado = String.format(
                "Consulta de valor R$ %.2f registrada com reembolso de R$ %.2f",
                consulta.getValor(),
                valorReembolso
        );

        // Verifica se o histórico de consultas contém a consulta registrada
        assertTrue(historicoConsulta.exibirHistorico().contains(esperado),
                "O histórico deve registrar a consulta com valor e reembolso");
    }
}


