package com.transporteescolar.view;

import com.transporteescolar.dao.MotoristaDAO;
import com.transporteescolar.model.Motorista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TelaMotorista extends JFrame {

    private final MotoristaDAO motoristaDAO = new MotoristaDAO();

    private JTextField campoNome;
    private JTextField campoCpf;
    private JTextField campoCnh;
    private JTextField campoLogin;
    private JPasswordField campoSenha;
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private Motorista motoristaSelecionado;

    public TelaMotorista() {
        setTitle("Cadastro de Motorista");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(560, 520);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        add(criarFormulario(), BorderLayout.NORTH);
        add(criarTabela(), BorderLayout.CENTER);
        add(criarBotoes(), BorderLayout.SOUTH);

        atualizarTabela();
    }

    private JPanel criarFormulario() {
        JPanel painel = new JPanel(new GridLayout(5, 2, 8, 8));
        painel.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));

        campoNome = new JTextField();
        campoCpf = new JTextField();
        campoCnh = new JTextField();
        campoLogin = new JTextField();
        campoSenha = new JPasswordField();

        painel.add(new JLabel("Nome:"));
        painel.add(campoNome);
        painel.add(new JLabel("CPF:"));
        painel.add(campoCpf);
        painel.add(new JLabel("CNH:"));
        painel.add(campoCnh);
        painel.add(new JLabel("Login:"));
        painel.add(campoLogin);
        painel.add(new JLabel("Senha:"));
        painel.add(campoSenha);

        return painel;
    }

    private JScrollPane criarTabela() {
        modeloTabela = new DefaultTableModel(new Object[]{"ID", "Nome", "CPF", "CNH", "Login"}, 0) {
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

        JButton btnSalvar = new JButton("Incluir");
        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnLimpar = new JButton("Limpar");

        btnSalvar.addActionListener(e -> incluir());
        btnEditar.addActionListener(e -> editar());
        btnExcluir.addActionListener(e -> excluir());
        btnLimpar.addActionListener(e -> limpar());

        painel.add(btnSalvar);
        painel.add(btnEditar);
        painel.add(btnExcluir);
        painel.add(btnLimpar);

        return painel;
    }

    private void incluir() {
        if (!validarCampos()) {
            return;
        }
        try {
            Motorista motorista = new Motorista(0, campoNome.getText().trim(), campoCpf.getText().trim(),
                    campoCnh.getText().trim(), campoLogin.getText().trim(), new String(campoSenha.getPassword()));
            motoristaDAO.incluir(motorista);
            limpar();
            atualizarTabela();
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editar() {
        if (motoristaSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um motorista na tabela.");
            return;
        }
        if (!validarCampos()) {
            return;
        }
        motoristaSelecionado.setNome(campoNome.getText().trim());
        motoristaSelecionado.setCpf(campoCpf.getText().trim());
        motoristaSelecionado.setCnh(campoCnh.getText().trim());
        motoristaSelecionado.setLogin(campoLogin.getText().trim());
        motoristaSelecionado.setSenha(new String(campoSenha.getPassword()));
        motoristaDAO.atualizar(motoristaSelecionado);
        limpar();
        atualizarTabela();
    }

    private void excluir() {
        if (motoristaSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um motorista na tabela.");
            return;
        }
        motoristaDAO.excluir(motoristaSelecionado);
        limpar();
        atualizarTabela();
    }

    private void carregarSelecao() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            return;
        }
        int id = (int) modeloTabela.getValueAt(linha, 0);
        motoristaSelecionado = motoristaDAO.buscarPorId(id);
        if (motoristaSelecionado != null) {
            campoNome.setText(motoristaSelecionado.getNome());
            campoCpf.setText(motoristaSelecionado.getCpf());
            campoCnh.setText(motoristaSelecionado.getCnh());
            campoLogin.setText(motoristaSelecionado.getLogin());
            campoSenha.setText(motoristaSelecionado.getSenha());
        }
    }

    private boolean validarCampos() {
        if (campoNome.getText().trim().isEmpty() || campoCpf.getText().trim().isEmpty()
                || campoCnh.getText().trim().isEmpty() || campoLogin.getText().trim().isEmpty()
                || campoSenha.getPassword().length == 0) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos, incluindo login e senha.");
            return false;
        }
        return true;
    }

    private void limpar() {
        campoNome.setText("");
        campoCpf.setText("");
        campoCnh.setText("");
        campoLogin.setText("");
        campoSenha.setText("");
        motoristaSelecionado = null;
        tabela.clearSelection();
    }

    private void atualizarTabela() {
        modeloTabela.setRowCount(0);
        for (Motorista m : motoristaDAO.listar()) {
            modeloTabela.addRow(new Object[]{m.getId(), m.getNome(), m.getCpf(), m.getCnh(), m.getLogin()});
        }
    }
}
