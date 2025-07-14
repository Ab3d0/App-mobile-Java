package iut.tp.flowfree;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CaseTouchedListener implements View.OnTouchListener {

    private Context c;
    private LinearLayout parent;
    private int currentColor = Color.TRANSPARENT;
    private boolean isDrawing = false;
    private String idStart;
    private String lastView;
    private ArrayList<String> trace = new ArrayList<>();

    private playActivity activity;

    public CaseTouchedListener(LinearLayout parent, Context c) {
        this.parent = parent;
        this.c = c;
        this.activity = (playActivity) c;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        LinearLayout caseLinearLayout = (LinearLayout) view;
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (caseLinearLayout.getChildCount() > 0 && caseLinearLayout.getChildAt(0) instanceof initialView) {
                    isDrawing = true;
                    initialView circle = (initialView) caseLinearLayout.getChildAt(0);
                    this.currentColor = circle.getColor();
                    this.idStart = (String) caseLinearLayout.getTag();
                    this.lastView = this.idStart;
                    this.trace.add(this.idStart);
                }

                if (caseLinearLayout.getChildCount() > 0 && caseLinearLayout.getChildAt(0) instanceof initialTraceView) {

                    initialTraceView caseReset = (initialTraceView) caseLinearLayout.getChildAt(0);
                    int color = caseReset.getColor();

                    deleteTracer(color);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                float x = motionEvent.getRawX();
                float y = motionEvent.getRawY();

                LinearLayout newView = (LinearLayout) findViewAtPosition(this.parent, x, y);
                if (newView == null) {
                    this.isDrawing = false;
                    clearTrace();
                } else if (newView != null && this.isDrawing) {
                    if(isInDiagonale((String) newView.getTag())){
                        this.isDrawing = false;
                        clearTrace();
                    } else {
                        if (newView.getChildCount() > 0) {
                            if (newView.getChildAt(0) instanceof initialView) {
                                if (!newView.getTag().equals(this.idStart)) {
                                    initialView circle = (initialView) newView.getChildAt(0);
                                    if (this.currentColor == circle.getColor()) {
                                        this.trace.add((String) newView.getTag());
                                        this.lastView = (String) newView.getTag();
                                        dessinerTracer();
                                        this.activity.addTrace(this.trace);
                                        this.isDrawing = false;


                                        // Vérification de la fin du jeu
                                        if (checkGameEnd()) {
                                            // Afficher le message de fin de jeu
                                            activity.showEndScreen();
                                        }
                                    } else {
                                        this.isDrawing = false;
                                        clearTrace();
                                    }
                                }
                            }
                            if (newView.getChildAt(0) instanceof traceView) {
                                String lastTrace = this.trace.get(this.trace.size() - 1);
                                if (!lastTrace.equals(newView.getTag())) {
                                    this.isDrawing = false;
                                    clearTrace();
                                }
                            }
                            if (newView.getChildAt(0) instanceof initialTraceView) {
                                if (!newView.getTag().equals(this.trace.get(this.trace.size() - 1))) {
                                    this.isDrawing = false;
                                    clearTrace();
                                }
                            }
                        } else {
                            String idNewView = (String) newView.getTag();
                            if (this.trace.size() <= 1) {
                                this.trace.add(idNewView);
                                this.lastView = (String) newView.getTag();
                                dessinerTracer();
                            } else {
                                boolean isOnTrace = false;
                                for (String point : this.trace) {
                                    if (idNewView.equals(point)) {
                                        isOnTrace = true;
                                        break;
                                    }
                                }
                                if (isOnTrace && this.trace.get(trace.size() - 2).equals(idNewView)) {
                                    this.isDrawing = false;
                                    clearTrace();
                                }
                                if (!isOnTrace) {
                                    this.trace.add(idNewView);
                                    this.lastView = (String) newView.getTag();
                                    dessinerTracer();
                                }
                            }
                        }
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                this.currentColor = Color.TRANSPARENT;
                if (!isDrawing) {
                    this.trace.clear();
                } else {
                    clearTrace();
                }
                break;
        }
        return true;
    }

    private View findViewAtPosition(LinearLayout root, float x, float y) {
        for (int i = 0; i < root.getChildCount(); i++) {
            LinearLayout child = (LinearLayout) root.getChildAt(i);
            for (int row = 0; row < child.getChildCount(); row++) {
                LinearLayout rowChild = (LinearLayout) child.getChildAt(row);
                int[] location = new int[2];
                rowChild.getLocationOnScreen(location);
                int left = location[0];
                int top = location[1];
                int right = left + rowChild.getWidth();
                int bottom = top + rowChild.getHeight();

                if (x >= left && x <= right && y >= top && y <= bottom) {
                    return rowChild;
                }
            }
        }
        return null;
    }

    private void clearTrace() {
        for (String tagCase : this.trace) {
            LinearLayout maCase = findViewWithTag(tagCase);
            if (maCase != null) {
                for (int i = 0; i < maCase.getChildCount(); i++) {
                    if (maCase.getChildAt(i) instanceof traceView) {
                        maCase.removeViewAt(i);
                    } else if (maCase.getChildAt(i) instanceof initialTraceView) {
                        initialTraceView resetTrace = (initialTraceView) maCase.getChildAt(i);
                        int co = resetTrace.getColor();
                        maCase.removeAllViews();
                        initialView newInitialView = new initialView(this.c, co);
                        maCase.addView(newInitialView);
                    }
                }
            }
        }
        this.trace.clear();
    }

    private LinearLayout findViewWithTag(String tag) {
        for (int i = 0; i < this.parent.getChildCount(); i++) {
            LinearLayout row = (LinearLayout) this.parent.getChildAt(i);
            for (int j = 0; j < row.getChildCount(); j++) {
                LinearLayout cases = (LinearLayout) row.getChildAt(j);
                if (cases.getTag() == tag) {
                    return cases;
                }
            }
        }
        return null;
    }

    public void dessinerTracer() {
        ArrayList<String> direction = new ArrayList<>();
        for (int i = 1; i < this.trace.size(); i++) {
            String tagAct = this.trace.get(i);
            String tagPrev = this.trace.get(i - 1);

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


        for (String maTrace : this.trace) {
            LinearLayout maCase = findViewWithTag(maTrace);
            System.out.println(maCase);
            for (int x = 0; x < maCase.getChildCount(); x++) {
                if (maCase.getChildAt(x) instanceof traceView) {
                    maCase.removeViewAt(x);
                }
            }
        }

        for (int j = 0; j < this.trace.size(); j++) {
            LinearLayout maCase = findViewWithTag(this.trace.get(j));
            if (maCase.getChildAt(0) instanceof initialView) {
                maCase.removeViewAt(0);
                initialTraceView trace = new initialTraceView(this.c, this.currentColor, direction, j);
                maCase.addView(trace);
            } else {
                traceView trace = new traceView(this.c, this.currentColor, direction, j);
                maCase.addView(trace);
            }
        }
    }

    private void deleteTracer(int color) {
        for (int i = 0; i < this.parent.getChildCount(); i++) {
            LinearLayout row = (LinearLayout) this.parent.getChildAt(i);
            for (int j = 0; j < row.getChildCount(); j++) {
                LinearLayout cases = (LinearLayout) row.getChildAt(j);
                if(cases.getChildCount() == 2){
                    if (cases.getChildAt(0) instanceof initialTraceView) {
                        initialTraceView traceCase = (initialTraceView) cases.getChildAt(0);
                        int colorTrace = traceCase.getColor();
                        if (colorTrace == color) {
                            this.trace.add((String) cases.getTag());
                        }
                    }
                } else {
                    for (int x = 0; x < cases.getChildCount(); x++) {
                        if (cases.getChildAt(x) instanceof traceView) {
                            traceView traceCase = (traceView) cases.getChildAt(x);
                            int colorTrace = traceCase.getColor();
                            if (colorTrace == color) {
                                this.trace.add((String) cases.getTag());
                            }
                        }
                        if (cases.getChildAt(x) instanceof initialTraceView) {
                            initialTraceView traceCase = (initialTraceView) cases.getChildAt(x);
                            int colorTrace = traceCase.getColor();
                            if (colorTrace == color) {
                                this.trace.add((String) cases.getTag());
                            }
                        }
                    }
                }

            }
        }
        this.activity.deleteTrace(this.trace);
        clearTrace();
    }

    private boolean checkGameEnd() {
        for (int i = 0; i < this.parent.getChildCount(); i++) {
            LinearLayout row = (LinearLayout) this.parent.getChildAt(i);
            for (int j = 0; j < row.getChildCount(); j++) {
                LinearLayout caseLayout = (LinearLayout) row.getChildAt(j);
                if (caseLayout.getChildCount() == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isInDiagonale(String tag){
        String[] newTag = tag.split("_");
        String[] lastTag = this.lastView.split("_");

        return !newTag[1].equals(lastTag[1]) && !newTag[2].equals(lastTag[2]);
    }
}