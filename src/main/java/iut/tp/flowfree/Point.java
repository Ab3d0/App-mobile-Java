package iut.tp.flowfree;

public class Point {

    int colonne;
    int ligne;

    public Point(int colonne, int ligne) {
        this.colonne = colonne;
        this.ligne = ligne;
    }

    public String toString(){
        return "["+colonne+", "+ligne+"]";
    }

    public int getColonne(){
        return this.colonne;
    }

    public int getLigne(){
        return this.ligne;
    }


}
