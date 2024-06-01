package excepciones;

public class NivelInvalidoException extends Exception {
    public NivelInvalidoException() {
        super("El nivel solicitado no existe");
    }
}
