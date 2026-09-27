package com.transporteescolar.view;

import com.transporteescolar.model.Aluno;
import com.transporteescolar.model.Rota;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TelaEmbarque extends JFrame {

    private final Rota rota;
    private final JPanel painelAlunos = new JPanel();

    public TelaEmbarque(Rota rota) {
        this.rota = rota;

        setTitle("Confirmar Embarque");
        setSize(400, 380);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        JLabel lblRota = new JLabel("Rota: " + rota.getNome() + " - " + rota.getHorario(), SwingConstants.CENTER);
        add(lblRota, BorderLayout.NORTH);

        painelAlunos.setLayout(new BoxLayout(painelAlunos, BoxLayout.Y_AXIS));
        List<Aluno> alunos = rota.getAlunos();
        for (Aluno aluno : alunos) {
            painelAlunos.add(new JCheckBox(aluno.getNome() + " (" + aluno.getResponsavel() + ")"));
        }
        if (alunos.isEmpty()) {
            painelAlunos.add(new JLabel("Nenhum aluno vinculado a esta rota."));
        }
        add(new JScrollPane(painelAlunos), BorderLayout.CENTER);

        JButton btnConfirmar = new JButton("Confirmar Embarque");
        add(btnConfirmar, BorderLayout.SOUTH);
        btnConfirmar.addActionListener(e -> confirmarEmbarque());

        setVisible(true);
    }

    private void confirmarEmbarque() {
        int presentes = 0;
        for (Component c : painelAlunos.getComponents()) {
            if (c instanceof JCheckBox checkBox && checkBox.isSelected()) {
                presentes++;
            }
        }

        JOptionPane.showMessageDialog(this,
                "Rota " + rota.getNome() + " iniciada!\n" + presentes + " aluno(s) confirmado(s) na van.");
        dispose();
    }
}
