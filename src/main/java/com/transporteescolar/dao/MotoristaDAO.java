package com.transporteescolar.dao;

import com.transporteescolar.model.Motorista;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MotoristaDAO {

    public void incluir(Motorista motorista) {
        String sql = "INSERT INTO motorista (nome, cpf, cnh, login, senha) VALUES (?, ?, ?, ?, ?)";
        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, motorista.getNome());
            stmt.setString(2, motorista.getCpf());
            stmt.setString(3, motorista.getCnh());
            stmt.setString(4, motorista.getLogin());
            stmt.setString(5, motorista.getSenha());
            stmt.executeUpdate();

            try (ResultSet chaves = stmt.getGeneratedKeys()) {
                if (chaves.next()) {
                    motorista.setId(chaves.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao incluir motorista", e);
        }
    }

    public void atualizar(Motorista motorista) {
        String sql = "UPDATE motorista SET nome = ?, cpf = ?, cnh = ?, login = ?, senha = ? WHERE id = ?";
        try (Connection conexao = Conexao.conectar(); PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, motorista.getNome());
            stmt.setString(2, motorista.getCpf());
            stmt.setString(3, motorista.getCnh());
            stmt.setString(4, motorista.getLogin());
            stmt.setString(5, motorista.getSenha());
            stmt.setInt(6, motorista.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar motorista", e);
        }
    }

    public void excluir(Motorista motorista) {
        String sql = "DELETE FROM motorista WHERE id = ?";
        try (Connection conexao = Conexao.conectar(); PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, motorista.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir motorista", e);
        }
    }

    public List<Motorista> listar() {
        List<Motorista> motoristas = new ArrayList<>();
        String sql = "SELECT id, nome, cpf, cnh, login, senha FROM motorista ORDER BY nome";
        try (Connection conexao = Conexao.conectar();
             Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                motoristas.add(new Motorista(rs.getInt("id"), rs.getString("nome"), rs.getString("cpf"),
                        rs.getString("cnh"), rs.getString("login"), rs.getString("senha")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar motoristas", e);
        }
        return motoristas;
    }

    public Motorista buscarPorId(int id) {
        String sql = "SELECT id, nome, cpf, cnh, login, senha FROM motorista WHERE id = ?";
        try (Connection conexao = Conexao.conectar(); PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Motorista(rs.getInt("id"), rs.getString("nome"), rs.getString("cpf"),
                            rs.getString("cnh"), rs.getString("login"), rs.getString("senha"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar motorista", e);
        }
        return null;
    }

    public Motorista autenticar(String login, String senha) {
        String sql = "SELECT id, nome, cpf, cnh, login, senha FROM motorista WHERE login = ? AND senha = ?";
        try (Connection conexao = Conexao.conectar(); PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, login);
            stmt.setString(2, senha);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Motorista(rs.getInt("id"), rs.getString("nome"), rs.getString("cpf"),
                            rs.getString("cnh"), rs.getString("login"), rs.getString("senha"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao autenticar motorista", e);
        }
        return null;
    }
}
