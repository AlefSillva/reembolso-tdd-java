package org.example;

public class CalculadoraReembolso {
    private Auditoria auditoria;
    private AutorizadorReembolso autorizador;
    private HistoricoConsultas historico;

    //-------------------------Construtores-------------------------
    public CalculadoraReembolso() {}

    public CalculadoraReembolso(Auditoria auditoria, AutorizadorReembolso autorizador, HistoricoConsultas historico) {
        this.auditoria = auditoria;
        this.autorizador = autorizador;
        this.historico = historico;
    }

    //-------------------------Métodos-----------------------------
    public double calcularReembolso(double valor, double percentCobertura, Paciente paciente) {

        if (autorizador != null && !autorizador.autorizar(paciente)) {
            throw new RuntimeException("Reembolso não autorizado");

        }

        double reembolso = valor * (percentCobertura / 100);
        reembolso = reembolso > 150 ? 150 : reembolso;


        if (auditoria != null) {
            auditoria.registrarConsulta("Consulta registrada");
        }

        if (historico != null) {
            historico.adicionarConsulta("Consulta de valor R$ " + valor + " registrada com reembolso de R$ " + reembolso);
        }

        return reembolso;
    }
}