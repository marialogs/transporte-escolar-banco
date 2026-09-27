package com.transporteescolar.view;

import com.transporteescolar.dao.MotoristaDAO;
import com.transporteescolar.dao.VanDAO;
import com.transporteescolar.model.Motorista;
import com.transporteescolar.model.Van;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TelaVan extends JFrame {

    private final VanDAO vanDAO = new VanDAO();
    private final MotoristaDAO motoristaDAO = new MotoristaDAO();

    private JTextField campoPlaca;
    private JTextField campoModelo;
    private JTextField campoCapacidade;
    private JComboBox<Motorista> comboMotorista;
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private Van vanSelecionada;

    public TelaVan() {
        setTitle("Cadastro de Van");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 480);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        add(criarFormulario(), BorderLayout.NORTH);
        add(criarTabela(), BorderLayout.CENTER);
        add(criarBotoes(), BorderLayout.SOUTH);

        atualizarCombo();
        atualizarTabela();
    }

    private JPanel criarFormulario() {
        JPanel painel = new JPanel(new GridLayout(4, 2, 8, 8));
        painel.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));

        campoPlaca = new JTextField();
        campoModelo = new JTextField();
        campoCapacidade = new JTextField();
        comboMotorista = new JComboBox<>();

        painel.add(new JLabel("Placa:"));
        painel.add(campoPlaca);
        painel.add(new JLabel("Modelo:"));
        painel.add(campoModelo);
        painel.add(new JLabel("Capacidade:"));
        painel.add(campoCapacidade);
        painel.add(new JLabel("Motorista responsável:"));
        painel.add(comboMotorista);

        return painel;
    }

    private JScrollPane criarTabela() {
        modeloTabela = new DefaultTableModel(new Object[]{"ID", "Placa", "Modelo", "Capacidade", "Motorista"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabela = new JTable(modeloTabela);
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                carregarSelecao();
            }
        });
        return new JScrollPane(tabela);
    }

    private JPanel criarBotoes() {
        JPanel painel = new JPanel();

        JButton btnIncluir = new JButton("Incluir");
        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnLimpar = new JButton("Limpar");
        JButton btnAtualizar = new JButton("Atualizar motoristas");

        btnIncluir.addActionListener(e -> incluir());
        btnEditar.addActionListener(e -> editar());
        btnExcluir.addActionListener(e -> excluir());
        btnLimpar.addActionListener(e -> limpar());
        btnAtualizar.addActionListener(e -> atualizarCombo());

        painel.add(btnIncluir);
        painel.add(btnEditar);
        painel.add(btnExcluir);
        painel.add(btnLimpar);
        painel.add(btnAtualizar);

        return painel;
    }

    private void incluir() {
        if (!validarCampos()) {
            return;
        }
        Van van = new Van(0, campoPlaca.getText().trim(), campoModelo.getText().trim(),
                Integer.parseInt(campoCapacidade.getText().trim()), (Motorista) comboMotorista.getSelectedItem());
        vanDAO.incluir(van);
        limpar();
        atualizarTabela();
    }

    private void editar() {
        if (vanSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma van na tabela.");
            return;
        }
        if (!validarCampos()) {
            return;
        }
        vanSelecionada.setPlaca(campoPlaca.getText().trim());
        vanSelecionada.setModelo(campoModelo.getText().trim());
        vanSelecionada.setCapacidade(Integer.parseInt(campoCapacidade.getText().trim()));
        vanSelecionada.setMotorista((Motorista) comboMotorista.getSelectedItem());
        vanDAO.atualizar(vanSelecionada);
        limpar();
        atualizarTabela();
    }

    private void excluir() {
        if (vanSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma van na tabela.");
            return;
        }
        vanDAO.excluir(vanSelecionada);
        limpar();
        atualizarTabela();
    }

    private void carregarSelecao() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            return;
        }
        int id = (int) modeloTabela.getValueAt(linha, 0);
        vanSelecionada = vanDAO.buscarPorId(id);
        if (vanSelecionada != null) {
            campoPlaca.setText(vanSelecionada.getPlaca());
            campoModelo.setText(vanSelecionada.getModelo());
            campoCapacidade.setText(String.valueOf(vanSelecionada.getCapacidade()));
            comboMotorista.setSelectedItem(vanSelecionada.getMotorista());
        }
    }

    private boolean validarCampos() {
        if (campoPlaca.getText().trim().isEmpty() || campoModelo.getText().trim().isEmpty()
                || campoCapacidade.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos.");
            return false;
        }
        try {
            Integer.parseInt(campoCapacidade.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Capacidade deve ser um número.");
            return false;
        }
        if (comboMotorista.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Cadastre um motorista antes.");
            return false;
        }
        return true;
    }

    private void limpar() {
        campoPlaca.setText("");
        campoModelo.setText("");
        campoCapacidade.setText("");
        vanSelecionada = null;
        tabela.clearSelection();
    }

    private void atualizarCombo() {
        comboMotorista.removeAllItems();
        for (Motorista m : motoristaDAO.listar()) {
            comboMotorista.addItem(m);
        }
    }

    private void atualizarTabela() {
        modeloTabela.setRowCount(0);
        for (Van v : vanDAO.listar()) {
            String motorista = v.getMotorista() != null ? v.getMotorista().getNome() : "";
            modeloTabela.addRow(new Object[]{v.getId(), v.getPlaca(), v.getModelo(), v.getCapacidade(), motorista});
        }
    }
}
