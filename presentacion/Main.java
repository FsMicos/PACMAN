package presentacion;

import logica.Dificultad;
import logica.Nivel;
import logica.PacMan;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
        PacMan pacman = new PacMan(
                new Nivel(Dificultad.MEDIA, "/res/mapa3.txt"),
                new Nivel(Dificultad.IMPOSIBLE, "/res/mapa2.txt"),
                new Nivel(Dificultad.IMPOSIBLE, "/res/mapa1.txt")
        );
        MenuPacMan menu = new MenuPacMan(pacman, new Dimension(716, 440));
    }
}