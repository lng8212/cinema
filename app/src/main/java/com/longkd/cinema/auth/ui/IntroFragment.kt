package com.longkd.cinema.auth.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.longkd.cinema.R
import com.longkd.cinema.core.fragment.BaseFragment
import com.longkd.cinema.core.fragment.FragmentConfiguration
import com.longkd.cinema.databinding.FragmentIntroBinding
import com.longkd.cinema.utils.showTextToast
import com.longkd.cinema.utils.viewbinding.viewBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class IntroFragment : BaseFragment(R.layout.fragment_intro) {
    override val fragmentConfiguration = FragmentConfiguration()

    private val binding by viewBinding(FragmentIntroBinding::bind)

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        handleIfHaveLoggedInUser()
    }

    private fun initUI() {
        hideBottomNavbar()
        with(binding) {
            signupButton.setOnClickListener {
                navToSignupFragment()
            }
            loginTextView.setOnClickListener {
                navToLoginFragment()
            }
            googleSignupButton.setOnClickListener {
                val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.request_id_token))
                    .requestEmail()
                    .requestProfile()
                    .build()
                val signInClient = GoogleSignIn.getClient(requireContext(), options)
                signInClient.signInIntent.also {
                    startActivityForResult(it, REQUEST_CODE_SIGN_IN)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            val account = GoogleSignIn.getSignedInAccountFromIntent(data).result
            account?.let {
                googleAuthForFirebase(it)
            }
        }
    }

    private fun googleAuthForFirebase(account: GoogleSignInAccount) {
        val credentials = GoogleAuthProvider.getCredential(account.idToken, null)
        showProgressDialog()
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                auth.signInWithCredential(credentials).addOnSuccessListener {
                    hideProgressDialog()
                    context?.showTextToast(getString(R.string.login_success))
                    setStartDestinationToHome()
                    nav(IntroFragmentDirections.actionIntroFragmentToHomeFragment())
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    context?.showTextToast(e.toString())
                    hideProgressDialog()
                }
            }
        }
    }

    private fun handleIfHaveLoggedInUser() {
        if (auth.currentUser != null) {
            setStartDestinationToHome()
            nav(IntroFragmentDirections.actionIntroFragmentToHomeFragment())
        }
    }

    private fun navToSignupFragment() {
        nav(IntroFragmentDirections.actionIntroFragmentToSignupFragment())
    }

    private fun navToLoginFragment() {
        nav(IntroFragmentDirections.actionIntroFragmentToLoginFragment())
    }

    companion object {
        const val REQUEST_CODE_SIGN_IN = 0
    }
}
