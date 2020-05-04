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

import androidx.annotation.NonNull;

import com.example.schoolteacher.Model.ClassModel;
import com.example.schoolteacher.NoteScreenActivity;
import com.example.schoolteacher.R;

import java.util.List;

public class NoteAdapter extends ArrayAdapter<ClassModel> {

    private Activity context;
    private List<ClassModel> classList;

    public NoteAdapter(Activity context, List<ClassModel> classList) {

        super(context, R.layout.custom_view, classList);

        this.context = context;
        this.classList = classList;
    }

    @Override
    public int getCount() {

        classList.size();
        return super.getCount();
    }

    @Override
    @NonNull
    public View getView(final int position, final View convertView, @NonNull final ViewGroup parent) {

        final View customView = LayoutInflater.from(context).inflate(R.layout.custom_view, parent, false);

        TextView tvCustomNoteTitle = customView.findViewById(R.id.tvCustomNoteTitle);
        TextView tvCustomNote = customView.findViewById(R.id.tvCustomNote);
        TextView tvCustomCreatedDate = customView.findViewById(R.id.tvCustomCreatedDate);
        final ImageView menuIcon = customView.findViewById(R.id.menuIcon);

        tvCustomNoteTitle.setText(classList.get(position).getClassName());
        tvCustomNote.setText(classList.get(position).getDescription());
        tvCustomCreatedDate.setText(classList.get(position).getCreatedAt());

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(context, menuIcon);
                popupMenu.inflate(R.menu.popup_menu);
                popupMenu.setGravity(Gravity.END);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if (item.getItemId() == R.id.action_edit) {

                            Toast.makeText(context, "Edit", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(context, NoteScreenActivity.class);

                            i.putExtra("id", classList.get(position).getClassId());
                            i.putExtra("noteTitle", classList.get(position).getClassName());
                            i.putExtra("note", classList.get(position).getDescription());

                            context.startActivity(i);
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
