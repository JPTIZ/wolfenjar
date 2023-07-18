//=============================================================================
// Raycaster
//-----------------------------------------------------------------------------
// Generates raycasting optimal wall heights calculating algorithmn to be sent
// to Renderer, for a 3D-like view game.
//=============================================================================

import java.lang.Math.*;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Color;

public class Raycaster {

    private int  height;
    private int  width;
    private double halffov;   // rads
    private double angleinc;  // rads
    private double lumindex;

    // Constantes - Tamanho
    public static final int LARGURA = 800;
    public static final int ALTURA = 600;
    public static final int CENTRO_LARGURA = LARGURA/2;
    public static final int CENTRO_ALTURA = ALTURA/2;

    // Constantes - Ângulo
    public static final int ANGULO360   = 4800;
    public static final int ANGULO180   = 2400;
    public static final int ANGULO60    = 800;
    public static final int ANGULO30    = 400;
    public static final int ANGULO6     = 80;

    // Atributos - Gráficos
    private BufferedImage imagem;
    private Graphics grafico;
    private Color[] listaCores;
    private int[][] mapa;

    // Atributos - Evento
    private double raioX, raioY, etapaX, etapaY;
    private int anguloRaio;

    public Raycaster(int width, int height, int fov, double lumindex) {
        this.imagem = new BufferedImage(LARGURA, ALTURA, BufferedImage.TYPE_INT_RGB);
        this.grafico = this.imagem.getGraphics();
        this.height   = height;
        this.width    = width;
        this.halffov  = Math.toRadians(fov) / 2;
        this.angleinc = Math.toRadians(fov / ((double) width));
        this.lumindex = lumindex;
    }


    private byte abscos(double angle) {
        byte abscosine = 0;
        if (Math.cos(angle) > 0) {abscosine = 1;}
        else if (Math.cos(angle) < 0) {abscosine = -1;}
        return abscosine;
    }

    private byte abssin(double angle) {
        byte abssine = 0;
        if (Math.sin(angle) > 0) {abssine = 1;}
        else if (Math.sin(angle) < 0) {abssine = -1;}
        return abssine;
    }

    private int dirround(double val, byte dir)
    {if (dir < 1) {return (int) Math.floor(val);} else {return (int) Math.ceil(val);}}


    private double getRayLength(double px, double py, double pa) {
        byte   aspa  = abssin(pa);
        byte   acpa  = abscos(pa);
        double tgdir = Math.tan(pa) * abscos(pa);  // abscos fixes tg signal flip on x

        // Calculates Grid Collisions with DDA
        int    current_step;
        byte   abstrig;
        double axdif;
        if (Math.abs(tgdir) < 1) {  // dX > dY
            abstrig      = abscos(pa);
            current_step = dirround(px, abstrig);
            axdif        = px - current_step;
        } else {  // dY > dX
            // not sure if the y block is right. as of now it's
            // just the x block with all variables flipped
            abstrig      = abssin(pa);
            current_step = dirround(py, abstrig);
            axdif        = py - current_step;
        }

        return 0.8;  // TODO kill this placeholder
    }


    private int[] getWallProperties(double raylen) {
        return new int[0];
    }///////////////

    private int[] castRay(double px, double py, double pa) {
        return getWallProperties(getRayLength(px, py, pa));
    }

    public static double toRad(int angulo) {
        return angulo * (Math.PI / ANGULO180);
    }

    public int[][] getFrameWalls(double xa, double ya, double dir) {
        int[][] frame = new int[ANGULO60][2];   // Cria um vetor 800x2 (800 pixels, [altura, n])
        anguloRaio = ((int)dir - ANGULO30);     // Ângulo mais à esquerda do player
        int anguloFixo;                         // Ângulo corretor de fisheye
        for (int raio = 0; raio < ANGULO60; raio++) {   // ...
            anguloFixo = (raio - ANGULO30);             //
            raioX = xa;                                 //
            raioY = ya;
            etapaX = (Math.cos(this.toRad(anguloRaio)) * (1.0 / Math.cos(this.toRad(anguloFixo))))/Game.DISTANCE_PROPORTION;
            etapaY = (Math.sin(this.toRad(anguloRaio)) * (1.0 / Math.cos(this.toRad(anguloFixo))))/Game.DISTANCE_PROPORTION;

            int parede = 0;
            int distancia = 1;
            while (parede == 0) {
                raioX += etapaX;
                raioY += etapaY;
                parede = Levels.map[(int)raioY][(int)raioX];
                distancia++;
            }

            anguloRaio++;

            frame[raio][0] = distancia;
            frame[raio][1] = parede;

        }
        // IMPLEMENT INTERLACED RENDERING HERE
        return frame;
    }
}
