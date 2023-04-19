package com.longkd.cinema.profile.ui

import android.os.Bundle
import android.view.View
import com.longkd.cinema.R
import com.longkd.cinema.core.fragment.BaseFragment
import com.longkd.cinema.core.fragment.FragmentConfiguration
import com.longkd.cinema.core.fragment.ToolbarConfiguration
import com.longkd.cinema.utils.showTextToast
import com.longkd.cinema.utils.viewbinding.viewBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.longkd.cinema.databinding.FragmentProfileBinding

class ProfileFragment : BaseFragment(R.layout.fragment_profile) {

    private val toolbarConfiguration = ToolbarConfiguration(titleResId = R.string.profile)

    override val fragmentConfiguration = FragmentConfiguration(toolbarConfiguration)

    private val binding by viewBinding(FragmentProfileBinding::bind)

    private lateinit var auth: FirebaseAuth

    private var logoutDialog: LogoutDialog? = null

    private var currentUser: FirebaseUser? = null

    private val logOutRequestListener = LogoutDialog.LogOutRequestListener {
        auth.signOut()
        context?.showTextToast(getString(R.string.logged_out))
        setStartDestinationToIntro()
        nav(ProfileFragmentDirections.actionProfileFragmentToIntroFragment())

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        currentUser = auth.currentUser
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }


    private fun initUI() {
        showBottomNavbar()
        with(binding) {
            legalPoliciesButton.setOnClickListener {
                nav(ProfileFragmentDirections.actionProfileFragmentToPrivacyPolicyFragment())
            }
            editProfileButton.setOnClickListener {
                nav(ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment())
            }
            languageButton.setOnClickListener {
                nav(ProfileFragmentDirections.actionProfileFragmentToLanguageFragment())
            }
            logoutButton.setOnClickListener {
                showLogoutDialog()
            }

            if (!currentUser?.email.isNullOrEmpty()) {
                emailTextView.text = currentUser?.email
            } else {
                emailTextView.text = currentUser?.providerData?.get(1)?.email
            }
            nameTextView.text = currentUser?.displayName
            Glide.with(requireContext())
                .load(currentUser?.photoUrl)
                .centerCrop().placeholder(R.drawable.ic_person_24)
                .into(personImageView)
        }
    }

    private fun showLogoutDialog() {
        logoutDialog = LogoutDialog(requireActivity(), logOutRequestListener)

        logoutDialog?.startDialog()
    }
}
