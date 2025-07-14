package iut.tp.flowfree;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import java.util.ArrayList;

public class puzzleSelectionListener implements AdapterView.OnItemSelectedListener {


    private ArrayList<String> items;
    private Spinner sp;
    private int defaultPos;


    public puzzleSelectionListener(ArrayList<String> items, Spinner sp, int defaultPos){
        this.items = items;
        this.sp = sp;
        this.defaultPos = defaultPos;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (this.items.get(i).toLowerCase().startsWith("disable")) {
            this.sp.setSelection(defaultPos); // Revenir à un autre choix valide
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //
    }
}
