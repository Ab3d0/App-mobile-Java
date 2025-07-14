package iut.tp.flowfree;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class choosePuzzleActivity extends Activity {

    private HashMap<String, String> mesPuzzles = new HashMap<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_puzzle);

        AssetManager assetManager = this.getAssets();

        LinearLayout scroll = findViewById(R.id.scrollPuzzle);

        try {
            String[] puzzleFiles = assetManager.list("puzzles");
            if (puzzleFiles != null) {
                for (String fileName : puzzleFiles) {
                    /* vérifier syntaxe du fichier via d'autres classes */
                    syntaxVerificator syntaxVerif = new syntaxVerificator(fileName, this);
                    if(syntaxVerif.getHasGoodSyntaxe()){
                        String s = syntaxVerif.getNamePuzzle() + " : " + syntaxVerif.getSize() + "x" + syntaxVerif.getSize();
                        this.mesPuzzles.put(s, fileName);
                    } else {
                        String s = "disable "+fileName;
                        this.mesPuzzles.put(s, fileName);
                    }


                    System.out.println("La syntaxe EST : " + syntaxVerif.getHasGoodSyntaxe());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (Map.Entry<String, String> entry : this.mesPuzzles.entrySet()) {
            System.out.println("Clé : " + entry.getKey() + ", Valeur : " + entry.getValue());

            if(entry.getKey().toLowerCase().startsWith("disable")){
                Button btn = new Button(this);
                btn.setText(entry.getKey());
                btn.setEnabled(false);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        200
                );
                params.setMargins(20, 0, 20, 0);
                btn.setLayoutParams(params);
                scroll.addView(btn);
            } else {
                Button btn = new Button(this);
                btn.setText(entry.getKey());
                btn.setEnabled(true);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        200
                );
                params.setMargins(20, 20, 20, 20);
                btn.setLayoutParams(params);
                btn.setBackgroundColor(Color.parseColor("#FF6200EE"));
                btn.setOnClickListener(new playPuzzleClicked(this, entry.getValue()));
                scroll.addView(btn);
            }



        }

    }
}