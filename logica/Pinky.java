package logica;

/**
 * Especialización de Fantasma que tras llegar a su esquina persigue al jugador
 * considerando que este se encuentra unos bloques hacia delante en el laberinto, es decir,
 * se adelanta a su posición
 */
public class Pinky extends Fantasma {
    public Pinky(int velocidad, Laberinto mapa, Jugador jugador) {
        super(velocidad, mapa, jugador, 4);
        this.spawnColumna = laberinto.getPinkyPuntoCol();
        this.spawnFila = laberinto.getPinkyPuntoFil();
    }

    @Override
    public void moverPorPersonalidad() {
        if (!puedeTeletransportarse()) {
            if (estaAlineado()) {
                direcciónAnterior = dirección;
                dirección = obtenerMovimiento();
            }
        }
        mover(dirección, velocidad);
    }

    public Tecla obtenerMovimiento() {
        if (jugador.obtenerDirección() == null) {
            return obtenerDirecciónRandom(direcciónAnterior);
        }
        int distanciaExtraARecorrer = 4;
        switch (jugador.obtenerDirección()) {
            case ARRIBA:
                return obtenerMejorDireccionPinky(-distanciaExtraARecorrer, 0);
            case ABAJO:
                return obtenerMejorDireccionPinky(distanciaExtraARecorrer, 0);
            case DERECHA:
                return obtenerMejorDireccionPinky(0, distanciaExtraARecorrer);
            case IZQUIERDA:
                return obtenerMejorDireccionPinky(0, -distanciaExtraARecorrer);
            default:
                return obtenerDirecciónRandom(direcciónAnterior);
        }
    }

    public Tecla obtenerMejorDireccionPinky(int extraColumnas, int extraFilas) {
        Tecla movimiento = null;
        double distanciaMínima = 10000;
        double distanciaTemporal = calcularDistanciaPinky(extraColumnas, extraFilas, 0, -VALOR_A_MOVERSE);
        if (sePuedeMover(Tecla.ARRIBA)) {
            distanciaMínima = distanciaTemporal;
            movimiento = Tecla.ARRIBA;
        }
        distanciaTemporal = calcularDistanciaPinky(extraColumnas, extraFilas, 0, VALOR_A_MOVERSE);
        if (distanciaTemporal < distanciaMínima && sePuedeMover(Tecla.ABAJO)) {
            distanciaMínima = distanciaTemporal;
            movimiento = Tecla.ABAJO;
        }
        distanciaTemporal = calcularDistanciaPinky(extraColumnas, extraFilas, -VALOR_A_MOVERSE, 0);
        if (distanciaTemporal < distanciaMínima && sePuedeMover(Tecla.IZQUIERDA)) {
            distanciaMínima = distanciaTemporal;
            movimiento = Tecla.IZQUIERDA;
        }
        distanciaTemporal = calcularDistanciaPinky(extraColumnas, extraFilas, VALOR_A_MOVERSE, 0);
        if (distanciaTemporal < distanciaMínima && sePuedeMover(Tecla.DERECHA)) {
            distanciaMínima = distanciaTemporal;
            movimiento = Tecla.DERECHA;
        }
        return movimiento;
    }

    public double calcularDistanciaPinky(int extraColumnas, int extraFilas, int extraFanColum, int extraFanFil) {
        return Math.sqrt(Math.pow(obtenerColumnaLaberinto() + extraFanColum - (jugador.obtenerColumnaLaberinto() + extraColumnas), 2) + Math.pow(obtenerFilaLaberinto() + extraFanFil - (jugador.obtenerFilaLaberinto() + extraFilas), 2));
    }
}
