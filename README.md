# TDD-Curso
Este repositório contém o projeto final (4ª semana) desenvolvido para o Curso de [TDD – Desenvolvimento de Software Guiado por Testes](https://www.coursera.org/learn/tdd-desenvolvimento-de-software-guiado-por-testes/) criado pelo ITA disponível na plataforma Coursera.

## Objetivo
Utilizar o **TDD** para desenvolver um *componente de gamificação* que deve armazenar diferentes tipos de pontos que o usuário pode receber.

## Arquitetura
Esse componente deve possuir uma classe principal chamada Placar, onde deve ficar a lógica, e uma classe chamada Armazenamento, que deve ser responsável por guardar e recuperar as informações de um arquivo.

1. **Armazenamento**: 
  * Armazenar que um usuário recebeu uma quantidade de um tipo de ponto. Exemplo: o usuário "guerra" recebeu "10" pontos do tipo "estrela".
  * Recuperar quantos pontos de um tipo tem um usuário. Exemplo: retornar quantos pontos do tipo "estrela" tem o usuário "guerra".
  * Retornar todos os usuários que já receberam algum tipo de ponto.
  * Retornar todos os tipos de ponto que já foram registrados para algum usuário.

Os dados serão lidos de um arquivo *.xml* que contenha os dados dos usuários e seus respectivos pontos. A classe também deve ser responsável por criar esse arquivo durante sua utilização pelas classes clientes.

  2. **Placar**: 
  * Possui uma instância de *Armazenamento*.
  * Registrar um tipo de ponto para um usuário. Exemplo: o usuário "guerra" recebeu "10" pontos do tipo "estrela".
  * Retornar:
    * Todos os pontos de um usuário
    * Ranking de usuários de acordo com a pontuação

Valores nulos não devem ser apresentados.

3. **XML**:
* Uma *tag* geral para criar um usuário
* Dentro dessa *tag* haverá o nome do usuário
* Pode haver as *tags* de marcadores de ponto dentro da *tag* de usuário

Exemplo de arquivo:

```<?xml version="1.0" encoding="UTF-8" standalone="no"?>
   <Usuarios>
   	<Usuario>
   		<Nome>Rafael</Nome>
   		<Pontos>
   			<Tipo>Moedas</Tipo>
   			<Valor>15</Valor>
   		</Pontos>
        <Pontos>
       			<Tipo>Fichas</Tipo>
       			<Valor>1000</Valor>
       		</Pontos>
   		<Pontos>
   			<Tipo>Curtidas</Tipo>
   			<Valor>110</Valor>
   		</Pontos>
   	</Usuario>
    <Usuario>
       		<Nome>Eduardo</Nome>
       		<Pontos>
       			<Tipo>Fichas</Tipo>
       			<Valor>3000</Valor>
       		</Pontos>
       		<Pontos>
       			<Tipo>Comentários</Tipo>
       			<Valor>11</Valor>
       		</Pontos>
       	</Usuario>
   </Usuarios>
```

## Hipóteses
* O usuário pode receber diversos tipos de pontos (Ex: *estrela*, *moeda*, *curtida*, *comentário*) e sua seleção cabe a classe cliente de *Placar*.

## Testes
* Para os testes da classe *Placar*, uma classe de *Mock Object* para a instância de *Armazenamento* deve ser utilizada
* Realizar testes de integração
