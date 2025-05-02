package com.gmwapp.slv_qr_card.helper;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import com.gmwapp.slv_qr_card.R;

public class TitleAndContentText extends LinearLayout {
    private TextView tvTitle, tvDescription;

    public TitleAndContentText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setOrientation(VERTICAL);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TitleAndContentText, 0, 0);

        String title = "";
        String description = "";

        try {
            title = a.getString(R.styleable.TitleAndContentText_title);
            description = a.getString(R.styleable.TitleAndContentText_description);
        } finally {
            a.recycle();
        }

        // Title TextView
        tvTitle = new TextView(context);
        tvTitle.setTextSize(14);
        tvTitle.setTypeface(getResources().getFont(R.font.poppins_bold));
        tvTitle.setTextColor(getResources().getColor(R.color.black, null)); // Fix color issue
        tvTitle.setText(title);

        if (!title.isEmpty()) {
            tvTitle.setPadding(0, 30, 0, 10);
            addView(tvTitle);
        }

        // Description TextView
        tvDescription = new TextView(context);
        tvDescription.setTextSize(12);
        tvDescription.setTypeface(getResources().getFont(R.font.poppins_medium));
        tvDescription.setTextColor(getResources().getColor(R.color.black, null));
        tvDescription.setText("          " + description);

        addView(tvDescription);
    }
}
