package com.ins.gradle.plugin.boxfuse

import org.gradle.api.tasks.Input
import java.io.Serializable
import javax.inject.Inject

open class Variant  {

    @Input var name: String? = null
    @Input
    private var enabled: Double? = null

    fun getDecimal(): Double? {
        return enabled
    }

    fun setDecimal(enabled: Double?) {
        this.enabled = enabled
    }

}