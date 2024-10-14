package com.example.playlistmaker2.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker2.R
import com.example.playlistmaker2.databinding.FragmentSettingsBinding
import com.example.playlistmaker2.sharing.domain.model.EmailData
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<SettingsViewModel>()
    private lateinit var themeSwitcher: SwitchMaterial

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val share = binding.buttonShare
        val support = binding.buttonMailToSupport
        val userAgreement = binding.buttonUserAgr
        themeSwitcher = binding.themeSwitcher

        viewModel.settingsState().observe(viewLifecycleOwner) {
                state ->
            handleSettingsState(state)
        }

        setUi()

        share.setOnClickListener{
            viewModel.shareApp(getString(R.string.messageShare))
        }

        support.setOnClickListener{
            val dataSupport = EmailData(
                getString(R.string.subjectMailto),
                getString(R.string.bodyMailto),
                getString(R.string.emailMailto))
            viewModel.contactSupport(dataSupport)
        }

        userAgreement.setOnClickListener{
            viewModel.openUserAgreement(getString(R.string.userAgreement))
        }

    }

    private fun handleSettingsState(state: SettingsState) {
        themeSwitcher.isChecked = state.isDarkThemeEnabled
    }

    override fun onResume() {
        super.onResume()
        viewModel.synchronizeTheme()
    }

    private fun setUi() {
        themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setDarkTheme(isChecked)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}