package presentacion;

import logica.Nivel;

/**
 * Especialización de InterfazGrafica que se encarga de dibujar al jugador,
 * solo cuenta con un tipo de sprites
 */
public class InterfazGraficaDeJugador extends InterfazGrafica {

    public InterfazGraficaDeJugador() {
        super();
        obtenerImagen("pacman", sprites);
    }

    @Override
    public void setNivel(Nivel nivel) {
        spriteEnUso = sprites;
        personaje = nivel.getJugador();
    }
}
