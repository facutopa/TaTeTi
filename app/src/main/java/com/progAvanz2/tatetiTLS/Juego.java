package com.progAvanz2.tatetiTLS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.Handler;
import java.util.Arrays;
import java.util.Random;

public class Juego extends AppCompatActivity
{
    //Inicializa boton Salir
    Button btnSalir;

    //Modo De Juego
    boolean btnElegido;

    //Empieza el juego
    TextView resultado;
    Integer[] botones;
    int[] tablero=new int[]{
            0, 0, 0,
            0, 0, 0,
            0, 0, 0
    }; //marca que toca cada uno ej Sist=-1, Jugador=1

    int seguirJugando = 0;
    int fichasPuestas = 0;
    int turno = 1;  // indica quien esta poniendo fichas en cada momento
    int[] posGanadora=new int[]{-1, -1, -1}; //inicializamos

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);

        btnSalir=findViewById(R.id.buttonSalir);
        btnSalir.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnElegido=getIntent().getBooleanExtra("Eleccion", btnElegido);

        resultado=(TextView) findViewById(R.id.textViewRtado);
        resultado.setVisibility(View.INVISIBLE);

        botones= new Integer[]{
                R.id.b1,R.id.b2,R.id.b3,
                R.id.b4,R.id.b5,R.id.b6,
                R.id.b7,R.id.b8,R.id.b9,
        };

    }

    public void marcarCasillero(View v) throws InterruptedException {
        if(seguirJugando == 0){
            if(btnElegido == false){
                turno = 1;}
            int numBoton= Arrays.asList(botones).indexOf(v.getId());
            if(tablero[numBoton] == 0)
            { //comprueba que no pongamos fichas en donde ya hay
                if(btnElegido == false)
                { //1 jugador
                    v.setBackgroundResource(R.drawable.marcax);
                    tablero[numBoton]=1;
                }
                fichasPuestas+=1;
                seguirJugando=comprobarEstado();
                terminoPartida();
                if(seguirJugando==0) {
                    if(btnElegido == false){
                        turno= -1;
                        maquina();
                        fichasPuestas+=1;
                        seguirJugando=comprobarEstado();
                        terminoPartida();
                    }
                }

            }
        }

    }

    public void maquina() {

        int pos=dosEnRaya();

        if (pos==-1) //no encontró ningún 2 en raya entonces busca una posición random
        {
            Random ran= new Random ();
            pos=ran.nextInt(tablero.length);

            while(tablero[pos] != 0) { //verifica que la posición no este ocupada
                pos = ran.nextInt(tablero.length);
            }
        }
        //posiciona en la posición random o directamente no entró al if xq sí había encontrado un 2 en raya (en ese caso usa esa posición)
        Button b=(Button) findViewById(botones[pos]);
        b.setBackgroundResource(R.drawable.marcao);
        tablero[pos]=-1;

    }


    public int dosEnRaya () {
        int posicion = -1;

        posicion=comprobar(posicion, -2); //comprueba si puede ganar

        if(posicion==-1) //si no encontró q puede ganar, vuelve a buscar pero para bloquear
        {
            posicion=comprobar(posicion,2);
        }

        return (posicion);
    }

    public int comprobar (int posicion, int resultado)
    {
        int suma=0;

        int i = 0; //verifica filas
        while (i < 9 && suma != resultado) {
            suma = 0;
            suma = (tablero[i] + tablero[i + 1] + tablero[i + 2]);
            i += 3;
        }
        i -= 3;
        int j = i + 1;
        int k = i + 2;
        if (suma != resultado) { //verifica columnas
            i = 0;
            j = 3;
            k = 6;
            while (i < 3 && suma != resultado) {
                suma = 0;
                suma = (tablero[i] + tablero[j] + tablero[k]);
                i++;
                j++;
                k++;
            }
            i--;
            j--;
            k--;
            if (suma != resultado) { //verifica diagonales
                i = 0;
                j = 4;
                k = 8;
                while (i < 4 && suma != resultado) {
                    suma = 0;
                    suma = (tablero[i] + tablero[j] + tablero[k]);
                    i += 2;
                    k -= 2;
                }
                i -= 2;
                k += 2;
            }
        }

        if (suma == resultado) //encontró un 2 en raya
        {
            //identifico cuál de las 3 posiciones es la que está vacía (0) para marcar
            if (tablero[i] == 0)
                posicion = i;
            else {
                if (tablero[j] == 0)
                    posicion = j;
                else
                    posicion = k;
            }
        }

        return (posicion);
    }



    public int comprobarEstado (){
        int nuevoEstado = 0;

        int suma=0;
        int i=0; //verifica filas
        while(i<9 && suma != 3){
            suma=0;
            suma=Math.abs(tablero[i]+tablero[i+1]+tablero[i+2]);
            i+=3;
        }
        i-=3;
        int j=i+1;
        int k=i+2;
        if(suma != 3){ //verifica columnas
            i=0;
            j=3;
            k=6;
            while(i<3 && suma != 3){
                suma=0;
                suma=Math.abs(tablero[i]+tablero[j]+tablero[k]);
                i++;
                j++;
                k++;
            }
            i--; j--; k--;
            if(suma != 3){ //verifica diagonales
                i=0;
                j=4;
                k=8;
                while(i<4 && suma != 3){
                    suma=0;
                    suma=Math.abs(tablero[i]+tablero[j]+tablero[k]);
                    i+=2;
                    k-=2;
                }
                i-=2; k+=2;
            }
        }

        if(suma == 3){
            posGanadora= new int[]{i , j, k};
            nuevoEstado=1*turno;
        }
        else{
            if(fichasPuestas == 9){
                nuevoEstado=2;
            }
        }

        return nuevoEstado;
    }

    public void terminoPartida(){
        int fichaGanadora = R.drawable.marcaganox;
        if(seguirJugando == 1 || seguirJugando == -1){ //si ganó o perdió
            if(seguirJugando == 1){ //si ganó
                resultado.setVisibility(View.VISIBLE);
                resultado.setText("Ganaste!");
                resultado.setTextColor(Color.rgb(173,201,101));


            }
            else{ //perdió
                resultado.setVisibility(View.VISIBLE);
                resultado.setText("Perdiste");
                resultado.setTextColor(Color.rgb(193,55,29));
                fichaGanadora=R.drawable.marcaganoo;

            }
            for(int i=0; i<posGanadora.length; i++){ //posiciona la ficha ganadora
                Button b =findViewById(botones[posGanadora[i]]);
                b.setBackgroundResource(fichaGanadora);
            }
        }
        else{
            if(seguirJugando == 2){
                resultado.setVisibility(View.VISIBLE);
                resultado.setText("¡Empate!");
                resultado.setTextColor(Color.rgb(255,255,255));
            }
        }
    }

}
