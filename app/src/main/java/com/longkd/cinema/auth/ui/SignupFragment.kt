package com.longkd.cinema.auth.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.longkd.cinema.R
import com.longkd.cinema.core.fragment.BaseFragment
import com.longkd.cinema.core.fragment.FragmentConfiguration
import com.longkd.cinema.core.fragment.ToolbarConfiguration
import com.longkd.cinema.databinding.FragmentSignupBinding
import com.longkd.cinema.utils.viewbinding.viewBinding
import com.google.firebase.auth.FirebaseAuth

class SignupFragment : BaseFragment(R.layout.fragment_signup) {

    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
    }

    private val toolbarConfiguration = ToolbarConfiguration(
        titleResId = R.string.sign_up,
        startIconResId = R.drawable.ic_arrow_back,
        startIconClick = ::navBack
    )

    override val fragmentConfiguration = FragmentConfiguration(toolbarConfiguration)

    private val binding by viewBinding(FragmentSignupBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        hideBottomNavbar()
        with(binding) {
            signupButton.setOnClickListener {
                createNewUser(
                    email = emailEditText.text.toString(),
                    password = passwordEditText.text.toString(),
                    termsAccepted = termsPrivacyCheckBox.isChecked
                )
            }
            termsPrivacyTextView.setOnClickListener {
                navToPrivacyPolicyFragment()
            }
        }
    }

    private fun createNewUser(email: String, password: String, termsAccepted: Boolean) {
        if (email.isNotBlank() && password.isNotBlank() && termsAccepted) {
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                Toast.makeText(requireContext(), getString(R.string.account_created), Toast.LENGTH_SHORT).show()
                setStartDestinationToHome()
                navToHomeFragment()
            }.addOnFailureListener { exception ->
                Toast.makeText(requireContext(), exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navToHomeFragment() {
        nav(SignupFragmentDirections.actionSignupFragmentToHomeFragment())
    }

    private fun navToPrivacyPolicyFragment() {
        nav(SignupFragmentDirections.actionSignupFragmentToPrivacyPolicyFragment())
    }

}
