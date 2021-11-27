package com.makeappssimple.abhimanyu.tooltip.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.TooltipCompat
import androidx.databinding.DataBindingUtil
import com.makeappssimple.abhimanyu.tooltip.android.databinding.MainActivityBinding


class MainActivity : AppCompatActivity() {

    // Data binding
    private lateinit var mainActivityBinding: MainActivityBinding

    // On activity creation starting
    override fun onCreate(
        savedInstanceState: Bundle?,
    ) {
        super.onCreate(savedInstanceState)

        mainActivityBinding = DataBindingUtil.setContentView(this, R.layout.main_activity)
        initClickListeners()
    }

    private fun initClickListeners() {
        TooltipCompat.setTooltipText(
            mainActivityBinding.mainActivityTextViewStandardAndroidTooltip,
            "Standard android tooltip in button",
        )

        mainActivityBinding.mainActivityTextViewPopupTooltipTopLeft.setOnLongClickListener {
            CustomTooltip(
                context = this,
                text = "This is a one line top left tooltip !"
            ).show(
                mainActivityBinding.mainActivityTextViewPopupTooltipTopLeft
            )
            true
        }

        mainActivityBinding.mainActivityTextViewPopupTooltipTopRight.setOnLongClickListener {
            CustomTooltip(
                context = this,
                text = "This is a one line top right tooltip !"
            ).show(mainActivityBinding.mainActivityTextViewPopupTooltipTopRight)
            true
        }

        mainActivityBinding.mainActivityTextViewPopupTooltip.setOnLongClickListener {
            CustomTooltip(
                context = this,
                text = "First tooltip!"
            ).show(
                mainActivityBinding.mainActivityTextViewPopupTooltip
            )
            true
        }

        mainActivityBinding.mainActivityTextViewPopupTooltip1.setOnLongClickListener {
            CustomTooltip(
                context = this,
                text = "Second tooltip!"
            ).show(
                mainActivityBinding.mainActivityTextViewPopupTooltip1
            )
            true
        }

        mainActivityBinding.mainActivityTextViewPopupTooltip2.setOnLongClickListener {
            CustomTooltip(
                context = this,
                text = "Left tooltip!"
            ).show(
                mainActivityBinding.mainActivityTextViewPopupTooltip2
            )
            true
        }

        mainActivityBinding.mainActivityTextViewPopupTooltip3.setOnLongClickListener {
            CustomTooltip(
                context = this,
                text = "Right tooltip!"
            ).show(
                mainActivityBinding.mainActivityTextViewPopupTooltip3
            )
            true
        }

        mainActivityBinding.mainActivityTextViewPopupTooltip4.setOnLongClickListener {
            CustomTooltip(
                context = this,
                text = "This is a tooltip with a very " +
                        "long text which will use multiple lines to test extreme cases." +
                        "This is a tooltip with a very " +
                        "long text which will use multiple lines to test extreme cases." +
                        "This is a tooltip with a very " +
                        "long text which will use multiple lines to test extreme cases." +
                        "This is a tooltip with a very " +
                        "long text which will use multiple lines to test extreme cases." +
                        "This is a tooltip with a very " +
                        "long text which will use multiple lines to test extreme cases."
            ).show(
                mainActivityBinding.mainActivityTextViewPopupTooltip4
            )
            true
        }

        mainActivityBinding.mainActivityTextViewPopupTooltip5.setOnLongClickListener {
            CustomTooltip(
                this@MainActivity,
                "My Tooltip !!"
            ).show(
                mainActivityBinding.mainActivityTextViewPopupTooltip5
            )
            true
        }
    }
}
