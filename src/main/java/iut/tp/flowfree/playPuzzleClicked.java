package iut.tp.flowfree;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import java.util.HashMap;

public class playPuzzleClicked implements View.OnClickListener {

    private Context c;
    private String v;
    private HashMap<String, String> listePuzzle;

    public playPuzzleClicked(Context c, String value){
        this.c = c;
        this.v = value;
    }



    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this.c, playActivity.class);

        intent.putExtra("puzzle", this.v);
        this.c.startActivity(intent);



    }
}
