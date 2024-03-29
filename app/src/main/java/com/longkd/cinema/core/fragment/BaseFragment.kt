package com.longkd.cinema.core.fragment

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import com.longkd.cinema.MainActivity
import com.longkd.cinema.customviews.CustomToolbar

abstract class BaseFragment(@LayoutRes layoutResId: Int) : Fragment(layoutResId) {

    abstract val fragmentConfiguration: FragmentConfiguration

    private val mainActivity
        get() = activity as? MainActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        customizeFragment()
    }

    private fun customizeFragment() {
        mainActivity?.configureToolbar(fragmentConfiguration.toolbarConfiguration)
    }

    protected fun getToolbar(): CustomToolbar? {
        return mainActivity?.getToolbar()
    }

    protected fun navBack() {
        mainActivity?.navBack()
    }

    protected fun nav(directions: NavDirections, onError: (() -> Unit)? = null) {
        mainActivity?.nav(directions, onError)
    }

    protected fun hideBottomNavbar() {
        mainActivity?.hideBottomNavbar()
    }

    protected fun showBottomNavbar() {
        mainActivity?.showBottomNavBar()
    }

    protected fun showProgressDialog() {
        mainActivity?.showProgressDialog()
    }

    protected fun hideProgressDialog() {
        mainActivity?.hideProgressDialog()
    }

    protected fun setStartDestinationToHome() {
        mainActivity?.setStartDestinationToHome()
    }

    protected fun setStartDestinationToIntro() {
        mainActivity?.setStartDestinationToIntro()
    }

    protected fun showToolbar() {
        mainActivity?.showToolbar()
    }

    protected fun hideToolbar() {
        mainActivity?.hideToolbar()
    }

    protected fun restartActivity() {
        mainActivity?.restartActivity()
    }
}
