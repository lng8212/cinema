package com.longkd.cinema.auth.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.longkd.cinema.R
import com.longkd.cinema.core.fragment.BaseFragment
import com.longkd.cinema.core.fragment.FragmentConfiguration
import com.longkd.cinema.core.fragment.ToolbarConfiguration
import com.longkd.cinema.databinding.FragmentLoginBinding
import com.longkd.cinema.utils.Resource
import com.longkd.cinema.utils.lifecycle.observe
import com.longkd.cinema.utils.showTextToast
import com.longkd.cinema.utils.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class LoginFragment : BaseFragment(R.layout.fragment_login) {

    private val toolbarConfiguration = ToolbarConfiguration(
        titleResId = R.string.login,
        startIconResId = R.drawable.ic_arrow_back,
        startIconClick = ::navBack
    )

    override val fragmentConfiguration = FragmentConfiguration(toolbarConfiguration)

    private val binding by viewBinding(FragmentLoginBinding::bind)

    private val viewModel by viewModels<LoginViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initObservers()
    }

    private fun initUI() {
        with(binding) {
            forgotPasswordTextView.setOnClickListener {
                navToForgotPasswordFragment()
            }
            loginButton.setOnClickListener {
                loginUser(email = emailEditText.text.toString(), password = passwordEditText.text.toString())
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        viewModel.loginWithEmailPassword(email, password)
    }

    private fun initObservers() {
        viewLifecycleOwner.observe {
            viewModel.loginState.collectLatest { state ->
                when (state) {
                    is Resource.Success -> {
                        hideProgressDialog()
                        requireContext().showTextToast(getString(R.string.login_success))
                        setStartDestinationToHome()
                        navToHomeFragment()
                    }
                    is Resource.Error -> {
                        hideProgressDialog()
                        state.message?.let {
                            requireContext().showTextToast(it)
                        }
                    }
                    is Resource.Loading -> {
                        if (state.isLoading)
                            showProgressDialog()
                    }
                }

            }
        }
    }

    private fun navToForgotPasswordFragment() {
        //TODO nav to forgotPassword fragment
    }

    private fun navToHomeFragment() {
        nav(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
    }
}
