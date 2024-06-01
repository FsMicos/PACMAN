package presentacion;

public enum Sonidos {
    MENU(0),
    MUERTE(1),
    PARTIDA(2),
    INICIO_PARTIDA(3),
    POWER_UP(4);

    private final int indiceSonido;

    Sonidos(int indiceSonido) {
        this.indiceSonido = indiceSonido;
    }

    public int getIndiceSonido() {
        return indiceSonido;
    }
}
