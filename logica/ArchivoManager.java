package logica;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

/**
 * Clase que permite leer y escribir tanto las puntuaciones de los jugadores como
 * la partida que el usuario desea guardar
 */
public class ArchivoManager implements Serializable {

    private File archivo;
    private final ArrayList<String> datosDelArchivo;
    private final ArrayList<Integer> puntuaciones;
    private FileWriter escritorArchivo;

    public ArchivoManager(File archivo) {
        this.archivo = archivo;
        this.datosDelArchivo = new ArrayList<>();
        this.puntuaciones = new ArrayList<>();
        leerArchivo();
    }

    public void guardarNombre(String nombre, int puntaje) {
        try {
            this.escritorArchivo = new FileWriter(archivo, false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ordenarArchivo(nombre, puntaje);
        if (puntuaciones.size() < 10) {
            escribirPuntaje(datosDelArchivo.size());
        } else {
            escribirPuntaje(10);
        }
    }

    public void guardarPuntuacion(int puntaje) {
        if (puntuaciones.isEmpty()) {
            return;
        }
        Collections.sort(puntuaciones);
        if (puntaje >= puntuaciones.get(0) || puntuaciones.size() < 10) {
            JFrame ventana = new JFrame("Guardar Puntuacion");
            JLabel etiqueta = new JLabel();
            JLabel etiqueta2 = new JLabel();
            JPanel panel = new JPanel();
            JTextField campoNombre = new JTextField();
            JButton botonGuardar = new JButton("Puntaje Guardar");


            ventana.setSize(500, 700);
            ventana.setResizable(false);
            ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            ventana.setLocationRelativeTo(null);

            panel.setLayout(null);
            panel.setBackground(Color.BLACK);

            etiqueta.setBounds(60, 60, 400, 200);
            etiqueta.setText("> INGRESA TU NOMBRE <");
            etiqueta.setFont(new Font("Creepster", 1, 30));
            etiqueta2.setBounds(150, 250, 400, 200);
            etiqueta2.setText(puntaje + " PUNTOS");//
            etiqueta2.setFont(new Font("Creepster", 1, 30));


            campoNombre.setBounds(90, 210, 300, 100);
            campoNombre.setFont(new Font("Creepster", 1, 28));
            campoNombre.setHorizontalAlignment(JTextField.CENTER);
            campoNombre.setSelectedTextColor(Color.BLUE);

            botonGuardar.setBounds(90, 400, 300, 90);
            botonGuardar.setFont(new Font("Creepster", 1, 20));
            botonGuardar.setContentAreaFilled(false);
            botonGuardar.setFocusPainted(false);

            panel.add(etiqueta2);
            panel.add(etiqueta);
            panel.add(campoNombre);
            panel.add(botonGuardar);
            ventana.add(panel);
            ventana.setVisible(true);

            botonGuardar.addActionListener(e -> {
                String nombre = campoNombre.getText();
                guardarNombre(nombre, puntaje);
                ventana.dispose();
            });
        }
    }

    public void escribirPuntaje(int limite) {
        try {
            String currentLine;
            int i = 0;
            while (limite != i) {
                currentLine = datosDelArchivo.get(i);
                i += 1;
                escritorArchivo.write(currentLine + System.getProperty("line.separator"));
            }
            escritorArchivo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> leerArchivo() {
        datosDelArchivo.clear();
        this.archivo = new File(this.archivo.getPath());
        String[] puntajePartida;
        Scanner scanerArchivo;
        try {
            scanerArchivo = new Scanner(archivo);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        while (scanerArchivo.hasNextLine()) {
            String linea = scanerArchivo.nextLine();
            puntajePartida = linea.split(" ");
            if (puntajePartida.length == 2) {
                puntuaciones.add(Integer.parseInt(puntajePartida[1]));
                datosDelArchivo.add(linea);
            }
        }
        return datosDelArchivo;
    }

    public int obtenerHighScore() {
        ordenarDatos();
        String[] highScore = datosDelArchivo.get(0).split(" ");
        if (highScore.length == 2) {
            return Integer.parseInt(highScore[1]);
        }
        return 0;
    }

    public void ordenarArchivo(String nombre, int puntaje) {
        if (!(puntuaciones.size() < 10)) {
            datosDelArchivo.remove(datosDelArchivo.size() - 1);
        }
        datosDelArchivo.add(nombre + " " + puntaje);
        ordenarDatos();
    }

    public void ordenarDatos() {
        String[] datosAOrdenar = datosDelArchivo.toArray(new String[0]);
        Arrays.sort(datosAOrdenar, (s1, s2) -> {
            int numero1 = Integer.parseInt(s1.split(" ")[1]);
            int numero2 = Integer.parseInt(s2.split(" ")[1]);
            return Integer.compare(numero2, numero1);
        });
        datosDelArchivo.clear();
        Collections.addAll(datosDelArchivo, datosAOrdenar);
    }

    public static void guardarNivel(Nivel nivel) {
        File archivo = new File("res/partida");
        FileOutputStream flujoDeSalida = null;
        try {
            flujoDeSalida = new FileOutputStream(archivo, false);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        ObjectOutputStream manejadorDeEscritura = null;
        try {
            manejadorDeEscritura = new ObjectOutputStream(flujoDeSalida);
            manejadorDeEscritura.writeObject(nivel);
            manejadorDeEscritura.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Nivel cargarNivel() {
        Nivel nivel;
        File archivo = new File("res/partida");
        ObjectInputStream manejadorDeLectura;
        try {
            manejadorDeLectura = new ObjectInputStream(new FileInputStream(archivo));
            nivel = (Nivel) manejadorDeLectura.readObject();
            manejadorDeLectura.close();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return nivel;
    }
}
