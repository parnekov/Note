package com.parnekov.sasha.note.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.parnekov.sasha.note.R;

public final class IntentUtils {
    public static boolean sIsInfoDialogShown;

    public static void shareIntent(Activity activity) {
        ShareCompat.IntentBuilder
                .from(activity)
                .setType("text/plain")
                .setChooserTitle(activity.getString(R.string.app_name))
                .setText(activity.getString(R.string.dialog_app_url))
                .startChooser();
    }

    public static void emailIntent(Context context) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);

        emailIntent.setData(Uri.parse("mailto:" + context.getString(R.string.email_address)));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.email_subject));
        emailIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.email_text_hello));

        checkIntentBeforeLaunching(context, emailIntent);
    }

    public static void feedbackIntent(Context context) {
        String url = context.getString(R.string.dialog_app_url);
        Intent feedbackIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        checkIntentBeforeLaunching(context, feedbackIntent);
    }

    public static void infoIntent(final Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setPositiveButton(R.string.dialog_info_close, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });


        final View view = View.inflate(context, R.layout.dialog_info, null);
        final TextView developerUrl = (TextView) view.findViewById(R.id.developerUrl);

        String text = context.getString(R.string.dialog_info_developer);
        developerUrl.setText(text);

        developerUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String developerUrl = context.getString(R.string.dialog_fb_url);
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(developerUrl));
                if (webIntent.resolveActivity(context.getPackageManager()) != null)
                    context.startActivity(webIntent);
            }

        });

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                sIsInfoDialogShown = false;
            }
        });

        sIsInfoDialogShown = true;
        AlertDialog dialog = builder.create();
        dialog.setView(view);
        dialog.show();
    }

    public static void infoIntentSDK16(final Context context) {
        Toast.makeText(context,
                context.getString(R.string.dialog_info_message)
                        + "  "
                        + context.getString(R.string.dialog_info_developer)
                , Toast.LENGTH_LONG).show();
    }


    private static void checkIntentBeforeLaunching(Context context, Intent intent) {
        if (intent.resolveActivity(context.getPackageManager()) != null)
            context.startActivity(intent);
    }
}