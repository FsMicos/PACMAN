package presentacion;

import logica.ArchivoManager;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Ventana que muestre las 10 puntuaciones más altas alcanzadas en
 * el juego
 */
public class Puntuacion extends JFrame {
    private File archivo = new File("res/puntuacion.txt");
    private ArrayList<String> datos;
    private JPanel panel;
    private JButton botonRegreso;
    private Font fuentePacMan;
    private MenuPacMan menu;
    private ArchivoManager archivoManager;

    public Puntuacion(MenuPacMan menu) {
        this.menu = menu;
        this.archivoManager = new ArchivoManager(archivo);
        datos = new ArrayList<>();
        this.panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setFont(fuentePacMan.deriveFont(18f));
                g2.setColor(Color.YELLOW);
                int anchoTexto = g2.getFontMetrics().stringWidth("PUNTUACIONES");
                g2.drawString("PUNTUACIONES", (getWidth() / 2) - (anchoTexto / 2), 36);
                g2.setColor(Color.white);
                int i = 1;
                for (String linea : datos) {
                    i += 2;
                    String[] lineaPartida = linea.split(" ");
                    g2.drawString((datos.indexOf(linea) + 1) + "." + lineaPartida[0], 58, 26 * i);
                    g2.drawString(lineaPartida[1], 296, 26 * i);
                }
            }
        };
        cargarFuente();
        this.datos = archivoManager.leerArchivo();
        ponerBotonMenu();
        panel.setBackground(Color.black);
        add(panel);
        setMinimumSize(new Dimension(440, 620));
        setResizable(false);//cambio de dimesiones de la pantalla
        setTitle("Puntuacion");
        setLocationRelativeTo(null); //ubique la pantalla en el centro
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void ponerBotonMenu() {
        botonRegreso = new JButton();
        botonRegreso.setFont(fuentePacMan.deriveFont(10f));
        botonRegreso.setText("Menu");
        botonRegreso.setForeground(Color.BLACK);
        botonRegreso.setBackground(new Color(69, 178, 245));
        botonRegreso.setBorder(new LineBorder(Color.BLACK));
        botonRegreso.setBounds(15, 15, 80, 30);
        botonRegreso.addActionListener(e -> {
            dispose();
            menu.setVisible(true);
            menu.reproducirSonidoEnLoop();
        });
        this.add(botonRegreso);
    }

    private void cargarFuente() {
        try {
            fuentePacMan = Font.createFont(Font.TRUETYPE_FONT, new File("res/pixel-nes.otf"));
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void mostrarPuntuaciones() {
        setVisible(true); //visible para el usuario
    }
}
