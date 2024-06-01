package presentacion;

import logica.Nivel;
import logica.Personaje;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Clase que se encarga de dibujar en pantalla distintos aspectos gráficos de un nivel.
 * Cuenta con varias especializaciones
 */

public abstract class InterfazGrafica {

    protected Nivel nivel;
    protected BufferedImage[] sprites, spriteEnUso;
    private int spriteNumber;
    protected Personaje personaje;

    public InterfazGrafica() {
        sprites = new BufferedImage[8];
    }


    /**
     * Método que procesa la lógica de los personajes para
     * luego mostrar una representación gráfica de los mismos
     */
    public void dibujarSprite(Graphics2D g2) {
        BufferedImage image = sprites[6];
        BufferedImage[] sprites = this.spriteEnUso;
        spriteNumber = this.personaje.spriteNumber;
        switch (this.personaje.getDirecciónImagen()) {
            case "arriba":
                if (spriteNumber == 1) {
                    image = sprites[0];
                }
                if (spriteNumber == 2) {
                    image = sprites[1];
                }
                break;
            case "abajo":
                if (spriteNumber == 1) {
                    image = sprites[2];
                }
                if (spriteNumber == 2) {
                    image = sprites[3];
                }
                break;
            case "izquierda":
                if (spriteNumber == 1) {
                    image = sprites[4];
                }
                if (spriteNumber == 2) {
                    image = sprites[5];
                }
                break;
            case "derecha":
                if (spriteNumber == 1) {
                    image = sprites[6];
                }
                if (spriteNumber == 2) {
                    image = sprites[7];
                }

                break;
        }
        g2.drawImage(image, this.personaje.getPosiciónX(), this.personaje.getPosiciónY(), nivel.getTamañoEntidades(), nivel.getTamañoEntidades(), null);
    }

    /**
     * Este método sirve para poder obtener una imagen (.jpg) con la
     * cual se representaran los personajes en el método dibujar
     */
    public void obtenerImagen(String direcciónImagen, BufferedImage[] sprites) {
        try {
            sprites[0] = ImageIO.read(getClass().getResourceAsStream("/res/" + direcciónImagen + "/" + direcciónImagen + "_arriba_1.png"));
            sprites[1] = ImageIO.read(getClass().getResourceAsStream("/res/" + direcciónImagen + "/" + direcciónImagen + "_arriba_2.png"));
            sprites[2] = ImageIO.read(getClass().getResourceAsStream("/res/" + direcciónImagen + "/" + direcciónImagen + "_abajo_1.png"));
            sprites[3] = ImageIO.read(getClass().getResourceAsStream("/res/" + direcciónImagen + "/" + direcciónImagen + "_abajo_2.png"));
            sprites[4] = ImageIO.read(getClass().getResourceAsStream("/res/" + direcciónImagen + "/" + direcciónImagen + "_izquierda_1.png"));
            sprites[5] = ImageIO.read(getClass().getResourceAsStream("/res/" + direcciónImagen + "/" + direcciónImagen + "_izquierda_2.png"));
            sprites[6] = ImageIO.read(getClass().getResourceAsStream("/res/" + direcciónImagen + "/" + direcciónImagen + "_derecha_1.png"));
            sprites[7] = ImageIO.read(getClass().getResourceAsStream("/res/" + direcciónImagen + "/" + direcciónImagen + "_derecha_2.png"));
        } catch (IOException  e) {

            e.printStackTrace();

        }
    }

    public abstract void setNivel(Nivel nivel);
}
