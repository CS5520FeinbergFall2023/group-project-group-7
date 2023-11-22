package com.NEU23FallGroup7.jobtrackpro.util;

import static android.app.ProgressDialog.show;

import android.content.Context;
import android.content.DialogInterface;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class Dialog{

    public interface DialogClickListener {
        void onPositiveClick();
    }
    public static void showDialog(Context context, String positive, String negative, String title, String text, final DialogClickListener dialogClickListener)
    {
        new MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(text)
            .setNegativeButton(negative, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            })
            .setPositiveButton(positive, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (dialogClickListener != null) {
                        dialogClickListener.onPositiveClick();
                    }
                }
            })
            .show();
    }
}
