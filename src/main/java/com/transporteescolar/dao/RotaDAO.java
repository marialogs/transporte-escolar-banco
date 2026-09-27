package com.transporteescolar.dao;

import com.transporteescolar.model.Aluno;
import com.transporteescolar.model.Rota;
import com.transporteescolar.model.Van;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RotaDAO {

    private final VanDAO vanDAO = new VanDAO();
    private final AlunoDAO alunoDAO = new AlunoDAO();

    public void incluir(Rota rota) {
        String sql = "INSERT INTO rota (nome, horario, van_id) VALUES (?, ?, ?)";
        try (Connection conexao = Conexao.conectar()) {
            try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, rota.getNome());
                stmt.setString(2, rota.getHorario());
                if (rota.getVan() != null) {
                    stmt.setInt(3, rota.getVan().getId());
                } else {
                    stmt.setNull(3, java.sql.Types.INTEGER);
                }
                stmt.executeUpdate();

                try (ResultSet chaves = stmt.getGeneratedKeys()) {
                    if (chaves.next()) {
                        rota.setId(chaves.getInt(1));
                    }
                }
            }
            salvarAlunosDaRota(conexao, rota);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao incluir rota", e);
        }
    }

    public void atualizar(Rota rota) {
        String sql = "UPDATE rota SET nome = ?, horario = ?, van_id = ? WHERE id = ?";
        try (Connection conexao = Conexao.conectar()) {
            try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
                stmt.setString(1, rota.getNome());
                stmt.setString(2, rota.getHorario());
                if (rota.getVan() != null) {
                    stmt.setInt(3, rota.getVan().getId());
                } else {
                    stmt.setNull(3, java.sql.Types.INTEGER);
                }
                stmt.setInt(4, rota.getId());
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = conexao.prepareStatement("DELETE FROM rota_aluno WHERE rota_id = ?")) {
                stmt.setInt(1, rota.getId());
                stmt.executeUpdate();
            }
            salvarAlunosDaRota(conexao, rota);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar rota", e);
        }
    }

    public void excluir(Rota rota) {
        try (Connection conexao = Conexao.conectar()) {
            try (PreparedStatement stmt = conexao.prepareStatement("DELETE FROM rota_aluno WHERE rota_id = ?")) {
                stmt.setInt(1, rota.getId());
                stmt.executeUpdate();
            }
            try (PreparedStatement stmt = conexao.prepareStatement("DELETE FROM rota WHERE id = ?")) {
                stmt.setInt(1, rota.getId());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir rota", e);
        }
    }

    private void salvarAlunosDaRota(Connection conexao, Rota rota) throws SQLException {
        String sql = "INSERT INTO rota_aluno (rota_id, aluno_id) VALUES (?, ?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            for (Aluno aluno : rota.getAlunos()) {
                stmt.setInt(1, rota.getId());
                stmt.setInt(2, aluno.getId());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    private List<Aluno> buscarAlunosDaRota(int rotaId) {
        List<Aluno> alunos = new ArrayList<>();
        String sql = "SELECT aluno_id FROM rota_aluno WHERE rota_id = ?";
        try (Connection conexao = Conexao.conectar(); PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, rotaId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Aluno aluno = alunoDAO.buscarPorId(rs.getInt("aluno_id"));
                    if (aluno != null) {
                        alunos.add(aluno);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar alunos da rota", e);
        }
        return alunos;
    }

    public List<Rota> listar() {
        List<Rota> rotas = new ArrayList<>();
        String sql = "SELECT id, nome, horario, van_id FROM rota ORDER BY nome";
        try (Connection conexao = Conexao.conectar();
             Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Van van = null;
                int vanId = rs.getInt("van_id");
                if (!rs.wasNull()) {
                    van = vanDAO.buscarPorId(vanId);
                }
                int id = rs.getInt("id");
                rotas.add(new Rota(id, rs.getString("nome"), rs.getString("horario"), van, buscarAlunosDaRota(id)));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar rotas", e);
        }
        return rotas;
    }
}
