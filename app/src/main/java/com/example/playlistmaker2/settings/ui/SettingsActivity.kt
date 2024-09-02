package com.example.playlistmaker2.settings.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker2.R
import com.example.playlistmaker2.sharing.domain.model.EmailData
import com.example.playlistmaker2.util.CreatorSharing
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {

    private lateinit var viewModel: SettingsViewModel
    private lateinit var themeSwitcher: SwitchMaterial

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val back = findViewById<View>(R.id.back)
        val share = findViewById<View>(R.id.buttonShare)
        val support = findViewById<View>(R.id.buttonMailToSupport)
        val userAgreement = findViewById<View>(R.id.buttonUserAgr)
        themeSwitcher = findViewById(R.id.themeSwitcher)

        val factory = SettingsViewModel.getViewModelFactory(
            CreatorSharing.provideSettingsInteractor(),
            CreatorSharing.provideSharingInteractor()
        )
        viewModel = ViewModelProvider(this, factory)[SettingsViewModel::class.java]
        viewModel.settingsState().observe(this) {
            state ->
            handleSettingsState(state)
        }

        setUi()

        share.setOnClickListener{
            viewModel.shareApp(getString(R.string.messageShare))
        }

        back.setOnClickListener{
            finish()
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


}