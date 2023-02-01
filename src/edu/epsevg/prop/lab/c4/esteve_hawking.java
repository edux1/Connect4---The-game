/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.epsevg.prop.lab.c4;

/**
 *
 * Autors: Araceli Sandoval i Eduardo Pinto
 */
public class esteve_hawking implements Jugador, IAuto
{
  private String nom;
  private int jugades_explorades;
  private int nodes_explorats;
  private int profunditat;
  private int jugades_explorades_total;
  private int nodes_explorats_total;
  
  /**
   * 
   * @param profunditat indica la profunditat que explorarà en l'arbre min-max.
   */
  public esteve_hawking(int profunditat)
  {
    nom = "Esteve Hawking";
    this.profunditat = profunditat;
  }
  /**
   * Tria una columna per fer el següent moviment
   * @param t estat del tauler a partir del qual triarem un moviment
   * @param jugador jugador al qual li toca escollir un moviment
   * @return enter entre 0 i t.getMida()-1
   */
  public int moviment(Tauler t, int jugador)
  {
    return Tria_moviment_alpha_beta(t, profunditat, jugador);
    //return Tria_moviment(t, profunditat, jugador);
  }
  
  /**
   * Calcula quina es la millor columna per afegir la següent fitxa fent servir l'algoritme min-max
   * @param estat estat del tauler a partir del qual triarem un moviment
   * @param profunditat Nivells de l'arbre que queden per visitar
   * @param jugador jugador al qual li toca escollir moviment
   * @return enter entre 0 i t.getMida()-1
   */
  public int Tria_moviment(Tauler estat, int profunditat, int jugador){
      int valor = Integer.MIN_VALUE;
      int millorMoviment = -1;
      //Incrementem els nodes explorats
      nodes_explorats++;
      nodes_explorats_total++;
      for (int i = 0; i < estat.getMida(); i++) {
          
          //Comprovem si el moviment es pot fer
          if (estat.movpossible(i)) {
              
              //Fem una copia del tauler actual i li afegim el moviment a la columna i
              Tauler aux = new Tauler(estat);
              aux.afegeix(i, jugador);
              
              //Comprovem si el moviment ens dona la victoria
              if(aux.solucio(i, jugador)) {
                  //Incrementem les jugades explorades
                  jugades_explorades++;
                  jugades_explorades_total++;
                  System.out.println("Jugades explorades: " + jugades_explorades + "\n");
                  
                  //Reiniciem les jugades explorades
                  jugades_explorades = 0;
                  //Returnem la columna que ens dona la victoria
                  return i;
              }
              else {
                  //Obtenim el valor de l'heuristica retornat pel node fill
                  int candidat = min_valor(aux, profunditat - 1, -jugador);
                  //Si el valor de candidat es més gran al valor actual, actualitzem el valor i actualitzem el millorMoviment
                  if (valor <= candidat) {
                      valor = candidat;
                      millorMoviment = i;
                  }
              }
              
              System.out.println("Tirada en columna " + i);
              System.out.println("Heuristica = " + valor + "\n");
              
          }
      }
      //Imprimim el tauler actual i els nodes i jugades explorades.
      estat.pintaTaulerALaConsola();
      System.out.println("Jugades explorades: " + jugades_explorades);
      System.out.println("Nodes explorats: " + nodes_explorats);
      System.out.println("Jugades explorades totals: " + jugades_explorades_total);
      System.out.println("Nodes explorats totals: " + nodes_explorats_total);
      //Reiniciem nodes i jugades explorades
      nodes_explorats = 0;
      jugades_explorades = 0;
      return millorMoviment;
  }
  
  /**
   * Simula la millor jugada del que seria el jugador rival, fent servir l'algoritme min-max
   * @param estat estat del tauler a partir del qual triarem un moviment
   * @param profunditat Nivells de l'arbre que queden per visitar
   * @param jugador jugador rival
   * @return valor calculat a partir de l'heurística. 
   */
  public int min_valor(Tauler estat, int profunditat, int jugador) {
      //Incrementem els nodes explorats
      nodes_explorats++;
      nodes_explorats_total++;
      //Si no hi ha cap moviment possible o no hem de visitar més nivells de profunditat.
      if(!estat.espotmoure() || profunditat == 0) {
          //Incrementem les jugades explorades
          jugades_explorades++;
          jugades_explorades_total++;
          //Calculem l'heuristica i retornem el valor
          return heuristica(estat, -jugador);
      } 
      int valor = Integer.MAX_VALUE;
      for (int i = 0; i < estat.getMida(); i++) {
          
          //Comprovem si el moviment es pot fer
          if (estat.movpossible(i)) {
              
              //Fem una copia del tauler actual i li afegim el moviment a la columna i
              Tauler aux = new Tauler(estat);
              aux.afegeix(i, jugador);
              //Comprovem si el moviment dona la victoria a jugador
              if(aux.solucio(i, jugador)) {
                  //Incrementem les jugades explorades
                  jugades_explorades++;
                  jugades_explorades_total++;
                  //El valor d'aquesta jugada es -Infinit
                  return Integer.MIN_VALUE;
              }
              //Es tria el valor més petit entre el valor actual i el valor retornat per el node fill
              valor = Math.min(valor, max_valor(aux, profunditat - 1, -jugador));
          }
      }
      //Retorna el valor de l'heuristica
      return valor;
  }
  
  /**
   * Simula la millor jugada del que seria el jugador actual, fent servir l'algoritme min-max
   * @param estat estat del tauler a partir del qual triarem un moviment
   * @param profunditat Nivells de l'arbre que queden per visitar
   * @param jugador jugador actual
   * @return valor calculat a partir de l'heurística. 
   */
  public int max_valor(Tauler estat, int profunditat, int jugador) {
      //Incrementem els nodes explorats
      nodes_explorats++;
      nodes_explorats_total++;
      //Si no hi ha cap moviment possible o no hem de visitar més nivells de profunditat.
      if(!estat.espotmoure() || profunditat == 0) {
          //Incrementem les jugades explorades
          jugades_explorades++;
          jugades_explorades_total++;
          //Calculem l'heuristica i retornem el valor
          return heuristica(estat, jugador);
      }
      int valor = Integer.MIN_VALUE;
      for (int i = 0; i < estat.getMida(); i++) {
          //Comprovem si el moviment es pot fer
          if (estat.movpossible(i)) {
              //Fem una copia del tauler actual i li afegim el moviment a la columna i
              Tauler aux = new Tauler(estat);
              aux.afegeix(i, jugador);
              //Comprovem si el moviment dona la victoria a jugador
              if(aux.solucio(i, jugador)) {
                  //Incrementem les jugades explorades
                  jugades_explorades++;
                  jugades_explorades_total++;
                  //El valor d'aquesta jugada es +Infinit
                  return Integer.MAX_VALUE;
              }
              //Es tria el valor més gran entre el valor actual i el valor retornat per el node fill
              valor = Math.max(valor, min_valor(aux, profunditat - 1, -jugador));
          }
      }
      //Retorna el valor de l'heuristica
      return valor;
  }
  
  /**
   * Millora de l'algoritme min-max, calcula quina es la millor columna per afegir la següent fitxa 
   * @param estat estat del tauler a partir del qual triarem un moviment
   * @param profunditat Nivells de l'arbre que queden per visitar
   * @param jugador jugador al qual li toca escollir moviment
   * @return enter entre 0 i t.getMida()-1
   */
  public int Tria_moviment_alpha_beta(Tauler estat, int profunditat, int jugador){
      int valor = Integer.MIN_VALUE;
      int millorMoviment = -1;
      int alpha = Integer.MIN_VALUE, beta = Integer.MAX_VALUE;
      //Incrementem els nodes explorats
      nodes_explorats++;
      nodes_explorats_total++;
      for (int i = 0; i < estat.getMida(); i++) {
          //Comprovem si el moviment es pot fer
          if (estat.movpossible(i)) {
              //Fem una copia del tauler actual i li afegim el moviment a la columna i
              Tauler aux = new Tauler(estat);
              aux.afegeix(i, jugador);
              //Comprovem si el moviment ens dona la victoria
              if(aux.solucio(i, jugador)) {
                  //Incrementem les jugades explorades
                  jugades_explorades++;
                  jugades_explorades_total++;
                  System.out.println("Jugades explorades: " + jugades_explorades + "\n");
                  //Reiniciem les jugades explorades
                  jugades_explorades = 0;
                  //Returnem la columna que ens dona la victoria
                  return i;
              }
              else {
                  //Obtenim el valor de l'heuristica retornat pel node fill
                  int candidat = min_valor_alpha_beta(aux, profunditat - 1, -jugador, alpha, beta);
                  //Si el valor de candidat es més gran al valor actual, actualitzem el valor i actualitzem el millorMoviment
                  if (valor <= candidat) {
                      valor = candidat;
                      millorMoviment = i;
                  }
              }
              System.out.println("Tirada en columna " + i);
              System.out.println("Heuristica = " + valor + "\n");
              
              
          }
      }
      //Imprimim el tauler actual i els nodes i jugades explorades.
      estat.pintaTaulerALaConsola();
      System.out.println("Jugades explorades: " + jugades_explorades);
      System.out.println("Nodes explorats: " + nodes_explorats);
      System.out.println("Jugades explorades totals: " + jugades_explorades_total);
      System.out.println("Nodes explorats totals: " + nodes_explorats_total);
      //Reiniciem nodes i jugades explorades
      nodes_explorats = 0;
      jugades_explorades = 0;
      return millorMoviment;
  }
  
  /**
   * Simula la millor jugada del que seria el jugador rival, fent servir la poda alfa-beta
   * @param estat estat del tauler a partir del qual triarem un moviment
   * @param profunditat Nivells de l'arbre que queden per visitar
   * @param jugador jugador rival
   * @param alpha Màxim vigent exigible al node pare
   * @param beta Mínim actual dels fills (= minimax)
   * @return enter entre 0 i t.getMida()-1
   */
  public int min_valor_alpha_beta(Tauler estat, int profunditat, int jugador, int alpha, int beta) {
      //Incrementem els nodes explorats
      nodes_explorats++;
      nodes_explorats_total++;
      //Si no hi ha cap moviment possible o no hem de visitar més nivells de profunditat.
      if(!estat.espotmoure() || profunditat == 0) {
          //Incrementem les jugades explorades
          jugades_explorades++;
          jugades_explorades_total++;
          //Calculem l'heuristica i retornem el valor
          return heuristica(estat, -jugador);
      } 
      int valor = Integer.MAX_VALUE;
      for (int i = 0; i < estat.getMida(); i++) {
          //Actualizem la columna a visitar, donant prioritat a les columnes centrals
          int millora = (i+1)%8;
          //Comprovem si el moviment es pot fer
          if (estat.movpossible(millora)) {
              //Fem una copia del tauler actual i li afegim el moviment a la columna i
              Tauler aux = new Tauler(estat);
              aux.afegeix(millora, jugador);
              //Comprovem si el moviment dona la victoria a jugador
              if(aux.solucio(millora, jugador)) {
                  //Incrementem les jugades explorades
                  jugades_explorades++;
                  jugades_explorades_total++;
                  //El valor d'aquesta jugada es -Infinit
                  return Integer.MIN_VALUE;
              }
              //Es tria el valor més petit entre el valor actual i el valor retornat per el node fill
              valor = Math.min(valor, max_valor_alpha_beta(aux, profunditat - 1, -jugador, alpha, beta));
              //Retornem el valor en cas de ser menor o igual a alpha
              if(valor <= alpha) {
                  return valor;
              }
              //Actualitzem beta amb el valor més petit entre el valor actual i la beta actual
              beta = Math.min(valor, beta);
          }
      }
      //Retorna el valor de l'heuristica
      return valor;
  }
  
  /**
   * Simula la millor jugada del que seria el jugador actual, fent servir la poda alfa-beta
   * @param estat estat del tauler a partir del qual triarem un moviment
   * @param profunditat Nivells de l'arbre que queden per visitar
   * @param jugador jugador actual
   * @param alpha Màxim actual dels fills (= minimax)
   * @param beta Mínim vigent  exigible al node pare 
   * @return enter entre 0 i t.getMida()-1
   */
  public int max_valor_alpha_beta(Tauler estat, int profunditat, int jugador, int alpha, int beta) {
      //Incrementem els nodes explorats
      nodes_explorats++;
      nodes_explorats_total++;
      //Si no hi ha cap moviment possible o no hem de visitar més nivells de profunditat.
      if(!estat.espotmoure() || profunditat == 0) {
          //Incrementem les jugades explorades
          jugades_explorades++;
          jugades_explorades_total++;
          //Calculem l'heuristica i retornem el valor
          return heuristica(estat, jugador);
      }
      int valor = Integer.MIN_VALUE;
      for (int i = 0; i < estat.getMida(); i++) {
          //Actualizem la columna a visitar, donant prioritat a les columnes centrals
          int millora = (i+1)%8;
          if (estat.movpossible(millora)) {
              //Fem una copia del tauler actual i li afegim el moviment a la columna i
              Tauler aux = new Tauler(estat);
              aux.afegeix(millora, jugador);
              //Comprovem si el moviment dona la victoria a jugador
              if(aux.solucio(millora, jugador)) {
                  //Incrementem les jugades explorades
                  jugades_explorades++;
                  jugades_explorades_total++;
                  //El valor d'aquesta jugada es +Infinit
                  return Integer.MAX_VALUE;
              }
              //Es tria el valor més gran entre el valor actual i el valor retornat per el node fill
              valor = Math.max(valor, min_valor_alpha_beta(aux, profunditat - 1, -jugador, alpha, beta));
              //Retornem el valor en cas de ser gran o igual a beta
              if(beta <= valor) {
                  return valor;
              }
              //Actualitzem alpha amb el valor més gran entre el valor actual i alpha actual
              alpha = Math.max(valor, alpha);
          }
      }
      //Retorna el valor de l'heuristica
      return valor;
  }
  
  /**
   * Dona valor al l'estat del tauler respecte a un jugador
   * @param estat estat del tauler rebut
   * @param jugador jugador al qual evaluarem la seva posició en el tauler
   * @return valor del tauler actual
   */
  public int heuristica(Tauler estat, int jugador) {
      int score = 0;
      //Comprovem els punts que obtenen horitzonat i verticalment
      score += comprova_horitzontal(estat, jugador);
      score += comprova_vertical(estat, jugador);
      return score;
  }
  
  /**
   * Comprova per files l'estat del tauler i puntua l'estat del tauler respecte al jugador indicat
   * @param estat tauler a puntuar
   * @param jugador jugador a avaluar
   * @return puntuació de l'estat del tauler respecte les files
   */
  public int comprova_horitzontal(Tauler estat, int jugador) {
      int score = 0; 
      //Recorrem la matriu començant per abaix a l'esquerra
      for(int i = estat.getMida()-1; i > -1; i--) {
          int puntuacio = 0, seguides = 0, colorAnt = 0;
          int lliure = 0, lliuresConsecutives = 0;
          for(int j = 0; j < estat.getMida(); j++) {
              //En cas colorAnt encara no tingui el valor d'un jugador
              if(colorAnt == 0) {
                  //Actualitzem colorAnt amb el valor de la casella actual
                  colorAnt = estat.getColor(i, j);
                  //Si colorAnt te el valor d'un jugador
                  if(colorAnt != 0) {
                      //El número de caselles lliures es igual al número de caselles amb valor 0 consecutives que hem trobat anteriorment
                      lliure = seguides;
                      //Reiniciem el valor de fitxes seguides
                      seguides = 0;
                  }
              }
              //Si el color de la casella actual es igual al colorAnt
              if(estat.getColor(i, j) == colorAnt) {
                  //Aumente el número de fitxes seguides
                  seguides++;
                  //No tenim espais lliures consecutius, perque hi ha una fitxa
                  lliuresConsecutives = 0;
              }
              //Si el color de la casella actual está ocupat per l'altre jugador
              else if (estat.getColor(i, j) == -colorAnt && colorAnt != 0) {
                  //Donem valor a les fitxes consecutives que hem trobat en cas que hi hagi espai per fer 4 en ratlla
                  if(seguides == 1 && lliure >= 3) {
                      puntuacio = 1;
                  }
                  else if(seguides == 2 && lliure >= 2) {
                      puntuacio = 3;
                  }
                  else if(seguides >= 3 && lliure >= 1){
                      puntuacio = 7;
                  }
                  else puntuacio = 0;
                  
                  //Remplacem les caselles lliures pel valor de caselles lliuresConsecutives
                  if(colorAnt != 0) {
                      lliure = lliuresConsecutives;
                  }
                  //Si les fitxes seguides son del jugador actual, sumem els punts
                  if(colorAnt == jugador) {
                      score += puntuacio;
                  }
                  //Si es fitxes son del rival, restem els punts
                  else if(colorAnt == -jugador) {
                      score -= puntuacio;
                  }
                  //Actualizem colorAnt
                  colorAnt = estat.getColor(i, j);
                  //Primera fitxa d'aquest color que trobem
                  seguides = 1;
                  //Reiniciem les lliuresConsecutives
                  lliuresConsecutives = 0;
              }
              //Si no hi ha cap fitxa, aumentem les caselles lliures
              else {
                  lliuresConsecutives++;
                  lliure++;
              }
          }
          //En cas de no haver-hi sumat la puntuació al fer canvi de fila, afegim els punts abans de fer el canvi.
          if(seguides == 1 && lliure >= 3) {
              puntuacio = 1;
          }
          else if(seguides == 2 && lliure >= 2) {
              puntuacio = 3;
          }
          else if(seguides >= 3 && lliure >= 1){
              puntuacio = 10;
          }
          else puntuacio = 0;
          //Si les fitxes seguides son del jugador actual, sumem els punts
          if(colorAnt == jugador) {
              score += puntuacio;
          }
          //Si les fitxes seguides son del jugador rival, restem els punts
          else if(colorAnt == -jugador) {
              score -= puntuacio;
          }
      }
      return score;
  }
  
  /**
   * Comprova per columnes l'estat del tauler i puntua l'estat del tauler respecte al jugador indicat
   * @param estat tauler a puntuar
   * @param jugador jugador a avaluar
   * @return puntuació de l'estat del tauler respecte les columnes
   */
  public int comprova_vertical(Tauler estat, int jugador) {
      int score = 0;
      //Recorrem la matriu començant per abaix a l'esquerra
      for(int i = estat.getMida()-1; i > -1; i--) {
          int puntuacio = 0;
          int seguides = 0;
          int colorBuscat = 0;
          for(int j = 0; j < estat.getMida(); j++) {
              //Comprovem si hi ha alguna fitxa en la casella actual
              if(estat.getColor(j, i) == 0) {
                  //En cas de no haver-hi cap fitxa, asignem un valor a la j que en asseguri fer el salt de columna en la següent iteració
                  j = estat.getMida();
                  //Actualizem la puntuació de la jugada en cas que hi hagin fitxes seguides.
                  if(seguides == 1) {
                      puntuacio = 1;
                  }
                  else if(seguides == 2) {
                      puntuacio = 3;
                  }
                  else if(seguides >= 3){
                      puntuacio = 10;
                  }
                  else puntuacio = 0;
                  //Si color buscat es el d'un jugador
                  if(colorBuscat != 0) {
                      //Sumem en cas de ser el jugador actual
                      if(colorBuscat == jugador) {
                          score += puntuacio;
                      }
                      else {
                          //Restem en cas de ser el jugador rival
                          score -= puntuacio;
                      }
                  }
              }
              else {
                  //Primera fila de la columna
                  if(j == 0) {
                      //Donem un valor d'un jugador al colorBuscat
                      colorBuscat = estat.getColor(j, i);
                      seguides = 1;
                  }
                  //Si la fitxa actual es la mateixa que la fitxa anterior
                  else if(estat.getColor(j, j) == colorBuscat) {
                      //Aumenten les fitxes seguides
                      seguides++;
                  }
                  //Si la fitxa actual es la del l'altra jugador
                  else if(estat.getColor(j, i) == -colorBuscat){
                      //Si no hi ha espai per 4 en ratlla.
                      if(j > 4) { 
                          // Fem aixó per saltar a la següent iteració.
                          j = estat.getMida();
                      }
                      else {
                          //Actualitzem colorBuscat
                          colorBuscat = estat.getColor(j, i);
                          //Reiniciem el recompte de fitxes seguides
                          seguides = 1;
                      }
                      
                  }
              }
          }
              
      }
      return score;
  }
  
  /**
   * Retorna el nom del jugador
   * @return nom del jugador 
   */
  public String nom()
  {
    return nom;
  }
  
}


