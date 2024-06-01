package logica;

/**
 * Especialización de Fantasma que tras llegar a su esquina respectiva
 * persigue continuamente al jugador
 */
public class Blinky extends Fantasma {

    public Blinky(int velocidad, Laberinto mapa, Jugador jugador) {
        super(velocidad, mapa, jugador, 1);
        this.spawnColumna = laberinto.getBlinkyPuntoCol();
        this.spawnFila = laberinto.getBlinkyPuntoFil();
    }

    @Override
    public void moverPorPersonalidad() {
        if (!puedeTeletransportarse()) {
            if (estaAlineado()) {
                direcciónAnterior = dirección;
                dirección = obtenerMejorDireccion(jugador.obtenerColumnaLaberinto(), jugador.obtenerFilaLaberinto());
            }
        }
        mover(dirección, velocidad);
    }

}
