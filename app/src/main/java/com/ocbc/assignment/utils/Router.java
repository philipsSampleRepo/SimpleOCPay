package com.ocbc.assignment.utils;

import android.content.Context;
import android.content.Intent;

import com.ocbc.assignment.ui.RegisterActivity;
import com.ocbc.assignment.ui.TopupActivity;

public final class Router {

    private static Router router = null;

    private Router() {

    }

    public static Router getInstance() {
        if (router == null) {
            synchronized (Router.class) {
                if (router == null) {
                    router = new Router();
                }
            }
        }
        return router;
    }

    public void startRegisterActivity(Context context) {
        Intent register = new Intent(context, RegisterActivity.class);
        context.startActivity(register);
    }

    public void startTopupActivity(Context context) {
        Intent topup = new Intent(context, TopupActivity.class);
        topup.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(topup);
    }
}
