package logica;

import presentacion.DetectorTeclas;

import java.io.Serializable;

/**
 * Clase lógica que representa un nivel de Pacman, compuesto por 4 Fantasmas,
 * un Jugador y un Laberinto
 */
public class Nivel implements Serializable {
    private Dificultad dificultad;
    private String direccionMapa;
    private boolean completado;
    private Nivel siguienteNivel;
    private Jugador jugador;
    private Fantasma clyde, blinky, pinky, inky;
    private Laberinto laberinto;
    private transient Thread hiloLogicaJugador, hiloLogicaClyde, hiloLogicaBlinky, hiloLogicaPinky, hiloLogicaInky;
    private static final int FPS = 60;
    private static final int TAMAÑO_ENTIDADES = 24;

    public Nivel(Dificultad dificultad, String direccionMapa) {
        this.dificultad = dificultad;
        this.direccionMapa = direccionMapa;
        laberinto = new Laberinto(direccionMapa);
        completado = false;
        // personajes
        jugador = new Jugador(dificultad.getValorVelocidad(), laberinto);
        clyde = new Clyde(dificultad.getValorVelocidad(), laberinto, jugador);
        blinky = new Blinky(dificultad.getValorVelocidad(), laberinto, jugador);
        pinky = new Pinky(dificultad.getValorVelocidad(), laberinto, jugador);
        inky = new Inky(dificultad.getValorVelocidad(), laberinto, jugador);
    }

    public void iniciarNivel(DetectorTeclas controles) {
        jugador.hilo = true;
        clyde.hilo = true;
        pinky.hilo = true;
        inky.hilo = true;
        blinky.hilo = true;
        hiloLogicaJugador = new Thread(jugador);
        hiloLogicaClyde = new Thread(clyde);
        hiloLogicaBlinky = new Thread(blinky);
        hiloLogicaPinky = new Thread(pinky);
        hiloLogicaInky = new Thread(inky);
        jugador.setNivel(this);
        jugador.setDetectorTeclas(controles);
        hiloLogicaJugador.start();
        hiloLogicaBlinky.start();
        hiloLogicaClyde.start();
        hiloLogicaPinky.start();
        hiloLogicaInky.start();
    }

    public boolean estáCompletado() {
        return completado;
    }

    public void completarNivel() {
        this.completado = true;
    }

    public void setSiguienteNivel(Nivel siguienteNivel) {
        this.siguienteNivel = siguienteNivel;
    }

    public Nivel getSiguienteNivel() {
        return siguienteNivel;
    }

    public Dificultad getDificultad() {
        return this.dificultad;
    }

    public String getDirecciónMapa() {
        return direccionMapa;
    }

    public static int getTamañoEntidades() {
        return TAMAÑO_ENTIDADES;
    }

    public Fantasma getBlinky() {
        return blinky;
    }

    public Fantasma getClyde() {
        return clyde;
    }

    public Fantasma getInky() {
        return inky;
    }

    public Fantasma getPinky() {
        return pinky;
    }

    public static int getFPS() {
        return FPS;
    }

    public Laberinto getLaberinto() {
        return laberinto;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void detener() {
        jugador.detenerSonido();
        jugador.pausado = true;
    }

    public void terminar() {
        jugador.hilo = false;
        clyde.hilo = false;
        pinky.hilo = false;
        inky.hilo = false;
        blinky.hilo = false;
    }
}