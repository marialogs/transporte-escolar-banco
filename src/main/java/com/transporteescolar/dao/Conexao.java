package com.transporteescolar.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexao {

    private static final String URL = "jdbc:sqlite:transporteescolar.db";

    public static Connection conectar() throws SQLException {
        Connection conexao = DriverManager.getConnection(URL);
        try (Statement stmt = conexao.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");
        }
        return conexao;
    }

    public static void criarTabelas() {
        String sqlMotorista = "CREATE TABLE IF NOT EXISTS motorista ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nome TEXT NOT NULL, "
                + "cpf TEXT NOT NULL, "
                + "cnh TEXT NOT NULL, "
                + "login TEXT NOT NULL UNIQUE, "
                + "senha TEXT NOT NULL)";

        String sqlVan = "CREATE TABLE IF NOT EXISTS van ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "placa TEXT NOT NULL, "
                + "modelo TEXT NOT NULL, "
                + "capacidade INTEGER NOT NULL, "
                + "motorista_id INTEGER, "
                + "FOREIGN KEY (motorista_id) REFERENCES motorista(id))";

        String sqlAluno = "CREATE TABLE IF NOT EXISTS aluno ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nome TEXT NOT NULL, "
                + "endereco TEXT, "
                + "escola TEXT, "
                + "responsavel TEXT, "
                + "telefone TEXT)";

        String sqlRota = "CREATE TABLE IF NOT EXISTS rota ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nome TEXT NOT NULL, "
                + "horario TEXT, "
                + "van_id INTEGER, "
                + "FOREIGN KEY (van_id) REFERENCES van(id))";

        String sqlRotaAluno = "CREATE TABLE IF NOT EXISTS rota_aluno ("
                + "rota_id INTEGER NOT NULL, "
                + "aluno_id INTEGER NOT NULL, "
                + "PRIMARY KEY (rota_id, aluno_id), "
                + "FOREIGN KEY (rota_id) REFERENCES rota(id), "
                + "FOREIGN KEY (aluno_id) REFERENCES aluno(id))";

        try (Connection conexao = conectar(); Statement stmt = conexao.createStatement()) {
            stmt.execute(sqlMotorista);
            stmt.execute(sqlVan);
            stmt.execute(sqlAluno);
            stmt.execute(sqlRota);
            stmt.execute(sqlRotaAluno);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar as tabelas do banco de dados", e);
        }
    }
}
