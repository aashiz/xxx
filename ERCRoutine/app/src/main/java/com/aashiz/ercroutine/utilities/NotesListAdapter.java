package com.aashiz.ercroutine.utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.aashiz.ercroutine.R;

import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * Created by Gaming on 5/3/2017.
 */

public class NotesListAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {
    JSONObject data ;
    Context parent;

    public NotesListAdapter(Context context , JSONObject json){
        this.data = json ;
        this.parent = context ;
    }
    @Override
    public int getCount() {
        return  data.length() -1;

    }

    @Override
    public JSONObject getItem(int position) {
        return data.optJSONObject(String.valueOf(position));

    }

    private String getURL(int position) {
        try {
            JSONObject k = data.optJSONObject(String.valueOf(position));
            return k.optString("url");
        }catch(Exception ex){
            return null;
        }
    }

    private String getDescription(int position) {
        try {
            JSONObject k = data.optJSONObject(String.valueOf(position));
            return k.optString("description");
        }catch(Exception ex){
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater f = LayoutInflater.from(this.parent);
        convertView  = f.inflate(R.layout.notes_list_item,null);


        TextView v = (TextView)convertView.findViewById(R.id.note_list_item);
        String text =getTitle(position);
        if(text !=null){
            v.setText(text);
        }
        return convertView;
    }


    @Override
    public void onItemClick(AdapterView<?> paren, View view, int position, long id) {
        final String url = getURL(position);
        final String description = getDescription(position);
        final CharSequence title = getTitle(position);
        Toast.makeText(parent,url,Toast.LENGTH_SHORT).show();
        AlertDialog dialog = new AlertDialog.Builder(parent)
                .setTitle(title)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        parent.startActivity(i);
                    }
                })
                .setMessage(description + "\nDo you want to view this material?").create();
        dialog.show();

    }

    private String getTitle(int position) {
        JSONObject obj = getItem(position);
        String text = obj.optString("title");
        return text;
    }
}
