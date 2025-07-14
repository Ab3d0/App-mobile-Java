package iut.tp.flowfree;

import java.util.ArrayList;
import java.util.List;

public class Puzzle {

    private int size;
    private String nom;
    private List<Paire> paires;

    public Puzzle(int size, String nom){
        this.size = size;
        this.nom = nom;
        this.paires = new ArrayList<>();
    }

    public void addPaire(Paire p){
        this.paires.add(p);
    }

    public List<Paire> getPaires() {
        return this.paires;
    }

    public int getSize(){
        return this.size;
    }






}
