package com.makeappssimple.abhimanyu.tooltip.android

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.*
import android.view.ViewGroup.MarginLayoutParams
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView

fun logE(s: String) {
    Log.e("Abhi", s)
}

/**
 * Reference : http://shardulprabhu.blogspot.com/2012/08/blog-post_29.html
 */
enum class Overflow {
    BOTH_SIDES, LEFT, RIGHT, NONE
}

class CustomTooltip(
    context: Context,
    text: String?,
) {
    private val windowManager: WindowManager
    private val popupWindow: PopupWindow

    private val tooltipView: View
    private val textView: TextView
    private val upArrowImageView: ImageView
    private val downArrowImageView: ImageView


    init {
        val viewResource = R.layout.tooltip
        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        tooltipView = layoutInflater.inflate(viewResource, null)
        popupWindow = PopupWindow(context)
        popupWindow.contentView = tooltipView

        textView = tooltipView.findViewById(R.id.text)
        upArrowImageView = tooltipView.findViewById(R.id.arrow_up)
        downArrowImageView = tooltipView.findViewById(R.id.arrow_down)

        textView.movementMethod = ScrollingMovementMethod.getInstance()
        textView.text = text
    }


    fun show(
        anchor: View,
    ) {
        val standardMargin = 16
        val yPos: Int
        val arrowOnBottom: Boolean
        val screenWidth: Int = calculateScreenWidth()
        val screenHeight: Int = calculateScreenHeight()
        val anchorViewRect: Rect = getAnchorViewDimensions(anchor)
        val tooltipHeight: Int
        val tooltipWidth: Int

        var overflow: Overflow = Overflow.NONE
        logE("screenWidth $screenWidth")
        logE("screenHeight $screenHeight")
        logE("anchorViewRect.top ${anchorViewRect.top}")
        logE("anchorViewRect.bottom ${anchorViewRect.bottom}")
        logE("anchorViewRect.left ${anchorViewRect.left}")
        logE("anchorViewRect.right ${anchorViewRect.right}")

        // initPopupWindow
        popupWindow.apply {
            setBackgroundDrawable(BitmapDrawable())
            width = WindowManager.LayoutParams.WRAP_CONTENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            isOutsideTouchable = true
            contentView = tooltipView
        }

        // calculateTooltipDimensions
        tooltipView.measure(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        logE("tooltipView.measuredWidth ${tooltipView.measuredWidth}")
        if (tooltipView.measuredWidth > screenWidth - standardMargin * 2) {
            logE("Both sides overflow")

            // Both sides overflow
            overflow = Overflow.BOTH_SIDES

            // Set maximum width
            textView.maxWidth = screenWidth - standardMargin * 2

            // Recalculate tooltip dimensions
            // calculateTooltipDimensions
            tooltipView.measure(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            tooltipHeight = tooltipView.measuredHeight
            tooltipWidth = tooltipView.measuredWidth
        } else {
            tooltipHeight = tooltipView.measuredHeight
            tooltipWidth = tooltipView.measuredWidth
        }

        logE("tooltipHeight $tooltipHeight")
        logE("tooltipWidth $tooltipWidth")

        if (anchorViewRect.top < screenHeight / 2) {
            arrowOnBottom = false
            yPos = anchorViewRect.bottom
        } else {
            arrowOnBottom = true
            yPos = anchorViewRect.top - tooltipHeight
        }
        logE("arrowOnBottom $arrowOnBottom")
        logE("yPos $yPos")

        // Hide an arrow according to tooltip direction
        if (arrowOnBottom) {
            upArrowImageView
        } else {
            downArrowImageView
        }.apply {
            visibility = View.GONE
        }

        // Show the other arrow according to tooltip direction
        val visibleArrow = if (arrowOnBottom) {
            downArrowImageView
        } else {
            upArrowImageView
        }.apply {
            visibility = View.VISIBLE
        }

        val arrowWidth = visibleArrow.measuredWidth
        val requestedX = anchorViewRect.centerX()
        val arrowLayoutParams = visibleArrow.layoutParams as MarginLayoutParams
        val textLayoutParams = textView.layoutParams as MarginLayoutParams

        if (overflow == Overflow.NONE) {
            if (anchorViewRect.centerX() + tooltipWidth / 2 > screenWidth - standardMargin) {
                overflow = Overflow.RIGHT
            } else if (anchorViewRect.centerX() - tooltipWidth / 2 - standardMargin < 0) {
                overflow = Overflow.LEFT
            }
        }
        logE("overflow $overflow")
        logE("requestedX $requestedX")
        logE("arrowWidth $arrowWidth")

        val xPos = when (overflow) {
            Overflow.BOTH_SIDES -> {
                standardMargin
            }
            Overflow.LEFT -> {
                anchorViewRect.left
            }
            Overflow.RIGHT -> {
                anchorViewRect.right - tooltipWidth
            }
            Overflow.NONE -> {
                anchorViewRect.centerX() - tooltipWidth / 2
            }
        }
        logE("xPos $xPos")

        arrowLayoutParams.leftMargin = getArrowLeftMargin(
            overflow = overflow,
            arrowLayoutParams = arrowLayoutParams,
            standardMargin = standardMargin,
            screenWidth = screenWidth,
            xPos = xPos,
            arrowWidth = arrowWidth,
            requestedX = requestedX,
        )
        logE("arrowLayoutParams.leftMargin ${arrowLayoutParams.leftMargin}")

        textLayoutParams.leftMargin = getTextLeftMargin(
            overflow = overflow,
            tooltipWidth = tooltipWidth,
            xPos = xPos,
            requestedX = requestedX,
        )
        logE("textLayoutParams.leftMargin ${textLayoutParams.leftMargin}")

        popupWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, xPos, yPos)
    }

    private fun getTextLeftMargin(
        overflow: Overflow,
        tooltipWidth: Int,
        xPos: Int,
        requestedX: Int
    ): Int {
        return if (overflow == Overflow.RIGHT) {
            0
        } else {
            maxOf(requestedX - xPos - tooltipWidth / 2, 0)
        }
    }

    private fun getArrowLeftMargin(
        overflow: Overflow,
        arrowLayoutParams: MarginLayoutParams,
        standardMargin: Int,
        screenWidth: Int,
        xPos: Int,
        arrowWidth: Int,
        requestedX: Int
    ): Int {
        return if (
            overflow == Overflow.RIGHT &&
            (screenWidth - (xPos + arrowLayoutParams.leftMargin + arrowWidth + standardMargin * 2) < standardMargin)
        ) {
            screenWidth - xPos - standardMargin * 4 - arrowWidth
        } else {
            maxOf((requestedX - xPos - arrowWidth / 2), (standardMargin * 2))
        }
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

    private fun calculateScreenWidth(): Int {
        return windowManager.defaultDisplay.width
    }

    private fun calculateScreenHeight(): Int {
        return windowManager.defaultDisplay.height
    }
}
