package hundirlaflota;

import java.util.Scanner;
import java.util.Arrays;

/**
 *
 * @author Daniel Jimenez Gutierrez 2021/2022
 */
public class HundirLaFlota {

    // función principal
    
    public static void main(String[] args) {
        
        String MatrizOculta[][];
        
        System.out.println("Bienvenido jugador!");
        
        Tutorial();
        
        boolean Jugar=true;
        
        while (Jugar==true) {
        
            System.out.println("Ahora vas a seleccionar tu desafio!"); 


            int DificultadSeleccionada = ElegirDificultad();

            MatrizOculta = InicializacionMatrizOrdenador(DificultadSeleccionada);

            String[] BarcosIndividuales = CantidadBarcos(DificultadSeleccionada,MatrizOculta);
            int TotalDeBarcos=0;

            for (int i=0;i<BarcosIndividuales.length;i++)
                TotalDeBarcos+=Integer.parseInt(BarcosIndividuales[i]);

            int CasillasOcupadas=(Integer.parseInt(BarcosIndividuales[0])*1+Integer.parseInt(BarcosIndividuales[1])*3+Integer.parseInt(BarcosIndividuales[2])*4+Integer.parseInt(BarcosIndividuales[3])*5);

            int NumeroDeDisparos= NumeroDisparos(DificultadSeleccionada,CasillasOcupadas);


            MatrizOculta = CalculoMatrizOrdenador(MatrizOculta,DificultadSeleccionada,TotalDeBarcos,BarcosIndividuales);

            String[][] MatrizJugador=new String[MatrizOculta.length][MatrizOculta.length];

            for (int i=0;i<MatrizOculta.length;i++) {
                for (int j=0;j<MatrizOculta.length;j++) {
                    MatrizJugador[i][j]=MatrizOculta[i][j];
                }
            }

            MatrizJugador=LimpiadorMatrizJugador(MatrizJugador);

            System.out.println("\n");
            
            System.out.println("\n");
            
            VisualizadorDeMatrices(MatrizJugador);
            
            for (String Ataque;(NumeroDeDisparos>0);NumeroDeDisparos--) {

                Ataque = IntroducirAtaque(MatrizOculta, DificultadSeleccionada);
                
                System.out.println("\n");

                MatrizJugador = ComparadorValoresAtaque(MatrizJugador,MatrizOculta,Ataque,DificultadSeleccionada);
                
                

                if (HaDado(MatrizJugador,Ataque,DificultadSeleccionada))
                    CasillasOcupadas--;
                if (CasillasOcupadas>NumeroDeDisparos) {
                    System.out.println("Te has quedado sin suficientes disparos.");
                    break;
                }
                
                System.out.println("Te quedan " + NumeroDeDisparos + " disparos\n");
            }

            if (CasillasOcupadas==0)
               System.out.println("Has ganado!!!\n");
            else {
               System.out.println("Has perdido :( \n");
            }
            
            System.out.println("Esta era la posción de los barcos:\n");
            
            VisualizadorDeMatrices(MatrizOculta);
            
            System.out.println("\n");
            
            System.out.println("Quieres jugar otra? (S/N)\n");
            
            Jugar=SioNo();
        
        }
        
        System.out.println("Gracias por jugar <3");
    }
 
    //funciones relacionadas con las matrices
    
    public static String[][] CalculoMatrizOrdenador(String[][] MatrizCopiada, int dificultad, int totalbarcos, String[] numerobarcos) {
        String[][] MatrizVolcada = MatrizCopiada;
        
        for (int i=0;i<MatrizVolcada.length;i++) {
            for (int j=0;j<MatrizVolcada[i].length;j++) {
                MatrizVolcada[i][j].replace("A", "-");
            }
        }
        
        String[][] MatrizConBarcos = PosicionBarcos(MatrizVolcada,dificultad,totalbarcos,numerobarcos);
        
        
        return MatrizConBarcos;
    }
    
    public static boolean ComparadorDeMatrices(String[][] Matriz1, String[][] Matriz2) {
        
        boolean igualdad;
        
        for (int i=0;i<Matriz1.length;i++) {
            for (int j=0;j<Matriz1[i].length;j++) {
                if (!(Matriz1[i][j].equals(Matriz2[i][j])))
                    igualdad=true;
                else
                    return false;
                
            }
        }
        
        return true;
    }
    
    public static String[][] LimpiadorMatrizJugador(String[][] MatrizCopiada) {
        String[][] MatrizLimpia = MatrizCopiada;
        
        for (int i=1;i<MatrizLimpia.length;i++) {
            for (int j=1;j<MatrizLimpia[i].length;j++) {
                MatrizLimpia[i][j]="- ";
            }
        }
        
        return MatrizLimpia;
    }
    
    public static void VisualizadorDeMatrices(String[][] Matriz1){
        
        for (int i = 0, j = 0 ;i < Matriz1.length; i++, j = 0) {
            for (j = 0;j < Matriz1[i].length; j++){
                System.out.printf((Matriz1[i][j])+" ");
            }
            System.out.println(" ");
        }
    }
    
    public static String[][] InicializacionMatrizOrdenador(int valordif) {
        
// Aquí es donde cargaremos al inicio de cada partida la matriz que tendrá el ordenador
// para posicionar sus barcos y donde después atacaremos nosotros en su búsqueda

        int x=10;
        
// Esto nos permitirá jugar en el modo personalizado y editar el máximo de amplitud del mapa
// hasta 27, más adelante explico porque ese límite
        
        if (valordif==3)
        {
            System.out.println("¿Cómo de grande quieres que sea tu campo de batalla?");
            x = IntroducirNumero() + 1;
            while ((x >= 28) || (x <= 2) ){
                System.out.println("El valor máximo es 27 y el mínimo 3");
                x = IntroducirNumero();
            }
        }
        
        String MatrizOrdenador[][] = new String [x][x];
        
// Convertimos todas las posiciones de la matriz en agua

        for (int i=0;i<MatrizOrdenador.length;i++) {
            for (int j = 0; j < MatrizOrdenador.length; j++) {
                MatrizOrdenador[i][j] = "A ";
                if (j >= 10)
                    MatrizOrdenador[i][j] = "A  ";
// Dejo los espacios en blanco para poder ajustar los números
// de dos cifras con respecto a las letras
            }
        }
        
// Vamos a limpiar la matriz para qu eno hayan As por el medio y que esté todo más monotono al principio de la partida

MatrizOrdenador=LimpiadorMatrizJugador(MatrizOrdenador);

// Ahora marcamos toda la fila superior con las coordenadas del mapa del 1 al inifinito
// aquí no hay límite
        
        int cuenta = 0;

        for (int j = 0; j < MatrizOrdenador.length; j++, cuenta++) {
                
            MatrizOrdenador[0][j] = Integer.toString(cuenta) + " ";       
        }
        
// Para lo siguiente he cambiado el valor de cuenta sólo por habituarme al orden ASCII, que prácticamente no lo he usado
// y la verdad que me apetecia probar a hacer un sistema de traducción jajaja
// Con esta parte construimos la columna de la izquierda, que tiene un máximo de amplitud de 27 por las letras en ASCII
// y por el espacio en blanco que tiene en la esquina superior izquierda
        cuenta = 65;
        
        for (int j = 1; j < MatrizOrdenador.length; j++, cuenta++) {
            
            MatrizOrdenador[j][0] = IntegerAscii(cuenta) + " ";
                    
        }
        
// Esta parte define el espacio en blanco de la parte superior izquierda

        MatrizOrdenador[0][0] = "  ";
        
        return MatrizOrdenador;
    }
     
    // funciones relacionadas con los barcos
    
    public static String[][] PosicionBarcos(String[][] terreno,int dificultad,int totalbarcos,String[] numerobarcos) {
        String[][] posicionesactuales = terreno;
        String[][] posicionesprevias;
        
        String[] datosbarco;
        
// Necesitamos saber cual es la cantidad exacta de barcos para poder adaptar el sistema a las exigencias del jugador        
        
               
        
// una vez hecho eso, podemos empezar a pasar todos los datos a una misma matriz con la que trabajaremos para colocar los barcos
// en el campo de tiro, poniendo el totalbarcos como número de filas en la matriz (mas detalles en EtiquetaBarcos()
        
        String[][] tablabarcos = EtiquetaBarcos(totalbarcos,numerobarcos);
        
        totalbarcos=tablabarcos.length;
            
        for (int tamaño, i=0;totalbarcos>0;totalbarcos--,i++) {
            
            String valor;
                
            valor = tablabarcos[i][2];
                    
            tamaño = Integer.parseInt(tablabarcos[i][0]);
                    
            posicionesprevias=CalculosBarcos(posicionesactuales,tamaño,valor);
                    
                    
            posicionesactuales=posicionesprevias;
        }
        
        return posicionesactuales;
    }
    
    public static String[][] CalculosBarcos(String[][] MatrizTerreno,int tamaño, String valor) {
        String[][] MatrizConBarcos=MatrizTerreno;
        int coordenadax,coordenaday;
        
        if ("L".equals(valor)) {
            do{
                do {
                    coordenadax=(int)(tamaño + Math.random()*(MatrizConBarcos.length - 1 - tamaño + 1));
                    coordenaday=(int)(tamaño + Math.random()*(MatrizConBarcos[coordenadax].length - 1 - tamaño + 1));
                } while (((coordenadax>MatrizConBarcos.length - 1)||(coordenaday>MatrizConBarcos[coordenadax].length - 1)||(coordenadax<1)||(coordenaday<1)));
            
            } while (!MatrizConBarcos[coordenadax][coordenaday].equals("- "));
            
            MatrizConBarcos[coordenadax][coordenaday]= "L ";
        }

        if ("B".equals(valor)) {
            do {
                do {
                coordenadax=(int)(tamaño + Math.random()*(MatrizConBarcos.length - 1 - tamaño + 1));
                coordenaday=(int)(tamaño + Math.random()*(MatrizConBarcos[coordenadax].length - 1 - tamaño + 1));
                } while (((coordenadax>=MatrizConBarcos.length - 1)||(coordenaday>=MatrizConBarcos[coordenadax].length - 3)||(coordenadax<=1)||(coordenaday<=2)));

            } while (!((MatrizConBarcos[coordenadax][coordenaday].equals("- ")&&(MatrizConBarcos[coordenadax][coordenaday-1].equals("- "))&&(MatrizConBarcos[coordenadax][coordenaday-2].equals("- ")))));
            MatrizConBarcos[coordenadax][coordenaday]= "B ";
            MatrizConBarcos[coordenadax][coordenaday-1]= "B ";
            MatrizConBarcos[coordenadax][coordenaday-2]= "B ";

        }


        if ("Z".equals(valor)) {
            do {
                do {
                coordenadax=(int)(tamaño + Math.random()*(MatrizConBarcos.length - 1 - tamaño + 1));
                coordenaday=(int)(tamaño + Math.random()*(MatrizConBarcos[coordenadax].length -1 - tamaño + 1));
                } while (((coordenadax>=MatrizConBarcos.length - 1)||(coordenaday>=MatrizConBarcos[coordenadax].length - 4)||(coordenadax<=1)||(coordenaday<=3)));

            } while (!((MatrizConBarcos[coordenadax][coordenaday].equals("- ")&&(MatrizConBarcos[coordenadax][coordenaday-1].equals("- "))&&(MatrizConBarcos[coordenadax][coordenaday-2].equals("- "))&&(MatrizConBarcos[coordenadax][coordenaday-2].equals("- ")))));
                MatrizConBarcos[coordenadax][coordenaday]= "Z ";
                MatrizConBarcos[coordenadax][coordenaday-1]= "Z ";
                MatrizConBarcos[coordenadax][coordenaday-2]= "Z ";
                MatrizConBarcos[coordenadax][coordenaday-3]= "Z ";
        } 


        if ("P".equals(valor)) {
            do {
                do {
                    coordenadax=(int)(tamaño + Math.random()*(MatrizConBarcos.length - 1 - tamaño + 1));
                    coordenaday=(int)(tamaño + Math.random()*(MatrizConBarcos[coordenadax].length - 1 - tamaño + 1));
                } while ((coordenadax>=MatrizConBarcos.length - 2)||(coordenaday>=MatrizConBarcos[coordenadax].length - 1)||(coordenadax<=2)||(coordenaday<=1));

            } while (!((MatrizConBarcos[coordenadax][coordenaday].equals("- "))&&(MatrizConBarcos[coordenadax-1][coordenaday].equals("- "))&&(MatrizConBarcos[coordenadax-2][coordenaday].equals("- "))&&(MatrizConBarcos[coordenadax+2][coordenaday].equals("- "))&&(MatrizConBarcos[coordenadax+1][coordenaday].equals("- "))));

            MatrizConBarcos[coordenadax][coordenaday]= "P ";
            MatrizConBarcos[coordenadax-1][coordenaday]= "P ";
            MatrizConBarcos[coordenadax-2][coordenaday]= "P ";
            MatrizConBarcos[coordenadax+1][coordenaday]= "P ";
            MatrizConBarcos[coordenadax+2][coordenaday]= "P ";

        }

        return MatrizConBarcos;
    }
    
    public static String[][] EtiquetaBarcos(int numerototalbarcos,String[] numerotiposdebarco) {
        int x=numerototalbarcos;
        int y=3;
        String[][] TotalDeBarcos=new String[x][y];
        int Lanchas = Integer.parseInt(numerotiposdebarco[0]);
        int Barco = Integer.parseInt(numerotiposdebarco[1]);
        int Acorazado = Integer.parseInt(numerotiposdebarco[2]);
        int Portaaviones = Integer.parseInt(numerotiposdebarco[3]);
        
        for (int i=0;i<TotalDeBarcos.length;i++) {
            
                if (Portaaviones>0) {
                        TotalDeBarcos[i][2]="P";
                        TotalDeBarcos[i][1]="1";
                        TotalDeBarcos[i][0]="5";
                        Portaaviones--;}
                else if (Acorazado>0) {
                        TotalDeBarcos[i][2]="Z";
                        TotalDeBarcos[i][1]="0";
                        TotalDeBarcos[i][0]="4";
                        Acorazado--;}
                else if (Barco>0) {
                        TotalDeBarcos[i][2]="B";
                        TotalDeBarcos[i][1]="0";
                        TotalDeBarcos[i][0]="3";
                        Barco--;}
                else if (Lanchas>0) {
                        TotalDeBarcos[i][2]="L";
                        TotalDeBarcos[i][1]="0";
                        TotalDeBarcos[i][0]="1";
                        Lanchas--;}
        }
        
        return TotalDeBarcos;
    }
    
    public static String[] CantidadBarcos(int dificultad,String[][] MatrizCopiada) {
        String cantidaddebarcos[] = new String[4];
        do {
            switch (dificultad) {
                case 0 -> {cantidaddebarcos[0] = "5";
                        cantidaddebarcos[1] = "3";
                        cantidaddebarcos[2] = "1";
                        cantidaddebarcos[3] = "1";}
                case 1 -> {cantidaddebarcos[0] = "2";
                        cantidaddebarcos[1] = "1";
                        cantidaddebarcos[2] = "1";
                        cantidaddebarcos[3] = "1";}
                case 2 -> {cantidaddebarcos[0] = "1";
                        cantidaddebarcos[1] = "1";
                        cantidaddebarcos[2] = "0";
                        cantidaddebarcos[3] = "0";}
                case 3 -> {System.out.println("¿Cuantas lanchas (L) quieres que hayan?");
                        cantidaddebarcos[0] = String.valueOf(IntroducirNumero());
                        System.out.println("¿Cuantos barcos (B) quieres que hayan?");
                        cantidaddebarcos[1] = String.valueOf(IntroducirNumero());
                        System.out.println("¿Cuantos acorazados (Z) quieres que hayan?");
                        cantidaddebarcos[2] = String.valueOf(IntroducirNumero());
                        System.out.println("¿Cuantos portaaviones (P) quieres que hayan?");
                        cantidaddebarcos[3] = String.valueOf(IntroducirNumero());}
            }
            if (((MatrizCopiada.length-1)*(MatrizCopiada.length-1))<=((Integer.parseInt(cantidaddebarcos[0]))+(Integer.parseInt(cantidaddebarcos[1]))*3+(Integer.parseInt(cantidaddebarcos[2]))*4+(Integer.parseInt(cantidaddebarcos[3]))*5))
                System.out.println("No caben tantos barcos en el campo de batalla");
        } while (((MatrizCopiada.length-1)*(MatrizCopiada.length-1))<=((Integer.parseInt(cantidaddebarcos[0]))+(Integer.parseInt(cantidaddebarcos[1]))*3+(Integer.parseInt(cantidaddebarcos[2]))*4+(Integer.parseInt(cantidaddebarcos[3]))*5));
        return cantidaddebarcos;
    }
    
    //funciones relacionadas con la introducción de datos
    
    public static String IntroducirAtaque(String[][] MatrizCopiada, int dificultad) {
         
        String orden="Valor que no se va a leer";
        boolean repetir=true;
        
        if (dificultad!=3) {
        
            
            
            while(repetir==true) {
                
                do  {
                    System.out.println("Introduce el formato correcto (ej:D2)");
                    orden=IntroducirTexto();
                } while (orden.length()!=2);

                if ((orden.length()==2)) {
                    if  (((Character.isDigit(orden.charAt(1))))) {

                        if      (((!(AlturaCorrectaCoordenadaY(orden, MatrizCopiada)))||
                                ((!(Character.getNumericValue(orden.charAt(1))<=MatrizCopiada.length-1))||
                                (Character.getNumericValue(orden.charAt(1))==0))))
                            System.out.println("Has puesto una coordenada no válida");
                        else {
                            break;
                        }
                    }
                }
            }
        }
     
// para las partidas personalizadas donde la distancia sea mayor

        else {
            
            while   (repetir==true) {
                
                do {
                    System.out.println("Donde quieres atacar? (ej:H12)");
                    orden=IntroducirTexto();
                } while (orden.length()!=3);
                
                if ((orden.length()==3)||(Integer.parseInt(orden.substring(1,3))==0)) {
                    if  (((Integer.parseInt(orden.substring(1,3)))>=0)) {
                        
                        if      (((!(AlturaCorrectaCoordenadaY(orden, MatrizCopiada)))||
                                ((!(Integer.parseInt(orden.substring(1,3))<=MatrizCopiada.length-1))||
                                (Integer.parseInt(orden.substring(1,3))==00))))
                            System.out.println("Has puesto una coordenada no válida");
                        else  {
                            break;
                        }
                    }
                }
                
                
            }
        
            
        }
        
        return orden;
    }
    
    public static boolean AlturaCorrectaCoordenadaY(String orden, String[][] matrizcopiada) {
        int altura=50;
        
        switch (orden.substring(0,1)) {
        
            case ("a") -> altura=1;
            case ("A") -> altura=1;
            
            case ("b") -> altura=2;
            case ("B") -> altura=2;
            
            case ("c") -> altura=3;
            case ("C") -> altura=3;
            
            case ("d") -> altura=4;
            case ("D") -> altura=4;
            
            case ("e") -> altura=5;
            case ("E") -> altura=5;
            
            case ("f") -> altura=6;
            case ("F") -> altura=6;
            
            case ("g") -> altura=7;
            case ("G") -> altura=7;
            
            case ("h") -> altura=8;
            case ("H") -> altura=8;
            
            case ("i") -> altura=9;
            case ("I") -> altura=9;
            
            case ("j") -> altura=10;
            case ("J") -> altura=10;
            
            case ("k") -> altura=11;
            case ("K") -> altura=11;
            
            case ("l") -> altura=12;
            case ("L") -> altura=12;
            
            case ("m") -> altura=13;
            case ("M") -> altura=13;
            
            case ("n") -> altura=14;
            case ("N") -> altura=14;
            
            case ("o") -> altura=15;
            case ("O") -> altura=15;
            
            case ("p") -> altura=16;
            case ("P") -> altura=16;
            
            case ("q") -> altura=17;
            case ("Q") -> altura=17;
            
            case ("r") -> altura=18;
            case ("R") -> altura=18;
            
            case ("s") -> altura=19;
            case ("S") -> altura=19;
            
            case ("t") -> altura=20;
            case ("T") -> altura=20;
            
            case ("u") -> altura=21;
            case ("U") -> altura=21;
            
            case ("v") -> altura=22;
            case ("V") -> altura=22;
            
            case ("w") -> altura=23;
            case ("W") -> altura=23;
            
            case ("x") -> altura=24;
            case ("X") -> altura=24;
            
            case ("y") -> altura=25;
            case ("Y") -> altura=25;
            
            case ("z") -> altura=26;
            case ("Z") -> altura=26;
        }
        
        return altura<=matrizcopiada.length-1;
    }
    
    public static int TraductorAltura(String orden) {
        int altura=50;
        
        switch (orden.substring(0,1)) {
        
            case ("a") -> altura=1;
            case ("A") -> altura=1;
            
            case ("b") -> altura=2;
            case ("B") -> altura=2;
            
            case ("c") -> altura=3;
            case ("C") -> altura=3;
            
            case ("d") -> altura=4;
            case ("D") -> altura=4;
            
            case ("e") -> altura=5;
            case ("E") -> altura=5;
            
            case ("f") -> altura=6;
            case ("F") -> altura=6;
            
            case ("g") -> altura=7;
            case ("G") -> altura=7;
            
            case ("h") -> altura=8;
            case ("H") -> altura=8;
            
            case ("i") -> altura=9;
            case ("I") -> altura=9;
            
            case ("j") -> altura=10;
            case ("J") -> altura=10;
            
            case ("k") -> altura=11;
            case ("K") -> altura=11;
            
            case ("l") -> altura=12;
            case ("L") -> altura=12;
            
            case ("m") -> altura=13;
            case ("M") -> altura=13;
            
            case ("n") -> altura=14;
            case ("N") -> altura=14;
            
            case ("o") -> altura=15;
            case ("O") -> altura=15;
            
            case ("p") -> altura=16;
            case ("P") -> altura=16;
            
            case ("q") -> altura=17;
            case ("Q") -> altura=17;
            
            case ("r") -> altura=18;
            case ("R") -> altura=18;
            
            case ("s") -> altura=19;
            case ("S") -> altura=19;
            
            case ("t") -> altura=20;
            case ("T") -> altura=20;
            
            case ("u") -> altura=21;
            case ("U") -> altura=21;
            
            case ("v") -> altura=22;
            case ("V") -> altura=22;
            
            case ("w") -> altura=23;
            case ("W") -> altura=23;
            
            case ("x") -> altura=24;
            case ("X") -> altura=24;
            
            case ("y") -> altura=25;
            case ("Y") -> altura=25;
            
            case ("z") -> altura=26;
            case ("Z") -> altura=26;
        }
        
        return altura;
    }
    
    public static int AlturaCoordenada(String orden, String[][] matrizcopiada) {
        int altura=50;
        
        if (!("a").contains(orden.toLowerCase())) {
            altura=1;
        }
        if (!("b").contains(orden.toLowerCase())) {
            altura=2;
        }
        if (!("c").contains(orden.toLowerCase())) {
            altura=3;
        }
        if (!("d").contains(orden.toLowerCase())) {
            altura=4;
        }
        if (!("e").contains(orden.toLowerCase())) {
            altura=5;
        }
        if (!("f").contains(orden.toLowerCase())) {
            altura=6;
        }
        if (!("g").contains(orden.toLowerCase())) {
            altura=7;
        }
        if (!("h").contains(orden.toLowerCase())) {
            altura=8;
        }
        if (!("i").contains(orden.toLowerCase())) {
            altura=9;
        }
        if (!("j").contains(orden.toLowerCase())) {
            altura=10;
        }
        if (!("k").contains(orden.toLowerCase())) {
            altura=11;
        }
        if (!("l").contains(orden.toLowerCase())) {
            altura=12;
        }
        if (!("m").contains(orden.toLowerCase())) {
            altura=13;
        }
        if (!("n").contains(orden.toLowerCase())) {
            altura=14;
        }
        if (!("o").contains(orden.toLowerCase())) {
            altura=15;
        }
        if (!("p").contains(orden.toLowerCase())) {
            altura=16;
        }
        if (!("q").contains(orden.toLowerCase())) {
            altura=17;
        }
        if (!("r").contains(orden.toLowerCase())) {
            altura=18;
        }
        if (!("s").contains(orden.toLowerCase())) {
            altura=19;
        }
        if (!("t").contains(orden.toLowerCase())) {
            altura=20;
        }
        if (!("u").contains(orden.toLowerCase())) {
            altura=21;
        }
        if (!("v").contains(orden.toLowerCase())) {
            altura=22;
        }
        if (!("w").contains(orden.toLowerCase())) {
            altura=23;
        }
        if (!("x").contains(orden.toLowerCase())) {
            altura=24;
        }
        if (!("y").contains(orden.toLowerCase())) {
            altura=25;
        }
        if (!("z").contains(orden.toLowerCase())) {
            altura=26;
        }              
        
        return altura;
    }
    
    public static String IntegerAscii(int num) {
// Esto solo voy a usarlo para poder calcular correctamente la columna izquierda
// de coordenadas, y para ello voy a convertir un Int a una letra del código ASCII
        
        String letra = "";
        
        switch(num) {
            case 65:
                letra="A";
                return letra;
            case 66:
                letra="B";
                return letra;
            case 67:
                letra="C";
                return letra;
            case 68:
                letra="D";
                return letra;
            case 69:
                letra="E";
                return letra;
            case 70:
                letra="F";
                return letra;
            case 71:
                letra="G";
                return letra;
            case 72:
                letra="H";
                return letra;
            case 73:
                letra="I";
                return letra;
            case 74:
                letra="J";
                return letra;
            case 75:
                letra="K";
                return letra;
            case 76:
                letra="L";
                return letra;
            case 77:
                letra="M";
                return letra;
            case 78:
                letra="N";
                return letra;
            case 79:
                letra="O";
                return letra;
            case 80:
                letra="P";
                return letra;
            case 81:
                letra="Q";
                return letra;
            case 82:
                letra="R";
                return letra;
            case 83:
                letra="S";
                return letra;
            case 84:
                letra="T";
                return letra;
            case 85:
                letra="U";
                return letra;
            case 86:
                letra="V";
                return letra;
            case 87:
                letra="W";
                return letra;
            case 88:
                letra="X";
                return letra;
            case 89:
                letra="Y";
                return letra;
            case 90:
                letra="Z";
                return letra;
        }
        return letra;
    }
    
    public static boolean SioNo() {
        String respuesta = IntroducirTexto();
        boolean afirmativo;
        boolean esletra;
        
        esletra = respuesta.equals("s")||respuesta.equals("S")||respuesta.equals("n")||respuesta.equals("N");  
        
        while (esletra==false) {
            System.out.println("Responde solo con S o N");
            respuesta=IntroducirTexto();
            esletra = respuesta.equals("s")||respuesta.equals("S")||respuesta.equals("n")||respuesta.equals("N");
        }
        
        switch (respuesta) {
            case "s" -> afirmativo=true;
            case "S" -> afirmativo=true;
            case "n" -> afirmativo=false;
            case "N" -> afirmativo=false;
            default -> afirmativo=true;
        }
        
     return afirmativo;   
}
    
    public static int IntroducirNumero() {
// Esta es la función para introducir un número dentro del programa,
// se utilizará cuando sea necesario que el jugador introduzca
// un dato (dificultad, ataque, etc)
// Le he acabado poninendo un filtro para evitar errores a la hora de introducir
// texto, ya sea por error o de forma intencionada
        Scanner in = new Scanner(System.in);
        String textointroducido; // He preferido poner dos valores para trabajar                           
        int valornum;            // más cómodo, uno para guardar el texto, y otro
        boolean esnum;           // para transformarlo a int
        
        textointroducido = in.nextLine();
// A partir de aquí está el filtro, estuve buscando por internet varias opciones
// y esta es la que más me gustó, responde con false si hay algún cáracter que 
// no sea un número dentro de la cadena de texto que hayamos introducido
        esnum = textointroducido.chars().allMatch( Character::isDigit );        
        while (esnum==false) {
            System.out.println("Introduce sólo dígitos");
            textointroducido=in.nextLine();
            esnum = textointroducido.chars().allMatch( Character::isDigit );
        }
        
        valornum = Integer.parseInt(textointroducido);
        
        return valornum;
    }
    
    public static String IntroducirTexto() {
// Esta es la función para introducir un texto dentro del programa,
// se utilizará cuando sea necesario que el jugador introduzca
// un texto (nombre, ataque, etc)
        Scanner in = new Scanner(System.in);
        String texto;
        
        texto = in.nextLine();
        
        return texto;
    }
    
    public static String[] CoordenadasDisparo() {
// Esta función es la que permite calcular las coordenadas del ataque
// del jugador contra la máquina, utilizando las ya creadas IntroducirTexto()
// y IntroducirNumero()
// Declaramos un vector con dos posiciones para almacenar el ataque
// del jugador para poder calcularlo posteriormente
        String disparointroducido[] = new String[2];
        
        
        while (IntroducirTexto().length() > 1) {
            System.out.println("Introduce la altura de tu ataque (Ej: A, F, J...):");
            disparointroducido[0] = IntroducirTexto();
        }
        
        while (String.valueOf(IntroducirNumero()).length() > 1) {
            System.out.println("Introduce la posición de tu ataque (Ej: 1, 6, 4...):");
                disparointroducido[1] = String.valueOf(IntroducirNumero());
        }
        return disparointroducido;
    }
    
    //funciones relacionadas con el juego en sí
    
    public static int NumeroDisparos(int dificultad, int casillasocupadas) {
        int numerodisparos=0;
        
        switch (dificultad) {
            case 0 ->   numerodisparos=50;
            case 1 ->   numerodisparos=30;
            case 2 ->   numerodisparos=10;
            case 3 ->   {
                System.out.println("Cuantos disparos quieres tener?");
                numerodisparos=IntroducirNumero();
                while (numerodisparos<casillasocupadas) {
                    System.out.println("Necesitas un mínimo de " + casillasocupadas + " para empezar a jugar");
                    numerodisparos=IntroducirNumero();
                }
            }
            
        }
        
        return numerodisparos;
    }
    
    public static void Tutorial() {
        boolean respuesta;
        System.out.println("¿Quieres un pequeño tutorial de como jugar? (S/N)");
        
        respuesta = SioNo();
        
        if (respuesta==true){
            System.out.println("""
                               Bienvenido al tutorial!
                               
                               En esta versi\u00f3n de hundir la flota puedes
                               escoger entre 4 dificultades distintas, siendo
                               una de ellas la personalizada, que es literalmente
                               un sandbox donde eliges el tipo de partida que vas
                               a tener al 100%, eligiendo el n\u00famero de disparos, el n\u00famero
                               de barcos, teniendo en cuenta cuantos de cada tipo quieres
                               poner, m\u00e1s adelante aclaro esto, y hasta el tama\u00f1o del campo de batalla!
                               
                               Despu\u00e9s tenemos las dificultades m\u00e1s t\u00edpicas, donde el tama\u00f1o
                               del campo de batalla siempre es el mismo y lo \u00fanico que cambia
                               son los intentos y el n\u00famero de barcos que tendr\u00e1s que destruir.
                               
                               Lo primero es lo primero, vamos a explicar los tipos de barco.
                               Disponemos de 4 distintos, cada uno con su identificador y
                               caracter\u00edstica \u00fanicos:
                               1 - Lancha (L): ocupan 1 espacio en el mapa
                               2 - Barco (B): ocupan 3 espacios en horizontal en el mapa
                               3 - Acorazado (Z): ocupan 4 espacios en horizontal en el mapa
                               4 - Portaaviones (P): ocupan 5 espacios en vertical en el mapa
                               
                               Pasemos ahora con los modos de juego, recuerdo, en todos el mapa es de 9*9
                               La primera y m\u00e1s sencilla:
                               
                               0 - F\u00e1cil
                                 Disparos: 50
                                 Barcos: 10 (5x(L), 3x(B), 1x(Z), 1x(P)
                               
                               1 - Media
                                 Disparos: 30
                                 Barcos: 5 (2x(L), 1x(B), 1x(Z), 1x(P)
                               
                               2 - Dif\u00edcil
                                 Disparos: 10
                                 Barcos: 2 (1x(L), 1x(B))
                               
                               3 - Personalizado
                                 Disparos: a tu gusto
                                 Barcos: a tu gusto
                                 Mapa: a tu gusto
                               
                               Solo necesitas aprender una cosa m\u00e1s, como se dispara?
                               Bien, solo necesitas escribir la casilla a la que quieras
                               disparar para hacerlo.
                               En el mapa te encontrar\u00e1s estos 3 iconos al estar jugando:
                               
                                 -: Significa que a\u00fan no has disparado a esa posici\u00f3n
                               
                                 A: Significa que tu disparo ha ca\u00eddo en el agua, o dicho de otra forma
                                    lo has fallado estrepitosamente
                               
                                 X: Significa que has golpeado con exito a tu objetivo, que a cual? \u00aa
                               
                               Bueno, me parece que ya est\u00e1s listo para esto de destrozar barcos en el mar
                               gl hf <3
                               """);
        }
        
        if (respuesta==false) {
            System.out.println("Ah, pues vaya :(");
        }
    }
    
    public static int ElegirDificultad() {
        int nivel;
        
        System.out.println("Escoge tu nivel de dificultad:");
        
        nivel = IntroducirNumero();
        while (nivel>3 || nivel<0) {
            System.out.println("Elige un valor desde 0 hasta 3:");
            nivel = IntroducirNumero();}
        
        return nivel;
    }
    
    public static String[][] ComparadorValoresAtaque(String[][] MatrizJugador, String[][] MatrizOculta,String ordencopiada,int DificultadSeleccionada) {
        
        String[][] MatrizFinal=MatrizJugador;
        String orden=ordencopiada;
        if (DificultadSeleccionada!=3) {
            
            while ((MatrizFinal[TraductorAltura(orden)][Character.getNumericValue(orden.charAt(1))].contains("A ")||(MatrizFinal[TraductorAltura(orden)][Character.getNumericValue(orden.charAt(1))].contains("X ")))) {
                System.out.println("Ya has atacado a esa zona\n");

                orden = IntroducirAtaque(MatrizOculta, DificultadSeleccionada);
            }
        
        
            switch (MatrizOculta[TraductorAltura(orden)][Character.getNumericValue(orden.charAt(1))]) {
                case "- "-> {
                    System.out.println("Agua");
                    MatrizFinal[TraductorAltura(orden)][Character.getNumericValue(orden.charAt(1))] = "A ";
                }
                default -> {
                    System.out.println("Tocado");
                    MatrizFinal[TraductorAltura(orden)][Character.getNumericValue(orden.charAt(1))] = "X ";
                }
            }
        }
        else {
            
            while ((MatrizFinal[TraductorAltura(orden)][Integer.parseInt(orden.substring(1,3))].contains("A ")||(MatrizFinal[TraductorAltura(orden)][Integer.parseInt(orden.substring(1,3))].contains("X ")))) {
                System.out.println("Ya has atacado a esa zona\n");

                orden = IntroducirAtaque(MatrizOculta, DificultadSeleccionada);
            }
            
            switch (MatrizOculta[TraductorAltura(orden)][Integer.parseInt(orden.substring(1,3))]) {
                case "- "-> {
                    System.out.println("Agua");
                    MatrizFinal[TraductorAltura(orden)][Integer.parseInt(orden.substring(1,3))] = "A ";
                }
                default -> {
                    System.out.println("Tocado");
                    MatrizFinal[TraductorAltura(orden)][Integer.parseInt(orden.substring(1,3))] = "X ";
                }
            }
        }
        
        System.out.println("\n");
        
        VisualizadorDeMatrices(MatrizFinal);
        
        System.out.println("\n");
        
        return MatrizFinal;
    }
    
    public static boolean HaDado(String[][] MatrizJugador,String ordencopiada,int DificultadSeleccionada) {
        
        if (DificultadSeleccionada!=3)
            if (MatrizJugador[TraductorAltura(ordencopiada)][Character.getNumericValue(ordencopiada.charAt(1))].equals("A ")) 
                return false;
            else
               return true;
        else
            if (MatrizJugador[TraductorAltura(ordencopiada)][Integer.parseInt(ordencopiada.substring(1,3))].equals("A ")) 
                return false;
            else
               return true;
    }
}
