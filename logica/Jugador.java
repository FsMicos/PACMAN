package logica;

import presentacion.DetectorTeclas;
import presentacion.Sonido;
import presentacion.Sonidos;

import java.io.*;
import java.util.*;

/**
 * Especialización de Personaje que es controlada por el usuario a través
 * de las teclas AWSD o las flechas del teclado. Tiene la capacidad de matar
 * a Fantasmas si consume un Power Pellet
 */
public class Jugador extends Personaje {
    private transient DetectorTeclas detectorTeclas;
    // registra la última dirección de movimiento que se le asignó al jugador, se ejecuta cuando
    // su realización es posible
    private Tecla últimaDirección;
    public int puntaje, bonusPorFantasmaAsesinado, highScore;
    private boolean powerPellet;
    private final int DURACIÓN_POWER_PELLET = calcularTiempo(5);
    private int contadorPowerUp;
    public boolean estaVivo;
    private int vidas = 3;
    private int pacDocsComidos = 0;
    private boolean consumióPowerPellet;
    private Nivel nivel;
    private Sonido sonido;
    private transient ArchivoManager archivoManager;

    public Jugador(int velocidad, Laberinto mapa) {
        this.laberinto = mapa;
        this.sonido = new Sonido();
        this.posiciónX = laberinto.getSpawnPersonajeCol();
        this.posiciónY = laberinto.getSpawnPersonajeFil();
        this.velocidad = velocidad;
        this.dirección = null;
        this.direcciónImagen = "arriba";
        this.últimaDirección = null;
        this.powerPellet = false;
        this.contadorPowerUp = 0;
        this.estaVivo = true;
        this.consumióPowerPellet = false;
        this.archivoManager = new ArchivoManager(new File("res/puntuacion.txt"));
        this.highScore = archivoManager.obtenerHighScore();
        this.tiempoDeEspera = calcularTiempo(3);
    }

    public void setDetectorTeclas(DetectorTeclas controles) {
        this.detectorTeclas = controles;
    }

    /**
     * Actualiza la posición X e Y del jugador en la ventana en función
     * del input del usuario, (tecla de movimiento presionada - AWSD)
     */
    @Override
    public void actualizar() {
        if (estaPausado()) {
            return;
        }
        if (!estáEsperando) {
            if (sonido.terminoSonido()) {
                sonido.reproducirEfecto(Sonidos.PARTIDA);
            }
            if (!puedeTeletransportarse()) {
                if (comióTodosLosPacDots()) {
                    sonido.parar();
                    nivel.completarNivel();
                    hilo = false;
                    if (nivel.getSiguienteNivel() == null) {
                        if (archivoManager != null) {
                            archivoManager.guardarPuntuacion(puntaje);
                            return;
                        }
                        archivoManager = new ArchivoManager(new File("res/puntuacion.txt"));
                        archivoManager.guardarPuntuacion(puntaje);
                        return;
                    }
                    nivel.getSiguienteNivel().getJugador().puntaje += this.puntaje;
                    return;
                }
                this.dirección = obtenerMovimiento();
                if (estaAlineado()) {
                    obtenerItem();
                }
                if (tienePowerPellet()) {
                    activarPowerUp();
                }
            }
            mover(dirección, velocidad);
        } else {
            if (sonido.terminoSonido()) {
                sonido.reproducirEfecto(Sonidos.INICIO_PARTIDA);
            }
            seAcabóElTiempo(tiempoDeEspera);
        }
    }


    public boolean estaPausado() {
        if (detectorTeclas.tecla == Tecla.PAUSAR) {
            detectorTeclas.tecla = null;
            this.pausado = !this.pausado;
            if (pausado) {
                sonido.parar();
            } else {
                sonido.reproducir();
            }
        }
        return this.pausado;
    }

    private void obtenerItem() {
        int indiceItem = laberinto.cambiarValorA0(obtenerFilaLaberinto(), obtenerColumnaLaberinto());
        if (indiceItem == Item.PAC_DOT.getCódigo()) {
            this.puntaje += Item.PAC_DOT.getPuntaje();
            pacDocsComidos += 1;
        } else if (indiceItem == Item.POWER_PELLET.getCódigo()) {
            this.puntaje += Item.POWER_PELLET.getPuntaje();
            this.powerPellet = true;
            this.contadorPowerUp = 0;
        } else if (indiceItem == Item.FRUTA.getCódigo()) {
            this.puntaje += Item.FRUTA.getPuntaje();
        }
    }

    public boolean comióTodosLosPacDots() {
        //return pacDocsComidos == 3;
        return pacDocsComidos == laberinto.getNumeroDePacDots();
    }

    /*
        Obtiene la dirección de movimiento del Jugador en el instante dado, considerando si se
        puede realizar un cambio de dirección
     */
    private Tecla obtenerMovimiento() {
        actualizarÚltimaDirección();
        if (this.últimaDirección != null && (sePuedeMover(this.últimaDirección)
                || esDireccionContraria(this.últimaDirección, dirección) || dirección == null)) {
            detectorTeclas.tecla = últimaDirección;
            últimaDirección = null;
            return detectorTeclas.tecla;
        }
        return dirección;
    }

    /*
        Cambia la última dirección asignada al jugador en caso de que esta sea diferente a la actual
    */
    private void actualizarÚltimaDirección() {
        if (!Objects.equals(this.dirección, detectorTeclas.tecla)) {
            this.últimaDirección = detectorTeclas.tecla;
            detectorTeclas.tecla = this.dirección;
        }
    }

    public void reAparecer() {
        sonido.parar();
        sonido.reproducirEfecto(Sonidos.MUERTE);
        powerPellet = false;
        contadorPowerUp = 0;
        vidas -= 1;
        if (vidas == 0) {
            hilo = false;
            sonido.reproducirEfecto(Sonidos.MUERTE);
            sonido.parar();
            if (archivoManager != null) {
                archivoManager.guardarPuntuacion(puntaje);
                return;
            }
            archivoManager = new ArchivoManager(new File("res/puntuacion.txt"));
            archivoManager.guardarPuntuacion(puntaje);
        }
        this.posiciónX = laberinto.getSpawnPersonajeCol();
        this.posiciónY = laberinto.getSpawnPersonajeFil();
        this.estaVivo = false;
        this.estáEsperando = true;
        this.contador = 0;
    }

    public void activarPowerUp() {
        if (contadorPowerUp == 0) {
            sonido.parar();
            sonido.obtenerSonidos(Sonidos.POWER_UP);
            sonido.correrEnLoop();
            bonusPorFantasmaAsesinado = 1;
            consumióPowerPellet = true;
        }
        if (contadorPowerUp == DURACIÓN_POWER_PELLET) {
            sonido.parar();
            this.powerPellet = false;
            bonusPorFantasmaAsesinado = 1;
            contadorPowerUp = 0;
        } else {
            contadorPowerUp += 1;
        }
        if (contadorPowerUp == 10) {
            consumióPowerPellet = false;
        }
    }

    @Override
    public boolean seAcabóElTiempo(int tiempoDeEspera) {
        contador++;
        if (contador == tiempoDeEspera) {
            estáEsperando = false;
            contador = 0;
            sonido.parar();
            return true;
        }
        if (contador == 10) {
            estaVivo = true;
        }
        return false;
    }

    public boolean activóPowerPellet() {
        return this.consumióPowerPellet;
    }

    public boolean tienePowerPellet() {
        return this.powerPellet;
    }

    public int getVidas() {
        return vidas;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    public void detenerSonido() {
        sonido.parar();
    }

    public Tecla obtenerDirección() {
        return dirección;
    }
}