package com.example.playlistmaker2.sharing.domain.impl

import com.example.playlistmaker2.sharing.data.ExternalNavigator
import com.example.playlistmaker2.sharing.domain.model.EmailData
import com.example.playlistmaker2.sharing.domain.SharingInteractor

class SharingInteractorImpl (private  val externalNavigator: ExternalNavigator,
) : SharingInteractor {

    override fun shareApp(text: String) {
        externalNavigator.shareAppLink(text)
    }

    override fun openLinkUserAgreement(url: String) {
        externalNavigator.openLinkUserAgreement(url)
    }

    override fun sendMailSupport(supportTextEmail: EmailData) {
        externalNavigator.sendMailSupport(supportTextEmail)
    }


}