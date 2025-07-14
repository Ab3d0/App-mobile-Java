package iut.tp.flowfree;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class createPuzzle {

    private String filename;
    private Context c;

    private AssetManager assetManager;
    private XmlPullParser parser;
    private int eventType;


    private Puzzle p;


    public createPuzzle(Context c, String filename){
        this.filename = filename;
        this.c = c;
        this.openFile();
        this.create();
    }


    public void openFile(){
        this.assetManager = this.c.getAssets();
        try {
            InputStream inputStream = assetManager.open("puzzles/" + filename);
            this.parser = Xml.newPullParser();
            this.parser.setInput(inputStream, null);
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }
    }


    public void create(){
        try {
            while(this.eventType != XmlPullParser.END_DOCUMENT){
                this.parser.next();
                this.eventType = this.parser.getEventType();

                while(this.eventType == XmlPullParser.TEXT){
                    this.parser.next();
                    this.eventType = this.parser.getEventType();
                }

                if(this.eventType == XmlPullParser.START_TAG){
                    String tagName = this.parser.getName();
                    if("puzzle".equals(tagName)){
                        int sizeP = Integer.parseInt(parser.getAttributeValue(null, "size"));
                        String nom = parser.getAttributeValue(null, "nom");

                        Puzzle monPuzzle = new Puzzle(sizeP, nom);
                        this.p = monPuzzle;
                    }

                    if("paire".equals(tagName)){
                        this.parser.next();
                        this.eventType = this.parser.getEventType();
                        while(this.eventType != XmlPullParser.START_TAG){
                            this.parser.next();
                            this.eventType = this.parser.getEventType();
                        }
                        // Prendre premier point
                        int colonne_p1 = Integer.parseInt(parser.getAttributeValue(null, "colonne"));
                        int ligne_p1 = Integer.parseInt(parser.getAttributeValue(null, "ligne"));
                        Point p1 = new Point(colonne_p1, ligne_p1);

                        this.parser.next();
                        this.eventType = this.parser.getEventType();

                        while(this.eventType != XmlPullParser.START_TAG){
                            this.parser.next();
                            this.eventType = this.parser.getEventType();
                        }

                        // Prendre deuxième point
                        int colonne_p2 = Integer.parseInt(parser.getAttributeValue(null, "colonne"));
                        int ligne_p2 = Integer.parseInt(parser.getAttributeValue(null, "ligne"));
                        Point p2 = new Point(colonne_p2, ligne_p2);

                        // Créer paire
                        Paire paire = new Paire(p1, p2);
                        // Ajouter paire au puzzle;
                        this.p.addPaire(paire);
                    }
                }
            }
        } catch(XmlPullParserException | IOException e){
            e.printStackTrace();
        }




    }

    public Puzzle getPuzzle(){
        return this.p;
    }
















}
