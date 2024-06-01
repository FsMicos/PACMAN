package logica;

import excepciones.NoEsSimetricoNumeroColumnasException;

import java.io.*;
import java.util.ArrayList;

/**
 * Clase que contiene un mapa, array bidimencional de números que se obtiene de
 * un archivo txt donde están diversas coordenadas y valores que requieren otras clases
 */
public class Laberinto implements Serializable {
    public int[][] mapa;
    private int tpDerechoFil, tpDerechoCol, tpIzquierdoFil, tpIzquierdoCol, spawnFantasmasFil, spawnFantasmasCol,
            spawnPersonajeFil, spawnPersonajeCol, numeroDePacDots, clydePuntoFil, clydePuntoCol, linkyPuntoFil, linkyPuntoCol,
            pinkyPuntoFil, pinkyPuntoCol, blinkyPuntoFil, blinkyPuntoCol, mensajeDeReadyFil, mensajeDeReadyCol, spawnCuadradoFantasmasCol, spawnCuadradoFantasmasFil;


    public Laberinto(String pathName) {
        cargarMapa(pathName);
        obtenerCoordenadasDeValores();
    }

    private void obtenerCoordenadasDeValores() {
        for (int filas = 0; filas < getFilas(); filas++) {
            for (int columnas = 0; columnas < getColumnas(); columnas++) {
                if (mapa[filas][columnas] == 5) {
                    this.tpDerechoFil = filas;
                    this.tpDerechoCol = columnas;
                } else if (mapa[filas][columnas] == 6) {
                    this.tpIzquierdoFil = filas;
                    this.tpIzquierdoCol = columnas;
                } else if (mapa[filas][columnas] == 7) {
                    this.spawnFantasmasFil = filas;
                    this.spawnFantasmasCol = columnas;
                } else if (mapa[filas][columnas] == 8) {
                    this.spawnPersonajeFil = filas;
                    this.spawnPersonajeCol = columnas;
                } else if (mapa[filas][columnas] == 2) {
                    numeroDePacDots += 1;
                } else if (mapa[filas][columnas] == 9) {
                    this.clydePuntoFil = filas;
                    this.clydePuntoCol = columnas;
                } else if (mapa[filas][columnas] == 10) {
                    this.linkyPuntoFil = filas;
                    this.linkyPuntoCol = columnas;
                } else if (mapa[filas][columnas] == 11) {
                    this.pinkyPuntoFil = filas;
                    this.pinkyPuntoCol = columnas;
                } else if (mapa[filas][columnas] == 12) {
                    this.blinkyPuntoFil = filas;
                    this.blinkyPuntoCol = columnas;
                } else if (mapa[filas][columnas] == 13) {
                    this.mensajeDeReadyFil = filas;
                    this.mensajeDeReadyCol = columnas;
                } else if (mapa[filas][columnas] == 14) {
                    this.spawnCuadradoFantasmasCol = columnas;
                    this.spawnCuadradoFantasmasFil = filas;
                }
            }
        }
    }

    private void cargarMapa(String pathName) {
        try {
            InputStream inputStream = getClass().getResourceAsStream(pathName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            //int fila = 0;
            //int columna = 0;

            ArrayList<String> filas = new ArrayList<>();

            String linea;
            do {
                linea = bufferedReader.readLine();
                if (linea != null) {
                    filas.add(linea);
                }
            } while (linea != null);

            int columnas = filas.get(0).split(" ").length;
            mapa = new int[filas.size()][columnas];
            for (int i = 0; i < filas.size(); i++) {
                String[] columna = filas.get(i).split(" ");
                estaBienLasColumnas(columnas, columna);
                for (int a = 0; a < columna.length; a++) {
                    mapa[i][a] = Integer.parseInt(columna[a]);
                }
            }
        } catch (NoEsSimetricoNumeroColumnasException e) {
            throw new RuntimeException(e);
        } catch (IOException i) {
            throw new RuntimeException(i);
        }
    }

    private static void estaBienLasColumnas(int columnas, String[] columna) throws NoEsSimetricoNumeroColumnasException {
        if (columna.length != columnas) {
            throw new NoEsSimetricoNumeroColumnasException();
        }
    }

    public int getValorMapa(int fila, int columna) {
        return this.mapa[fila][columna];
    }

    public int cambiarValorA0(int fila, int columna) {
        if (this.mapa[fila][columna] != 5 && this.mapa[fila][columna] != 6) {
            int i = this.mapa[fila][columna];
            this.mapa[fila][columna] = 0;
            return i;
        }
        return 0;
    }

    public int getTpDerechoFil() {
        return tpDerechoFil;
    }

    public int getPinkyPuntoCol() {
        return pinkyPuntoCol;
    }

    public int getPinkyPuntoFil() {
        return pinkyPuntoFil;
    }

    public int getBlinkyPuntoCol() {
        return blinkyPuntoCol;
    }

    public int getBlinkyPuntoFil() {
        return blinkyPuntoFil;
    }

    public int getTpDerechoCol() {
        return tpDerechoCol;
    }

    public int getTpIzquierdoFil() {
        return tpIzquierdoFil;
    }

    public int getTpIzquierdoCol() {
        return tpIzquierdoCol;
    }

    public int getMensajeDeReadyFil() {
        return ((mensajeDeReadyFil + 1) * 24) - 5;
    }

    public int getMensajeDeReadyCol() {
        return ((mensajeDeReadyCol - 1) * 24);
    }

    public int getSpawnCuadradoFantasmasCol() {
        return spawnCuadradoFantasmasCol * 24;
    }

    public int getSpawnCuadradoFantasmasFil() {
        return spawnCuadradoFantasmasFil * 24;
    }

    public int getLinkyPuntoCol() {
        return linkyPuntoCol;
    }

    public int getLinkyPuntoFil() {
        return linkyPuntoFil;
    }

    public int getSpawnFantasmasFil() {
        return spawnFantasmasFil * 24;
    }

    public int getSpawnFantasmasCol() {
        return spawnFantasmasCol * 24;
    }

    public int getSpawnPersonajeFil() {
        return spawnPersonajeFil * 24;
    }

    public int getSpawnPersonajeCol() {
        return spawnPersonajeCol * 24;
    }

    public int getNumeroDePacDots() {
        return numeroDePacDots;
    }

    public int getClydePuntoCol() {
        return clydePuntoCol;
    }

    public int getClydePuntoFil() {
        return clydePuntoFil;
    }

    public int getFilas() {
        return mapa.length;
    }

    public int getColumnas() {
        return mapa[0].length;
    }
}