package presentacion;

import logica.Nivel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * Panel donde se ejecutan tanto los hilos lógicos como el hilo gráfico de un
 * determinado nivel
 */
public class ContenedorPacMan extends JPanel {
    private final Thread hiloGrafico;
    private InterfazGraficaDeFantasma interfazGraficaBlinky, interfazGraficaPinky, interfazGraficaInky, interfazGraficaClyde;
    private InterfazGrafica interfazGraficaJugador, interfazGraficaMarco;
    private InterfazGraficaDeLaberinto interfazGraficaLaberinto;
    private Nivel nivel;
    private final MenuPacMan menu;
    private final DetectorTeclas controles;
    private final JButton botonRegreso;

    public ContenedorPacMan(MenuPacMan menu) {
        this.menu = menu;
        controles = new DetectorTeclas();
        botonRegreso = new JButton();
        addKeyListener(controles);
        setBackground(Color.black);
        interfazGraficaLaberinto = new InterfazGraficaDeLaberinto(this);
        pausarInterfaz();
        hiloGrafico = new Thread(interfazGraficaLaberinto);
        interfazGraficaJugador = new InterfazGraficaDeJugador();
        interfazGraficaBlinky = new InterfazGraficaDeFantasma("pochita");
        interfazGraficaClyde = new InterfazGraficaDeFantasma("max");
        interfazGraficaPinky = new InterfazGraficaDeFantasma("benji");
        interfazGraficaInky = new InterfazGraficaDeFantasma("doge");
        interfazGraficaMarco = new InterfazGraficaDelMarco();
        hiloGrafico.start();
    }

    public void iniciar(Nivel nivel) {
        this.nivel = nivel;
        interfazGraficaLaberinto.setNivel(nivel);
        interfazGraficaJugador.setNivel(nivel);
        interfazGraficaBlinky.setFantasma(nivel.getBlinky());
        interfazGraficaClyde.setFantasma(nivel.getClyde());
        interfazGraficaPinky.setFantasma(nivel.getPinky());
        interfazGraficaInky.setFantasma(nivel.getInky());
        interfazGraficaMarco.setNivel(nivel);
        setFocusable(true);
        despausarInterfaz();
        nivel.iniciarNivel(controles);
        despausarInterfaz();
        SwingUtilities.getWindowAncestor(this).setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        interfazGraficaBlinky.actualizar();
        interfazGraficaPinky.actualizar();
        interfazGraficaInky.actualizar();
        interfazGraficaClyde.actualizar();
        interfazGraficaLaberinto.dibujarSprite(g2);
        interfazGraficaJugador.dibujarSprite(g2);
        interfazGraficaBlinky.dibujarSprite(g2);
        interfazGraficaClyde.dibujarSprite(g2);
        interfazGraficaPinky.dibujarSprite(g2);
        interfazGraficaInky.dibujarSprite(g2);
        interfazGraficaMarco.dibujarSprite(g2);
    }

    public void configurarBoton() {
        Font fuentePacMan;
        try {
            fuentePacMan = Font.createFont(Font.TRUETYPE_FONT, new File("res/pixel-nes.otf"));
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ContenedorPacMan contenedorPacMan = this;
        setLayout(null);
        botonRegreso.setBounds(600, 15, 80, 20);
        botonRegreso.setFont(fuentePacMan.deriveFont(12f));
        botonRegreso.setText("Menu");
        botonRegreso.setBackground(new Color(245, 227, 35));
        botonRegreso.setBorder(new LineBorder(Color.BLACK));
        botonRegreso.addActionListener(e -> {
            pausarInterfaz();
            nivel.detener();
            SwingUtilities.getWindowAncestor(contenedorPacMan).dispose();
            menu.reconfigurar();
        });
        add(botonRegreso);
    }

    public void reanudar() {
        setFocusable(true);
        SwingUtilities.getWindowAncestor(this).setVisible(true);
        interfazGraficaLaberinto.despausar();
    }

    public void despausarInterfaz() {
        interfazGraficaLaberinto.despausar();
    }

    public void pausarInterfaz() {
        interfazGraficaLaberinto.pausar();
    }
}
