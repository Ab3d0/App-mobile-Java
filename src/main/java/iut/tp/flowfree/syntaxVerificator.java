package iut.tp.flowfree;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class syntaxVerificator {

    private Context c;

    private String filename;
    private AssetManager assetManager;
    private XmlPullParser parser;
    private int eventType;
    private int size;
    private String namePuzzle;

    private Boolean hasGoodSyntaxe;

    public syntaxVerificator(String filename, Context c){
        this.filename = filename;
        this.c = c;
        /* Ouverture du fichier */
        this.openFile(this.filename, this.c);
        /* Vérifier d'abord si la racine (<puzzle>) est bonne via une fonction */
        this.hasGoodSyntaxe = this.hasGoodRacine();

        /* Vérifier les paires (son nombre et sa syntaxe) */
        this.hasGoodSyntaxe = this.hasGoodPaire();
    }

    public void openFile(String filename, Context c){
        this.assetManager = this.c.getAssets();
        try {
            InputStream inputStream = assetManager.open("puzzles/" + filename);
            this.parser = Xml.newPullParser();
            this.parser.setInput(inputStream, null);
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }


    }

    public Boolean hasGoodRacine(){
        try {
            this.parser.next();
            this.eventType = this.parser.getEventType();
            if(this.eventType == XmlPullParser.START_TAG){
                String tagName = this.parser.getName();

                if("puzzle".equals(tagName)){
                    if( (this.parser.getAttributeValue(null, "size") == null) || (this.parser.getAttributeValue(null, "nom") == null) ) {
                        return false;
                    } else {
                        this.size = Integer.parseInt(parser.getAttributeValue(null, "size"));
                        this.namePuzzle = parser.getAttributeValue(null, "nom");
                        return this.size >= 5 && this.size <= 14;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch(XmlPullParserException | IOException e){
            e.printStackTrace();
            return false;
        }
    }

    public Boolean hasGoodPaire(){
        try {
            ArrayList<String> pointsList = new ArrayList<>();
            Boolean endOfPuzzle = false;
            Boolean hasOnePaire = false;
            int inPaire = 0; /* 0 = pas dans une paire
                                1 = manque 1 point pour finir la paire
                                2 == manque 2 points pour finir la paire  */

            while(this.eventType != XmlPullParser.END_DOCUMENT) {
                this.parser.next();
                this.eventType = this.parser.getEventType();
                String tagName = this.parser.getName();

                if ("puzzle".equals(tagName)){
                    endOfPuzzle = true;
                    return hasOnePaire;
                } else

                /* Si je ne suis pas dans une paire ( */
                if (inPaire == 0){
                    if (eventType != XmlPullParser.START_TAG && eventType != XmlPullParser.TEXT){
                        return false;
                    } else if(eventType != XmlPullParser.TEXT){
                        if ("paire".equals(tagName)) {
                            inPaire = 2;
                        } else {
                            return false;
                        }
                    }
                } else
                /* si je suis dans une paire, je dois trouver 1 point */
                if (inPaire == 2) {
                    if (eventType != XmlPullParser.START_TAG && eventType != XmlPullParser.TEXT) {
                        return false;
                    } else if(this.eventType != XmlPullParser.TEXT){
                        if ("point".equals(tagName)) {
                            inPaire -= 1;
                            if( (this.parser.getAttributeValue(null, "colonne") == null) || (this.parser.getAttributeValue(null, "ligne") == null) ) {
                                return false;
                            } else {
                                int col = Integer.parseInt(parser.getAttributeValue(null, "colonne"));
                                int row = Integer.parseInt(parser.getAttributeValue(null, "ligne"));
                                if(col > (this.size-1) || row > (this.size-1)){
                                    return false;
                                }

                                String coord = col + "," + row;
                                if (pointsList.contains(coord)) {
                                    return false; // Retourne une erreur si un point est en double
                                }
                                pointsList.add(coord);
                            }
                            this.parser.next();
                            this.eventType = this.parser.getEventType();
                        } else {
                            return false;
                        }
                    }
                } else

                if (inPaire == 1) {
                    if (eventType != XmlPullParser.START_TAG && eventType != XmlPullParser.TEXT) {
                        return false;
                    } else if(this.eventType != XmlPullParser.TEXT){
                        if ("point".equals(tagName)) {
                            inPaire -= 1;
                            if( (this.parser.getAttributeValue(null, "colonne") == null) || (this.parser.getAttributeValue(null, "ligne") == null) ) {
                                return false;
                            } else {
                                int col = Integer.parseInt(parser.getAttributeValue(null, "colonne"));
                                int row = Integer.parseInt(parser.getAttributeValue(null, "ligne"));
                                if(col > (this.size-1) || row > (this.size-1)){
                                    return false;
                                }

                                String coord = col + "," + row;
                                System.out.println(coord);
                                if (pointsList.contains(coord)) {
                                    return false; // Retourne une erreur si un point est en double
                                }
                                pointsList.add(coord);
                            }


                            this.parser.next(); // fermeture du point
                            this.parser.next(); // indentation
                            this.parser.next(); // nouvel balise
                            this.eventType = this.parser.getEventType();
                            hasOnePaire = true;
                            String ClosedtagName = this.parser.getName();
                            if (this.eventType != XmlPullParser.END_TAG && eventType != XmlPullParser.TEXT) {
                                return false;
                            } else if (!("paire".equals(ClosedtagName))) {
                                return false;
                            }
                        }
                    }
                }
            }


            return endOfPuzzle && hasOnePaire;
        }  catch(XmlPullParserException | IOException e){
            e.printStackTrace();
            return false;
        }
    }




    public Boolean getHasGoodSyntaxe(){
        return this.hasGoodSyntaxe;
    }

    public String getNamePuzzle(){
        return this.namePuzzle;
    }

    public int getSize(){
        return this.size;
    }










}
