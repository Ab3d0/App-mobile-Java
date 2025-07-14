package iut.tp.flowfree;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class initialView extends View {

    private int circleColor; // Couleur du cercle

    // Constructeur pour définir la couleur du cercle
    public initialView(Context context, int color) {
        super(context);
        this.circleColor = color;
    }

    // Redéfinir la méthode onDraw pour dessiner un cercle
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Créer un objet Paint pour dessiner
        Paint paint = new Paint();
        paint.setColor(circleColor);  // Définir la couleur du cercle
        paint.setStyle(Paint.Style.FILL); // Définir le style pour un cercle plein

        // Calculer la position du cercle au centre de la vue
        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;

        // Calculer le rayon en fonction de la taille de la vue
        float radius = Math.min(getWidth(), getHeight()) / 4f;

        // Dessiner un cercle
        canvas.drawCircle(centerX, centerY, radius, paint);
    }

    public int getColor(){
        return this.circleColor;
    }
}
