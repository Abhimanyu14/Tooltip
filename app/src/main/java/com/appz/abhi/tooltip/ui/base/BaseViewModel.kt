package com.appz.abhi.tooltip.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.appz.abhi.tooltip.util.constant.NO_BUTTON_CLICK
import com.appz.abhi.tooltip.util.constant.NO_NAVIGATION
import com.appz.abhi.tooltip.util.constant.NO_SNACKBAR_MESSAGE
import com.appz.abhi.tooltip.util.constant.NO_TOAST_MESSAGE

abstract class BaseViewModel : ViewModel() {


    // Navigation state
    private var navigationState: MutableLiveData<Int> = MutableLiveData()

    // Button click state
    private var buttonClickState: MutableLiveData<Int> = MutableLiveData()

    // Toast message state
    private var toastMessageState: MutableLiveData<Int> = MutableLiveData()

    // Snackbar message state
    private var snackbarMessageState: MutableLiveData<Int> = MutableLiveData()


    // Initializer
    init {

        // Initialize navigation state
        navigationState.value = NO_NAVIGATION

        // Initialize button click state
        buttonClickState.value = NO_BUTTON_CLICK

        // Initialize toast message state
        toastMessageState.value = NO_TOAST_MESSAGE

        // Initialize snackbar message state
        snackbarMessageState.value = NO_SNACKBAR_MESSAGE
    }


    // Getters
    fun getNavigationState(): MutableLiveData<Int> {
        return navigationState
    }

    fun getButtonClickState(): MutableLiveData<Int> {
        return buttonClickState
    }

    fun getToastMessageState(): MutableLiveData<Int> {
        return toastMessageState
    }

    fun getSnackbarMessageState(): MutableLiveData<Int> {
        return snackbarMessageState
    }


    // Setters
    fun setNavigationState(navigationStateValue: Int) {
        navigationState.value = navigationStateValue
    }

    fun setButtonClickState(buttonClickStateValue: Int) {
        buttonClickState.value = buttonClickStateValue
    }

    fun setToastMessageState(toastMessageStateValue: Int) {
        toastMessageState.value = toastMessageStateValue
    }

    fun setSnackbarMessageState(snackbarMessageStateValue: Int) {
        snackbarMessageState.value = snackbarMessageStateValue
    }
}
