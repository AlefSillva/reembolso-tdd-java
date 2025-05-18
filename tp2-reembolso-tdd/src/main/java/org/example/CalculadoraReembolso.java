package org.example;

public class CalculadoraReembolso {

    public double calcularReembolso(double valor, double percentCobertura, Paciente paciente) {
        return valor * (percentCobertura / 100);
    }
}