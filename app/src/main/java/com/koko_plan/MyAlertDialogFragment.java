package com.koko_plan;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MyAlertDialogFragment extends DialogFragment {

    private EditText mName;
    private TitleInputListener listener;

    public static MyAlertDialogFragment newInstance(TitleInputListener listener){
        MyAlertDialogFragment frag = new MyAlertDialogFragment();
        frag.listener = listener;
        return frag;
    }

    public interface TitleInputListener {
        void onTitleInputComplete(String title);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit_title, null);
        mName = (EditText)view.findViewById(R.id.id_txt_input);
        builder.setView(view) .setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                listener.onTitleInputComplete(mName.getText().toString());

            }
        }).setNegativeButton("취소", null); return builder.create();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("title", mName.getText().toString());
    }
}