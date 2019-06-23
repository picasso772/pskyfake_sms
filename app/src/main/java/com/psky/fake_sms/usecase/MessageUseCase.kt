package com.psky.fake_sms.usecase

import com.psky.fake_sms.service.SingletonService

class MessageUseCase {
    companion object {
        val shared = SingletonService.get(MessageUseCase::class)
    }


}