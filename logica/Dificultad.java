package logica;

public enum Dificultad {
    FACIL(1),
    MEDIA(2),
    DIFICIL(3),
    IMPOSIBLE(4);

    private final int valorVelocidad;

    Dificultad(int valorVelocidad) {
        this.valorVelocidad = valorVelocidad;
    }

    public int getValorVelocidad() {
        return valorVelocidad;
    }
}
