# Projeto TP2 - Testes e Dublês em Java

Projeto desenvolvido para o **TP2 da disciplina Desenvolvimento de Serviços Web e Testes com Java**, focado em garantir qualidade e confiabilidade através de testes automatizados utilizando dublês.

---

## Descrição

Este projeto implementa uma calculadora de reembolso que considera regras de negócio como percentual de cobertura do plano de saúde, teto máximo de reembolso e autorização prévia. O foco principal é aplicar testes unitários e de integração usando técnicas de dublês (mocks, stubs, spies e fakes), assegurando que todos os componentes funcionem corretamente isoladamente e integrados.

---

## Tecnologias e Ferramentas Utilizadas
- Java 21
- IntelliJ IDEA
- JUnit 5
- Mockito

---

## Funcionalidades

- Cálculo do reembolso baseado em valores e percentuais de cobertura  
- Aplicação de teto máximo para o valor do reembolso  
- Integração com sistemas de auditoria, autorização e histórico de consultas  
- Validação da autorização antes do reembolso  
- Registro do histórico de consultas com detalhes do reembolso  

---

## Como Funciona

A classe principal `CalculadoraReembolso` recebe informações da consulta, plano de saúde, paciente e realiza o cálculo do valor a ser reembolsado, respeitando limites e regras definidas. Durante o processo, a auditoria registra as consultas e o autorizador confirma se o reembolso pode ser liberado. O histórico mantém o registro detalhado das operações.

---

## Testes

Foram desenvolvidos testes unitários e de integração que abrangem:

- Cálculo correto do reembolso para diferentes percentuais  
- Comportamento com valores e percentuais zero ou extremos  
- Verificação da chamada dos componentes de auditoria e autorização  
- Uso de diferentes tipos de dublês para simular dependências externas  
- Garantia do registro correto no histórico de consultas  
- Validação do teto máximo de reembolso  

---

## Considerações Finais

Este projeto demonstra boas práticas de desenvolvimento orientado a testes, com foco especial no uso de dublês para isolar e validar funcionalidades complexas. Além disso, reforça a importância da integração entre componentes para assegurar um sistema robusto e confiável.
