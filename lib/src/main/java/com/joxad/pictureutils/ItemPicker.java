package com.joxad.pictureutils;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Josh on 19/10/2016.
 */

public class ItemPicker extends LinearLayout {

    TextView tvLabel;
    ImageView ivIcon;

    public ItemPicker(Context context) {
        this(context, null, 0);
    }

    public ItemPicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(HORIZONTAL);
        View view = inflate(context, R.layout.item_picker, this);

        tvLabel = (TextView) view.findViewById(R.id.tv_label);
        ivIcon = (ImageView) view.findViewById(R.id.iv_picto);

        if (isInEditMode()) return;
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ItemPicker,
                0, 0);

        String title = "";
        int image;
        try {
            title = a.getString(R.styleable.ItemPicker_ip_label);
            image = a.getResourceId(R.styleable.ItemPicker_ip_icon, 0);
        } finally {
            a.recycle();
        }

        tvLabel.setText(title);
        ivIcon.setImageResource(image);
    }


}
