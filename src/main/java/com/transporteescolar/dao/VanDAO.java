package com.transporteescolar.dao;

import com.transporteescolar.model.Motorista;
import com.transporteescolar.model.Van;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class VanDAO {

    private final MotoristaDAO motoristaDAO = new MotoristaDAO();

    public void incluir(Van van) {
        String sql = "INSERT INTO van (placa, modelo, capacidade, motorista_id) VALUES (?, ?, ?, ?)";
        try (Connection conexao = Conexao.conectar();
             PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, van.getPlaca());
            stmt.setString(2, van.getModelo());
            stmt.setInt(3, van.getCapacidade());
            if (van.getMotorista() != null) {
                stmt.setInt(4, van.getMotorista().getId());
            } else {
                stmt.setNull(4, java.sql.Types.INTEGER);
            }
            stmt.executeUpdate();

            try (ResultSet chaves = stmt.getGeneratedKeys()) {
                if (chaves.next()) {
                    van.setId(chaves.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao incluir van", e);
        }
    }

    public void atualizar(Van van) {
        String sql = "UPDATE van SET placa = ?, modelo = ?, capacidade = ?, motorista_id = ? WHERE id = ?";
        try (Connection conexao = Conexao.conectar(); PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, van.getPlaca());
            stmt.setString(2, van.getModelo());
            stmt.setInt(3, van.getCapacidade());
            if (van.getMotorista() != null) {
                stmt.setInt(4, van.getMotorista().getId());
            } else {
                stmt.setNull(4, java.sql.Types.INTEGER);
            }
            stmt.setInt(5, van.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar van", e);
        }
    }

    public void excluir(Van van) {
        String sql = "DELETE FROM van WHERE id = ?";
        try (Connection conexao = Conexao.conectar(); PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, van.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir van", e);
        }
    }

    public List<Van> listar() {
        List<Van> vans = new ArrayList<>();
        String sql = "SELECT id, placa, modelo, capacidade, motorista_id FROM van ORDER BY placa";
        try (Connection conexao = Conexao.conectar();
             Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Motorista motorista = null;
                int motoristaId = rs.getInt("motorista_id");
                if (!rs.wasNull()) {
                    motorista = motoristaDAO.buscarPorId(motoristaId);
                }
                vans.add(new Van(rs.getInt("id"), rs.getString("placa"), rs.getString("modelo"),
                        rs.getInt("capacidade"), motorista));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar vans", e);
        }
        return vans;
    }

    public Van buscarPorId(int id) {
        String sql = "SELECT id, placa, modelo, capacidade, motorista_id FROM van WHERE id = ?";
        try (Connection conexao = Conexao.conectar(); PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Motorista motorista = null;
                    int motoristaId = rs.getInt("motorista_id");
                    if (!rs.wasNull()) {
                        motorista = motoristaDAO.buscarPorId(motoristaId);
                    }
                    return new Van(rs.getInt("id"), rs.getString("placa"), rs.getString("modelo"),
                            rs.getInt("capacidade"), motorista);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar van", e);
        }
        return null;
    }
}
