package com.vdocipherdemo.vdocipher_offline;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import com.vdocipher.aegis.offline.DownloadOptions;
import com.vdocipherdemo.R;
import java.util.List;

public class OptionSelector implements AlertDialog.OnClickListener, View.OnClickListener {

    private DownloadOptions downloadOptions;
    private OnOptionSelectedListener optionSelectedListener;
    private List<DownloadTrack> downloadTracks;
    private DownloadTrack selectedTrack;
    private AlertDialog dialog = null;
    private ViewGroup rootView = null;

    public OptionSelector(DownloadOptions downloadOptions, List<DownloadTrack> downloadTracks, OnOptionSelectedListener optionSelectedListener) {
        this.downloadOptions = downloadOptions;
        this.optionSelectedListener = optionSelectedListener;
        this.downloadTracks = downloadTracks;
    }

    public void showSelectionDialog(Context context, String dialogTitle) {
        selectedTrack = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(dialogTitle);
        builder.setView(buildView(context, downloadTracks));
        builder.setPositiveButton("Download", this);
        builder.setNegativeButton(android.R.string.cancel, this);
        dialog = builder.create();
        dialog.show();
        dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);

    }

    private View buildView(Context context, List<DownloadTrack> downloadTracks) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.track_selection_dialog, null);
        this.rootView = (ViewGroup) view.findViewById(R.id.root);

        TypedArray attributeArray = context.getTheme().obtainStyledAttributes(
                new int[] {android.R.attr.selectableItemBackground});
        int selectableItemBackgroundResourceId = attributeArray.getResourceId(0, 0);
        attributeArray.recycle();

        int size = downloadTracks.size();
        for (int i=0; i<size; i++) {
            DownloadTrack downloadTrack = downloadTracks.get(i);
            addOptionToView(downloadTrack, inflater, this.rootView, selectableItemBackgroundResourceId);
        }
        return view;

    }

    private void addOptionToView(DownloadTrack downloadTrack, LayoutInflater inflater, ViewGroup root, int selectableItemBackgroundResourceId) {
        String optionText = downloadTrack.getTrackName(this.downloadOptions);
        int trackViewLayoutId = android.R.layout.simple_list_item_single_choice;
        CheckedTextView trackView = (CheckedTextView) inflater.inflate(trackViewLayoutId, root, false);
        trackView.setBackgroundResource(selectableItemBackgroundResourceId);
        trackView.setText(optionText);
        trackView.setFocusable(true);
        trackView.setChecked(false);
        trackView.setTag(downloadTrack);
        trackView.setOnClickListener(this);
        root.addView(trackView);
    }


    @Override
    public void onClick(View view) {
        if (view instanceof CheckedTextView) {
            CheckedTextView optionView = (CheckedTextView) view;
            DownloadTrack downloadTrack = (DownloadTrack) optionView.getTag();

            if (optionView.isChecked()) {
                selectedTrack = null;
                optionView.setChecked(false);
                dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(false);
            } else {

                int numChildren = this.rootView.getChildCount();
                for (int i=0; i<numChildren; i++) {
                    CheckedTextView caseOption = (CheckedTextView) this.rootView.getChildAt(i);
                    caseOption.setChecked(false);
                }

                selectedTrack = downloadTrack;
                optionView.setChecked(true);
                dialog.getButton(Dialog.BUTTON_POSITIVE).setEnabled(true);
            }
        }
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int which) {
        if (which == DialogInterface.BUTTON_NEGATIVE) {
            selectedTrack = null;
        }
        else if (which == DialogInterface.BUTTON_POSITIVE) {
            optionSelectedListener.onOptionSelected(downloadOptions, selectedTrack);
        }
    }
}

interface OnOptionSelectedListener{
    void onOptionSelected(DownloadOptions downloadOptions, DownloadTrack selectedTrack);
}


