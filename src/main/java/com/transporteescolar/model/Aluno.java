package com.transporteescolar.model;

public class Aluno {

    private int id;
    private String nome;
    private String endereco;
    private String escola;
    private String responsavel;
    private String telefone;

    public Aluno(int id, String nome, String endereco, String escola, String responsavel, String telefone) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.escola = escola;
        this.responsavel = responsavel;
        this.telefone = telefone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getEscola() {
        return escola;
    }

    public void setEscola(String escola) {
        this.escola = escola;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @Override
    public String toString() {
        return nome;
    }
}
