package com.transporteescolar;

import com.transporteescolar.dao.Conexao;
import com.transporteescolar.view.TelaLogin;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        Conexao.criarTabelas();
        SwingUtilities.invokeLater(TelaLogin::new);
    }
}
