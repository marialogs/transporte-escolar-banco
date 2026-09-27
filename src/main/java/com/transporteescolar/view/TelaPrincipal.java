package com.transporteescolar.view;

import javax.swing.*;
import java.awt.*;

public class TelaPrincipal extends JFrame {

    public TelaPrincipal() {
        setTitle("Sistema de Transporte Escolar");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 380);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Transporte Escolar", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titulo, BorderLayout.NORTH);

        JPanel painelBotoes = new JPanel(new GridLayout(4, 1, 10, 10));
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(10, 60, 30, 60));

        JButton btnVan = new JButton("Cadastro de Van");
        JButton btnMotorista = new JButton("Cadastro de Motorista");
        JButton btnAluno = new JButton("Cadastro de Aluno");
        JButton btnRota = new JButton("Cadastro de Rota");

        btnVan.addActionListener(e -> new TelaVan().setVisible(true));
        btnMotorista.addActionListener(e -> new TelaMotorista().setVisible(true));
        btnAluno.addActionListener(e -> new TelaAluno().setVisible(true));
        btnRota.addActionListener(e -> new TelaRota().setVisible(true));

        painelBotoes.add(btnVan);
        painelBotoes.add(btnMotorista);
        painelBotoes.add(btnAluno);
        painelBotoes.add(btnRota);

        add(painelBotoes, BorderLayout.CENTER);
    }
}
