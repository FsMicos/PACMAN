package logica;

public enum Tecla {
    ARRIBA(119),
    ABAJO(115),
    DERECHA(100),
    IZQUIERDA(97),
    PAUSAR(112);

    private final int codigo;

    Tecla(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }
}
