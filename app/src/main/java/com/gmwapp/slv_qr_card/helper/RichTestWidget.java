package com.gmwapp.slv_qr_card.helper;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gmwapp.slv_qr_card.R;
import com.google.firebase.database.annotations.Nullable;

public class RichTestWidget extends LinearLayout {
    private TextView tvTitle, tvOnNumber, tvRichText;

    public RichTestWidget(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setOrientation(VERTICAL);

        // Read XML attributes
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RichTestWidget);
        String title = a.getString(R.styleable.RichTestWidget_rtw_title);
        String onNumber = a.getString(R.styleable.RichTestWidget_rtw_onNumber);
        String subTitle = a.getString(R.styleable.RichTestWidget_rtw_subTitle);
        String description = a.getString(R.styleable.RichTestWidget_rtw_description);
        a.recycle();

        // Title TextView
        if (title != null && !title.isEmpty()) {
            tvTitle = new TextView(context);
            tvTitle.setTextSize(14);
            tvTitle.setTypeface(getResources().getFont(R.font.poppins_bold));
            tvTitle.setTextColor(0xFF000000);
            tvTitle.setText(title);
            tvTitle.setPadding(0, 30, 0, 10);
            addView(tvTitle);
        }

        // Row Layout for Number and RichText
        LinearLayout rowLayout = new LinearLayout(context);
        rowLayout.setOrientation(HORIZONTAL);
        rowLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        // On Number TextView
        tvOnNumber = new TextView(context);
        tvOnNumber.setTextSize(12);
        tvOnNumber.setTypeface(getResources().getFont(R.font.poppins_bold));
        tvOnNumber.setTextColor(getResources().getColor(R.color.black, null));
        tvOnNumber.setText(onNumber);
        rowLayout.addView(tvOnNumber);

        // Rich Text with SpannableString
        tvRichText = new TextView(context);
        tvRichText.setTextSize(12);
        tvRichText.setTextColor(getResources().getColor(R.color.black, null));
        tvRichText.setTypeface(getResources().getFont(R.font.poppins_medium));

        SpannableString richText = new SpannableString(subTitle + " " + description);
        richText.setSpan(new StyleSpan(Typeface.BOLD), 0, subTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvRichText.setText(richText);

        rowLayout.addView(tvRichText);
        addView(rowLayout);
    }
}
