package iut.tp.flowfree;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class playActivity extends Activity {

    private String puzzleFileName;
    private  List<Integer> colors = new ArrayList<>();
    private List<Integer> colorsAchromate = new ArrayList<>();
    private LinearLayout mainLinear;
    private ArrayList<ArrayList<String>> allTraces = new ArrayList<>();
    private boolean gameFinished = false;
    private CaseTouchedListener caseTouchedListener;
    private MainActivity mainActivity;
    private int currentColor = Color.TRANSPARENT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);


        this.puzzleFileName = this.getIntent().getStringExtra("puzzle");

        PreferenceManager.setDefaultValues(this, R.xml.preference, false);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isChecked = prefs.getBoolean("pref_achromate", false);

        mainLinear = findViewById(R.id.gamePuzzle);
        createPuzzle puzzleConstruct = new createPuzzle(this, this.puzzleFileName);
        Puzzle monPuzzle = puzzleConstruct.getPuzzle();
        int nbPaires = monPuzzle.getPaires().size();
        int size = monPuzzle.getSize();
        boolean isEnd = false;

        if (savedInstanceState != null) {
            // Récupération des couleurs sauvegardées
            int[] colorsArray = savedInstanceState.getIntArray("colors");
            int[] colorsAchromateArray = savedInstanceState.getIntArray("colorsAchromate");
            boolean end = savedInstanceState.getBoolean(("endGame"));

            if (colorsArray != null) {
                colors.clear();
                for (int color : colorsArray) {
                    colors.add(color);
                }
            }

            if (colorsAchromateArray != null) {
                colorsAchromate.clear();
                for (int colorA : colorsAchromateArray) {
                    colorsAchromate.add(colorA);
                }
            }


            this.allTraces = (ArrayList<ArrayList<String>>) savedInstanceState.getSerializable("traces");
            if(end){
                this.gameFinished = true;
            }
        } else {
            createListColor(nbPaires);
        }


        for (int i = 0; i < size; i++) {
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1
            ));

            for (int j = 0; j < size; j++) {
                LinearLayout caseLinear = new LinearLayout(this);
                caseLinear.setOrientation(LinearLayout.VERTICAL);
                caseLinear.setLayoutParams(new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1
                ));
                caseLinear.setId(View.generateViewId());
                caseLinear.setTag("case_" + i + "_" + j);

                GradientDrawable border = new GradientDrawable();
                border.setColor(Color.TRANSPARENT);
                border.setStroke(1, Color.WHITE);
                border.setCornerRadius(15f);
                caseLinear.setBackground(border);

                linearLayout.addView(caseLinear);
                if(this.gameFinished){
                    caseLinear.setOnTouchListener(null);
                } else {
                    caseLinear.setOnTouchListener(new CaseTouchedListener(mainLinear, this));
                }

            }
            mainLinear.addView(linearLayout);
        }

        List<Paire> mesPaires = monPuzzle.getPaires();
        int count = 0;
        for (Paire p : mesPaires) {
            Point p1 = p.getP1();
            String idP1 = "case_" + p1.getLigne() + "_" + p1.getColonne();
            LinearLayout caseP1 = findViewById(R.id.gamePuzzle).findViewWithTag(idP1);

            int color = isChecked ? this.colorsAchromate.get(count) : this.colors.get(count);

            initialView circle1 = new initialView(this, color);
            caseP1.addView(circle1);

            Point p2 = p.getP2();
            String idP2 = "case_" + p2.getLigne() + "_" + p2.getColonne();
            LinearLayout caseP2 = findViewById(R.id.gamePuzzle).findViewWithTag(idP2);

            initialView circle2 = new initialView(this, color);
            caseP2.addView(circle2);

            count++;
        }

        this.caseTouchedListener = new CaseTouchedListener(mainLinear, this);
        restoreTracesState(savedInstanceState);

        mainLinear.invalidate();
    }

    public void showEndScreen() {
        if (!gameFinished) {
            gameFinished = true;
        }
        for (int i = 0; i < mainLinear.getChildCount(); i++) {
            LinearLayout row = (LinearLayout) mainLinear.getChildAt(i);
            for (int j = 0; j < row.getChildCount(); j++) {
                row.getChildAt(j).setOnTouchListener(null);
            }
        }

        LinearLayout endScreen = findViewById(R.id.end_screen);
        endScreen.setVisibility(View.VISIBLE);

        Button restartButton = findViewById(R.id.menu_button);
        restartButton.setOnClickListener(v -> GoToMenu());
    }

    private void GoToMenu() {
        LinearLayout endScreen = findViewById(R.id.end_screen);
        endScreen.setVisibility(View.GONE);

        gameFinished = false;

        Intent intent = new Intent(mainActivity, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void createListColor(int nb){
        Random rand = new Random();

        while (this.colors.size() < nb) {
            int color;
            do {
                int red = rand.nextInt(256);
                int green = rand.nextInt(256);
                int blue = rand.nextInt(256);

                color = Color.rgb(red, green, blue);
            } while (this.colors.contains(color));
            this.colors.add(color);
        }

        while (this.colorsAchromate.size() < nb) {
            int colorA;
            int value;
            do {
                value = rand.nextInt(256);

                colorA = Color.rgb(value, value, value);
            } while (this.colorsAchromate.contains(colorA) || value < 100);
            this.colorsAchromate.add(colorA);
        }
    }

    public void addTrace(ArrayList<String> maTrace){
        this.allTraces.add(new ArrayList<>(maTrace));
    }

    public void deleteTrace(ArrayList<String> maTrace) {
        Set<String> maTraceSet = new HashSet<>(maTrace);

        for (int i = 0; i < this.allTraces.size(); i++) {
            ArrayList<String> trace = this.allTraces.get(i);

            Set<String> traceSet = new HashSet<>(trace);

            if (traceSet.equals(maTraceSet)) {
                this.allTraces.remove(i);
                break;
            }
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Convertir les listes de couleurs en tableaux
        int[] colorsArray = new int[colors.size()];
        int[] colorsAchromateArray = new int[colorsAchromate.size()];

        for (int i = 0; i < colors.size(); i++) {
            colorsArray[i] = colors.get(i);
        }
        for (int i = 0; i < colorsAchromate.size(); i++) {
            colorsAchromateArray[i] = colorsAchromate.get(i);
        }

        // Sauvegarde des tableaux
        outState.putIntArray("colors", colorsArray);
        outState.putIntArray("colorsAchromate", colorsAchromateArray);



        outState.putSerializable("traces", new ArrayList<>(this.allTraces));

        outState.putBoolean("endGame", this.gameFinished);
    }

    private void restoreTracesState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (this.allTraces != null && caseTouchedListener != null) {
                for (ArrayList<String> trace : this.allTraces) {
                    dessinerTracer(trace);
                }
            }

            if(this.gameFinished){
                this.showEndScreen();
            }
        }
    }


    public void dessinerTracer(ArrayList<String> trace) {
        ArrayList<String> direction = new ArrayList<>();
        for (int i = 1; i < trace.size(); i++) {
            String tagAct = trace.get(i);
            String tagPrev = trace.get(i - 1);

            String[] casesActPos = tagAct.split("_");
            int ligneAct = Integer.parseInt(casesActPos[1]);
            int colonneAct = Integer.parseInt(casesActPos[2]);

            String[] casesPrevPos = tagPrev.split("_");
            int lignePrev = Integer.parseInt(casesPrevPos[1]);
            int colonnePrev = Integer.parseInt(casesPrevPos[2]);

            if (ligneAct > lignePrev) {
                direction.add("bas");
            }
            if (ligneAct < lignePrev) {
                direction.add("haut");
            }
            if (colonneAct > colonnePrev) {
                direction.add("droite");
            }
            if (colonneAct < colonnePrev) {
                direction.add("gauche");
            }
        }


        for (String maTrace : trace) {
            LinearLayout maCase = findViewWithTag(maTrace);
            for (int x = 0; x < maCase.getChildCount(); x++) {
                if (maCase.getChildAt(x) instanceof traceView) {
                    maCase.removeViewAt(x);
                }
            }
        }

        for (int j = 0; j < trace.size(); j++) {
            LinearLayout maCase = findViewWithTag(trace.get(j));
            if (maCase.getChildAt(0) instanceof initialView) {
                initialView v = (initialView) maCase.getChildAt(0);
                maCase.removeViewAt(0);
                initialTraceView t = new initialTraceView(this, v.getColor(), direction, j);
                maCase.addView(t);
                this.currentColor =  v.getColor();
            } else {
                traceView t = new traceView(this, this.currentColor, direction, j);
                maCase.addView(t);
            }
        }
    }

    private LinearLayout findViewWithTag(String tag) {
        for (int i = 0; i < mainLinear.getChildCount(); i++) {
            LinearLayout row = (LinearLayout) mainLinear.getChildAt(i);
            for (int j = 0; j < row.getChildCount(); j++) {
                LinearLayout cases = (LinearLayout) row.getChildAt(j);
                if (cases.getTag().equals(tag)) {
                    return cases;
                }
            }
        }
        return null;
    }




}
