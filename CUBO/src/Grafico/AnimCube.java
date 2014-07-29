package Grafico;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.util.Hashtable;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.*;
import Forma.Fcubo;

/**
 *
 * @author YEISON
 */

public final class AnimCube extends Applet implements Runnable, MouseListener, MouseMotionListener {
  //configuración externa
    
  private Fcubo cubo; 
  
  private final Hashtable config = new Hashtable();
  // Los colores de fondo
  private Color bgColor;
  private Color bgColor2;
  private Color hlColor;
  private Color textColor;
  private Color buttonBgColor;
  // Los colores del cubo
  private final Color[] colores = new Color[24];
  private final int[][] cube = new int[6][9];
  private final int[][] inicialCubo = new int[6][9];
  // Los vectores normales
  private static final double[][] vnormales = {
    { 0.0, -1.0,  0.0}, // U
    { 0.0,  1.0,  0.0}, // D
    { 0.0,  0.0, -1.0}, // F
    { 0.0,  0.0,  1.0}, // B
    {-1.0,  0.0,  0.0}, // L
    { 1.0,  0.0,  0.0}  // R
  };
  // El vértice se coordinadas
  private static final double[][] vcoordina = {
    {-1.0, -1.0, -1.0}, // UFL
    { 1.0, -1.0, -1.0}, // UFR
    { 1.0, -1.0,  1.0}, // UBR
    {-1.0, -1.0,  1.0}, // UBL
    {-1.0,  1.0, -1.0}, // DFL
    { 1.0,  1.0, -1.0}, // DFR
    { 1.0,  1.0,  1.0}, // DBR
    {-1.0,  1.0,  1.0}  // DBL
  };
  // Los vertices de cada cara
  private static final int[][] vccara = {
    {0, 1, 2, 3}, // U: UFL UFR UBR UBL
    {4, 7, 6, 5}, // D: DFL DBL DBR DFR
    {0, 4, 5, 1}, // F: UFL DFL DFR UFR
    {2, 6, 7, 3}, // B: UBR DBR DBL UBL
    {0, 3, 7, 4}, // L: UFL UBL DBL DFL
    {1, 5, 6, 2}  // R: UFR DFR DBR UBR
  };
  // Las esquinas correspondientes en la cara opuesta
  private static final int[][] esqccaraopuesta = {
    {0, 3, 2, 1}, // U->D
    {0, 3, 2, 1}, // D->U
    {3, 2, 1, 0}, // F->B
    {3, 2, 1, 0}, // B->F
    {0, 3, 2, 1}, // L->R
    {0, 3, 2, 1}, // R->L
  };
  // Las caras adyacentes para cada cara
  private static final int[][] adjacentccara = {
    {2, 5, 3, 4}, // U: F R B L
    {4, 3, 5, 2}, // D: L B R F
    {4, 1, 5, 0}, // F: L D R U
    {5, 1, 4, 0}, // B: R D L U
    {0, 3, 1, 2}, // L: U B D F
    {2, 1, 3, 0}  // R: F D B U
  };
  private int capa;
  private int modocapa;
  private static final int[] caraTD = {1, 1, -1, -1, -1, -1};
  //iniciales (la vista) de coordenada del observador
  private final double[] ojo = {0.0, 0.0, -1.0};
  private final double[] ojoX = {1.0, 0.0, 0.0}; // (Lateralmente)
  private final double[] ojoY = new double[3]; // (vertical)
  private final double[] inicialOjo = new double[3];
  private final double[] inicialOjoX = new double[3];
  private final double[] inicialOjoY = new double[3];
  private double anguloeditado; // El ángulo editado 
  private double angulooriginal; // El ángulo original
  // La velocidad de animación
  private int velocidad;
  private int dobleVelocidad;
  // El estado actual del programa
  private boolean natural = true;
  private boolean Torcerse; 
  private boolean cortaHilo; // El hilo fue al que se  cortó el hilo
  private boolean animacionDetenida; // La animación estaba detenida
  private boolean imagenVistaCubo; // Dando la imagen de la vista del cubo
  private boolean editRatonCubo; // La editación del cubo con un ratón
  private boolean usuarioTorceCubo; // Un usuario retuerce un estrato del cubo
  private boolean dandovueltas; // Una animación retuerce un estrato del cubo
  private boolean animando; // La carrera de animación
  private boolean arrastramiento; // La barra de progreso se controla
  private boolean demo; // El modo del demo
  private int deformacion; // La deformación perspectiva
  private double escama; // La escama del cubo
  private int alineacion; // La alineación del cubo (llegue al final, concéntrese, llegue al fondo)
  private boolean indicio;
  private double cambioCara;
  // Los datos de secuencia de movimiento
  private int[][] mover;
  private int[][] demoMover;
  private int curMover;
  private int moverPos;
  private int moverDir;
  private boolean moverOne;
  private boolean moverAnimacion;
  private int metrica;
  private String[] infoText;
  private int curInfoText;
  // El estado de botones
  private int buttonBar; // El modo de la barra del botón
  private int buttonHeight;
  private boolean dibujeBotones = true;
  private boolean pushed;
  private int buttonPressed = -1;
  private int progressHeight = 6;
  private int textHeight;
  private int moveText;
  private boolean outlined = true;
  // La transformación pospone para la compatibilidad con applet de Lares
  private static final int[] posFaceTransform = {3, 2, 0, 5, 1, 4};
  private static final int[][] posFaceletTransform = {
    {6, 3, 0, 7, 4, 1, 8, 5, 2}, // B +27
    {2, 5, 8, 1, 4, 7, 0, 3, 6}, // F +18
    {0, 1, 2, 3, 4, 5, 6, 7, 8}, // U +0
    {0, 1, 2, 3, 4, 5, 6, 7, 8}, // R +45
    {6, 3, 0, 7, 4, 1, 8, 5, 2}, // D +9
    {0, 1, 2, 3, 4, 5, 6, 7, 8}  // L +36
  };
  // para almacenar 
  private final int[] hex = new int[6];

  public void setCubo(Fcubo c){ 
    cube[0][6] = c.caras[0].casillas[0].getColor() + 10;
    cube[0][7] = c.caras[0].casillas[1].getColor() + 10;
    cube[0][8] = c.caras[0].casillas[2].getColor() + 10;
    cube[0][5] = c.caras[0].casillas[3].getColor() + 10;
    cube[0][2] = c.caras[0].casillas[4].getColor() + 10;
    cube[0][1] = c.caras[0].casillas[5].getColor() + 10;
    cube[0][0] = c.caras[0].casillas[6].getColor() + 10;
    cube[0][3] = c.caras[0].casillas[7].getColor() + 10;
    cube[0][4] = c.caras[0].casillas[8].getColor() + 10;
    
    cube[1][0] = c.caras[5].casillas[0].getColor() + 10;
    cube[1][3] = c.caras[5].casillas[1].getColor() + 10;
    cube[1][6] = c.caras[5].casillas[2].getColor() + 10;
    cube[1][7] = c.caras[5].casillas[3].getColor() + 10;
    cube[1][8] = c.caras[5].casillas[4].getColor() + 10;
    cube[1][5] = c.caras[5].casillas[5].getColor() + 10;
    cube[1][2] = c.caras[5].casillas[6].getColor() + 10;
    cube[1][1] = c.caras[5].casillas[7].getColor() + 10;
    cube[1][4] = c.caras[5].casillas[8].getColor() + 10;
     
    cube[2][0] = c.caras[2].casillas[0].getColor() + 10;
    cube[2][3] = c.caras[2].casillas[1].getColor() + 10;
    cube[2][6] = c.caras[2].casillas[2].getColor() + 10;
    cube[2][7] = c.caras[2].casillas[3].getColor() + 10;
    cube[2][8] = c.caras[2].casillas[4].getColor() + 10;
    cube[2][5] = c.caras[2].casillas[5].getColor() + 10;
    cube[2][2] = c.caras[2].casillas[6].getColor() + 10;
    cube[2][1] = c.caras[2].casillas[7].getColor() + 10;
    cube[2][4] = c.caras[2].casillas[8].getColor() + 10;

    cube[3][0] = c.caras[4].casillas[0].getColor() + 10;
    cube[3][3] = c.caras[4].casillas[1].getColor() + 10;
    cube[3][6] = c.caras[4].casillas[2].getColor() + 10;
    cube[3][7] = c.caras[4].casillas[3].getColor() + 10;
    cube[3][8] = c.caras[4].casillas[4].getColor() + 10;
    cube[3][5] = c.caras[4].casillas[5].getColor() + 10;
    cube[3][2] = c.caras[4].casillas[6].getColor() + 10;
    cube[3][1] = c.caras[4].casillas[7].getColor() + 10;
    cube[3][4] = c.caras[4].casillas[8].getColor() + 10;

    cube[4][2] = c.caras[1].casillas[0].getColor() + 10;
    cube[4][1] = c.caras[1].casillas[1].getColor() + 10;
    cube[4][0] = c.caras[1].casillas[2].getColor() + 10;
    cube[4][3] = c.caras[1].casillas[3].getColor() + 10;
    cube[4][6] = c.caras[1].casillas[4].getColor() + 10;
    cube[4][7] = c.caras[1].casillas[5].getColor() + 10;
    cube[4][8] = c.caras[1].casillas[6].getColor() + 10;
    cube[4][5] = c.caras[1].casillas[7].getColor() + 10;
    cube[4][4] = c.caras[1].casillas[8].getColor() + 10;

    cube[5][0] = c.caras[3].casillas[0].getColor() + 10;
    cube[5][3] = c.caras[3].casillas[1].getColor() + 10;
    cube[5][6] = c.caras[3].casillas[2].getColor() + 10;
    cube[5][7] = c.caras[3].casillas[3].getColor() + 10;
    cube[5][8] = c.caras[3].casillas[4].getColor() + 10;
    cube[5][5] = c.caras[3].casillas[5].getColor() + 10;
    cube[5][2] = c.caras[3].casillas[6].getColor() + 10;
    cube[5][1] = c.caras[3].casillas[7].getColor() + 10;
    cube[5][4] = c.caras[3].casillas[8].getColor() + 10;
    
    repaint(); 
  }
  
  
  public Fcubo getCubo(){ 
      Fcubo nc = new Fcubo();
      nc.caras[0].casillas[0].setColor((byte) (cube[0][6] - 10));
      nc.caras[0].casillas[1].setColor((byte) (cube[0][7] - 10));
      nc.caras[0].casillas[2].setColor((byte) (cube[0][8] - 10));
      nc.caras[0].casillas[3].setColor((byte) (cube[0][5] - 10));
      nc.caras[0].casillas[4].setColor((byte) (cube[0][2] - 10));
      nc.caras[0].casillas[5].setColor((byte) (cube[0][1] - 10));
      nc.caras[0].casillas[6].setColor((byte) (cube[0][0] - 10));
      nc.caras[0].casillas[7].setColor((byte) (cube[0][3] - 10));
      nc.caras[0].casillas[8].setColor((byte) (cube[0][4] - 10));
    
     nc.caras[5].casillas[0].setColor((byte) (cube[1][0] - 10));
     nc.caras[5].casillas[1].setColor((byte) (cube[1][3] - 10));
     nc.caras[5].casillas[2].setColor((byte) (cube[1][6] - 10));
     nc.caras[5].casillas[3].setColor((byte) (cube[1][7] - 10));
     nc.caras[5].casillas[4].setColor((byte) (cube[1][8] - 10));
     nc.caras[5].casillas[5].setColor((byte) (cube[1][5] - 10));
     nc.caras[5].casillas[6].setColor((byte) (cube[1][2] - 10));
     nc.caras[5].casillas[7].setColor((byte) (cube[1][1] - 10));
     nc.caras[5].casillas[8].setColor((byte) (cube[1][4] - 10));
     
    nc.caras[2].casillas[0].setColor((byte) (cube[2][0]- 10));
    nc.caras[2].casillas[1].setColor((byte) (cube[2][3]- 10));
    nc.caras[2].casillas[2].setColor((byte) (cube[2][6]- 10));
    nc.caras[2].casillas[3].setColor((byte) (cube[2][7]- 10));
    nc.caras[2].casillas[4].setColor((byte) (cube[2][8]- 10));
    nc.caras[2].casillas[5].setColor((byte) (cube[2][5]- 10));
    nc.caras[2].casillas[6].setColor((byte) (cube[2][2]- 10));
    nc.caras[2].casillas[7].setColor((byte) (cube[2][1]- 10));
    nc.caras[2].casillas[8].setColor((byte) (cube[2][4]- 10));

    nc.caras[4].casillas[0].setColor((byte) (cube[3][0]- 10));
    nc.caras[4].casillas[1].setColor((byte) (cube[3][3]- 10));
    nc.caras[4].casillas[2].setColor((byte) (cube[3][6]- 10));
    nc.caras[4].casillas[3].setColor((byte) (cube[3][7]- 10));
    nc.caras[4].casillas[4].setColor((byte) (cube[3][8]- 10));
    nc.caras[4].casillas[5].setColor((byte) (cube[3][5]- 10));
    nc.caras[4].casillas[6].setColor((byte) (cube[3][2]- 10));
    nc.caras[4].casillas[7].setColor((byte) (cube[3][1]- 10));
    nc.caras[4].casillas[8].setColor((byte) (cube[3][4]- 10));

    nc.caras[1].casillas[0].setColor((byte) (cube[4][2]- 10));
    nc.caras[1].casillas[1].setColor((byte) (cube[4][1]- 10));
    nc.caras[1].casillas[2].setColor((byte) (cube[4][0]- 10));
    nc.caras[1].casillas[3].setColor((byte) (cube[4][3]- 10));
    nc.caras[1].casillas[4].setColor((byte) (cube[4][6]- 10));
    nc.caras[1].casillas[5].setColor((byte) (cube[4][7]- 10));
    nc.caras[1].casillas[6].setColor((byte) (cube[4][8]- 10));
    nc.caras[1].casillas[7].setColor((byte) (cube[4][5]- 10));
    nc.caras[1].casillas[8].setColor((byte) (cube[4][4]- 10));

    nc.caras[3].casillas[0].setColor((byte) (cube[5][0]- 10));
    nc.caras[3].casillas[1].setColor((byte) (cube[5][3]- 10));
    nc.caras[3].casillas[2].setColor((byte) (cube[5][6]- 10));
    nc.caras[3].casillas[3].setColor((byte) (cube[5][7]- 10));
    nc.caras[3].casillas[4].setColor((byte) (cube[5][8]- 10));
    nc.caras[3].casillas[5].setColor((byte) (cube[5][5]- 10));
    nc.caras[3].casillas[6].setColor((byte) (cube[5][2]- 10));
    nc.caras[3].casillas[7].setColor((byte) (cube[5][1]- 10));
    nc.caras[3].casillas[8].setColor((byte) (cube[5][4]- 10));
      
    return(nc);
  }
  
  public void init() {
    // El registro para recibir todos los acontecimientos del ratón
     
    config.put("hint", "10");
    config.put("buttonbar","0");
    
    addMouseListener(this);
    addMouseMotionListener(this);
    // Los colores de esquema
    colores[0] = new Color(255, 128, 64);   // 0 - Ilumina naranja
    colores[1] = new Color(255, 0, 0);      // 1 - rojo puro
    colores[2] = new Color(0, 255, 0);      // 2 - verde puro
    colores[3] = new Color(0, 0, 255);      // 3 - azul puro
    colores[4] = new Color(153, 153, 153);  // 4 - gris blanco
    colores[5] = new Color(170, 170, 68);   // 5 - gris amarillo
    colores[6] = new Color(187, 119, 68);   // 6 - gris naranjado
    colores[7] = new Color(153, 68, 68);    // 7 - gris rojo
    colores[8] = new Color(68, 119, 68);    // 8 - gris verde
    colores[9] = new Color(0, 68, 119);     // 9 - Azul gris
    colores[10] = new Color(255, 255, 255); // W - blanco
    colores[11] = new Color(255, 255, 0);   // Y - amarillo
    colores[12] = new Color(255, 96, 32);   // O - naranja
    colores[13] = new Color(208, 0, 0);     // R - rojo
    colores[14] = new Color(0, 144, 0);     // G - verde
    colores[15] = new Color(32, 64, 208);   // B - azul
    colores[16] = new Color(176, 176, 176); // L - ilumina gris
    colores[17] = new Color(80, 80, 80);    // D - gris oscuro
    colores[18] = new Color(255, 0, 255);   // M - magenta
    colores[19] = new Color(0, 255, 255);   // C - cyan
    colores[20] = new Color(255, 160, 192); // P - rosado
    colores[21] = new Color(32, 255, 16);   // N - verde claro
    colores[22] = new Color(0, 0, 0);       // K - negro
    colores[23] = new Color(128, 128, 128); // . - gris
    // crea hilo de animación
    animThread = new Thread(this, "Animacion de Cubo");
    animThread.start();
    // configuración de defecto de esquema
    String param = getParameter("config");
    if (param != null) {
      try {
        URL url = new URL(getDocumentBase(), param);
        InputStream input = url.openStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String line = reader.readLine();
        while (line != null) {
          int pos = line.indexOf('=');
          if (pos > 0) {
            String key = line.substring(0, pos).trim();
            String value = line.substring(pos + 1).trim();
            config.put(key, value);
          }
          line = reader.readLine();
        }
        reader.close();
      }
      catch (MalformedURLException ex) {
        System.err.println("Malformed URL: " + param + ": " + ex);
      }
      catch (IOException ex) {
        System.err.println("Input error: " + param + ": " + ex);
      }
    }
    // color de fondo de la ventana de esquema
    param = getParameter("bgcolor");
    if (param != null && param.length() == 6) {
      for (int i = 0; i < 6; i++) {
        for (int j = 0; j < 16; j++) {
          if (Character.toLowerCase(param.charAt(i)) == "0123456789abcdef".charAt(j)) {
            hex[i] = j;
            break;
          }
        }
      }
      bgColor = new Color(hex[0] * 16 + hex[1], hex[2] * 16 + hex[3], hex[4] * 16 + hex[5]);
    }
    else
      bgColor = Color.gray;
    // color del fondo de la barra del botón de esquema
    param = getParameter("butbgcolor");
    if (param != null && param.length() == 6) {
      for (int i = 0; i < 6; i++) {
        for (int j = 0; j < 16; j++) {
          if (Character.toLowerCase(param.charAt(i)) == "0123456789abcdef".charAt(j)) {
            hex[i] = j;
            break;
          }
        }
      }
      buttonBgColor = new Color(hex[0] * 16 + hex[1], hex[2] * 16 + hex[3], hex[4] * 16 + hex[5]);
    }
    else
      buttonBgColor = bgColor;
    // colores aduaneros
    param = getParameter("colores");
    if (param != null) {
      for (int k = 0; k < 10 && k < param.length() / 6; k++) {
        for (int i = 0; i < 6; i++) {
          for (int j = 0; j < 16; j++) {
            if (Character.toLowerCase(param.charAt(k * 6 + i)) == "0123456789abcdef".charAt(j)) {
              hex[i] = j;
              break;
            }
          }
        }
        colores[k] = new Color(hex[0] * 16 + hex[1], hex[2] * 16 + hex[3], hex[4] * 16 + hex[5]);
      }
    }
    // Limpia el cubo
    for (int i = 0; i < 6; i++)
      for (int j = 0; j < 9; j++)
        cube[i][j] = i + 10;
    String posicionInicial = "lluu";
    //  configuración de color de esquema del cubo solucionado
    param = getParameter("colorescheme");
    if (param != null && param.length() == 6) {
      for (int i = 0; i < 6; i++) { 
        int color = 23;
        for (int j = 0; j < 23; j++) {
          if (Character.toLowerCase(param.charAt(i)) == "0123456789wyorgbldmcpnk".charAt(j)) {
            color = j;
            break;
          }
        }
        for (int j = 0; j < 9; j++)
          cube[i][j] = color;
      }
    }
    param = getParameter("pos");
    if (param != null && param.length() == 54) {
      posicionInicial = "uuuuff";
      if (bgColor == Color.gray)
        bgColor = Color.white;
      for (int i = 0; i < 6; i++) {
        int ti = posFaceTransform[i];
        for (int j = 0; j < 9; j++) {
          int tj = posFaceletTransform[i][j];
          cube[ti][tj] = 23;
          for (int k = 0; k < 14; k++) {
            // "abcdefgh" ~ "gbrwoyld"
            if (param.charAt(i * 9 + j) == "DFECABdfecabgh".charAt(k)) {
              cube[ti][tj] = k + 4;
              break;
            }
          }
        }
      }
    }
    param = getParameter("facelets");
    if (param != null && param.length() == 54) {
      for (int i = 0; i < 6; i++) {
        for (int j = 0; j < 9; j++) {
          cube[i][j] = 23;
          for (int k = 0; k < 23; k++) {
            if (Character.toLowerCase(param.charAt(i * 9 + j)) == "0123456789wyorgbldmcpnk".charAt(k)) {
              cube[i][j] = k;
              break;
            }
          }
        }
      }
    }
    // secuencia de movimiento de esquema (y los textos de información)
    param = getParameter("move");
    mover = (param == null ? new int[0][0] : getMove(param, true));
    moverPos = 0;
    curInfoText = -1;
    // secuencia de movimiento de la inicial de esquema
    param = getParameter("initmove");
    if (param != null) {
      int[][] initialMove = param.equals("#") ? mover : getMove(param, false);
      if (initialMove.length > 0)
        doMove(cube, initialMove[0], 0, initialMove[0].length, false);
    }
    // inicial de esquema puso al revés secuencia de movimiento
    param = getParameter("initrevmove");
    if (param != null) {
      int[][] initialReversedMove = param.equals("#") ? mover : getMove(param, false);
      if (initialReversedMove.length > 0)
        doMove(cube, initialReversedMove[0], 0, initialReversedMove[0].length, true);
    }
    // inicial de esquema puso al revés secuencia de movimiento
    param = getParameter("demo");
    if (param != null) {
      demoMover = param.equals("#") ? mover : getMove(param, true);
      if (demoMover.length > 0 && demoMover[0].length > 0)
        demo = true;
    }
    // posición del cubo de la inicial de esquema
    param = getParameter("posicion");
    vNorm(vMul(ojoY, ojo, ojoX));
    if (param == null)
      param = posicionInicial;
    double pi12 = Math.PI / 12;
    for (int i = 0; i < param.length(); i++) {
      double angulo = pi12;
      switch (Character.toLowerCase(param.charAt(i))) {
       case 'd':
        angulo = -angulo;
       case 'u':
        vRotY(ojo, angulo);
        vRotY(ojoX, angulo);
        break;
       case 'f':
        angulo = -angulo;
       case 'b':
        vRotZ(ojo, angulo);
        vRotZ(ojoX, angulo);
        break;
       case 'l':
        angulo = -angulo;
       case 'r':
        vRotX(ojo, angulo);
        vRotX(ojoX, angulo);
        break;
      }
    }
    vNorm(vMul(ojoY, ojo, ojoX)); // fix eyeY
    //velocidad de cuarto de vuelta de esquema y la vuelta de doble aceleran
    velocidad = 0;
    dobleVelocidad = 0;
    param = getParameter("speed");
    if (param != null)
      for (int i = 0; i < param.length(); i++)
        if (param.charAt(i) >= '0' && param.charAt(i) <= '9')
          velocidad = velocidad * 10 + (int)param.charAt(i) - '0';
    param = getParameter("doublespeed");
    if (param != null)
      for (int i = 0; i < param.length(); i++)
        if (param.charAt(i) >= '0' && param.charAt(i) <= '9')
          dobleVelocidad = dobleVelocidad * 10 + (int)param.charAt(i) - '0';
    if (velocidad == 0)
      velocidad = 10;
    if (dobleVelocidad == 0)
      dobleVelocidad = velocidad * 3 / 2;
    // deformación perspectiva
    deformacion = 0;
    param = getParameter("perspective");
    if (param == null)
      deformacion = 2;
    else
      for (int i = 0; i < param.length(); i++)
        if (param.charAt(i) >= '0' && param.charAt(i) <= '9')
          deformacion = deformacion * 10 + (int)param.charAt(i) - '0';
    // escama del cubo
    int intscale = 0;
    param = getParameter("scale");
    if (param != null)
      for (int i = 0; i < param.length(); i++)
        if (param.charAt(i) >= '0' && param.charAt(i) <= '9')
          intscale = intscale * 10 + (int)param.charAt(i) - '0';
    escama = 1.0 / (1.0 + intscale / 10.0);
    indicio = false;
    param = getParameter("hint");
    if (param != null) {
      indicio = true;
      cambioCara = 0.0;
      for (int i = 0; i < param.length(); i++)
        if (param.charAt(i) >= '0' && param.charAt(i) <= '9')
          cambioCara = cambioCara * 10 + (int)param.charAt(i) - '0';
      if (cambioCara < 1.0)
        indicio = false;
      else
        cambioCara /= 10.0;
    }
    // apariencia y la configuración de la barra del botón
    buttonBar = 1;
    buttonHeight = 13;
    progressHeight = mover.length == 0 ? 0 : 6;
    param = getParameter("buttonbar");
    if ("0".equals(param)) {
      buttonBar = 0;
      buttonHeight = 0;
      progressHeight = 0;
    }
    else if ("1".equals(param))
      buttonBar = 1;
    else if ("2".equals(param) || mover.length == 0) {
      buttonBar = 2;
      progressHeight = 0;
    }
    // cubo puede ser editado con ratón
    param = getParameter("edit");
    if ("0".equals(param))
      editRatonCubo = false;
    else
      editRatonCubo = true;
    //  representación textual del movimiento
    param = getParameter("movetext");
    if ("1".equals(param))
      moveText = 1;
    else if ("2".equals(param))
      moveText = 2;
    else if ("3".equals(param))
      moveText = 3;
    else if ("4".equals(param))
      moveText = 4;
    else
      moveText = 0;
    param = getParameter("fonttype");
    if (param == null || "1".equals(param))
      outlined = true;
    else
      outlined = false;
    metrica = 0;
    param = getParameter("metric");
    if (param != null) {
      if ("1".equals(param))
        metrica = 1;
      else if ("2".equals(param))
        metrica = 2;
      else if ("3".equals(param))
        metrica = 3;
    }
    // Métrico
    alineacion = 1;
    param = getParameter("align");
    if (param != null) {
      if ("0".equals(param)) // superior
        alineacion = 0;
      else if ("1".equals(param)) // centro
        alineacion = 1;
      else if ("2".equals(param)) // fondo
        alineacion = 2;
    }
    // valores de la inicial de esquema
    for (int i = 0; i < 6; i++)
      for (int j = 0; j < 9; j++)
        inicialCubo[i][j] = cube[i][j];
    for (int i = 0; i < 3; i++) {
      inicialOjo[i] = ojo[i];
      inicialOjoX[i] = ojoX[i];
      inicialOjoY[i] = ojoY[i];
    }
    // colores de esquema (haga contraste)
    int rojo = bgColor.getRed();
    int verde = bgColor.getGreen();
    int azul = bgColor.getBlue();
    int average = (rojo * 299 + verde * 587 + azul * 114) / 1000;
    if (average < 128) {
      textColor = Color.white;
      hlColor = bgColor.brighter();
      hlColor = new Color(hlColor.getBlue(), hlColor.getRed(), hlColor.getGreen());
    }
    else {
      textColor = Color.black;
      hlColor = bgColor.darker();
      hlColor = new Color(hlColor.getBlue(), hlColor.getRed(), hlColor.getGreen());
    }
    bgColor2 = new Color(rojo / 2, verde / 2, azul / 2);
    curInfoText = -1;
    if (demo)
      startAnimation(-1);
  }

  public String getParameter(String nombre) { 
    //String parametro = super.getParametro(name);
    //if (parametro == null)
      return (String)config.get(nombre);
    //return parametro;
  }

  private static final int[] moverModos = {
    0, 0, 0, 0, 0, 0, // UDFBLR
    1, 1, 1,          // ESM
    3, 3, 3, 3, 3, 3, // XYZxyz
    2, 2, 2, 2, 2, 2  // udfblr
  };
  private static final int[] moveCodes = {
    0, 1, 2, 3, 4, 5, // UDFBLR
    1, 2, 4,          // ESM
    5, 2, 0, 5, 2, 0, // XYZxyz
    0, 1, 2, 3, 4, 5  // udfblr
  };

  private int[][] getMove(String secuencia, boolean info) {
    if (info) {
      int inum = 0;
      int pos = secuencia.indexOf('{');
      while (pos != -1) {
        inum++;
        pos = secuencia.indexOf('{', pos + 1);
      }
      if (infoText == null) {
        curInfoText = 0;
        infoText = new String[inum];
      }
      else {
        String[] infoText2 = new String[infoText.length + inum];
        for (int i = 0; i < infoText.length; i++)
          infoText2[i] = infoText[i];
        curInfoText = infoText.length;
        infoText = infoText2;
      }
    }
    int num = 1;
    int pos = secuencia.indexOf(';');
    while (pos != -1) {
      num++;
      pos = secuencia.indexOf(';', pos + 1);
    }
    int[][] move = new int[num][];
    int lastPos = 0;
    pos = secuencia.indexOf(';');
    num = 0;
    while (pos != -1) {
      move[num++] = getMovePart(secuencia.substring(lastPos, pos), info);
      lastPos = pos + 1;
      pos = secuencia.indexOf(';', lastPos);
    }
    move[num] = getMovePart(secuencia.substring(lastPos), info);
    return move;
  }

  private static final char[] modeChar = {'m', 't', 'c', 's', 'a'};

  private int[] getMovePart(String Secuencia, boolean info) {
    int longitud = 0;
    int[] move = new int[Secuencia.length()];
    for (int i = 0; i < Secuencia.length(); i++) {
      if (Secuencia.charAt(i) == '.') {
        move[longitud] = -1;
        longitud++;
      }
      else if (Secuencia.charAt(i) == '{') {
        i++;
        String s = "";
        while (i < Secuencia.length()) {
          if (Secuencia.charAt(i) == '}')
            break;
          if (info)
            s += Secuencia.charAt(i);
          i++;
        }
        if (info) {
          infoText[curInfoText] = s;
          move[longitud] = 1000 + curInfoText;
          curInfoText++;
          longitud++;
        }
      }
      else {
        for (int j = 0; j < 21; j++) {
          if (Secuencia.charAt(i) == "UDFBLRESMXYZxyzudfblr".charAt(j)) {
            i++;
            int mode = moverModos[j];
            move[longitud] = moveCodes[j] * 24;
            if (i < Secuencia.length()) {
              if (moverModos[j] == 0) { 
                for (int k = 0; k < modeChar.length; k++) {
                  if (Secuencia.charAt(i) == modeChar[k]) {
                    mode = k + 1;
                    i++;
                    break;
                  }
                }
              }
            }
            move[longitud] += mode * 4;
            if (i < Secuencia.length()) {
              if (Secuencia.charAt(i) == '1')
                i++;
              else if (Secuencia.charAt(i) == '\'' || Secuencia.charAt(i) == '3') {
                move[longitud] += 2;
                i++;
              }
              else if (Secuencia.charAt(i) == '2') {
                i++;
                if (i < Secuencia.length() && Secuencia.charAt(i) == '\'') {
                  move[longitud] += 3;
                  i++;
                }
                else
                  move[longitud] += 1;
              }
            }
            longitud++;
            i--;
            break;
          }
        }
      }
    }
    int[] returnMove = new int[longitud];
    for (int i = 0; i < longitud; i++)
      returnMove[i] = move[i];
    return returnMove;
  }

  private String moveText(int[] move, int start, int end) {
    if (start >= move.length)
      return "";
    String s = "";
    for (int i = start; i < end; i++)
      s += turnText(move, i);
    return s;
  }

  private static final String[][][] simboloVuelta = {
    { // "notacion
      {"U", "D", "F", "B", "L", "R"},
      {"Um", "Dm", "Fm", "Bm", "Lm", "Rm"},
      {"Ut", "Dt", "Ft", "Bt", "Lt", "Rt"},
      {"Uc", "Dc", "Fc", "Bc", "Lc", "Rc"},
      {"Us", "Ds", "Fs", "Bs", "Ls", "Rs"},
      {"Ua", "Da", "Fa", "Ba", "La", "Ra"}
    },
    { 
      {"U", "D", "F", "B", "L", "R"},
      {"~E", "E", "S", "~S", "M", "~M"},
      {"u", "d", "f", "b", "l", "r"},
      {"Z", "~Z", "Y", "~Y", "~X", "X"},
      {"Us", "Ds", "Fs", "Bs", "Ls", "Rs"},
      {"Ua", "Da", "Fa", "Ba", "La", "Ra"}
    },
    { 
      {"U", "D", "F", "B", "L", "R"},
      {"~E", "E", "S", "~S", "M", "~M"},
      {"u", "d", "f", "b", "l", "r"},
      {"Y", "~Y", "Z", "~Z", "~X", "X"},
      {"Us", "Ds", "Fs", "Bs", "Ls", "Rs"},
      {"Ua", "Da", "Fa", "Ba", "La", "Ra"}
    },
    { 
      {"U", "D", "F", "B", "L", "R"},
      {"u", "d", "f", "b", "l", "r"},
      {"Uu", "Dd", "Ff", "Bb", "Ll", "Rr"},
      {"QU", "QD", "QF", "QB", "QL", "QR"},
      {"UD'", "DU'", "FB'", "BF'", "LR'", "RL'"},
      {"UD", "DU", "FB", "BF", "LR", "RL"}
    }
  };
  private static final String[] modificaForma = {"", "2", "'", "2'"};

  private String turnText(int[] move, int pos) {
    if (pos >= move.length)
      return "";
    if (move[pos] >= 1000)
      return "";
    if (move[pos] == -1)
      return ".";
    String s = simboloVuelta[moveText - 1][move[pos] / 4 % 6][move[pos] / 24];
    if (s.charAt(0) == '~')
      return s.substring(1) + modificaForma[(move[pos] + 2) % 4];
    return s + modificaForma[move[pos] % 4];
  }

  private static final String[] metricChar = {"", "q", "f", "s"};

  private static int realMoveLength(int[] move) {
    int length = 0;
    for (int i = 0; i < move.length; i++)
      if (move[i] < 1000)
        length++;
    return length;
  }

  private static int realMovePos(int[] move, int pos) {
    int rpos = 0;
    for (int i = 0; i < pos; i++)
      if (move[i] < 1000)
        rpos++;
    return rpos;
  }

  private static int arrayMovePos(int[] move, int realPos) {
    int pos = 0;
    int rpos = 0;
    while (true) {
      while (pos < move.length && move[pos] >= 1000)
        pos++;
      if (rpos == realPos)
        break;
      if (pos < move.length) {
        rpos++;
        pos++;
      }
    }
    return pos;
  }

  private int moveLength(int[] move, int end) {
    int length = 0;
    for (int i = 0; i < move.length && (i < end || end < 0); i++)
      length += turnLength(move[i]);
    return length;
  }

  private int turnLength(int turn) {
    if (turn < 0 || turn >= 1000)
      return 0;
    int modifier = turn % 4;
    int mode = turn / 4 % 6;
    int n = 1;
    switch (metrica) {
     case 1: 
      if (modifier == 1 || modifier == 3)
        n *= 2;
     case 2:
      if (mode == 1 || mode == 4 || mode == 5)
        n *= 2;
     case 3:
      if (mode == 3)
        n = 0;
    }
    return n;
  }

  private void initInfoText(int[] move) {
    if (move.length > 0 && move[0] >= 1000)
      curInfoText = move[0] - 1000;
    else
      curInfoText = -1;
  }

  private void doMove(int[][] cubo, int[] mover, int prencipio, int longitud, boolean reves) {
    int posicion = reves ? prencipio + longitud : prencipio;
    while (true) {
      if (reves) {
        if (posicion <= prencipio)
          break;
        posicion--;
      }
      if (mover[posicion] >= 1000) {
        curInfoText = reves ? -1 : mover[posicion] - 1000;
      }
      else if (mover[posicion] >= 0) {
        int modificador = mover[posicion] % 4 + 1;
        int modo = mover[posicion] / 4 % 6;
        if (modificador == 4) 
          modificador = 2;
        if (reves)
          modificador = 4 - modificador;
        retuerzaEstratos(cubo, mover[posicion] / 24, modificador, modo);
      }
      if (!reves) {
        posicion++;
        if (posicion >= prencipio + longitud)
          break;
      }
    }
  }

  private Thread animThread = null; // hilo para realizar la animación

  private void startAnimation(int mode) {
    synchronized (animThread) {
      cerrarAnimacion();
      if (!demo && (mover.length == 0 || mover[curMover].length == 0))
        return;
      if (demo && (demoMover.length == 0 || demoMover[0].length == 0))
        return;
      moverDir = 1;
      moverOne = false;
      moverAnimacion = true;
      switch (mode) {
       case 0: // adelante
        break;
       case 1: // Atrás
        moverDir = -1;
        break;
       case 2: 
        moverOne = true;
        break;
       case 3: 
        moverDir = -1;
        moverOne = true;
        break;
       case 4: 
        moverAnimacion = false;
        break;
      }
      animThread.notify();
    }
  }

  public void cerrarAnimacion() {
    synchronized (animThread) {
      animacionDetenida = true;
      animThread.notify();
      try {
        animThread.wait();
      }
      catch (InterruptedException e) {
        cortaHilo = true;
      }
      animacionDetenida = false;
    }
  }

  public void run() {
    synchronized (animThread) {
      cortaHilo = false;
      do {
        if (animacionDetenida) {
          animThread.notify();
        }
        try {
          animThread.wait();
        }
        catch (InterruptedException e) {
          break;
        }
        if (animacionDetenida)
          continue;
        boolean restart = false;
        animando = true;
        dibujeBotones = true;
        int[] mv = demo ? demoMover[0] : mover[curMover];
        if (moverDir > 0) {
          if (moverPos >= mv.length) {
            moverPos = 0;
            initInfoText(mv);
          }
        }
        else {
          curInfoText = -1;
          if (moverPos == 0)
            moverPos = mv.length;
        }
        while (true) {
          if (moverDir < 0) {
            if (moverPos == 0)
              break;
            moverPos--;
          }
          if (mv[moverPos] == -1) {
            repaint();
            if (!moverOne)
              sleep(33 * velocidad);
          }
          else if (mv[moverPos] >= 1000) {
            curInfoText = moverDir > 0 ? mv[moverPos] - 1000 : -1;
          }
          else {
            int num = mv[moverPos] % 4 + 1;
            int mode = mv[moverPos] / 4 % 6;
            boolean sentidoReloj = num < 3;
            if (num == 4)
              num = 2;
            if (moverDir < 0) {
              sentidoReloj = !sentidoReloj;
              num = 4 - num;
            }
            spin(mv[moverPos] / 24, num, mode, sentidoReloj, moverAnimacion);
            if (moverOne)
              restart = true;
          }
          if (moverDir > 0) {
            moverPos++;
            if (moverPos < mv.length && mv[moverPos] >= 1000) {
              curInfoText = mv[moverPos] - 1000;
              moverPos++;
            }
            if (moverPos == mv.length) {
              if (!demo)
                break;
              moverPos = 0;
              initInfoText(mv);
              for (int i = 0; i < 6; i++)
                for (int j = 0; j < 9; j++)
                  cube[i][j] = inicialCubo[i][j];
            }
          }
          else
            curInfoText = -1;
          if (cortaHilo || animacionDetenida || restart)
            break;
        }
        animando = false;
        dibujeBotones = true;
        repaint();
        if (demo) {
          clear();
          demo = false;
        }
      } while (!cortaHilo);
    }
  } 

  private void sleep(int time) {
    synchronized (animThread) {
      try {
        animThread.wait(time);
      }
      catch (InterruptedException e) {
        cortaHilo = true;
      }
    }
  }

  private void clear() {
    synchronized (animThread) {
      moverPos = 0;
      if (mover.length > 0)
        initInfoText(mover[curMover]);
      natural = true;
      imagenVistaCubo = false;
      for (int i = 0; i < 6; i++)
        for (int j = 0; j < 9; j++)
          cube[i][j] = inicialCubo[i][j];
      for (int i = 0; i < 3; i++) {
        ojo[i] = inicialOjo[i];
        ojoX[i] = inicialOjoX[i];
        ojoY[i] = inicialOjoY[i];
      }
    }
  }

  private void spin(int estrato, int num, int modo, boolean sentidoReloj, boolean animado) {
    usuarioTorceCubo = false;
    natural = true;
    dandovueltas = true;
    angulooriginal = 0;
    if (caraTD[estrato] > 0)
      sentidoReloj = !sentidoReloj;
    if (animado) {
      double phit = Math.PI / 2; 
      double phis = sentidoReloj ? 1.0 : -1.0;
      int tiempoVuelta = 67 * velocidad; 
      if (num == 2) {
        phit = Math.PI;
        tiempoVuelta = 67 * dobleVelocidad;
      }
      usuarioTorceCubo = true;
      capa = estrato;
      modocapa = modo;
      cuboDividido(estrato);
      long sTiempo = System.currentTimeMillis();
      long lTime = sTiempo;
      double d = phis * phit / tiempoVuelta;
      for (anguloeditado = 0; anguloeditado * phis < phit; anguloeditado = d * (lTime - sTiempo)) {
        repaint();
        sleep(25);
        if (cortaHilo || animacionDetenida)
          break;
        lTime = System.currentTimeMillis();
      }
    }
    anguloeditado = 0;
    usuarioTorceCubo = false;
    natural = true;
    retuerzaEstratos(cube, estrato, num, modo);
    dandovueltas = false;
    if (animado)
      repaint();
  }

  //numero de deminsiones de cubo
  private static final int[][][] cubobloque = {
    {{0, 3}, {0, 3}}, // U
    {{0, 3}, {0, 3}}, // D
    {{0, 3}, {0, 3}}, // F
    {{0, 3}, {0, 3}}, // B
    {{0, 3}, {0, 3}}, // L
    {{0, 3}, {0, 3}}  // R
  };
  // deminsiones subcubo 
  private final int[][][] topBloque = new int[6][][];
  private final int[][][] midBloque = new int[6][][];
  private final int[][][] botBloque = new int[6][][];
  private static final int[][][] CoroTablaBloque = {
    {{0, 0}, {0, 0}},
    {{0, 3}, {0, 3}},
    {{0, 3}, {0, 1}},
    {{0, 1}, {0, 3}},
    {{0, 3}, {2, 3}},
    {{2, 3}, {0, 3}}
  };
  private static final int[][][] midiaBloqueTabla = {
    {{0, 0}, {0, 0}},
    {{0, 3}, {1, 2}},
    {{1, 2}, {0, 3}}
  };
  private static final int[][] sobrepaseCaraBloque = {
  // U  D  F  B  L  R
    {1, 0, 3, 3, 2, 3}, // U
    {0, 1, 5, 5, 4, 5}, // D
    {2, 3, 1, 0, 3, 2}, // F
    {4, 5, 0, 1, 5, 4}, // B
    {3, 2, 2, 4, 1, 0}, // L
    {5, 4, 4, 2, 0, 1}  // R
  };
  private static final int[][] mediaCaraBloque = {
  // U  D  F  B  L  R
    {0, 0, 2, 2, 1, 2}, // U
    {0, 0, 2, 2, 1, 2}, // D
    {1, 2, 0, 0, 2, 1}, // F
    {1, 2, 0, 0, 2, 1}, // B
    {2, 1, 1, 1, 0, 0}, // L
    {2, 1, 1, 1, 0, 0}  // R
  };
  private static final int[][] caraBloqueMoscard = {
  // U  D  F  B  L  R
    {0, 1, 5, 5, 4, 5}, // U
    {1, 0, 3, 3, 2, 3}, // D
    {4, 5, 0, 1, 5, 4}, // F
    {2, 3, 1, 0, 3, 2}, // B
    {5, 4, 4, 2, 0, 1}, // L
    {3, 2, 2, 4, 1, 0}  // R
  };

  private void cuboDividido(int layer) {
    for (int i = 0; i < 6; i++) { 
      topBloque[i] = CoroTablaBloque[sobrepaseCaraBloque[layer][i]];
      botBloque[i] = CoroTablaBloque[caraBloqueMoscard[layer][i]];
      midBloque[i] = midiaBloqueTabla[mediaCaraBloque[layer][i]];
    }
    natural = false;
  }

  private void retuerzaEstratos(int[][] cubo, int estrato, int num, int modo) {
    switch (modo) {
     case 3:
      estratoTorsion(cubo, estrato ^ 1, num, false);
     case 2:
      estratoTorsion(cubo, estrato, 4 - num, false);
     case 1:
      estratoTorsion(cubo, estrato, 4 - num, true);
      break;
     case 5:
      estratoTorsion(cubo, estrato ^ 1, 4 - num, false);
      estratoTorsion(cubo, estrato, 4 - num, false);
      break;
     case 4:
      estratoTorsion(cubo, estrato ^ 1, num, false);
     default:
      estratoTorsion(cubo, estrato, 4 - num, false);
    }
  }

  // ciclo
  private static final int[] ordenRec = {0, 1, 2, 5, 8, 7, 6, 3};
  private static final int[] factoresRec = {1, 3, -1, -3, 1, 3, -1, -3};
  private static final int[] OffsetsRec = {0, 2,  8,  6, 3, 1,  5,  7};
  private static final int[][] recTomaParte = {
    {3, 3, 3, 0}, // U: F=6-3k R=6-3k B=6-3k L=k
    {2, 1, 1, 1}, // D: L=8-k  B=2+3k R=2+3k F=2+3k
    {3, 3, 0, 0}, // F: L=6-3k D=6-3k R=k    U=k
    {2, 1, 1, 2}, // B: R=8-k  D=2+3k L=2+3k U=8-k
    {3, 2, 0, 0}, // L: U=6-3k B=8-k  D=k    F=k
    {2, 2, 0, 1}  // R: F=8-k  D=8-k  B=k    U=2+3k
  };
  // indices for sides of center layers
  private static final int[][] RecCentro = {
    {7, 7, 7, 4}, // E'(U): F=7-3k R=7-3k B=7-3k L=3+k
    {6, 5, 5, 5}, // E (D): L=5-k  B=1+3k R=1+3k F=1+3k
    {7, 7, 4, 4}, // S (F): L=7-3k D=7-3k R=3+k  U=3+k
    {6, 5, 5, 6}, // S'(B): R=5-k  D=1+3k L=1+3k U=5-k
    {7, 6, 4, 4}, // M (L): U=7-3k B=8-k  D=3+k  F=3+k
    {6, 6, 4, 5}  // M'(R): F=5-k  D=5-k  B=3+k  U=1+3k
  };
  private final int[] amortTorcion = new int[12];

  private void estratoTorsion(int[][] cube, int layer, int num, boolean middle) {
    if (!middle) {
      for (int i = 0; i < 8; i++)
        amortTorcion[(i + num * 2) % 8] = cube[layer][ordenRec[i]];
      for (int i = 0; i < 8; i++)
        cube[layer][ordenRec[i]] = amortTorcion[i];
    }
    int k = num * 3;
    for (int i = 0; i < 4; i++) {
      int n = adjacentccara[layer][i];
      int c = middle ? RecCentro[layer][i] : recTomaParte[layer][i];
      int factor = factoresRec[c];
      int offset = OffsetsRec[c];
      for (int j = 0; j < 3; j++) {
        amortTorcion[k % 12] = cube[n][j * factor + offset];
        k++;
      }
    }
    k = 0; 
    for (int i = 0; i < 4; i++) { 
      int n = adjacentccara[layer][i];
      int c = middle ? RecCentro[layer][i] : recTomaParte[layer][i];
      int factor = factoresRec[c];
      int offset = OffsetsRec[c];
      int j = 0; 
      while (j < 3) {
        cube[n][j * factor + offset] = amortTorcion[k];
        j++;
        k++;
      }
    }
  }

  // Duplique animación moderada
  private Graphics graficos = null;
  private Image imagen = null;
  // El tamaño de la ventana del cubo
  private int anchura;
  private int altura;
  // última posición de ratón
  private int dureX;
  private int dureY; 
  private int dureObstaculoX;
  private int dureObstaculoY;
  // areas
  private int obstaculoAreas;
  private final int[][] obstaculoEsquinaX = new int[18][4];
  private final int[][] obstaculoEsquinaY = new int[18][4];
  private final double[] obstaculoDirsX = new double[18];
  private final double[] obstaculoDirsY = new double[18];
  private static final int[][][] torsionBloque = {
    {{0, 0}, {3, 0}, {3, 1}, {0, 1}},
    {{3, 0}, {3, 3}, {2, 3}, {2, 0}},
    {{3, 3}, {0, 3}, {0, 2}, {3, 2}},
    {{0, 3}, {0, 0}, {1, 0}, {1, 3}},
    // centro
    {{0, 1}, {3, 1}, {3, 2}, {0, 2}},
    {{2, 0}, {2, 3}, {1, 3}, {1, 0}}
  };
  private static final int[][] areaDirs = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}, {1, 0}, {0, 1}};
  private static final int[][] torsionDirs = {
    { 1,  1,  1,  1,  1, -1}, // U
    { 1,  1,  1,  1,  1, -1}, // D
    { 1, -1,  1, -1,  1,  1}, // F
    { 1, -1,  1, -1, -1,  1}, // B
    {-1,  1, -1,  1, -1, -1}, // L
    { 1, -1,  1, -1,  1,  1}  // R
  };
  private int[] obstaculosEstratos = new int[18]; 
  private int[] obstaculoModos = new int[18]; 
  // direcciones actuales
  private double obstaculoX;
  private double obstaculoY;
  // direcciones de rotaciones
  private static final int[][][] rotCos = {
    {{ 1,  0,  0}, { 0,  0,  0}, { 0,  0,  1}}, // U-D
    {{ 1,  0,  0}, { 0,  1,  0}, { 0,  0,  0}}, // F-B
    {{ 0,  0,  0}, { 0,  1,  0}, { 0,  0,  1}}  // L-R
  };
  private static final int[][][] rotSin = {
    {{ 0,  0,  1}, { 0,  0,  0}, {-1,  0,  0}}, // U-D
    {{ 0,  1,  0}, {-1,  0,  0}, { 0,  0,  0}}, // F-B
    {{ 0,  0,  0}, { 0,  0,  1}, { 0, -1,  0}}  // L-R
  };
  private static final int[][][] rotVec = {
    {{ 0,  0,  0}, { 0,  1,  0}, { 0,  0,  0}}, // U-D
    {{ 0,  0,  0}, { 0,  0,  0}, { 0,  0,  1}}, // F-B
    {{ 1,  0,  0}, { 0,  0,  0}, { 0,  0,  0}}  // L-R
  };
  private static final int[] rotSign = {1, -1, 1, -1, 1, -1}; // U, D, F, B, L, R
  // vectores temporalesn
  private final double[] tempOjo = new double[3];
  private final double[] tempOjoX = new double[3];
  private final double[] tempOjoY = new double[3];
  private final double[] tempOjo2 = new double[3];
  private final double[] tempOjoX2 = new double[3];
  private final double[] tempOjoY2 = new double[3];
  private final double[] perspOjo = new double[3];
  private final double[] perspOjoI = new double[3];
  private final double[] perspNormal = new double[3];
  private final double[][] ojoArray = new double[3][];
  private final double[][] ojoArrayX = new double[3][];
  private final double[][] ojoArrayY = new double[3][];
  private final int[][] ojoOrder = {{1, 0, 0}, {0, 1, 0}, {1, 1, 0}, {1, 1, 1}, {1, 0, 1}, {1, 0, 2}};
  private final int[][][][] bloqueArray = new int[3][][][];
  private final int[][] blockModo = {{0, 2, 2}, {2, 1, 2}, {2, 2, 2}, {2, 2, 2}, {2, 2, 2}, {2, 2, 2}};
  private final int[][] extrategiaOrden = {{0, 1, 2}, {2, 1, 0}, {0, 2, 1}};

  public void paint(Graphics g) {
    Dimension size = getSize();
    //  almacenamiento intermedio doble
    if (imagen == null || size.width != anchura || size.height - buttonHeight != altura) {
      anchura = size.width;
      altura = size.height;
      imagen = createImage(anchura, altura);
      graficos = imagen.getGraphics();
      textHeight = graficos.getFontMetrics().getHeight() - graficos.getFontMetrics().getLeading();
      if (buttonBar == 1)
        altura -= buttonHeight;
      dibujeBotones = true;
    }
    graficos.setColor(bgColor);
    graficos.setClip(0, 0, anchura, altura);
    graficos.fillRect(0, 0, anchura, altura);
    synchronized (animThread) {
      obstaculoAreas = 0;
      if (natural)
        bloqueApuro(ojo, ojoX, ojoY, cubobloque, 3); // Dibuja cubo y llena áreas de obstáculo
      else {
        double cosA = Math.cos(angulooriginal + anguloeditado);
        double sinA = Math.sin(angulooriginal + anguloeditado) * rotSign[capa];
        for (int i = 0; i < 3; i++) {
          tempOjo[i] = 0;
          tempOjoX[i] = 0;
          for (int j = 0; j < 3; j++) {
            int axis = capa / 2;
            tempOjo[i] += ojo[j] * (rotVec[axis][i][j] + rotCos[axis][i][j] * cosA + rotSin[axis][i][j] * sinA);
            tempOjoX[i] += ojoX[j] * (rotVec[axis][i][j] + rotCos[axis][i][j] * cosA + rotSin[axis][i][j] * sinA);
          }
        }
        vMul(tempOjoY, tempOjo, tempOjoX);
        double cosB = Math.cos(angulooriginal - anguloeditado);
        double sinB = Math.sin(angulooriginal - anguloeditado) * rotSign[capa];
        for (int i = 0; i < 3; i++) {
          tempOjo2[i] = 0;
          tempOjoX2[i] = 0;
          for (int j = 0; j < 3; j++) {
            int axis = capa / 2;
            tempOjo2[i] += ojo[j] * (rotVec[axis][i][j] + rotCos[axis][i][j] * cosB + rotSin[axis][i][j] * sinB);
            tempOjoX2[i] += ojoX[j] * (rotVec[axis][i][j] + rotCos[axis][i][j] * cosB + rotSin[axis][i][j] * sinB);
          }
        }
        vMul(tempOjoY2, tempOjo2, tempOjoX2);
        ojoArray[0] = ojo;
        ojoArrayX[0] = ojoX;
        ojoArrayY[0] = ojoY;
        ojoArray[1] = tempOjo;
        ojoArrayX[1] = tempOjoX;
        ojoArrayY[1] = tempOjoY;
        ojoArray[2] = tempOjo2;
        ojoArrayX[2] = tempOjoX2;
        ojoArrayY[2] = tempOjoY2;
        bloqueArray[0] = topBloque;
        bloqueArray[1] = midBloque;
        bloqueArray[2] = botBloque;
        // coreccioens
        vSub(vScale(vCopy(perspOjo, ojo), 5.0 + deformacion), vScale(vCopy(perspNormal, vnormales[capa]), 1.0 / 3.0));
        vSub(vScale(vCopy(perspOjoI, ojo), 5.0 + deformacion), vScale(vCopy(perspNormal, vnormales[capa ^ 1]), 1.0 / 3.0));
        double topProd = vProd(perspOjo, vnormales[capa]);
        double botProd = vProd(perspOjoI, vnormales[capa ^ 1]);
        int modoOrden;
        if (topProd < 0 && botProd > 0)
          modoOrden = 0;
        else if (topProd > 0 && botProd < 0)
          modoOrden = 1;
        else 
          modoOrden = 2;
        bloqueApuro(ojoArray[ojoOrder[modocapa][extrategiaOrden[modoOrden][0]]],
                 ojoArrayX[ojoOrder[modocapa][extrategiaOrden[modoOrden][0]]],
                 ojoArrayY[ojoOrder[modocapa][extrategiaOrden[modoOrden][0]]],
                 bloqueArray[extrategiaOrden[modoOrden][0]],
                 blockModo[modocapa][extrategiaOrden[modoOrden][0]]);
        bloqueApuro(ojoArray[ojoOrder[modocapa][extrategiaOrden[modoOrden][1]]],
                 ojoArrayX[ojoOrder[modocapa][extrategiaOrden[modoOrden][1]]],
                 ojoArrayY[ojoOrder[modocapa][extrategiaOrden[modoOrden][1]]],
                 bloqueArray[extrategiaOrden[modoOrden][1]],
                 blockModo[modocapa][extrategiaOrden[modoOrden][1]]);
        bloqueApuro(ojoArray[ojoOrder[modocapa][extrategiaOrden[modoOrden][2]]],
                 ojoArrayX[ojoOrder[modocapa][extrategiaOrden[modoOrden][2]]],
                 ojoArrayY[ojoOrder[modocapa][extrategiaOrden[modoOrden][2]]],
                 bloqueArray[extrategiaOrden[modoOrden][2]],
                 blockModo[modocapa][extrategiaOrden[modoOrden][2]]);
      }
      if (!pushed && !animando) 
        buttonPressed = -1;
      if (!demo && mover.length > 0) {
        if (mover[curMover].length > 0) {
          graficos.setColor(Color.black);
          graficos.drawRect(0, altura - progressHeight, anchura - 1, progressHeight - 1);
          graficos.setColor(textColor);
          int progreso = (anchura - 2) * realMovePos(mover[curMover], moverPos) / realMoveLength(mover[curMover]);
          graficos.fillRect(1, altura - progressHeight + 1, progreso, progressHeight - 2);
          graficos.setColor(bgColor.darker());
          graficos.fillRect(1 + progreso, altura - progressHeight + 1, anchura - 2 - progreso, progressHeight - 2);
          String s = "" + moveLength(mover[curMover], moverPos) + "/" + moveLength(mover[curMover], -1) + metricChar[metrica];
          int w = graficos.getFontMetrics().stringWidth(s);
          int x = anchura - w - 2;
          int y = altura - progressHeight - 2;
          if (moveText > 0 && textHeight > 0) {
            drawString(graficos, s, x, y - textHeight);
            drawMoveText(graficos, y);
          }
          else
            drawString(graficos, s, x, y);
        }
        if (mover.length > 1) {
          graficos.setClip(0, 0, anchura, altura);
          int b = graficos.getFontMetrics().getDescent();
          int y = textHeight - b;
          String s = "" + (curMover + 1) + "/" + mover.length;
          int w = graficos.getFontMetrics().stringWidth(s);
          int x = anchura - w - buttonHeight - 2;
          drawString(graficos, s, x, y);
    
          graficos.setColor(buttonBgColor);
          graficos.fill3DRect(anchura - buttonHeight, 0, buttonHeight, buttonHeight, buttonPressed != 7);
          drawButton(graficos, 7, anchura - buttonHeight / 2, buttonHeight / 2);
        }
      }
      if (curInfoText >= 0) {
        graficos.setClip(0, 0, anchura, altura);
        int b = graficos.getFontMetrics().getDescent();
        int y = textHeight - b;
        drawString(graficos, infoText[curInfoText], 0, y);
      }
      if (dibujeBotones && buttonBar != 0)
        drawButtons(graficos);
    }
    g.drawImage(imagen, 0, 0, this);
  } 
  public void update(Graphics g) {
    paint(g);
  }

  // poligono para llenarse
  private final int[] fillX = new int[4];
  private final int[] fillY = new int[4];
  private final double[] coordsX = new double[8];
  private final double[] coordsY = new double[8];
  private final double[][] cooX = new double[6][4];
  private final double[][] cooY = new double[6][4];
  private static final double[][] borde = {{0.10, 0.10}, {0.90, 0.10}, {0.90, 0.90}, {0.10, 0.90}};
  private static final int[][] factores = {{0, 0}, {0, 1}, {1, 1}, {1, 0}};
  private final double[] faceShiftX = new double[6];
  private final double[] faceShiftY = new double[6];
  private final double[] tempNormal = new double[3];

  private void bloqueApuro(double[] ojo, double[] eyeX, double[] eyeY, int[][][] bloque, int modo) {
    // 3d a 2d
    for (int i = 0; i < 8; i++) {
      double min = anchura < altura ? anchura : altura - progressHeight;
      double x = min / 3.7 * vProd(vcoordina[i], eyeX) * escama;
      double y = min / 3.7 * vProd(vcoordina[i], eyeY) * escama;
      double z = min / (5.0 + deformacion) * vProd(vcoordina[i], ojo) * escama;
      x = x / (1 - z / min); 
      y = y / (1 - z / min); //transformacion
      coordsX[i] = anchura / 2.0 + x;
      if (alineacion == 0)
        coordsY[i] = (altura - progressHeight) / 2.0 * escama  - y;
      else if (alineacion == 2)
        coordsY[i] = altura - progressHeight - (altura - progressHeight) / 2.0 * escama  - y;
      else
        coordsY[i] = (altura - progressHeight) / 2.0 - y;
    }
    for (int i = 0; i < 6; i++) { 
      for (int j = 0; j < 4; j++) { 
        cooX[i][j] = coordsX[vccara[i][j]];
        cooY[i][j] = coordsY[vccara[i][j]];
      }
    }
    if (indicio) {
      for (int i = 0; i < 6; i++) {
        vSub(vScale(vCopy(perspOjo, ojo), 5.0 + deformacion), vnormales[i]); 
        if (vProd(perspOjo, vnormales[i]) < 0) { 
          vScale(vCopy(tempNormal, vnormales[i]), cambioCara);
          double min = anchura < altura ? anchura : altura - progressHeight;
          double x = min / 3.7 * vProd(tempNormal, eyeX);
          double y = min / 3.7 * vProd(tempNormal, eyeY);
          double z = min / (5.0 + deformacion) * vProd(tempNormal, ojo);
          x = x / (1 - z / min);
          y = y / (1 - z / min); 
          int sideW = bloque[i][0][1] - bloque[i][0][0];
          int sideH = bloque[i][1][1] - bloque[i][1][0];
          if (sideW > 0 && sideH > 0) {
            for (int n = 0, p = bloque[i][1][0]; n < sideH; n++, p++) {
              for (int o = 0, q = bloque[i][0][0]; o < sideW; o++, q++) {
                for (int j = 0; j < 4; j++) {
                  getCorners(i, j, fillX, fillY, q + borde[j][0], p + borde[j][1], imagenVistaCubo);
                  fillX[j] += imagenVistaCubo ? -x : x;
                  fillY[j] -= y;
                }
                graficos.setColor(colores[cube[i][p * 3 + q]]);
                graficos.fillPolygon(fillX, fillY, 4);
                graficos.setColor(colores[cube[i][p * 3 + q]].darker());
                graficos.drawPolygon(fillX, fillY, 4);
              }
            }
          }
        }
      }
    }
    for (int i = 0; i < 6; i++) {
      int sideW = bloque[i][0][1] - bloque[i][0][0];
      int sideH = bloque[i][1][1] - bloque[i][1][0];
      if (sideW > 0 && sideH > 0) {
        for (int j = 0; j < 4; j++)
          getCorners(i, j, fillX, fillY, bloque[i][0][factores[j][0]], bloque[i][1][factores[j][1]], imagenVistaCubo);
        if (sideW == 3 && sideH == 3)
          graficos.setColor(bgColor2);
        else
          graficos.setColor(Color.black);
        graficos.drawPolygon(fillX, fillY, 4);
      }
    }
    for (int i = 0; i < 6; i++) { 
      int sideW = bloque[i][0][1] - bloque[i][0][0];
      int sideH = bloque[i][1][1] - bloque[i][1][0];
      if (sideW <= 0 || sideH <= 0) {
        for (int j = 0; j < 4; j++) {
          int k = esqccaraopuesta[i][j];
          fillX[j] = (int)(cooX[i][j] + (cooX[i ^ 1][k] - cooX[i][j]) * 2.0 / 3.0);
          fillY[j] = (int)(cooY[i][j] + (cooY[i ^ 1][k] - cooY[i][j]) * 2.0 / 3.0);
          if (imagenVistaCubo)
            fillX[j] = anchura - fillX[j];
        }
        graficos.setColor(Color.black);
        graficos.fillPolygon(fillX, fillY, 4);
      }
      else {
        // Dibuja fondo negro de la cara
          for (int j = 0; j < 4; j++)
          getCorners(i, j, fillX, fillY, bloque[i][0][factores[j][0]], bloque[i][1][factores[j][1]], imagenVistaCubo);
        graficos.setColor(Color.black);
        graficos.fillPolygon(fillX, fillY, 4);
      }
    }
    // Dibuja todas las caras visibles
    for (int i = 0; i < 6; i++) {
      vSub(vScale(vCopy(perspOjo, ojo), 5.0 + deformacion), vnormales[i]);
      if (vProd(perspOjo, vnormales[i]) > 0) { 
        int ladoW = bloque[i][0][1] - bloque[i][0][0];
        int ladoH = bloque[i][1][1] - bloque[i][1][0];
        if (ladoW > 0 && ladoH > 0) { 
          for (int n = 0, p = bloque[i][1][0]; n < ladoH; n++, p++) {
            for (int o = 0, q = bloque[i][0][0]; o < ladoW; o++, q++) {
              for (int j = 0; j < 4; j++)
                getCorners(i, j, fillX, fillY, q + borde[j][0], p + borde[j][1], imagenVistaCubo);
              graficos.setColor(colores[cube[i][p * 3 + q]].darker());
              graficos.drawPolygon(fillX, fillY, 4);
              graficos.setColor(colores[cube[i][p * 3 + q]]);
              graficos.fillPolygon(fillX, fillY, 4);
            }
          }
        }
        if (!editRatonCubo || animando) 
          continue;
        // direcciones horizontales y verticales de cara
        double dxh = (cooX[i][1] - cooX[i][0] + cooX[i][2] - cooX[i][3]) / 6.0;
        double dyh = (cooX[i][3] - cooX[i][0] + cooX[i][2] - cooX[i][1]) / 6.0;
        double dxv = (cooY[i][1] - cooY[i][0] + cooY[i][2] - cooY[i][3]) / 6.0;
        double dyv = (cooY[i][3] - cooY[i][0] + cooY[i][2] - cooY[i][1]) / 6.0;
        if (modo == 3) { 
          for (int j = 0; j < 6; j++) { 
            for (int k = 0; k < 4; k++) 
              getCorners(i, k, obstaculoEsquinaX[obstaculoAreas], obstaculoEsquinaY[obstaculoAreas],
                               torsionBloque[j][k][0], torsionBloque[j][k][1], false);
            obstaculoDirsX[obstaculoAreas] = (dxh * areaDirs[j][0] + dxv * areaDirs[j][1]) * torsionDirs[i][j];
            obstaculoDirsY[obstaculoAreas] = (dyh * areaDirs[j][0] + dyv * areaDirs[j][1]) * torsionDirs[i][j];
            obstaculosEstratos[obstaculoAreas] = adjacentccara[i][j % 4];
            if (j >= 4)
              obstaculosEstratos[obstaculoAreas] &= ~1;
            obstaculoModos[obstaculoAreas] = j / 4;
            obstaculoAreas++;
            if (obstaculoAreas == 18)
              break;
          }
        }
        else if (modo == 0) {
          if (i != capa && ladoW > 0 && ladoH > 0) {
            int j = ladoW == 3 ? (bloque[i][1][0] == 0 ? 0 : 2) : (bloque[i][0][0] == 0 ? 3 : 1);
            for (int k = 0; k < 4; k++)
              getCorners(i, k, obstaculoEsquinaX[obstaculoAreas], obstaculoEsquinaY[obstaculoAreas],
                               torsionBloque[j][k][0], torsionBloque[j][k][1], false);
            obstaculoDirsX[obstaculoAreas] = (dxh * areaDirs[j][0] + dxv * areaDirs[j][1]) * torsionDirs[i][j];
            obstaculoDirsY[obstaculoAreas] = (dyh * areaDirs[j][0] + dyv * areaDirs[j][1]) * torsionDirs[i][j];
            obstaculosEstratos[obstaculoAreas] = capa;
            obstaculoModos[obstaculoAreas] = 0;
            obstaculoAreas++;
          }
        }
        else if (modo == 1) {
          if (i != capa && ladoW > 0 && ladoH > 0) {
            int j = ladoW == 3 ? 4 : 5;
            for (int k = 0; k < 4; k++)
              getCorners(i, k, obstaculoEsquinaX[obstaculoAreas], obstaculoEsquinaY[obstaculoAreas],
                               torsionBloque[j][k][0], torsionBloque[j][k][1], false);
            obstaculoDirsX[obstaculoAreas] = (dxh * areaDirs[j][0] + dxv * areaDirs[j][1]) * torsionDirs[i][j];
            obstaculoDirsY[obstaculoAreas] = (dyh * areaDirs[j][0] + dyv * areaDirs[j][1]) * torsionDirs[i][j];
            obstaculosEstratos[obstaculoAreas] = capa;
            obstaculoModos[obstaculoAreas] = 1;
            obstaculoAreas++;
          }
        }
      }
    }
  }

  private void getCorners(int cara, int esquina, int[] esquinaX, int[] esquinasY, double factor1, double factor2, boolean espejo) {
    factor1 /= 3.0;
    factor2 /= 3.0;
    double x1 = cooX[cara][0] + (cooX[cara][1] - cooX[cara][0]) * factor1;
    double y1 = cooY[cara][0] + (cooY[cara][1] - cooY[cara][0]) * factor1;
    double x2 = cooX[cara][3] + (cooX[cara][2] - cooX[cara][3]) * factor1;
    double y2 = cooY[cara][3] + (cooY[cara][2] - cooY[cara][3]) * factor1;
    esquinaX[esquina] = (int)(0.5 + x1 + (x2 - x1) * factor2);
    esquinasY[esquina] = (int)(0.5 + y1 + (y2 - y1) * factor2);
    if (espejo)
      esquinaX[esquina] = anchura - esquinaX[esquina];
  }

  private void drawButtons(Graphics g) {
    if (buttonBar == 2) {
      g.setColor(buttonBgColor);
      g.fill3DRect(0, altura - buttonHeight, buttonHeight, buttonHeight, buttonPressed != 0);
      drawButton(g, 0, buttonHeight / 2, altura - (buttonHeight + 1) / 2);
      return;
    }
    if (buttonBar == 1) {
      g.setClip(0, altura, anchura, buttonHeight);
      int buttonX = 0;
      for (int i = 0; i < 7; i++) {
        int buttonWidth = (anchura - buttonX) / (7 - i);
        g.setColor(buttonBgColor);
        g.fill3DRect(buttonX, altura, buttonWidth, buttonHeight, buttonPressed != i);
        drawButton(g, i, buttonX + buttonWidth / 2, altura + buttonHeight / 2);
        buttonX += buttonWidth;
      }
      dibujeBotones = false;
      return;
    }
  }

  private void drawButton(Graphics g, int i, int x, int y) {
    g.setColor(Color.white);
    switch (i) {
     case 0: // cuerda
      drawRect(g, x - 4, y - 3, 3, 7);
      drawArrow(g, x + 3, y, -1); // izquierda
      break;
     case 1: //  paso revés
      drawRect(g, x + 2, y - 3, 3, 7);
      drawArrow(g, x, y, -1); // izquierda
      break;
     case 2: // central
      drawArrow(g, x + 2, y, -1); // izquierda
      break;
     case 3: // espejo
      if (animando)
        drawRect(g, x - 3, y - 3, 7, 7);
      else {
        drawRect(g, x - 3, y - 2, 7, 5);
        drawRect(g, x - 1, y - 4, 3, 9);
      }
      break;
     case 4: 
      drawArrow(g, x - 2, y, 1); 
      break;
     case 5: 
      drawRect(g, x - 4, y - 3, 3, 7);
      drawArrow(g, x, y, 1);
      break;
     case 6: 
      drawRect(g, x + 1, y - 3, 3, 7);
      drawArrow(g, x - 4, y, 1); 
      break;
     case 7: 
      drawArrow(g, x - 2, y, 1); 
      break;
    }
  }

  private static void drawArrow(Graphics g, int x, int y, int dir) {
    g.setColor(Color.black);
    g.drawLine(x, y - 3, x, y + 3);
    x += dir;
    for (int i = 0; i >= -3 && i <= 3; i += dir) {
      int j = 3 - i * dir;
      g.drawLine(x + i, y + j, x + i, y - j);
    }
    g.setColor(Color.white);
    for (int i = 0; i >= -1 && i <= 1; i += dir) {
      int j = 1 - i * dir;
      g.drawLine(x + i, y + j, x + i, y - j);
    }
  }

  private static void drawRect(Graphics g, int x, int y, int width, int height) {
    g.setColor(Color.black);
    g.drawRect(x, y, width - 1, height - 1);
    g.setColor(Color.white);
    g.fillRect(x + 1, y + 1, width - 2, height - 2);
  }

  private static final int[] textOffset = {1, 1, -1, -1, -1, 1, 1, -1, -1, 0, 1, 0, 0, 1, 0, -1};

  private void drawString(Graphics g, String s, int x, int y) {
    if (outlined) {
      g.setColor(Color.black);
      for (int i = 0; i < textOffset.length; i += 2)
        g.drawString(s, x + textOffset[i], y + textOffset[i + 1]);
      g.setColor(Color.white);
    }
    else
      g.setColor(textColor);
    g.drawString(s, x, y);
  }

  private void drawMoveText(Graphics g, int y) {
    g.setClip(0, altura - progressHeight - textHeight, anchura, textHeight);
    g.setColor(Color.black);
    int pos = moverPos == 0 ? arrayMovePos(mover[curMover], moverPos) : moverPos;
    String s1 = moveText(mover[curMover], 0, pos);
    String s2 = turnText(mover[curMover], pos);
    String s3 = moveText(mover[curMover], pos + 1, mover[curMover].length);
    int w1 = g.getFontMetrics().stringWidth(s1);
    int w2 = g.getFontMetrics().stringWidth(s2);
    int w3 = g.getFontMetrics().stringWidth(s3);
    int x = 1;
    if (x + w1 + w2 + w3 > anchura) {
      x = Math.min(1, anchura / 2 - w1 - w2 / 2);
      x = Math.max(x, anchura - w1 - w2 - w3 - 2);
    }
    if (w2 > 0) {
      g.setColor(hlColor);
      g.fillRect(x + w1 - 1, altura - progressHeight - textHeight, w2 + 2, textHeight);
    }
    if (w1 > 0)
      drawString(g, s1, x, y);
    if (w2 > 0)
      drawString(g, s2, x + w1, y);
    if (w3 > 0)
      drawString(g, s3, x + w1 + w2, y);
  }

  private int selectButton(int x, int y) {
    if (buttonBar == 0)
      return -1;
    if (mover.length > 1 && x >= anchura - buttonHeight && x < anchura && y >= 0 && y < buttonHeight)
      return 7;
    if (buttonBar == 2) {
      if (x >= 0 && x < buttonHeight && y >= altura - buttonHeight && y < altura)
        return 0;
      return -1;
    }
    if (y < altura)
      return -1;
    int buttonX = 0;
    for (int i = 0; i < 7; i++) {
      int buttonWidth = (anchura - buttonX) / (7 - i);
      if (x >= buttonX && x < buttonX + buttonWidth && y >= altura && y < altura + buttonHeight)
        return i;
      buttonX += buttonWidth;
    }
    return -1;
  }

  // manipuladores de ratón

  private final static int[] buttonAction = {-1, 3, 1, -1, 0, 2, 4, -1};

  public void mousePressed(MouseEvent e) {
    dureObstaculoX = dureX = e.getX();
    dureObstaculoY = dureY = e.getY();
    Torcerse = false;
    buttonPressed = selectButton(dureX, dureY);
    if (buttonPressed >= 0) {
      pushed = true;
      if (buttonPressed == 3) {
        if (!animando) 
          imagenVistaCubo = !imagenVistaCubo;
        else
          cerrarAnimacion();
      }
      else if (buttonPressed == 0) {
        cerrarAnimacion();
        clear();
      }
      else if (buttonPressed == 7) {
        cerrarAnimacion();
        clear();
        curMover = curMover < mover.length - 1 ? curMover + 1 : 0;
      }
      else
        startAnimation(buttonAction[buttonPressed]);
      dibujeBotones = true;
      repaint();
    }
    else if (progressHeight > 0 && mover.length > 0 && mover[curMover].length > 0 && dureY >= altura - progressHeight && dureY < altura) {
      cerrarAnimacion();
      int len = realMoveLength(mover[curMover]);
      int pos = ((dureX - 1) * len * 2 / (anchura - 2) + 1) / 2;
      pos = Math.max(0, Math.min(len, pos));
      if (pos > 0)
        pos = arrayMovePos(mover[curMover], pos);
      if (pos > moverPos)
        doMove(cube, mover[curMover], moverPos, pos - moverPos, false);
      if (pos < moverPos)
        doMove(cube, mover[curMover], pos, moverPos - pos, true);
      moverPos = pos;
      arrastramiento = true;
      repaint();
    }
    else {
      if (imagenVistaCubo)
        dureObstaculoX = dureX = anchura - dureX;
      if (editRatonCubo && !animando &&
       (e.getModifiers() & InputEvent.BUTTON1_MASK) != 0 &&
       (e.getModifiers() & InputEvent.SHIFT_MASK) == 0)
        Torcerse = true;
    }
  }

  public void mouseReleased(MouseEvent e) {
    arrastramiento = false;
    if (pushed) {
      pushed = false;
      dibujeBotones = true;
      repaint();
    }
    else if (usuarioTorceCubo && !dandovueltas) {
      usuarioTorceCubo = false;
      angulooriginal += anguloeditado;
      anguloeditado = 0.0;
      double angle = angulooriginal;
      while (angle < 0.0)
        angle += 32.0 * Math.PI;
      int num = (int)(angle * 8.0 / Math.PI) % 16; 
      if (num % 4 == 0 || num % 4 == 3) {
        num = (num + 1) / 4;
        if (caraTD[capa] > 0)
          num = (4 - num) % 4;
        angulooriginal = 0;
        natural = true; 
        retuerzaEstratos(cube, capa, num, modocapa);
      }
      repaint();
    }
  }

  private final double[] eyeD = new double[3];

  public void mouseDragged(MouseEvent e) {
    if (pushed)
      return;
    if (arrastramiento) {
      cerrarAnimacion();
      int len = realMoveLength(mover[curMover]);
      int pos = ((e.getX() - 1) * len * 2 / (anchura - 2) + 1) / 2;
      pos = Math.max(0, Math.min(len, pos));
      if (pos > 0)
        pos = arrayMovePos(mover[curMover], pos);
      if (pos > moverPos)
        doMove(cube, mover[curMover], moverPos, pos - moverPos, false);
      if (pos < moverPos)
        doMove(cube, mover[curMover], pos, moverPos - pos, true);
      moverPos = pos;
      repaint();
      return;
    }
    int x = imagenVistaCubo ? anchura - e.getX() : e.getX();
    int y = e.getY();
    int dx = x - dureX;
    int dy = y - dureY;
    if (editRatonCubo && Torcerse && !usuarioTorceCubo && !animando) {
      dureObstaculoX = x;
      dureObstaculoY = y;
      for (int i = 0; i < obstaculoAreas; i++) {
        double d1 = obstaculoEsquinaX[i][0];
        double x1 = obstaculoEsquinaX[i][1] - d1;
        double y1 = obstaculoEsquinaX[i][3] - d1;
        double d2 = obstaculoEsquinaY[i][0];
        double x2 = obstaculoEsquinaY[i][1] - d2;
        double y2 = obstaculoEsquinaY[i][3] - d2;
        double a = (y2 * (dureX - d1) - y1 * (dureY - d2)) / (x1 * y2 - y1 * x2);
        double b = (-x2 * (dureX - d1) + x1 * (dureY - d2)) / (x1 * y2 - y1 * x2);
        if (a > 0 && a < 1 && b > 0 && b < 1) { 
          if (dx * dx + dy * dy < 144)
            return;
          obstaculoX = obstaculoDirsX[i];
          obstaculoY = obstaculoDirsY[i];
          double d = Math.abs(obstaculoX * dx + obstaculoY * dy) / Math.sqrt((obstaculoX * obstaculoX + obstaculoY * obstaculoY) * (dx * dx + dy * dy));
          if (d > 0.75) {
            usuarioTorceCubo = true;
            capa = obstaculosEstratos[i];
            modocapa = obstaculoModos[i];
            break;
          }
        }
      }
      Torcerse = false;
      dureX = dureObstaculoX;
      dureY = dureObstaculoY;
    }
    dx = x - dureX;
    dy = y - dureY;
    if (!usuarioTorceCubo || animando) {
      vNorm(vAdd(ojo, vScale(vCopy(eyeD, ojoX), dx * -0.016)));
      vNorm(vMul(ojoX, ojoY, ojo));
      vNorm(vAdd(ojo, vScale(vCopy(eyeD, ojoY), dy * 0.016)));
      vNorm(vMul(ojoY, ojo, ojoX));
      dureX = x;
      dureY = y;
    }
    else {
      if (natural)
        cuboDividido(capa);
      anguloeditado = 0.03 * (obstaculoX * dx + obstaculoY * dy) / Math.sqrt(obstaculoX * obstaculoX + obstaculoY * obstaculoY); // dv * cos a
    }
    repaint();
  }
  private static final String[] buttonDescriptions = {
    "Clear to the initial state",
    "Show the previous step",
    "Play backward",
    "Stop",
    "Play",
    "Show the next step",
    "Go to the end",
    "Next sequence"
  };
  private String buttonDescription = "";

  public void mouseMoved(MouseEvent e) {
    int x = e.getX();
    int y = e.getY();
    String descripcion = "Drag the cube with a mouse";
    if (x >= 0 && x < anchura) {
      if (y >= altura && y < altura + buttonHeight || y >= 0 && y < buttonHeight) {
        buttonPressed = selectButton(x, y);
        if (buttonPressed >= 0)
          descripcion = buttonDescriptions[buttonPressed];
        if (buttonPressed == 3 && !animando)
          descripcion = "Mirror the cube view";
      }
      else if (progressHeight > 0 && mover.length > 0 && mover[curMover].length > 0 && y >= altura - progressHeight && y < altura) {
        descripcion = "Current progress";
      }
    }
    if (!descripcion.equals((Object) buttonDescription)) {
      buttonDescription = descripcion;
    }
  }

  public void mouseClicked(MouseEvent e) {}
  public void mouseEntered(MouseEvent e) {}
  public void mouseExited(MouseEvent e) {}

  private static double[] vCopy(double[] vector, double[] srcVec) {
    vector[0] = srcVec[0];
    vector[1] = srcVec[1];
    vector[2] = srcVec[2];
    return vector;
  }

  private static double[] vNorm(double[] vector) {
    double longitud = Math.sqrt(vProd(vector, vector));
    vector[0] /= longitud;
    vector[1] /= longitud;
    vector[2] /= longitud;
    return vector;
  }

  private static double[] vScale(double[] vector, double valor) {
    vector[0] *= valor;
    vector[1] *= valor;
    vector[2] *= valor;
    return vector;
  }

  private static double vProd(double[] vec1, double[] vec2) {
    return vec1[0] * vec2[0] + vec1[1] * vec2[1] + vec1[2] * vec2[2];
  }

  private static double[] vAdd(double[] vector, double[] srcVec) {
    vector[0] += srcVec[0];
    vector[1] += srcVec[1];
    vector[2] += srcVec[2];
    return vector;
  }

  private static double[] vSub(double[] vector, double[] srcVec) {
    vector[0] -= srcVec[0];
    vector[1] -= srcVec[1];
    vector[2] -= srcVec[2];
    return vector;
  }

  private static double[] vMul(double[] vector, double[] vec1, double[] vec2) {
    vector[0] = vec1[1] * vec2[2] - vec1[2] * vec2[1];
    vector[1] = vec1[2] * vec2[0] - vec1[0] * vec2[2];
    vector[2] = vec1[0] * vec2[1] - vec1[1] * vec2[0];
    return vector;
  }

  private static double[] vRotX(double[] vector, double angle) {
    double sinA = Math.sin(angle);
    double cosA = Math.cos(angle);
    double y = vector[1] * cosA - vector[2] * sinA;
    double z = vector[1] * sinA + vector[2] * cosA;
    vector[1] = y;
    vector[2] = z;
    return vector;
  }

  private static double[] vRotY(double[] vector, double angulo) {
    double sinA = Math.sin(angulo);
    double cosA = Math.cos(angulo);
    double x = vector[0] * cosA - vector[2] * sinA;
    double z = vector[0] * sinA + vector[2] * cosA;
    vector[0] = x;
    vector[2] = z;
    return vector;
  }

  private static double[] vRotZ(double[] vector, double angulo) {
    double sinA = Math.sin(angulo);
    double cosA = Math.cos(angulo);
    double x = vector[0] * cosA - vector[1] * sinA;
    double y = vector[0] * sinA + vector[1] * cosA;
    vector[0] = x;
    vector[1] = y;
    return vector;
  }
}
