package com.example.playlistmaker2.sharing.domain

import com.example.playlistmaker2.sharing.domain.model.EmailData

interface SharingInteractor {
    fun shareApp(shareText: String)
    fun openLinkUserAgreement(url: String)
    fun sendMailSupport(supportTextEmail: EmailData)
}