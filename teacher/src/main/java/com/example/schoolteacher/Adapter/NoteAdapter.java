package com.example.schoolteacher.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.schoolteacher.Model.NoteClass;
import com.example.schoolteacher.NoteScreenActivity;
import com.example.schoolteacher.R;

import java.util.List;

public class NoteAdapter extends ArrayAdapter<NoteClass> {

    private Activity context;
    List<NoteClass> notes;

    public NoteAdapter(Activity context, List<NoteClass> notes) {
        super(context, R.layout.custom_view, notes);
        this.context = context;
        this.notes = notes;
    }

    @Override
    public int getCount() {
        notes.size();
        return super.getCount();
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        LayoutInflater layoutInflater = context.getLayoutInflater();
        final View customView = layoutInflater.inflate(R.layout.custom_view, null, true);

        TextView tvCustomNoteTitle = (TextView) customView.findViewById(R.id.tvCustomNoteTitle);
        TextView tvCustomNote = (TextView) customView.findViewById(R.id.tvCustomNote);
        TextView tvCustomCreatedDate = (TextView) customView.findViewById(R.id.tvCustomCreatedDate);
        final ImageView menuIcon = customView.findViewById(R.id.menuIcon);

        final NoteClass note = notes.get(position);
        tvCustomNoteTitle.setText(note.getNoteTitle());
        tvCustomNote.setText(note.getNote());
        tvCustomCreatedDate.setText(note.getCreatedDate());

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(context, menuIcon);
                popupMenu.inflate(R.menu.popup_menu);
                popupMenu.setGravity(Gravity.END);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.action_edit:
                                Toast.makeText(context, "Edit", Toast.LENGTH_SHORT).show();
                                Intent i=new Intent(context, NoteScreenActivity.class);
                                i.putExtra("id",note.noteId);
                                i.putExtra("noteTitle",note.noteTitle);
                                i.putExtra("note",note.note);
                                context.startActivity(i);
                                break;
                            default:
                                break;
                        }

                        return false;
                    }
                });
                popupMenu.show();
            }
        });







        return customView;
    }
}
