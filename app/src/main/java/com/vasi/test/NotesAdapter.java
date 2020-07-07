package com.vasi.test;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.vasi.test.Data.UserNotes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesHolder> {

    private Context context;
    private ArrayList<UserNotes> dataList = new ArrayList<>();

    private String noteId="", noteDate="";

    private UpdateInterface updateInterface;

    NotesAdapter(Context con, ArrayList<UserNotes> list) {
        context = con;
        dataList = list;
        updateInterface=(UpdateInterface)context;
    }

    @Override
    public NotesHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_recycler_all_notes, parent, false);
        NotesHolder notesHolder = new NotesHolder(view);
        return notesHolder;
    }

    @Override
    public void onBindViewHolder(NotesHolder holder, final int position) {
        UserNotes userNotes = dataList.get(position);
        String title = userNotes.getNoteTitle();
        String desc = userNotes.getNoteDesc();
        String date = userNotes.getNoteDate();

        holder.textRowTitle.setText(title);
        holder.textRowDesc.setText(desc);
        holder.textRowDate.setText(date);

        holder.imageRowEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserNotes userNotes1 = dataList.get(position);
                showDialog(userNotes1);
            }
        });

        holder.imageRowDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserNotes userNotes1 = dataList.get(position);
                confirmDeleteDialog(userNotes1);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class NotesHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_row_title)
        TextView textRowTitle;
        @BindView(R.id.text_row_desc)
        TextView textRowDesc;
        @BindView(R.id.text_row_date)
        TextView textRowDate;
        @BindView(R.id.image_row_edit)
        ImageView imageRowEdit;
        @BindView(R.id.image_row_delete)
        ImageView imageRowDelete;

        NotesHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    private void showDialog(UserNotes noteObj){

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_update_note);
        dialog.show();

        final EditText editTitle = (EditText)dialog.findViewById(R.id.editText_update_title);
        EditText editDesc = (EditText)dialog.findViewById(R.id.editText_update_desc);

        editTitle.setText(noteObj.getNoteTitle());
        editDesc.setText(noteObj.getNoteDesc());

        noteId = noteObj.getNoteId();

        Button buttonUpdate = (Button)dialog.findViewById(R.id.button_update_note);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy");
                Calendar calendar = Calendar.getInstance();
                noteDate = simpleDateFormat.format(calendar.getTime());

                String title = editTitle.getText().toString();
                String desc = editTitle.getText().toString();

                UserNotes userNotes = new UserNotes(title,desc,noteDate,noteId);
                updateInterface.updateUsernote(userNotes);
            }
        });
    }

    private void confirmDeleteDialog(final UserNotes notesObj){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_delete);
        dialog.show();

        TextView tvDelete = (TextView)dialog.findViewById(R.id.text_delete);
        TextView tvCancel = (TextView)dialog.findViewById(R.id.text_cancel);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateInterface.deleteNote(notesObj);
                dialog.dismiss();

            }
        });

    }

}
