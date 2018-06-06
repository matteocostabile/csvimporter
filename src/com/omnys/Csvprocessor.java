package com.omnys;

import com.sun.xml.internal.ws.util.StringUtils;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.FileOutputStream;
import java.util.regex.Pattern;
import java.sql.*;

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

            insertLine(lines);

            takeLine();

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

    public static void insertLine(ArrayList<Csvline> linee){
        try{
            for(int i = 0; i < linee.size(); i++) {
                Csvline linea = linee.get(i);

                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/csvimporter?useUnicode=true&serverTimezone=UTC", "root", "root");

                //Statement st = conn.createStatement();

                String query = "" + "INSERT INTO csvlines(name, surname, email)" + "VALUES (?, ?, ?)";
                PreparedStatement preparedStmt = conn.prepareStatement(query);
                preparedStmt.setString(1, linea.getName());
                preparedStmt.setString(2, linea.getSurname());
                preparedStmt.setString(3, linea.getEmail());
                preparedStmt.executeUpdate();

                //st.executeUpdate("INSERT INTO csvlines(name, surname, email)" + "VALUES ('Luca', 'Costabile', 'costabileluca@gmail.com')");

                conn.close();
            }

        }catch(Exception e){
            System.err.println("errore!");
            System.err.println(e.getMessage());
        }
    }
    public static void takeLine(){
        try{

            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/csvimporter?useUnicode=true&serverTimezone=UTC", "root", "root");
            String query = "SELECT * FROM csvlines";

            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery(query);

            while(rs.next()){
                int id = rs.getInt("id");
                String nome = rs.getString("name");
                String cognome = rs.getString("surname");
                String email = rs.getString("email");

                System.out.format("%s, %s, %s, %s\n", id, nome, cognome, email);
            }
            st.close();

        }catch(Exception e){
            System.err.println("errore!");
            System.err.println(e.getMessage());
        }
    }
}