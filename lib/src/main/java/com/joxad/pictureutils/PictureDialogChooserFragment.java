package com.joxad.pictureutils;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Ephemera on 19/10/2016.
 */

public class PictureDialogChooserFragment extends BottomSheetDialogFragment {

    private static final String TITLE = "title";
    ItemPicker itemPickerTakePhoto;
    ItemPicker itemPickerPick;
    TextView tvTitle;
    private String title="";

    public static PictureDialogChooserFragment newInstance(final String title) {
        
        Bundle args = new Bundle();
        
        PictureDialogChooserFragment fragment = new PictureDialogChooserFragment();
        args.putString(TITLE,title);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getArguments().getString(TITLE);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_dialog_chooser,container,false);
        itemPickerPick = (ItemPicker) contentView.findViewById(R.id.item_pick);
        itemPickerTakePhoto = (ItemPicker) contentView.findViewById(R.id.item_take);
        itemPickerPick.setOnClickListener(v -> PictureUtils.pickPhoto());
        itemPickerTakePhoto.setOnClickListener(v -> PictureUtils.takePhoto());

        tvTitle = (TextView) contentView.findViewById(R.id.tv_title);
        tvTitle.setText(title);
        return contentView;
    }

}
