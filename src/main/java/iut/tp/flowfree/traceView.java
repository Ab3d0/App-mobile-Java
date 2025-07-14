package iut.tp.flowfree;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;
import java.util.ArrayList;

public class traceView extends View {
    private int colorTrace;
    private Paint paint;
    private Path path;
    private ArrayList<String> direction;
    private int numero;

    public traceView(Context context, int color, ArrayList<String> direction, int numero) {
        super(context);
        this.colorTrace = color;
        this.direction = direction;
        this.numero = numero;

        this.paint = new Paint();
        this.paint.setColor(this.colorTrace);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeWidth(50);
        this.paint.setAntiAlias(true);
        this.paint.setStrokeCap(Paint.Cap.ROUND);
        this.paint.setStrokeJoin(Paint.Join.ROUND);

        this.path = new Path();
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        path.reset();

        if (numero == 0) {
            path.moveTo(getWidth() / 2f, getHeight() / 2f);
            if (!direction.isEmpty() && "bas".equals(direction.get(0))) {
                path.lineTo(getWidth() / 2f, getHeight());
            } else if (!direction.isEmpty() && "gauche".equals(direction.get(0))) {
                path.lineTo(0, getHeight() / 2f);
            } else if (!direction.isEmpty() && "haut".equals(direction.get(0))) {
                path.lineTo(getWidth() / 2f, 0);
            } else if (!direction.isEmpty() && "droite".equals(direction.get(0))) {
                path.lineTo(getWidth(), getHeight() / 2f);
            }
        } else if (numero - 1 < direction.size()) {
            String prevDir = direction.get(numero - 1);
            String currentDir = (numero < direction.size()) ? direction.get(numero) : "";

            switch (prevDir) {
                case "bas":
                    path.moveTo(getWidth() / 2f, 0);
                    break;
                case "gauche":
                    path.moveTo(getWidth(), getHeight() / 2f);
                    break;
                case "haut":
                    path.moveTo(getWidth() / 2f, getHeight());
                    break;
                case "droite":
                    path.moveTo(0, getHeight() / 2f);
                    break;
            }
            path.lineTo(getWidth() / 2f, getHeight() / 2f);

            switch (currentDir) {
                case "bas":
                    path.lineTo(getWidth() / 2f, getHeight());
                    break;
                case "gauche":
                    path.lineTo(0, getHeight() / 2f);
                    break;
                case "haut":
                    path.lineTo(getWidth() / 2f, 0);
                    break;
                case "droite":
                    path.lineTo(getWidth(), getHeight() / 2f);
                    break;
            }
        }

        canvas.drawPath(path, paint);
    }

    public int getColor() {
        return this.colorTrace;
    }

    public int getNumero() {
        return this.numero;
    }
}
