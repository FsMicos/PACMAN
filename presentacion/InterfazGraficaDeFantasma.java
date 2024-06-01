package presentacion;

import logica.Fantasma;
import logica.Nivel;

import java.awt.image.BufferedImage;

/**
 * Especialización de InterfazGrafica que se encarga de dibujar a un Fantasma en
 * función de su estado (perseguir o huir), por ello cuenta con dos tipos de sprites
 */
public class InterfazGraficaDeFantasma extends InterfazGrafica {

    protected Fantasma fantasma;
    protected BufferedImage[] spritesHuida;
    private boolean actualizar;

    public InterfazGraficaDeFantasma(String imagen) {
        super();
        this.spritesHuida = new BufferedImage[8];
        obtenerImagen(imagen, sprites);
        obtenerImagen("vulnerableP1", spritesHuida);
    }

    public void actualizar() {
        if (fantasma.estaHuyendo() && actualizar) {
            this.spriteEnUso = spritesHuida;
            actualizar = false;
        }
        if (!fantasma.estaHuyendo() && !actualizar) {
            this.spriteEnUso = sprites;
            actualizar = true;
        }
    }

    //public void setNivel(Fantasma fantasma) {
    //    this.actualizar = true;
    //    setFantasma(fantasma);
    //}

    public void setFantasma(Fantasma fantasma) {
        this.actualizar = true;
        spriteEnUso = sprites;
        this.fantasma = fantasma;
        personaje = this.fantasma;
    }

    @Override
    public void setNivel(Nivel nivel) {
    }
}
