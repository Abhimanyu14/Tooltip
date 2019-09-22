package com.appz.abhi.tooltip.ui.common.customview;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.appz.abhi.tooltip.R;

public class ModifiedChromeHelpPopup {


    private WindowManager windowManager;
    private PopupWindow popupWindow;

    private TextView textView;
    private ImageView upArrowImageView;
    private ImageView downArrowImageView;
    private View tooltipView;
    private View popupLayout;

    private Drawable backgroundDrawable = null;


    public ModifiedChromeHelpPopup(Context context, String text) {

        // Layout resource
        int viewResource = R.layout.modified_popup;

        // Popup window
        popupWindow = new PopupWindow(context);

        // Window manager
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        // Layout inflater
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (layoutInflater != null) {

            // Inflate tooltip layout
            tooltipView = layoutInflater.inflate(viewResource, null);
            popupWindow.setContentView(tooltipView);
        }

        // UI variables
        textView = tooltipView.findViewById(R.id.text);
        upArrowImageView = tooltipView.findViewById(R.id.arrow_up);
        downArrowImageView = tooltipView.findViewById(R.id.arrow_down);
        popupLayout = tooltipView.findViewById(R.id.popup_layout);

        textView.setMovementMethod(ScrollingMovementMethod.getInstance());
        textView.setText(text);
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

        boolean onTop = true;

        if (anchorRect.top < screenHeight / 2) {
            yPos = anchorRect.bottom;
            onTop = false;
        }

        Log.e("Log", "onTop : " + onTop);

        int requestedX = anchorRect.centerX();
        Log.e("Log", "requestedX : " + requestedX);

        View arrow = onTop ? downArrowImageView : upArrowImageView;
        View hideArrow = onTop ? upArrowImageView : downArrowImageView;

        final int arrowWidth = arrow.getMeasuredWidth();
        Log.e("Log", "arrowWidth : " + arrowWidth);

        arrow.setVisibility(View.VISIBLE);
        hideArrow.setVisibility(View.INVISIBLE);

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

        if (onTop) {
            textView.setMaxHeight(anchorRect.top - anchorRect.height());
        } else {
            textView.setMaxHeight(screenHeight - yPos);
        }

        Log.e("Log", "xPos : " + xPos);
        Log.e("Log", "yPos : " + yPos);
        // popupWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, xPos, yPos);
        popupWindow.showAsDropDown(anchor, anchorRect.width() / 2, 0);
    }

    private void preShow() {

        if (tooltipView == null) {
            throw new IllegalStateException("view undefined");
        }

        if (backgroundDrawable == null) {
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
        } else {
            popupWindow.setBackgroundDrawable(backgroundDrawable);
        }

        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);

        popupWindow.setContentView(tooltipView);
    }
}
