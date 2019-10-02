package com.example.translator.Controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.translator.Model.Word;
import com.example.translator.R;

public class detailDialogFragment extends DialogFragment {

    public static final String EXTRA_TITLE = "Title";
    public static final String EXTRA_TRANSLATION = "Translation";
    private EditText dTitle;
    private EditText dTranslation;
    private boolean hasChanges;
    private Word dWord;
    private View dView;

    private detailDialogFragment(){}

    public static detailDialogFragment newInstance(boolean hasChanges, Word word) {
        detailDialogFragment dialog = new detailDialogFragment();
        dialog.dWord = word;
        dialog.hasChanges = hasChanges;
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        dView = inflater.inflate(R.layout.detail_dialog, null,false);
        dTitle = dView.findViewById(R.id.edit_title);
        dTranslation = dView.findViewById(R.id.edit_translation);

        dTitle.setText(dWord.getTitle());
        dTranslation.setText(dWord.getTranslation());

        if(hasChanges)
            return editDialog();

        return detailDialog();

    }

    private Dialog.OnClickListener Listener(final boolean isPositive) {
        return new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (isPositive) {
                    Intent intent = new Intent();
                    intent.putExtra(EXTRA_TITLE,dTitle.getText().toString());
                    intent.putExtra(EXTRA_TRANSLATION,dTranslation.getText().toString());
                    getTargetFragment()
                            .onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                }else {
                    dismiss();
                }
            }
        };
    }

    private AlertDialog editDialog() {

        return  new AlertDialog.Builder(getActivity())
                .setView(dView)
                .setPositiveButton(android.R.string.ok, Listener(true))
                .setNegativeButton(android.R.string.cancel,Listener(false))
                .create();
    }

    private AlertDialog detailDialog(){
        return  new AlertDialog.Builder(getContext())
                .setView(dView)
                .create();
    }
}
