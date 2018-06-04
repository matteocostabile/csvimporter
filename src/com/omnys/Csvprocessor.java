package com.omnys;

import com.sun.xml.internal.ws.util.StringUtils;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.FileOutputStream;
import java.util.regex.Pattern;


class Csvprocessor{

    private String filename;
    private ArrayList<Csvline> lines;


    Csvprocessor(String filename) {

        this.filename = filename;
        this.lines = new ArrayList<Csvline>();


        File file = new File(filename);

        try{
            Scanner input = new Scanner(file);

            System.out.println("file caricato");

            while(input.hasNext()){

                String dati = input.next();
                String[] colonne = dati.split(",");


                String regex = "[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}";
                Pattern.compile(regex);

                if (check(regex, colonne[2])== true) {
                    //System.out.println(dati);
                    Csvline linea = new Csvline(StringUtils.capitalize(colonne[0]), StringUtils.capitalize(colonne[1]), colonne[2]);
                    this.lines.add(linea);
                }else {
                    System.out.println("errore!");
                }


            }
            input.close();

            stampaArray(lines);

            FileOutputStream risultato = new FileOutputStream("risultato.csv");
            PrintStream stampa = new PrintStream(risultato);
            for(int i = 0; i < lines.size(); i++){
                stampa.println(lines.get(i));
            }

        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static boolean check(String regex, String email){
        if(Pattern.matches(regex, email))
            return true;
        else
            return false;
    }
    public static void stampaArray(ArrayList<Csvline> daStampare){
        for(int i = 0; i < daStampare.size(); i++){
            System.out.println(daStampare.get(i));
        }
    }

}
