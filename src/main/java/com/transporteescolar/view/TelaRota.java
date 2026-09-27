package com.transporteescolar.view;

import com.transporteescolar.dao.AlunoDAO;
import com.transporteescolar.dao.RotaDAO;
import com.transporteescolar.dao.VanDAO;
import com.transporteescolar.model.Aluno;
import com.transporteescolar.model.Rota;
import com.transporteescolar.model.Van;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.stream.Collectors;

public class TelaRota extends JFrame {

    private final RotaDAO rotaDAO = new RotaDAO();
    private final VanDAO vanDAO = new VanDAO();
    private final AlunoDAO alunoDAO = new AlunoDAO();

    private JTextField campoNome;
    private JTextField campoHorario;
    private JComboBox<Van> comboVan;
    private JList<Aluno> listaAlunos;
    private DefaultListModel<Aluno> modeloListaAlunos;
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private Rota rotaSelecionada;

    public TelaRota() {
        setTitle("Cadastro de Rota");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(640, 560);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        add(criarFormulario(), BorderLayout.NORTH);
        add(criarTabela(), BorderLayout.CENTER);
        add(criarBotoes(), BorderLayout.SOUTH);

        atualizarCombosELista();
        atualizarTabela();
    }

    private JPanel criarFormulario() {
        JPanel painel = new JPanel(new BorderLayout(8, 8));
        painel.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));

        JPanel campos = new JPanel(new GridLayout(3, 2, 8, 8));
        campoNome = new JTextField();
        campoHorario = new JTextField();
        comboVan = new JComboBox<>();

        campos.add(new JLabel("Nome/Descrição:"));
        campos.add(campoNome);
        campos.add(new JLabel("Horário:"));
        campos.add(campoHorario);
        campos.add(new JLabel("Van:"));
        campos.add(comboVan);

        painel.add(campos, BorderLayout.NORTH);

        modeloListaAlunos = new DefaultListModel<>();
        listaAlunos = new JList<>(modeloListaAlunos);
        listaAlunos.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listaAlunos.setVisibleRowCount(4);
        JPanel painelAlunos = new JPanel(new BorderLayout(4, 4));
        painelAlunos.add(new JLabel("Alunos (Ctrl+clique para selecionar vários):"), BorderLayout.NORTH);
        painelAlunos.add(new JScrollPane(listaAlunos), BorderLayout.CENTER);
        painel.add(painelAlunos, BorderLayout.CENTER);

        return painel;
    }

    private JScrollPane criarTabela() {
        modeloTabela = new DefaultTableModel(new Object[]{"ID", "Nome", "Horário", "Van", "Alunos"}, 0) {
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
        JButton btnAtualizar = new JButton("Atualizar listas");

        btnIncluir.addActionListener(e -> incluir());
        btnEditar.addActionListener(e -> editar());
        btnExcluir.addActionListener(e -> excluir());
        btnLimpar.addActionListener(e -> limpar());
        btnAtualizar.addActionListener(e -> atualizarCombosELista());

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
        Rota rota = new Rota(0, campoNome.getText().trim(), campoHorario.getText().trim(),
                (Van) comboVan.getSelectedItem(), listaAlunos.getSelectedValuesList());
        rotaDAO.incluir(rota);
        limpar();
        atualizarTabela();
    }

    private void editar() {
        if (rotaSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma rota na tabela.");
            return;
        }
        if (!validarCampos()) {
            return;
        }
        rotaSelecionada.setNome(campoNome.getText().trim());
        rotaSelecionada.setHorario(campoHorario.getText().trim());
        rotaSelecionada.setVan((Van) comboVan.getSelectedItem());
        rotaSelecionada.setAlunos(listaAlunos.getSelectedValuesList());
        rotaDAO.atualizar(rotaSelecionada);
        limpar();
        atualizarTabela();
    }

    private void excluir() {
        if (rotaSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma rota na tabela.");
            return;
        }
        rotaDAO.excluir(rotaSelecionada);
        limpar();
        atualizarTabela();
    }

    private void carregarSelecao() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            return;
        }
        int id = (int) modeloTabela.getValueAt(linha, 0);
        rotaSelecionada = rotaDAO.listar().stream().filter(r -> r.getId() == id).findFirst().orElse(null);
        if (rotaSelecionada != null) {
            campoNome.setText(rotaSelecionada.getNome());
            campoHorario.setText(rotaSelecionada.getHorario());
            comboVan.setSelectedItem(rotaSelecionada.getVan());

            listaAlunos.clearSelection();
            for (Aluno aluno : rotaSelecionada.getAlunos()) {
                for (int i = 0; i < modeloListaAlunos.size(); i++) {
                    if (modeloListaAlunos.get(i).getId() == aluno.getId()) {
                        listaAlunos.addSelectionInterval(i, i);
                    }
                }
            }
        }
    }

    private boolean validarCampos() {
        if (campoNome.getText().trim().isEmpty() || campoHorario.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha o nome e o horário da rota.");
            return false;
        }
        if (comboVan.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Cadastre uma van antes.");
            return false;
        }
        return true;
    }

    private void limpar() {
        campoNome.setText("");
        campoHorario.setText("");
        listaAlunos.clearSelection();
        rotaSelecionada = null;
        tabela.clearSelection();
    }

    private void atualizarCombosELista() {
        comboVan.removeAllItems();
        for (Van v : vanDAO.listar()) {
            comboVan.addItem(v);
        }
        modeloListaAlunos.clear();
        for (Aluno a : alunoDAO.listar()) {
            modeloListaAlunos.addElement(a);
        }
    }

    private void atualizarTabela() {
        modeloTabela.setRowCount(0);
        for (Rota r : rotaDAO.listar()) {
            String van = r.getVan() != null ? r.getVan().getPlaca() : "";
            String alunos = r.getAlunos().stream().map(Aluno::getNome).collect(Collectors.joining(", "));
            modeloTabela.addRow(new Object[]{r.getId(), r.getNome(), r.getHorario(), van, alunos});
        }
    }
}
