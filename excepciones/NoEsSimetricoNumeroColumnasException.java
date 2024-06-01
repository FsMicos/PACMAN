package excepciones;

public class NoEsSimetricoNumeroColumnasException extends Exception {
    public NoEsSimetricoNumeroColumnasException() {
        super("El número de columnas no es igual en todas las filas");
        System.out.println(getMessage());
    }
}
