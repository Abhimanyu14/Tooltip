package com.appz.abhi.tooltip.ui.common.customview;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.appz.abhi.tooltip.R;

/*
 * Reference : http://shardulprabhu.blogspot.com/2012/08/blog-post_29.html
 * */

public class ChromeHelpPopup {


    private WindowManager windowManager;

    private PopupWindow popupWindow;

    private TextView textTextView;
    private ImageView upArrowImageView;
    private ImageView downArrowImageView;
    private View tooltipView;

    private Drawable backgroundDrawable = null;

    private Rect anchorViewRect;
    private int screenWidth, screenHeight, tooltipHeight, tooltipWidth, xPos, yPos, requestedX;
    private int standardMargin = 16;
    private int overflow = 0;
    private boolean tooltipOnTop;


    public ChromeHelpPopup(Context context, String text) {
        int viewResource = R.layout.popup;
        popupWindow = new PopupWindow(context);

        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View root = layoutInflater.inflate(viewResource, null);
        tooltipView = root;

        popupWindow.setContentView(root);

        textTextView = tooltipView.findViewById(R.id.text);
        upArrowImageView = tooltipView.findViewById(R.id.arrow_up);
        downArrowImageView = tooltipView.findViewById(R.id.arrow_down);

        textTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
        textTextView.setText(text);
    }

    public void show(View anchor) {

        preShow();

        getAnchorViewDimensions(anchor);

        getScreenDimensions();

        getTooltipDimensions();

        if (tooltipWidth > (screenWidth - (standardMargin * 2))) {

            // Both sides overflow
            overflow = 1;

            // Set maximum width
            textTextView.setMaxWidth(screenWidth - (standardMargin * 2));
            Log.e("Log", "textTextView.getMaxWidth() : " + textTextView.getMaxWidth());

            // Recalculate tooltip dimensions
            getTooltipDimensions();
        }

        getTooltipPosition();

        View arrow = tooltipOnTop ? downArrowImageView : upArrowImageView;
        View hideArrow = tooltipOnTop ? upArrowImageView : downArrowImageView;
        arrow.setVisibility(View.VISIBLE);
        hideArrow.setVisibility(View.GONE);

        final int arrowWidth = arrow.getMeasuredWidth();
        Log.e("Log", "arrowWidth : " + arrowWidth);

        requestedX = anchorViewRect.centerX();
        Log.e("Log", "requestedX : " + requestedX);

        ViewGroup.MarginLayoutParams arrowLayoutParams =
                (ViewGroup.MarginLayoutParams) arrow.getLayoutParams();
        ViewGroup.MarginLayoutParams textLayoutParams =
                (ViewGroup.MarginLayoutParams) textTextView.getLayoutParams();

        if (overflow != 1) {

            // Right overflow
            if (anchorViewRect.centerX() + (tooltipWidth / 2) > (screenWidth - standardMargin)) {
                Log.e("Log", "Case : Right overflow");
                overflow = 3;
            }

            // Left overflow
            else if ((anchorViewRect.centerX() - (tooltipWidth / 2) - standardMargin) < 0) {
                Log.e("Log", "Case : left overflow");
                overflow = 2;
            }

            // No overflow
            else {
                Log.e("Log", "Case : No overflow");
                overflow = 4;
            }
        }

        switch (overflow) {
            case 1:
                // Both
                xPos = standardMargin * 2;
                break;
            case 2:
                // Left
                xPos = anchorViewRect.left;
                break;
            case 3:
                // Right
                xPos = anchorViewRect.right - tooltipWidth;
                break;
            case 4:
                // No
                xPos = (anchorViewRect.centerX() - (tooltipWidth / 2));
                break;
            default:
                break;
        }

        arrowLayoutParams.leftMargin = (requestedX - xPos) - (arrowWidth / 2);
        if (arrowLayoutParams.leftMargin < standardMargin * 2) {

            // Minimum arrow margin 24dp
            arrowLayoutParams.leftMargin = standardMargin * 2;
        }
        Log.e("Log", "Arrow leftMargin : " + arrowLayoutParams.leftMargin);
        if (overflow == 3) {
            if ((screenWidth - (xPos + arrowLayoutParams.leftMargin + arrowWidth + standardMargin * 2)) < standardMargin) {
                Log.e("Log", "Changing arrow margin");
                arrowLayoutParams.leftMargin =
                        screenWidth - xPos - (standardMargin * 4) - arrowWidth;
            }
        }

        textLayoutParams.leftMargin = (requestedX - xPos) - (tooltipWidth / 2);
        if (textLayoutParams.leftMargin < 0) {
            textLayoutParams.leftMargin = 0;
        }
        if (overflow == 3) {
            textLayoutParams.leftMargin = 0;
        }
        Log.e("Log", "Text leftMargin : " + textLayoutParams.leftMargin);

        Log.e("Log", "Final xPos : " + xPos);
        Log.e("Log", "Final yPos : " + yPos);
        popupWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, xPos, yPos);
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

    private void getTooltipPosition() {
        if (anchorViewRect.top < screenHeight / 2) {
            tooltipOnTop = false;
            yPos = anchorViewRect.bottom;
        } else {
            tooltipOnTop = true;
            yPos = anchorViewRect.top - tooltipHeight;
        }
        Log.e("Log", "tooltipOnTop : " + tooltipOnTop);
        Log.e("Log", "yPos : " + yPos);
    }

    private void getTooltipDimensions() {
        tooltipView.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        tooltipHeight = tooltipView.getMeasuredHeight();
        tooltipWidth = tooltipView.getMeasuredWidth();
        Log.e("Log", "Tooltip measured Height : " + tooltipHeight);
        Log.e("Log", "Tooltip measured Width : " + tooltipWidth);
    }

    private void getScreenDimensions() {
        screenWidth = windowManager.getDefaultDisplay().getWidth();
        screenHeight = windowManager.getDefaultDisplay().getHeight();
        Log.e("Log", "screen Width : " + screenWidth);
        Log.e("Log", "screen Height : " + screenHeight);
    }

    private void getAnchorViewDimensions(View anchor) {

        int[] location = new int[2];
        anchor.getLocationOnScreen(location);
        anchorViewRect = new Rect(
                location[0],
                location[1],
                location[0] + anchor.getWidth(),
                location[1] + anchor.getHeight()
        );
        Log.e("Log", "Anchor left : " + anchorViewRect.left);
        Log.e("Log", "Anchor right : " + anchorViewRect.right);
        Log.e("Log", "Anchor top : " + anchorViewRect.top);
        Log.e("Log", "Anchor bottom : " + anchorViewRect.bottom);
        Log.e("Log", "Anchor centerX() : " + anchorViewRect.centerX());
        Log.e("Log", "Anchor centerY() : " + anchorViewRect.centerY());
        Log.e("Log", "Anchor height() : " + anchorViewRect.height());
        Log.e("Log", "Anchor width() : " + anchorViewRect.width());
    }
}
