package org.example;

import java.time.LocalDate;

public class Consulta {
    private String medico;
    private LocalDate data;
    private double valor;
    private Paciente paciente;

    public Consulta(String medico, LocalDate data, double valor, Paciente paciente) {
        this.medico = medico;
        this.data = data;
        this.valor = valor;
        this.paciente = paciente;
    }

    public String getMedico() {
        return medico;
    }

    public LocalDate getData() {
        return data;
    }

    public double getValor() {
        return valor;
    }

    public Paciente getPaciente() {
        return paciente;
    }
}
