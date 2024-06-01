package presentacion;

import logica.Laberinto;
import logica.Nivel;
import logica.Personaje;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Especialización de InterfazGrafica que se encarga de dibujar al Laberinto
 * y los items contenidos en este. Es la única interfazGrafica que puede ejecutarse como un Thread
 */
public class InterfazGraficaDeLaberinto extends InterfazGrafica implements Runnable {
    protected JPanel panelDelJuego;
    private Laberinto laberinto;
    private BufferedImage fruta;
    private BufferedImage[] muros;
    private boolean pausado;


    public InterfazGraficaDeLaberinto(JPanel panel) {
        super();
        panelDelJuego = panel;
        pausado = false;
        cargarFrutas();
        this.muros = new BufferedImage[14];
        getImagenLaberinto();
    }

    /**
     * Método que muestra las representaciones gráficas
     * del método dibujar 70 veces cada segundo segundos
     */
    @Override
    public void run() {
        while (true) {
            if (!pausado) {
                panelDelJuego.repaint();
            }
            Personaje.verificarTiempo();
        }
    }

    public void pausar() {
        pausado = true;
    }

    public void despausar() {
        pausado = false;
    }

    @Override
    public void setNivel(Nivel nivel) {
        laberinto = nivel.getLaberinto();
        this.nivel = nivel;
    }

    @Override
    public void dibujarSprite(Graphics2D g2) {
        int temporalX = 0;
        int temporaly = 0;
        int tamaño_entidad = Nivel.getTamañoEntidades();
        for (int i = 0; i < laberinto.getFilas(); i++) {
            for (int a = 0; a < laberinto.getColumnas(); a++) {
                if (laberinto.getValorMapa(i, a) == 0 || laberinto.getValorMapa(i, a) == 6 || laberinto.getValorMapa(i, a) == 8 || laberinto.getValorMapa(i, a) == 13
                        || laberinto.getValorMapa(i, a) == 14 || laberinto.getValorMapa(i, a) == 7) {
                    temporalX += tamaño_entidad;
                } else if (laberinto.getValorMapa(i, a) == 2 || laberinto.getValorMapa(i, a) == 9 || laberinto.getValorMapa(i, a) == 10
                        || laberinto.getValorMapa(i, a) == 11 || laberinto.getValorMapa(i, a) == 12) {
                    g2.setColor(Color.yellow);
                    g2.setStroke(new BasicStroke(2f));
                    g2.fillOval(temporalX + 9, temporaly + 9, 6, 6);
                    temporalX += tamaño_entidad;
                } else if (laberinto.getValorMapa(i, a) == 1) {
                    g2.drawImage(muros[0], temporalX, temporaly, Nivel.getTamañoEntidades(), Nivel.getTamañoEntidades() - 1, null);
                    temporalX += tamaño_entidad;
                } else if (laberinto.getValorMapa(i, a) == 15) {
                    g2.drawImage(muros[1], temporalX, temporaly, Nivel.getTamañoEntidades(), Nivel.getTamañoEntidades(), null);
                    temporalX += tamaño_entidad;
                } else if (laberinto.getValorMapa(i, a) == 16) {
                    g2.drawImage(muros[3], temporalX, temporaly, Nivel.getTamañoEntidades(), Nivel.getTamañoEntidades(), null);
                    temporalX += tamaño_entidad;
                } else if (laberinto.getValorMapa(i, a) == 17) {
                    g2.drawImage(muros[2], temporalX, temporaly, Nivel.getTamañoEntidades(), Nivel.getTamañoEntidades(), null);
                    temporalX += tamaño_entidad;
                } else if (laberinto.getValorMapa(i, a) == 18) {
                    g2.drawImage(muros[4], temporalX, temporaly, Nivel.getTamañoEntidades(), Nivel.getTamañoEntidades(), null);
                    temporalX += tamaño_entidad;
                } else if (laberinto.getValorMapa(i, a) == 19) {
                    g2.drawImage(muros[5], temporalX, temporaly, Nivel.getTamañoEntidades(), Nivel.getTamañoEntidades(), null);
                    temporalX += tamaño_entidad;
                } else if (laberinto.getValorMapa(i, a) == 20) {
                    g2.drawImage(muros[9], temporalX, temporaly, Nivel.getTamañoEntidades(), Nivel.getTamañoEntidades(), null);
                    temporalX += tamaño_entidad;
                } else if (laberinto.getValorMapa(i, a) == 21) {
                    g2.drawImage(muros[8], temporalX, temporaly, Nivel.getTamañoEntidades(), Nivel.getTamañoEntidades(), null);
                    temporalX += tamaño_entidad;
                } else if (laberinto.getValorMapa(i, a) == 22) {
                    g2.drawImage(muros[6], temporalX, temporaly, Nivel.getTamañoEntidades(), Nivel.getTamañoEntidades(), null);
                    temporalX += tamaño_entidad;
                } else if (laberinto.getValorMapa(i, a) == 23) {
                    g2.drawImage(muros[7], temporalX, temporaly, Nivel.getTamañoEntidades(), Nivel.getTamañoEntidades(), null);
                    temporalX += tamaño_entidad;
                } else if (laberinto.getValorMapa(i, a) == 24) {
                    g2.drawImage(muros[10], temporalX, temporaly, Nivel.getTamañoEntidades(), Nivel.getTamañoEntidades(), null);
                    temporalX += tamaño_entidad;
                } else if (laberinto.getValorMapa(i, a) == 25) {
                    g2.drawImage(muros[11], temporalX, temporaly, Nivel.getTamañoEntidades(), Nivel.getTamañoEntidades(), null);
                    temporalX += tamaño_entidad;
                } else if (laberinto.getValorMapa(i, a) == 26) {
                    g2.drawImage(muros[12], temporalX, temporaly, Nivel.getTamañoEntidades(), Nivel.getTamañoEntidades(), null);
                    temporalX += tamaño_entidad;
                } else if (laberinto.getValorMapa(i, a) == 27) {
                    g2.drawImage(muros[13], temporalX, temporaly, Nivel.getTamañoEntidades(), Nivel.getTamañoEntidades(), null);
                    temporalX += tamaño_entidad;
                } else if (laberinto.getValorMapa(i, a) == 3) {
                    g2.setColor(Color.WHITE);
                    g2.setStroke(new BasicStroke(6f));
                    g2.fillOval(temporalX + 7, temporaly + 7, 10, 10);
                    temporalX += tamaño_entidad;
                } else if (laberinto.getValorMapa(i, a) == 4) {
                    g2.drawImage(fruta, temporalX, temporaly, Nivel.getTamañoEntidades(), Nivel.getTamañoEntidades() - 5, null);
                    temporalX += tamaño_entidad;
                }
            }
            temporalX = 0;
            temporaly += tamaño_entidad;
        }
    }

    public void cargarFrutas() {
        String direcciónImagen = "manzana";
        try {
            this.fruta = ImageIO.read(getClass().getResourceAsStream("/res/frutas/" + direcciónImagen + ".png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void getImagenLaberinto() {
        String direccionImagen = "laberinto";
        try {
            muros[0] = ImageIO.read(getClass().getResourceAsStream("/res/" + direccionImagen + "/normal_x.png"));
            muros[1] = ImageIO.read(getClass().getResourceAsStream("/res/" + direccionImagen + "/normal_y.png"));
            muros[2] = ImageIO.read(getClass().getResourceAsStream("/res/" + direccionImagen + "/arista_derecha.png"));
            muros[3] = ImageIO.read(getClass().getResourceAsStream("/res/" + direccionImagen + "/arista_izquierda.png"));
            muros[4] = ImageIO.read(getClass().getResourceAsStream("/res/" + direccionImagen + "/arista_abajoI.png"));
            muros[5] = ImageIO.read(getClass().getResourceAsStream("/res/" + direccionImagen + "/arista_abajoD.png"));
            muros[6] = ImageIO.read(getClass().getResourceAsStream("/res/" + direccionImagen + "/esquina_abajo.png"));
            muros[7] = ImageIO.read(getClass().getResourceAsStream("/res/" + direccionImagen + "/esquina_arriba.png"));
            muros[8] = ImageIO.read(getClass().getResourceAsStream("/res/" + direccionImagen + "/esquina_izquierda.png"));
            muros[9] = ImageIO.read(getClass().getResourceAsStream("/res/" + direccionImagen + "/esquina_derecha.png"));
            muros[10] = ImageIO.read(getClass().getResourceAsStream("/res/" + direccionImagen + "/arriba.png"));
            muros[11] = ImageIO.read(getClass().getResourceAsStream("/res/" + direccionImagen + "/abajo.png"));
            muros[12] = ImageIO.read(getClass().getResourceAsStream("/res/" + direccionImagen + "/izquierda.png"));
            muros[13] = ImageIO.read(getClass().getResourceAsStream("/res/" + direccionImagen + "/derecha.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
