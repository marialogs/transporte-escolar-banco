package com.transporteescolar.dao;

import com.transporteescolar.model.Aluno;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AlunoDAO {

    public void incluir(Aluno aluno) {
        String sql = "INSERT INTO aluno (nome, endereco, escola, responsavel, telefone) VALUES (?, ?, ?, ?, ?)";
        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, aluno.getNome());
            stmt.setString(2, aluno.getEndereco());
            stmt.setString(3, aluno.getEscola());
            stmt.setString(4, aluno.getResponsavel());
            stmt.setString(5, aluno.getTelefone());
            stmt.executeUpdate();

            try (ResultSet chaves = stmt.getGeneratedKeys()) {
                if (chaves.next()) {
                    aluno.setId(chaves.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao incluir aluno", e);
        }
    }

    public void atualizar(Aluno aluno) {
        String sql = "UPDATE aluno SET nome = ?, endereco = ?, escola = ?, responsavel = ?, telefone = ? WHERE id = ?";
        try (Connection conexao = Conexao.conectar(); PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, aluno.getNome());
            stmt.setString(2, aluno.getEndereco());
            stmt.setString(3, aluno.getEscola());
            stmt.setString(4, aluno.getResponsavel());
            stmt.setString(5, aluno.getTelefone());
            stmt.setInt(6, aluno.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar aluno", e);
        }
    }

    public void excluir(Aluno aluno) {
        String sql = "DELETE FROM aluno WHERE id = ?";
        try (Connection conexao = Conexao.conectar(); PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, aluno.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir aluno", e);
        }
    }

    public List<Aluno> listar() {
        List<Aluno> alunos = new ArrayList<>();
        String sql = "SELECT id, nome, endereco, escola, responsavel, telefone FROM aluno ORDER BY nome";
        try (Connection conexao = Conexao.conectar();
             Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                alunos.add(new Aluno(rs.getInt("id"), rs.getString("nome"), rs.getString("endereco"),
                        rs.getString("escola"), rs.getString("responsavel"), rs.getString("telefone")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar alunos", e);
        }
        return alunos;
    }

    public Aluno buscarPorId(int id) {
        String sql = "SELECT id, nome, endereco, escola, responsavel, telefone FROM aluno WHERE id = ?";
        try (Connection conexao = Conexao.conectar(); PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Aluno(rs.getInt("id"), rs.getString("nome"), rs.getString("endereco"),
                            rs.getString("escola"), rs.getString("responsavel"), rs.getString("telefone"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar aluno", e);
        }
        return null;
    }
}
