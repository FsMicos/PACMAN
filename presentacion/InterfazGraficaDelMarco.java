package presentacion;

import logica.Jugador;
import logica.Laberinto;
import logica.Nivel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Especialización de InterfazGrafica que se encarga de dibujar en pantalla la puntuación
 * actual del jugador, el highscore, las vidas y texto
 */
public class InterfazGraficaDelMarco extends InterfazGrafica {
    private Jugador jugador;
    private Laberinto laberinto;
    private BufferedImage imagenPacmanSkin;
    private Font fuentePacMan;

    public InterfazGraficaDelMarco() {
        super();
        cargarImagenes();
        cargarFuente();
    }

    private void cargarImagenes() {
        try {
            imagenPacmanSkin = ImageIO.read(getClass().getResourceAsStream("/res/pacman/pacman_derecha_1.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    @Override
    public void dibujarSprite(Graphics2D g2) {
        g2.setFont(fuentePacMan.deriveFont(18f));
        g2.setColor(Color.white);
        g2.drawString(String.valueOf(jugador.puntaje), 12, 40);
        g2.drawString("NOTA BECADO", ((laberinto.getColumnas() - 6) * 24) / 2, 20);
        g2.drawString(String.valueOf(jugador.highScore), ((laberinto.getColumnas() - 2) * 24) / 2, 40);
        if (jugador.isEstáEsperando() && jugador.getVidas() > 0) {
            g2.setColor(Color.YELLOW);
            g2.drawString("COMIENZA EL SEMESTRE", laberinto.getMensajeDeReadyCol() - 94, laberinto.getMensajeDeReadyFil());
        } else if (jugador.getVidas() == 0) {
            g2.setColor(Color.RED);
            g2.drawString("TE JALASTE EL SEMESTRE", laberinto.getMensajeDeReadyCol() - 94, laberinto.getMensajeDeReadyFil());
        } else if (jugador.comióTodosLosPacDots()) {
            g2.setColor(Color.pink);
            g2.drawString("PASASTE EL SEMESTRE", laberinto.getMensajeDeReadyCol() - 86, laberinto.getMensajeDeReadyFil());
        } else if (jugador.isPausado() && !jugador.isEstáEsperando()) {
            g2.setColor(Color.GRAY);
            g2.drawString("TOMASTE UN BREAK", laberinto.getMensajeDeReadyCol() - 60, laberinto.getMensajeDeReadyFil());
        }
        for (int i = 1; i < jugador.getVidas(); i++) {
            g2.drawImage(imagenPacmanSkin, 24 * i, (laberinto.getFilas() * 24) - 24, null);
        }
    }

    @Override
    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
        jugador = nivel.getJugador();
        personaje = jugador;
        laberinto = nivel.getLaberinto();
    }
}
