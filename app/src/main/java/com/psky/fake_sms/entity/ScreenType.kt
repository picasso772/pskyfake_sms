package com.psky.fake_sms.entity

enum class ScreenType {
    splash,
    home,
    result;
}

enum class ScreenHomeType {
    createChat,
    listChat,
    listCall;
}

enum class NotificationError {
    none,
    nameEmpty,
    phoneEmpty,
    phoneNotValid,
    timeOutEmpty,
    imageEmpty
}