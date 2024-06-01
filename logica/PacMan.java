package logica;

/**
 * Clase que representa al juego a nivel lógico, está compuesta por
 * niveles y permite que estos estén relacionados y puedan reiniciarse
 */
public class PacMan implements Cloneable {
    private Nivel[] niveles;

    public PacMan(Nivel... niveles) {
        this.niveles = niveles;
        enlazarNiveles();
    }

    private void enlazarNiveles() {
        if (niveles.length == 1) {
            return;
        }
        for (int i = 0; i < niveles.length; i++) {
            if (i + 1 < niveles.length) {
                niveles[i].setSiguienteNivel(niveles[i + 1]);
            }
        }
    }

    public Nivel getNivel(int posicion) {
        if (posicion < 0 || posicion >= niveles.length) {
            return null;
        }
        return niveles[posicion];
    }

    public void reiniciarNiveles() {
        Nivel[] nivelesNuevos = new Nivel[niveles.length];
        for (int i = 0; i < niveles.length; i++) {
            Nivel nivel = niveles[i];
            nivelesNuevos[i] = new Nivel(nivel.getDificultad(), nivel.getDirecciónMapa());
        }
        niveles = nivelesNuevos;
    }

    public Nivel[] getNiveles() {
        return this.niveles;
    }

}
