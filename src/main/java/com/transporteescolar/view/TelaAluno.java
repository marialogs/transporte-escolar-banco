package com.transporteescolar.view;

import com.transporteescolar.dao.AlunoDAO;
import com.transporteescolar.model.Aluno;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TelaAluno extends JFrame {

    private final AlunoDAO alunoDAO = new AlunoDAO();

    private JTextField campoNome;
    private JTextField campoEndereco;
    private JTextField campoEscola;
    private JTextField campoResponsavel;
    private JTextField campoTelefone;
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private Aluno alunoSelecionado;

    public TelaAluno() {
        setTitle("Cadastro de Aluno");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(640, 500);
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
        campoEndereco = new JTextField();
        campoEscola = new JTextField();
        campoResponsavel = new JTextField();
        campoTelefone = new JTextField();

        painel.add(new JLabel("Nome:"));
        painel.add(campoNome);
        painel.add(new JLabel("Endereço:"));
        painel.add(campoEndereco);
        painel.add(new JLabel("Escola:"));
        painel.add(campoEscola);
        painel.add(new JLabel("Responsável:"));
        painel.add(campoResponsavel);
        painel.add(new JLabel("Telefone:"));
        painel.add(campoTelefone);

        return painel;
    }

    private JScrollPane criarTabela() {
        modeloTabela = new DefaultTableModel(
                new Object[]{"ID", "Nome", "Endereço", "Escola", "Responsável", "Telefone"}, 0) {
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

        btnIncluir.addActionListener(e -> incluir());
        btnEditar.addActionListener(e -> editar());
        btnExcluir.addActionListener(e -> excluir());
        btnLimpar.addActionListener(e -> limpar());

        painel.add(btnIncluir);
        painel.add(btnEditar);
        painel.add(btnExcluir);
        painel.add(btnLimpar);

        return painel;
    }

    private void incluir() {
        if (!validarCampos()) {
            return;
        }
        Aluno aluno = new Aluno(0, campoNome.getText().trim(), campoEndereco.getText().trim(),
                campoEscola.getText().trim(), campoResponsavel.getText().trim(), campoTelefone.getText().trim());
        alunoDAO.incluir(aluno);
        limpar();
        atualizarTabela();
    }

    private void editar() {
        if (alunoSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um aluno na tabela.");
            return;
        }
        if (!validarCampos()) {
            return;
        }
        alunoSelecionado.setNome(campoNome.getText().trim());
        alunoSelecionado.setEndereco(campoEndereco.getText().trim());
        alunoSelecionado.setEscola(campoEscola.getText().trim());
        alunoSelecionado.setResponsavel(campoResponsavel.getText().trim());
        alunoSelecionado.setTelefone(campoTelefone.getText().trim());
        alunoDAO.atualizar(alunoSelecionado);
        limpar();
        atualizarTabela();
    }

    private void excluir() {
        if (alunoSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um aluno na tabela.");
            return;
        }
        alunoDAO.excluir(alunoSelecionado);
        limpar();
        atualizarTabela();
    }

    private void carregarSelecao() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            return;
        }
        int id = (int) modeloTabela.getValueAt(linha, 0);
        alunoSelecionado = alunoDAO.buscarPorId(id);
        if (alunoSelecionado != null) {
            campoNome.setText(alunoSelecionado.getNome());
            campoEndereco.setText(alunoSelecionado.getEndereco());
            campoEscola.setText(alunoSelecionado.getEscola());
            campoResponsavel.setText(alunoSelecionado.getResponsavel());
            campoTelefone.setText(alunoSelecionado.getTelefone());
        }
    }

    private boolean validarCampos() {
        if (campoNome.getText().trim().isEmpty() || campoEndereco.getText().trim().isEmpty()
                || campoEscola.getText().trim().isEmpty() || campoResponsavel.getText().trim().isEmpty()
                || campoTelefone.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos.");
            return false;
        }
        return true;
    }

    private void limpar() {
        campoNome.setText("");
        campoEndereco.setText("");
        campoEscola.setText("");
        campoResponsavel.setText("");
        campoTelefone.setText("");
        alunoSelecionado = null;
        tabela.clearSelection();
    }

    private void atualizarTabela() {
        modeloTabela.setRowCount(0);
        for (Aluno a : alunoDAO.listar()) {
            modeloTabela.addRow(new Object[]{a.getId(), a.getNome(), a.getEndereco(),
                    a.getEscola(), a.getResponsavel(), a.getTelefone()});
        }
    }
}
