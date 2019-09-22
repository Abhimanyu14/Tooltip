package com.appz.abhi.tooltip.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.TooltipCompat
import androidx.databinding.DataBindingUtil
import com.appz.abhi.tooltip.databinding.MainActivityBinding
import com.appz.abhi.tooltip.ui.common.customview.ChromeHelpPopup
import kotlinx.android.synthetic.main.main_activity.*


class MainActivity : AppCompatActivity() {


    // Data binding
    private lateinit var mainActivityBinding: MainActivityBinding


    // On activity creation starting
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Log message
        Log.e(TAG, "onCreate")

        // Set activity layout
        mainActivityBinding =
            DataBindingUtil.setContentView(this, com.appz.abhi.tooltip.R.layout.main_activity)

        // Setup action bar
        setSupportActionBar(mainActivityBinding.mainActivityToolbar)

        setListeners()
    }

    private fun setListeners() {

        TooltipCompat.setTooltipText(
            main_activity_text_view_standard_android_tooltip,
            "Standard android tooltip in button"
        )

        val chromeHelpPopupTopLeft =
            ChromeHelpPopup(
                this@MainActivity,
                "This is a one line top left tooltip !"
            )
        main_activity_text_view_popup_tooltip_top_left.setOnLongClickListener {
            chromeHelpPopupTopLeft.show(main_activity_text_view_popup_tooltip_top_left)
            true
        }

        val chromeHelpPopupTopRight =
            ChromeHelpPopup(
                this@MainActivity,
                "This is a one line top right tooltip !"
            )
        main_activity_text_view_popup_tooltip_top_right.setOnLongClickListener {
            chromeHelpPopupTopRight.show(main_activity_text_view_popup_tooltip_top_right)
            true
        }

        val chromeHelpPopup = ChromeHelpPopup(
            this@MainActivity,
            "First tooltip!"
        )
        main_activity_text_view_popup_tooltip.setOnLongClickListener {
            chromeHelpPopup.show(main_activity_text_view_popup_tooltip)
            true
        }

        val chromeHelpPopup1 = ChromeHelpPopup(
            this@MainActivity,
            "Second tooltip!"
        )
        main_activity_text_view_popup_tooltip_1.setOnLongClickListener {
            chromeHelpPopup1.show(main_activity_text_view_popup_tooltip_1)
            true
        }

        val chromeHelpPopup2 = ChromeHelpPopup(
            this@MainActivity,
            "Left tooltip!"
        )
        main_activity_text_view_popup_tooltip_2.setOnLongClickListener {
            chromeHelpPopup2.show(main_activity_text_view_popup_tooltip_2)
            true
        }

        val chromeHelpPopup3 = ChromeHelpPopup(
            this@MainActivity,
            "Right tooltip!"
        )
        main_activity_text_view_popup_tooltip_3.setOnLongClickListener {
            chromeHelpPopup3.show(main_activity_text_view_popup_tooltip_3)
            true
        }

        val chromeHelpPopup4 = ChromeHelpPopup(
            this@MainActivity,
            "This is a tooltip with a very " +
                    "long text which will use multiple lines to test extreme cases." +
                    "This is a tooltip with a very " +
                    "long text which will use multiple lines to test extreme cases." +
                    "This is a tooltip with a very " +
                    "long text which will use multiple lines to test extreme cases." +
                    "This is a tooltip with a very " +
                    "long text which will use multiple lines to test extreme cases." +
                    "This is a tooltip with a very " +
                    "long text which will use multiple lines to test extreme cases."
        )
        main_activity_text_view_popup_tooltip_4.setOnLongClickListener {
            chromeHelpPopup4.show(main_activity_text_view_popup_tooltip_4)
            true
        }

        main_activity_text_view_popup_tooltip_5

        val chromeHelpPopup5 = ChromeHelpPopup(this@MainActivity, "My Tooltip !!")
        main_activity_text_view_popup_tooltip_5.setOnLongClickListener {
            chromeHelpPopup5.show(main_activity_text_view_popup_tooltip_5)
            true
        }

        /*
        main_activity_text_view_standard_android_tooltip.setOnLongClickListener{

            true
        }
         */

    }


    companion object {


        // Logging constant
        private val TAG = MainActivity::class.java.simpleName
    }
}
