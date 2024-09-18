package com.example.playlistmaker2.sharing.data.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker2.sharing.data.ExternalNavigator
import com.example.playlistmaker2.sharing.domain.model.EmailData

class ExternalNavigatorImpl(private val context: Context): ExternalNavigator {

    override fun shareAppLink(shareText: String) {
        val shareApp = Intent()
        shareApp.action = Intent.ACTION_SEND
        shareApp.type = "text/plain"
        shareApp.putExtra(Intent.EXTRA_TEXT, shareText)
        shareApp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareApp)
    }

    override fun openLinkUserAgreement(link: String) {
        val userAgreementUrl = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        userAgreementUrl.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(userAgreementUrl)
    }

    override fun sendMailSupport(email: EmailData) {
        val supportIntent = Intent(Intent.ACTION_SENDTO)
        supportIntent.data = Uri.parse("mailto:")
        supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email.email))
        supportIntent.putExtra(Intent.EXTRA_SUBJECT, email.subject)
        supportIntent.putExtra(Intent.EXTRA_TEXT, email.message)
        supportIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(supportIntent)
    }


}