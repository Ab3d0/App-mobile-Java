package iut.tp.flowfree;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class choosePuzzleClicked implements View.OnClickListener {

    private Context c;

    public choosePuzzleClicked(Context c){
        this.c = c;
    }



    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this.c, choosePuzzleActivity.class);
        this.c.startActivity(intent);
    }
}
