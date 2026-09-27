package com.transporteescolar.model;

import java.util.ArrayList;
import java.util.List;

public class Rota {

    private int id;
    private String nome;
    private String horario;
    private Van van;
    private List<Aluno> alunos;

    public Rota(int id, String nome, String horario, Van van, List<Aluno> alunos) {
        this.id = id;
        this.nome = nome;
        this.horario = horario;
        this.van = van;
        this.alunos = new ArrayList<>(alunos);
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

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public Van getVan() {
        return van;
    }

    public void setVan(Van van) {
        this.van = van;
    }

    public List<Aluno> getAlunos() {
        return alunos;
    }

    public void setAlunos(List<Aluno> alunos) {
        this.alunos = new ArrayList<>(alunos);
    }

    @Override
    public String toString() {
        return nome + " - " + horario;
    }
}
