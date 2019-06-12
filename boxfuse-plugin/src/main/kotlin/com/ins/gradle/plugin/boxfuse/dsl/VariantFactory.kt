package com.ins.gradle.plugin.boxfuse.dsl

import com.ins.gradle.plugin.boxfuse.Variant
import org.gradle.api.NamedDomainObjectFactory
import org.gradle.internal.reflect.Instantiator

class VariantFactory  constructor(val instantiator: Instantiator) :
    NamedDomainObjectFactory<Variant> {

    override fun create( name: String) : Variant {
        return instantiator.newInstance(Variant::class.java, name)
    }
}