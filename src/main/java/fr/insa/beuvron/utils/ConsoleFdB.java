/*
    Copyright 2000-2011 Francois de Bertrand de Beuvron

    This file is part of UtilsBeuvron.

    UtilsBeuvron is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    UtilsBeuvron is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with UtilsBeuvron.  If not, see <http://www.gnu.org/licenses/>.
 */

package fr.insa.beuvron.utils;

import java.io.*;

public class ConsoleFdB implements java.io.Serializable {

    static final long serialVersionUID = 30101L;

    /** même méthode appelle System.out.println mais sans envoie d'exceptions
     * @param mess la chaîne à afficher
     * @see java.io.PrintStream#println(String)
     * @see java.lang.System#out
     */
    public static void println(String mess) {
        System.out.println(mess);
    }

    /** même méthode appelle System.out.print mais sans envoie d'exceptions
     * @param mess la chaîne à afficher
     * @see java.io.PrintStream#println(String)
     * @see java.lang.System#out
     */
    public static void print(String mess) {
        System.out.print(mess);
    }

    /** cette methode affiche un message sur le flux de sortie standard (System.out), et
     * attend que l'utilisateur entre une ligne de texte qui sera convertie
     * en entier sur le flux d'entrée standard (System.in)
     * cette methode redemande tant qu'un entier correct n'a pas été entré
     * elle termine le programme en cas d'exeption d'entré/sortie
     *
     * @param s message à l'utilisateur
     * @return un entier tapé par l'utilisateur
     * @see java.lang.System#in
     * @see java.lang.System#out
     */
    public static int entreeEntier(String s) {
        boolean encore = true;
        int res = 0;

        while (encore) {
            try {
                println(s);
                res = Integer.parseInt(myinput.readLine());
                encore = false;
            } catch (IOException e) {
                throw new Error(e);
            } catch (NumberFormatException e) {
                println("entier non valide, essayez encore");
                encore = true;
            }
        }
        return res;
    }

    /** même chose que {@link #entreeEntier}, seul le nom change
     */
    public static int entreeInt(String s) {
        return entreeEntier(s);
    }

    /** cette methode affiche un message sur le flux de sortie standard (System.out), et
     * attend que l'utilisateur entre une ligne de texte qui sera convertie
     * en entier long sur le flux d'entrée standard (System.in)
     * cette methode redemande tant qu'un entier correct n'a pas été entré
     * elle termine le programme en cas d'exeption d'entré/sortie
     *
     * @param s message à l'utilisateur
     * @return un entier tapé par l'utilisateur
     * @see java.lang.System#in
     * @see java.lang.System#out
     */
    public static long entreeLong(String s) {
        boolean encore = true;
        long res = 0;

        while (encore) {
            try {
                println(s);
                res = Long.parseLong(myinput.readLine());
                encore = false;
            } catch (IOException e) {
                throw new Error(e);
            } catch (NumberFormatException e) {
                println("entier non valide, essayez encore");
                encore = true;
            }
        }
        return res;
    }

    /** cette methode affiche un message sur le flux de sortie standard (System.out), et
     * attend que l'utilisateur entre O pour oui ou N pour non
     * cette methode redemande tant qu'un entier correct n'a pas été entré
     * elle termine le programme en cas d'exeption d'entré/sortie
     *
     * @param s message à l'utilisateur
     * @return un entier tapé par l'utilisateur
     * @see java.lang.System#in
     * @see java.lang.System#out
     */
    public static boolean entreeBooleanON(String s) {
        String rep = "";

        while ((rep.compareToIgnoreCase("O") != 0) && (rep.compareToIgnoreCase("N") != 0)) {
            try {
                println(s);
                rep = myinput.readLine();
            } catch (IOException e) {
                throw new Error(e);
            }
        }
        return rep.compareToIgnoreCase("O") == 0;
    }

    /** cette methode affiche un message sur le flux de sortie standard (System.out), et
     * attend que l'utilisateur entre une ligne de texte qui sera convertie
     * en flottant double précision sur le flux d'entrée standard (System.in)
     * cette methode redemande tant qu'un flottant correct n'a pas été entré
     * elle termine le programme en cas d'exeption d'entré/sortie
     *
     * @param s message à l'utilisateur
     * @return le premier caractère tapé par l'utilisateur
     * @see java.lang.System#in
     * @see java.lang.System#out
     */
    public static double entreeDouble(String s) {
        boolean encore = true;
        double res = 0;

        while (encore) {
            try {
                println(s);
                res = Double.parseDouble(myinput.readLine());
                encore = false;
            } catch (IOException e) {
                throw new Error(e);
            } catch (NumberFormatException e) {
                println("flottant non valide, essayez encore");
                encore = true;
            }
        }
        return res;
    }

    /** cette methode affiche un message sur le flux de sortie standard (System.out), et
     * attend que l'utilisateur entre une ligne de texte  sur le flux d'entrée standard (System.in)
     * dont le premier caractère sera retourné
     * cette methode redemande tant qu'un caractère n'a pas été entré
     * elle termine le programme en cas d'exeption d'entré/sortie
     *
     * @param s message à l'utilisateur
     * @return un entier tapé par l'utilisateur
     * @see java.lang.System#in
     * @see java.lang.System#out
     */
    public static char entreeChar(String s) {
        boolean encore = true;
        char res = ' ';

        while (encore) {
            try {
                println(s);
                String rep = myinput.readLine();
                if (rep != null && rep.length() > 0) {
                    res = rep.charAt(0);
                    encore = false;
                }
            } catch (IOException e) {
                throw new Error(e);
            }
        }
        return res;
    }

    /** cette methode affiche un message sur le flux de sortie standard (System.out), et
     * attend que l'utilisateur entre une ligne de texte  sur le flux d'entrée standard (System.in)
     * qui sera retournée sous forme de String (sans le caractère de fin de ligne)
     * cette methode termine le programme en cas d'exeption d'entré/sortie
     *
     * @param s message à l'utilisateur
     * @return une chaîne tapées par l'utilisateur
     * @see java.lang.System#in
     * @see java.lang.System#out
     */
    public static String entreeString(String s) {
        String res = null;

        try {
            println(s);
            res = myinput.readLine();
        } catch (IOException e) {
            throw new Error(e);
        }
        return res;
    }

    /** cette methode affiche un message sur le flux de sortie standard (System.out), et
     * attend que l'utilisateur entre plusieurs lignes de texte  sur le flux d'entrée standard (System.in)
     * qui sera retournée sous forme de String.
     * La saisie s'arrete quand l'utilisateur tape "fin" sur une ligne.
     * cette methode termine le programme en cas d'exeption d'entré/sortie
     *
     * @param s message à l'utilisateur
     * @return une chaîne tapées par l'utilisateur
     * @see java.lang.System#in
     * @see java.lang.System#out
     */
    public static String entreeTexte(String s) {
        boolean encore = true;
        String res = null;

        try {
            println(s);
            println("(tapez une ligne contenant seulement \"fin\" pour arreter la saisie)");
            res = "";
            String cur = "";
            while (cur.compareTo("fin") != 0) {
                cur = myinput.readLine();
                if (cur.compareTo("fin") != 0) {
                    res = res + cur;
                }
            }
        } catch (IOException e) {
            System.exit(1);
        }
        return res;
    }
    /* myinput permet a l'utilisateur d'utiliser le flux standard d'entré
    (usuellement associé au clavier) pour entrer des données
     */
    private static BufferedReader myinput = new BufferedReader(new InputStreamReader(System.in));
}
