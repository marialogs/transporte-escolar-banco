package com.transporteescolar.view;

import com.transporteescolar.dao.MotoristaDAO;
import com.transporteescolar.model.Motorista;

import javax.swing.*;
import java.awt.*;

public class TelaLogin extends JFrame {

    private final MotoristaDAO motoristaDAO = new MotoristaDAO();

    private JTextField campoUsuario;
    private JPasswordField campoSenha;

    public TelaLogin() {
        setTitle("Login - Transporte Escolar");
        setSize(360, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Usuário:"), gbc);

        campoUsuario = new JTextField(15);
        gbc.gridx = 1;
        add(campoUsuario, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Senha:"), gbc);

        campoSenha = new JPasswordField(15);
        gbc.gridx = 1;
        add(campoSenha, gbc);

        JButton btnEntrar = new JButton("Entrar");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(btnEntrar, gbc);

        btnEntrar.addActionListener(e -> autenticar());

        setVisible(true);
    }

    private void autenticar() {
        String usuario = campoUsuario.getText().trim();
        String senha = new String(campoSenha.getPassword());

        if (usuario.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe usuário e senha.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (usuario.equalsIgnoreCase("admin") && senha.equals("admin")) {
            JOptionPane.showMessageDialog(this, "Bem-vindo(a), Administrador!");
            dispose();
            new TelaPrincipal().setVisible(true);
            return;
        }

        Motorista motorista = motoristaDAO.autenticar(usuario, senha);

        if (motorista != null) {
            JOptionPane.showMessageDialog(this, "Bem-vindo(a), " + motorista.getNome() + "!");
            dispose();
            new TelaRotasMotorista(motorista);
            return;
        }

        JOptionPane.showMessageDialog(this, "Usuário ou senha inválidos.", "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
