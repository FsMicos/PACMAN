package presentacion;

import logica.Tecla;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Clase que permite registrar el input del usuario a través del teclado,
 * se usa para mover al jugador y poner pausa a un nivel
 */
public class DetectorTeclas implements KeyListener {
    public Tecla tecla;

    @Override
    public void keyTyped(KeyEvent e) {
        int code = e.getKeyChar();
        if (code == Tecla.ABAJO.getCodigo()) {
            tecla = Tecla.ABAJO;
        } else if (code == Tecla.ARRIBA.getCodigo()) {
            tecla = Tecla.ARRIBA;
        } else if (code == Tecla.IZQUIERDA.getCodigo()) {
            tecla = Tecla.IZQUIERDA;
        } else if (code == Tecla.DERECHA.getCodigo()) {
            tecla = Tecla.DERECHA;
        } else if (code == Tecla.PAUSAR.getCodigo()) {
            tecla = Tecla.PAUSAR;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCodigo = e.getKeyCode();
        switch (keyCodigo) {
            case KeyEvent.VK_UP -> tecla = Tecla.ARRIBA;
            case KeyEvent.VK_DOWN -> tecla = Tecla.ABAJO;
            case KeyEvent.VK_LEFT -> tecla = Tecla.IZQUIERDA;
            case KeyEvent.VK_RIGHT -> tecla = Tecla.DERECHA;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
