package com.appz.abhi.tooltip.ui.common.customview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.appz.abhi.tooltip.R;

public class MyChromeHelpPopup {


    private WindowManager windowManager;
    private Context context;
    private PopupWindow popupWindow;

    private LinearLayout tooltipView;
    private ImageView upArrowImageView;
    private TextView textView;

    private String text;


    public MyChromeHelpPopup(Context context, String text) {

        // Window manager
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        // Context
        this.context = context;

        // Popup window
        popupWindow = new PopupWindow(context);

        //
        this.text = text;

        // popupWindow.setContentView(tooltipView);
    }


    public void show(View anchor) {

        preShow();

        int[] anchorViewLocation = new int[2];
        anchor.getLocationOnScreen(anchorViewLocation);

        Rect anchorRect = new Rect(
                anchorViewLocation[0],
                anchorViewLocation[1],
                anchorViewLocation[0] + anchor.getWidth(),
                anchorViewLocation[1] + anchor.getHeight()
        );

        Log.e("Log", "anchorRect.left : " + anchorRect.left);
        Log.e("Log", "anchorRect.right : " + anchorRect.right);
        Log.e("Log", "anchorRect.top : " + anchorRect.top);
        Log.e("Log", "anchorRect.bottom : " + anchorRect.bottom);
        Log.e("Log", "anchorRect.centerX() : " + anchorRect.centerX());
        Log.e("Log", "anchorRect.centerY() : " + anchorRect.centerY());
        Log.e("Log", "anchorRect.height() : " + anchorRect.height());
        Log.e("Log", "anchorRect.width() : " + anchorRect.width());

        tooltipView.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        int tooltipViewMeasuredHeight = tooltipView.getMeasuredHeight();
        int tooltipViewMeasuredWidth = tooltipView.getMeasuredWidth();

        tooltipView.invalidate();
        tooltipView.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        Log.e("Log", "tooltipViewMeasuredHeight : " + tooltipViewMeasuredHeight);
        Log.e("Log", "tooltipViewMeasuredWidth : " + tooltipViewMeasuredWidth);

        Log.e("Log", "tooltipView.getHeight() : " + tooltipView.getHeight());
        Log.e("Log", "tooltipView.getWidth() : " + tooltipView.getWidth());


        final int screenWidth = windowManager.getDefaultDisplay().getWidth();
        final int screenHeight = windowManager.getDefaultDisplay().getHeight();

        Log.e("Log", "screenWidth : " + screenWidth);
        Log.e("Log", "screenHeight : " + screenHeight);

        Log.e("Log", "tooltipViewMeasuredHeight : " + textView.getMeasuredHeight());
        Log.e("Log", "tooltipViewMeasuredWidth : " + textView.getMeasuredWidth());

        Log.e("Log", "tooltipView.getHeight() : " + textView.getHeight());
        Log.e("Log", "tooltipView.getWidth() : " + textView.getWidth());


        int yPos = anchorRect.top - tooltipViewMeasuredHeight;

        int requestedX = anchorRect.centerX();
        Log.e("Log", "requestedX : " + requestedX);

        View arrow = upArrowImageView;

        final int arrowWidth = arrow.getMeasuredWidth();
        Log.e("Log", "arrowWidth : " + arrowWidth);

        arrow.setVisibility(View.VISIBLE);

        ViewGroup.MarginLayoutParams param = (ViewGroup.MarginLayoutParams) arrow.getLayoutParams();

        int xPos;

        // EXTREME RIGHT CLICKED
        if (anchorRect.left + tooltipViewMeasuredWidth > screenWidth) {
            Log.e("Log", "Case : Extreme right");

            xPos = (screenWidth - tooltipViewMeasuredWidth);
        }
        // EXTREME LEFT CLICKED
        else if (anchorRect.left - (tooltipViewMeasuredWidth / 2) < 0) {
            Log.e("Log", "Case : Extreme left");
            xPos = anchorRect.left;
        }
        // IN BETWEEN
        else {
            Log.e("Log", "Case : In between");
            xPos = (anchorRect.centerX() - (tooltipViewMeasuredWidth / 2));
        }

        param.leftMargin = (requestedX - xPos) - (arrowWidth / 2);
        Log.e("Log", "Arrow leftMargin : " + param.leftMargin);

        textView.setMaxHeight(anchorRect.top - anchorRect.height());
//            textView.setMaxHeight(screenHeight - yPos);

        popupWindow.setContentView(tooltipView);

        Log.e("Log", "xPos : " + xPos);
        Log.e("Log", "yPos : " + yPos);
        // popupWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, xPos, yPos);
        popupWindow.showAsDropDown(anchor, anchorRect.width() / 2, 0);
    }

    private void preShow() {

        tooltipView = new LinearLayout(context);
        tooltipView.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams linearLayoutLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayoutLayoutParams.setMargins(16, 16, 16, 16);
        tooltipView.setLayoutParams(linearLayoutLayoutParams);
        tooltipView.setBackgroundColor(Color.TRANSPARENT);

        upArrowImageView = new ImageView(context);
        upArrowImageView.setImageDrawable(context.getDrawable(R.drawable.up_arrow));
        LayoutParams arrowLayoutParams =
                new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        upArrowImageView.setLayoutParams(arrowLayoutParams);

        textView = new TextView(context);
        textView.setText(text);
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());
        textView.setPadding(8, 8, 8, 8);

        // textView.setTextAppearance(R.style.ChromeStylePopup);
        textView.setBackground(context.getDrawable(R.drawable.blue_bg));
        textView.setTextColor(context.getResources().getColor(android.R.color.white));
        textView.setGravity(Gravity.CENTER);
        textView.setMinWidth(100);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTypeface(null, Typeface.BOLD);

        LayoutParams textLayoutParams =
                new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(textLayoutParams);
        textView.setVerticalScrollBarEnabled(true);

        tooltipView.addView(upArrowImageView);
        tooltipView.addView(textView);

        /*
        if (backgroundDrawable == null) {
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
        } else {
            popupWindow.setBackgroundDrawable(backgroundDrawable);
        }
        */

        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
    }
}
