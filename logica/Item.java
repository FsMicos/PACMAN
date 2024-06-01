package logica;

public enum Item {
    PAC_DOT(2, 10),
    POWER_PELLET(3, 50),
    FRUTA(4, 100);
    private final int código;
    private final int puntaje;

    Item(int código, int puntaje) {
        this.código = código;
        this.puntaje = puntaje;
    }

    public int getCódigo() {
        return código;
    }

    public int getPuntaje() {
        return puntaje;
    }
}
