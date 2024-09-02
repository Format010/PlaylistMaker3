package com.example.playlistmaker2.sharing.data

import com.example.playlistmaker2.sharing.domain.model.EmailData

interface ExternalNavigator {

    fun shareAppLink(text: String)
    fun openLinkUserAgreement(url: String)
    fun sendMailSupport(email: EmailData)

}