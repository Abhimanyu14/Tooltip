package com.makeappssimple.abhimanyu.tooltip.android

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.text.method.ScrollingMovementMethod
import android.view.*
import android.view.ViewGroup.MarginLayoutParams
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView

/**
 * Reference : http://shardulprabhu.blogspot.com/2012/08/blog-post_29.html
 */
class CustomTooltip(
    context: Context,
    text: String?,
) {
    private val windowManager: WindowManager
    private val popupWindow: PopupWindow

    private val textTextView: TextView
    private val upArrowImageView: ImageView
    private val downArrowImageView: ImageView
    private val tooltipView: View?

    private var anchorViewRect: Rect? = null
    private var screenWidth = 0
    private var screenHeight = 0
    private var tooltipHeight = 0
    private var tooltipWidth = 0
    private var xPos = 0
    private var yPos = 0
    private var overflow = 0
    private var tooltipOnTop = false


    init {
        val viewResource = R.layout.tooltip
        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        tooltipView = layoutInflater.inflate(viewResource, null)
        popupWindow = PopupWindow(context)
        popupWindow.contentView = tooltipView

        textTextView = tooltipView.findViewById(R.id.text)
        upArrowImageView = tooltipView.findViewById(R.id.arrow_up)
        downArrowImageView = tooltipView.findViewById(R.id.arrow_down)

        textTextView.movementMethod = ScrollingMovementMethod.getInstance()
        textTextView.text = text
    }


    fun show(
        anchor: View,
    ) {
        initPopupWindow()
        anchorViewRect = getAnchorViewDimensions(anchor)
        screenWidth = calculateScreenWidth()
        screenHeight = calculateScreenHeight()
        calculateTooltipDimensions()
        val standardMargin = 16
        if (tooltipWidth > screenWidth - standardMargin * 2) {

            // Both sides overflow
            overflow = 1

            // Set maximum width
            textTextView.maxWidth = screenWidth - standardMargin * 2

            // Recalculate tooltip dimensions
            calculateTooltipDimensions()
        }
        calculateTooltipPosition()
        val arrow: View = if (tooltipOnTop) downArrowImageView else upArrowImageView
        val hideArrow: View = if (tooltipOnTop) upArrowImageView else downArrowImageView
        arrow.visibility = View.VISIBLE
        hideArrow.visibility = View.GONE
        val arrowWidth = arrow.measuredWidth
        val requestedX = anchorViewRect!!.centerX()
        val arrowLayoutParams = arrow.layoutParams as MarginLayoutParams
        val textLayoutParams = textTextView.layoutParams as MarginLayoutParams
        if (overflow != 1) {

            // Right overflow
            overflow = when {
                anchorViewRect!!.centerX() + tooltipWidth / 2 > screenWidth - standardMargin -> {
                    3
                }
                anchorViewRect!!.centerX() - tooltipWidth / 2 - standardMargin < 0 -> {
                    2
                }
                else -> {
                    4
                }
            }
        }
        when (overflow) {
            1 -> {
                // Both
                xPos = standardMargin * 2
            }
            2 -> {
                // Left
                xPos = anchorViewRect!!.left
            }
            3 -> {
                // Right
                xPos = anchorViewRect!!.right - tooltipWidth
            }
            4 -> {
                // No
                xPos = anchorViewRect!!.centerX() - tooltipWidth / 2
            }
            else -> {
            }
        }
        arrowLayoutParams.leftMargin = requestedX - xPos - arrowWidth / 2
        if (arrowLayoutParams.leftMargin < standardMargin * 2) {

            // Minimum arrow margin 24dp
            arrowLayoutParams.leftMargin = standardMargin * 2
        }
        if (overflow == 3) {
            if (screenWidth - (xPos + arrowLayoutParams.leftMargin + arrowWidth + standardMargin * 2) < standardMargin) {
                arrowLayoutParams.leftMargin = screenWidth - xPos - standardMargin * 4 - arrowWidth
            }
        }
        textLayoutParams.leftMargin = requestedX - xPos - tooltipWidth / 2
        if (textLayoutParams.leftMargin < 0) {
            textLayoutParams.leftMargin = 0
        }
        if (overflow == 3) {
            textLayoutParams.leftMargin = 0
        }
        popupWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, xPos, yPos)
    }

    private fun calculateTooltipPosition() {
        anchorViewRect?.let { anchorViewRect ->
            if (anchorViewRect.top < screenHeight / 2) {
                tooltipOnTop = false
                yPos = anchorViewRect.bottom
            } else {
                tooltipOnTop = true
                yPos = anchorViewRect.top - tooltipHeight
            }
        }
    }

    private fun calculateTooltipDimensions() {
        tooltipView!!.measure(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        tooltipHeight = tooltipView.measuredHeight
        tooltipWidth = tooltipView.measuredWidth
    }

    private fun initPopupWindow() {
        checkNotNull(tooltipView) { "view undefined" }
        popupWindow.apply {
            setBackgroundDrawable(BitmapDrawable())
            width = WindowManager.LayoutParams.WRAP_CONTENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            isOutsideTouchable = true
            contentView = tooltipView
        }
    }

    private fun calculateScreenWidth(): Int {
        return windowManager.defaultDisplay.width
    }

    private fun calculateScreenHeight(): Int {
        return windowManager.defaultDisplay.height
    }

    private fun getAnchorViewDimensions(
        anchor: View,
    ): Rect {
        val location = IntArray(2)
        anchor.getLocationOnScreen(location)
        return Rect(
            location[0],
            location[1],
            location[0] + anchor.width,
            location[1] + anchor.height
        )
    }
}
