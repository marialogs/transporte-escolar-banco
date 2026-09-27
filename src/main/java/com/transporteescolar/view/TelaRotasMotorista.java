package com.transporteescolar.view;

import com.transporteescolar.dao.RotaDAO;
import com.transporteescolar.model.Motorista;
import com.transporteescolar.model.Rota;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class TelaRotasMotorista extends JFrame {

    private final RotaDAO rotaDAO = new RotaDAO();
    private final Motorista motoristaLogado;
    private DefaultListModel<Rota> modeloLista;
    private JList<Rota> listaRotas;

    public TelaRotasMotorista(Motorista motoristaLogado) {
        this.motoristaLogado = motoristaLogado;

        setTitle("Minhas Rotas - " + motoristaLogado.getNome());
        setSize(420, 380);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        add(new JLabel("Rotas vinculadas à sua van:", SwingConstants.CENTER), BorderLayout.NORTH);

        modeloLista = new DefaultListModel<>();
        listaRotas = new JList<>(modeloLista);
        add(new JScrollPane(listaRotas), BorderLayout.CENTER);

        JButton btnIniciarRota = new JButton("Iniciar Rota / Confirmar Presença");
        add(btnIniciarRota, BorderLayout.SOUTH);

        btnIniciarRota.addActionListener(e -> iniciarRota());

        atualizarLista();
        setVisible(true);
    }

    private void atualizarLista() {
        modeloLista.clear();
        List<Rota> minhasRotas = rotaDAO.listar().stream()
                .filter(r -> r.getVan() != null && r.getVan().getMotorista() != null
                        && r.getVan().getMotorista().getId() == motoristaLogado.getId())
                .collect(Collectors.toList());
        for (Rota rota : minhasRotas) {
            modeloLista.addElement(rota);
        }
    }

    private void iniciarRota() {
        Rota selecionada = listaRotas.getSelectedValue();
        if (selecionada == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma rota para iniciar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        new TelaEmbarque(selecionada);
    }
}
