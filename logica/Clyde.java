package logica;

/**
 * Especialización de Fantasma que tras llegar a su esquina se mueve
 * únicamente de manera aleatoria por el mapa
 */
public class Clyde extends Fantasma {
    public Clyde(int velocidad, Laberinto mapa, Jugador jugador) {
        super(velocidad, mapa, jugador, 3);
        this.spawnColumna = laberinto.getClydePuntoCol();
        this.spawnFila = laberinto.getClydePuntoFil();
    }

    @Override
    public void moverPorPersonalidad() {
        if (!puedeTeletransportarse()) {
            if (estaAlineado()) {
                direcciónAnterior = dirección;
                dirección = obtenerDirecciónRandom(direcciónAnterior);
            }
        }
        mover(dirección, velocidad);
    }

}
