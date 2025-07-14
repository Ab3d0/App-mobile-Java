package iut.tp.flowfree;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class adapterCustom extends ArrayAdapter<String> {
    private Context context;
    private List<String> items;

    public adapterCustom(Context context, List<String> items) {
        super(context, android.R.layout.simple_spinner_item, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);
        TextView textView = (TextView) view;

        if (items.get(position).startsWith("disable")) {
            // Griser l'élément "Vert"
            textView.setTextColor(0xFFAAAAAA); // Gris
        } else {
            textView.setTextColor(0xFFFFFFFF); // Noir normal
        }
        return view;
    }
}
