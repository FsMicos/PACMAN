package presentacion;

import logica.ArchivoManager;
import logica.Nivel;
import logica.PacMan;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Ventana que conecta el apartado gráfico del juego con el lógico y que permite
 * guardar una partida, cargarla o reiniciar los niveles
 */
public class MenuPacMan extends JFrame {
    private final Sonido sonido;
    private final PacMan pacman;
    private JButton botonStart, botonScore, botonGuardar, botonCargar, botonReset;
    private final JFrame nivelGrafico;
    private final ContenedorPacMan contenedorPacMan;
    private final ArrayList<Nivel> niveles;
    private Nivel nivelActual;

    public MenuPacMan(PacMan pacman, Dimension dimension) {
        this.pacman = pacman;
        niveles = new ArrayList<>();
        sonido = new Sonido();
        nivelGrafico = new JFrame();
        contenedorPacMan = new ContenedorPacMan(this);
        agregarNiveles();
        configurarNivelGrafico();
        nivelGrafico.add(contenedorPacMan);
        sonido.obtenerSonidos(Sonidos.MENU);
        setMinimumSize(dimension);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("PacMan");
        setLocationRelativeTo(null);
        setLayout(null);
        reproducirSonidoEnLoop();
        colocarBotones();
        setVisible(true);
    }

    private void configurarNivelGrafico() {
        nivelGrafico.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        nivelGrafico.setResizable(false);
    }

    private void colocarBotones() {
        Font fuentePacMan;
        try {
            fuentePacMan = Font.createFont(Font.TRUETYPE_FONT, new File("res/pixel-nes.otf"));
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // BOTON START
        botonStart = new JButton();
        botonStart.setBounds((getWidth() / 2) - 200, 200, 150, 62);
        configurarBoton(botonStart, "boton-de-inicio", 150);
        this.add(botonStart);
        // BOTON SCORE
        botonScore = new JButton();
        botonScore.setBounds((getWidth() / 2) + 50, 178, 100, 100);
        configurarBoton(botonScore, "score", 100);
        this.add(botonScore);
        // Boton guardar partida
        botonGuardar = new JButton();
        botonGuardar.setBounds((getWidth() / 2) - 250, 300, 100, 30);
        botonGuardar.setFont(fuentePacMan.deriveFont(12f));
        botonGuardar.setText("Guardar");
        botonGuardar.setForeground(Color.BLACK);
        botonGuardar.setBackground(new Color(245, 227, 35));
        botonGuardar.setBorder(new LineBorder(Color.BLACK));
        this.add(botonGuardar);
        // Boton cargar partida
        botonCargar = new JButton();
        botonCargar.setBounds((getWidth() / 2) - 50, 300, 100, 30);
        botonCargar.setFont(fuentePacMan.deriveFont(12f));
        botonCargar.setText("Cargar");
        botonCargar.setForeground(Color.BLACK);
        botonCargar.setBackground(new Color(245, 203, 98));
        botonCargar.setBorder(new LineBorder(Color.BLACK));
        this.add(botonCargar);
        // Boton reiniciar Juego
        botonReset = new JButton();
        botonReset.setBounds((getWidth() / 2) + 150, 300, 100, 30);
        botonReset.setFont(fuentePacMan.deriveFont(12f));
        botonReset.setText("Reset");
        botonReset.setForeground(Color.BLACK);
        botonReset.setBackground(new Color(69, 178, 245));
        botonReset.setBorder(new LineBorder(Color.BLACK));
        this.add(botonReset);
        colocarEtiquetas();
        generarEventosBotones();
    }

    private Nivel getNivelActual() {
        if (nivelActual == null) {
            for (Nivel nivel : niveles) {
                if (!nivel.estáCompletado()) {
                    return nivel;
                }
            }
            return null;
        }
        return nivelActual.getSiguienteNivel();
    }

    private void configurarBoton(JButton boton, String url, int medida) {
        ImageIcon imagen = obtenerImagen(url);
        boton.setIcon(new ImageIcon(imagen.getImage().getScaledInstance(medida, medida, Image.SCALE_DEFAULT)));
        boton.setContentAreaFilled(false);
        boton.setFocusPainted(false);
        boton.setBorder(null);
        ImageIcon imagenOff = obtenerImagen(url + "Off");
        boton.setPressedIcon(new ImageIcon(imagenOff.getImage().getScaledInstance(medida, medida, Image.SCALE_DEFAULT)));
    }

    private ImageIcon obtenerImagen(String urlImagen) {
        try {
            return new ImageIcon(getClass().getResourceAsStream("/res/imagenesMenu/" + urlImagen + ".png").readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Metodo que se ejecuta con el boton start
     * se agrega un escuchador de acciones
     */
    public int obtenerAncho(Nivel nivel) {
        return Nivel.getTamañoEntidades() * nivel.getLaberinto().getColumnas();
    }

    public int obtenerAlto(Nivel nivel) {
        return Nivel.getTamañoEntidades() * nivel.getLaberinto().getFilas();
    }

    public void generarEventosBotones() {
        MenuPacMan menu = this;
        botonStart.addActionListener(e -> {
            if (nivelActual != null && !nivelActual.estáCompletado()) {
                pararSonido();
                contenedorPacMan.reanudar();
                dispose();
                return;
            }
            nivelActual = getNivelActual();
            if (nivelActual == null) {
                return;
            }
            pararSonido();
            administrarNivel(nivelActual);
            dispose();
        });
        botonScore.addActionListener(e -> {
            pararSonido();
            Puntuacion puntuacion1 = new Puntuacion(menu);
            puntuacion1.mostrarPuntuaciones();
            dispose();
        });
        botonGuardar.addActionListener(e -> {
            if (nivelActual == null) {
                return;
            }
            ArchivoManager.guardarNivel(nivelActual);
        });
        botonCargar.addActionListener(e -> {
            if (nivelActual != null) {
                nivelActual.terminar();
            }
            nivelActual = ArchivoManager.cargarNivel();
            administrarNivel(nivelActual);
            dispose();
            pararSonido();
        });
        botonReset.addActionListener(e -> {
            if (nivelActual != null) {
                nivelActual.terminar();
            }
            this.nivelActual = null;
            pacman.reiniciarNiveles();
            agregarNiveles();
        });
    }

    private void agregarNiveles() {
        niveles.clear();
        for (Nivel nivel : pacman.getNiveles()) {
            niveles.add(nivel);
        }
    }

    public void administrarNivel(Nivel nivel) {
        contenedorPacMan.setPreferredSize(new Dimension(obtenerAncho(nivel), obtenerAlto(nivel)));
        contenedorPacMan.configurarBoton();
        nivelGrafico.pack();
        contenedorPacMan.iniciar(nivel);
    }


    /**
     * Metodo que se inicia desde el constructor
     * Anade imagenes y se ayuda de librerias de java
     */
    private void colocarEtiquetas() {
        //Esta es la imagen del Logo que dice Pac-Man
        ImageIcon imagenLogo = obtenerImagen("logo.pac_man");
        JLabel etiqueta = new JLabel(imagenLogo);
        etiqueta.setBounds(148, 0, 400, 213);
        etiqueta.setIcon(new ImageIcon(imagenLogo.getImage().getScaledInstance(400, 213, Image.SCALE_DEFAULT)));
        this.add(etiqueta);
        //Esta es la imagen del fondo
        ImageIcon imagenFondo = obtenerImagen("pacman2");
        JLabel etiqueta2 = new JLabel(imagenFondo);
        etiqueta2.setBounds(0, 0, 700, 400);
        etiqueta2.setIcon(new ImageIcon(imagenFondo.getImage().getScaledInstance(700, 400, Image.SCALE_DEFAULT)));
        this.add(etiqueta2);
    }

    public void reproducirSonidoEnLoop() {
        sonido.correrEnLoop();
    }

    public void pararSonido() {
        sonido.parar();
    }

    public void reconfigurar() {
        setVisible(true);
        reproducirSonidoEnLoop();
    }


}