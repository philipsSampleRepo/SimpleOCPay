package com.ocbc.assignment.utils;

import android.content.Context;
import android.content.Intent;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.ocbc.assignment.R;
import com.ocbc.assignment.ui.BaseActivity;
import com.ocbc.assignment.ui.RegisterActivity;


public class UiUtils {

    private static UiUtils uiUtils = null;

    private UiUtils() {
    }

    public static UiUtils getInstance() {
        if (uiUtils == null) {
            synchronized (UiUtils.class) {
                if (uiUtils == null) {
                    uiUtils = new UiUtils();
                }
            }
        }
        return uiUtils;
    }

    public void showMaterialDialog(BaseActivity context, CustomDialogInterface customDialogInterface,
                                   String title, String message, boolean isMultiChoice) {
        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(context);
        alertDialogBuilder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(context.getString(R.string.ok), (dialogInterface, i)
                        -> {
                    customDialogInterface.ok(dialogInterface);
                });

        if (isMultiChoice) {
            alertDialogBuilder.setNegativeButton(context.getString(R.string.cancel),
                    (dialogInterface, i)
                            -> customDialogInterface.cancel(dialogInterface));
        }
        alertDialogBuilder.show();
    }
}
