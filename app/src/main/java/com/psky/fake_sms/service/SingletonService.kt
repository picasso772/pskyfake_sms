package com.psky.fake_sms.service

import kotlin.reflect.KClass

object SingletonService {
    var map: MutableMap<KClass<*>, Any> = mutableMapOf()

    fun <T : Any> get(type: KClass<T>): T {
        var instance = map[type] as? T
        if (instance != null) {
            return instance
        }
        instance = type.java.newInstance()
        map[type] = instance
        return instance
    }

    fun remove(type: KClass<*>) {
        map.remove(type)
    }

    fun clear() {
        // TODO : removeAll()
    }
}