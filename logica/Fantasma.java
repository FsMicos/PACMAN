package logica;

import java.util.Random;

/**
 * Especialización de Personaje cuyo movimiento es controlado por el sistema
 * en función de las acciones del jugador. Si colisiona con un jugador sin Power
 * Pellet hace que este pierda una vida
 */
public abstract class Fantasma extends Personaje {
    private final int PUNTAJE_POR_FANTASMA = 200;
    protected final int VELOCIDAD_HUIDA = 1;
    protected final int VALOR_A_MOVERSE = 1;
    protected Tecla direcciónAnterior;
    protected Jugador jugador;
    protected boolean huyendo = false;
    protected int spawnColumna, spawnFila;
    private boolean llegóAlaEsquina;

    public Fantasma(int velocidad, Laberinto mapa, Jugador jugador, int tiempoAEsperar) {
        this.velocidad = velocidad;
        this.laberinto = mapa;
        this.direcciónImagen = "arriba";
        this.dirección = null;
        this.direcciónAnterior = null;
        this.llegóAlaEsquina = false;
        this.jugador = jugador;
        this.tiempoDeEspera = jugador.tiempoDeEspera + calcularTiempo(tiempoAEsperar);
    }

    public abstract void moverPorPersonalidad();

    @Override
    public void actualizar() {
        estaPausado();
        if (!elJuegoPuedeContinuar()) {
            return;
        }
        verificarEstadoJugador();
        if (estáEsperando) {
            accionAlEsperar(tiempoDeEspera);
        } else if (huyendo) {
            huir();
        } else if (estaColisionandoConJugador()) {
            jugador.reAparecer();
        } else if (!llegóAlaEsquina) {
            corregirPosición();
            moverAEsquina();
        } else {
            corregirPosición();
            moverPorPersonalidad();
        }
    }

    /**
     * Cambia el estado del fantasma en función de las acciones del jugador
     */
    private void verificarEstadoJugador() {
        if (!jugador.estaVivo) {
            huyendo = false;
            estáEsperando = true;
            contador = 0;
            llegóAlaEsquina = false;
        }
        if (!estáEsperando && jugador.activóPowerPellet()) {
            huyendo = true;
        } else if (!jugador.tienePowerPellet()) {
            huyendo = false;
        }
    }

    /**
     * Pausa el hilo lógico del fantasma si el usuario ha pausado el juego
     *
     * @return
     */
    public boolean estaPausado() {
        if (!jugador.pausado) {
            return pausado = false;
        } else {
            return pausado = true;
        }
    }

    /**
     * Verifica si el juego ha terminado, en tal caso, termina el hilo lógico del
     * fantasma
     *
     * @return
     */
    private boolean elJuegoPuedeContinuar() {
        if (jugador.getVidas() == 0 || jugador.comióTodosLosPacDots()) {
            hilo = false;
            return false;
        }
        return true;
    }

    protected boolean estaColisionandoConJugador() {
        for (int i = 0; i < 12; i++) {
            if (((this.posiciónX + i) == jugador.posiciónX && (this.posiciónY) == jugador.posiciónY)
                    || ((this.posiciónX - i) == jugador.posiciónX && this.posiciónY == jugador.posiciónY)
                    || (this.posiciónX == jugador.posiciónX && (this.posiciónY + i) == jugador.posiciónY)
                    || (this.posiciónX == jugador.posiciónX && (this.posiciónY - i) == jugador.posiciónY)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Define el comportamiento del fantasma en su spawn mientras el juego
     * todavía no empieza
     *
     * @param tiempoDeEspera
     */
    public void accionAlEsperar(int tiempoDeEspera) {
        if (contador == 0) {
            this.posiciónX = laberinto.getSpawnCuadradoFantasmasCol();
            this.posiciónY = laberinto.getSpawnCuadradoFantasmasFil();
        }
        moverRandom();
        if (seAcabóElTiempo(tiempoDeEspera)) {
            estáEsperando = false;
            this.posiciónX = laberinto.getSpawnFantasmasCol();
            this.posiciónY = laberinto.getSpawnFantasmasFil();
        }
    }

    public void moverRandom() {
        if (estaAlineado()) {
            direcciónAnterior = dirección;
            dirección = obtenerDirecciónRandom(direcciónAnterior);
        }
        mover(dirección, velocidad);
    }

    private void moverAEsquina() {
        if (!puedeTeletransportarse()) {
            if (estaAlineado()) {
                if (obtenerColumnaLaberinto() == spawnColumna && obtenerFilaLaberinto() == spawnFila) {
                    this.llegóAlaEsquina = true;
                }
                direcciónAnterior = dirección;
                dirección = obtenerMejorDireccion(spawnColumna, spawnFila);
            }
        }
        mover(dirección, velocidad);
    }

    /**
     * Usa la velocidad de huida para mover al fantasma hasta que la posición de este sea un múltiplo
     * de su velocidad original. De este modo el personaje se mantiene alineado pese a varias su
     * velocidad
     */
    private void corregirPosición() {
        while (posiciónX % velocidad != 0 || posiciónY % velocidad != 0) {
            mover(dirección, VELOCIDAD_HUIDA);
        }
    }

    @Override
    public boolean sePuedeMover(Tecla tecla) {
        return super.sePuedeMover(tecla) && !esDireccionContraria(tecla, direcciónAnterior);
    }

    public void huir() {
        if (!puedeTeletransportarse()) {
            if (estaAlineado()) {
                direcciónAnterior = dirección;
                dirección = obtenerMejorDirecciónHuida();
            }
        }
        if (estaColisionandoConJugador()) {
            estáEsperando = true;
            huyendo = false;
            jugador.puntaje += PUNTAJE_POR_FANTASMA * jugador.bonusPorFantasmaAsesinado;
            jugador.bonusPorFantasmaAsesinado++;
        }
        mover(dirección, VELOCIDAD_HUIDA);
    }

    /**
     * Retorna la dirección de movimiento con la cual el fantasma estará más alejado del jugador,
     * considerando que esta no debe ser contraria a su dirección actual, de tal manera que no se
     * le permita regresar
     *
     * @return movimiento
     */
    private Tecla obtenerMejorDirecciónHuida() {
        Tecla movimiento = null;
        double distanciaMaxima = 0;
        double distanciaTemporal = calcularDistancia(0, -VALOR_A_MOVERSE, jugador.obtenerColumnaLaberinto(), jugador.obtenerFilaLaberinto());
        if (sePuedeMover(Tecla.ARRIBA)) {
            distanciaMaxima = distanciaTemporal;
            movimiento = Tecla.ARRIBA;
        }
        distanciaTemporal = calcularDistancia(0, VALOR_A_MOVERSE, jugador.obtenerColumnaLaberinto(), jugador.obtenerFilaLaberinto());
        if (distanciaTemporal > distanciaMaxima && sePuedeMover(Tecla.ABAJO)) {
            distanciaMaxima = distanciaTemporal;
            movimiento = Tecla.ABAJO;
        }
        distanciaTemporal = calcularDistancia(-VALOR_A_MOVERSE, 0, jugador.obtenerColumnaLaberinto(), jugador.obtenerFilaLaberinto());
        if (distanciaTemporal > distanciaMaxima && sePuedeMover(Tecla.IZQUIERDA)) {
            distanciaMaxima = distanciaTemporal;
            movimiento = Tecla.IZQUIERDA;
        }
        distanciaTemporal = calcularDistancia(VALOR_A_MOVERSE, 0, jugador.obtenerColumnaLaberinto(), jugador.obtenerFilaLaberinto());
        if (distanciaTemporal > distanciaMaxima && sePuedeMover(Tecla.DERECHA)) {
            distanciaMaxima = distanciaTemporal;
            movimiento = Tecla.DERECHA;
        }
        return movimiento;
    }

    public Tecla obtenerDirecciónRandom(Tecla teclaActual) {
        Random rand = new Random();
        Tecla tecla;
        Tecla[] teclaArray = {Tecla.ARRIBA, Tecla.ABAJO, Tecla.IZQUIERDA, Tecla.DERECHA};
        do {
            tecla = teclaArray[rand.nextInt(4)];
        } while (esDireccionContraria(tecla, teclaActual) || !sePuedeMover(tecla));
        return tecla;
    }

    /**
     * Retorna la dirección de movimiento con la cual el fantasma estará más cerca de una determinada
     * fila y columna, considerando que esta no debe ser contraria a su dirección actual
     *
     * @return
     */
    public Tecla obtenerMejorDireccion(int columna, int fila) {
        Tecla movimiento = null;
        double distanciaMínima = 10000;
        double distanciaTemporal = calcularDistancia(0, -VALOR_A_MOVERSE, columna, fila);
        if (sePuedeMover(Tecla.ARRIBA)) {
            distanciaMínima = distanciaTemporal;
            movimiento = Tecla.ARRIBA;
        }
        distanciaTemporal = calcularDistancia(0, VALOR_A_MOVERSE, columna, fila);
        if (distanciaTemporal < distanciaMínima && sePuedeMover(Tecla.ABAJO)) {
            distanciaMínima = distanciaTemporal;
            movimiento = Tecla.ABAJO;
        }
        distanciaTemporal = calcularDistancia(-VALOR_A_MOVERSE, 0, columna, fila);
        if (distanciaTemporal < distanciaMínima && sePuedeMover(Tecla.IZQUIERDA)) {
            distanciaMínima = distanciaTemporal;
            movimiento = Tecla.IZQUIERDA;
        }
        distanciaTemporal = calcularDistancia(VALOR_A_MOVERSE, 0, columna, fila);
        if (distanciaTemporal < distanciaMínima && sePuedeMover(Tecla.DERECHA)) {
            distanciaMínima = distanciaTemporal;
            movimiento = Tecla.DERECHA;
        }
        return movimiento;
    }

    /**
     * Obtiene la magnitud de la diagonal desde la posición actual del fantasma a una determinada
     * coordenada en el laberinto compuesta por una fila y una columna
     *
     * @return
     */
    protected double calcularDistancia(int extraColumnas, int extraFilas, int lugarACompararColu, int lugarACompararFila) {
        return Math.sqrt(Math.pow(obtenerColumnaLaberinto() + extraColumnas - lugarACompararColu, 2) + Math.pow(obtenerFilaLaberinto() + extraFilas - lugarACompararFila, 2));
    }

    public boolean estaHuyendo() {
        return huyendo;
    }
}