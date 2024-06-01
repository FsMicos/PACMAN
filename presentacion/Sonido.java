package presentacion;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;

/**
 * Clase que permite la reproducción de diversos efectos de sonido en el
 * juego para una mejor experiencia de usuario
 */
public class Sonido implements Serializable {
    private final URL[] sonidos;
    private transient Clip clip = null;

    public Sonido() {
        this.sonidos = new URL[30];
        cargarSonidos();
    }

    private void cargarSonidos() {
        sonidos[0] = obtenerUrl("sonidoMenu");
        sonidos[1] = obtenerUrl("sonidoMuerte");
        sonidos[2] = obtenerUrl("sonidoPartida");
        sonidos[3] = obtenerUrl("sonidoInicioPartida");
        sonidos[4] = obtenerUrl("sonidoPowerUp");
    }

    private URL obtenerUrl(String nombreAudio) {
        return getClass().getResource("/res/sonidos/" + nombreAudio + ".wav");
    }

    public void obtenerSonidos(Sonidos s) {
        try {
            AudioInputStream sonido = AudioSystem.getAudioInputStream(sonidos[s.getIndiceSonido()]);
            clip = AudioSystem.getClip();
            clip.open(sonido);
        } catch (UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    public void reproducir() {
        if (clip == null) {
            return;
        }
        clip.start();
    }

    public void correrEnLoop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void parar() {
        if (clip == null) {
            try {
                clip = AudioSystem.getClip();
            } catch (LineUnavailableException e) {
                throw new RuntimeException(e);
            }
            clip.stop();
            return;
        }
        clip.stop();
    }

    public void reproducirEfecto(Sonidos sonidos) {
        obtenerSonidos(sonidos);
        reproducir();
    }

    public boolean terminoSonido() {
        if (clip == null) {
            return true;
        }
        return !clip.isRunning();
    }
}
