package com.ins.gradle.plugin.boxfuse

import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.Logging
import org.gradle.api.model.ObjectFactory
import org.gradle.internal.reflect.Instantiator
import javax.inject.Inject


/**
 * Created by Jonathan DA ROS on 11/06/2019.
 */
class BoxfusePlugin @Inject constructor(val instantiator: Instantiator) : Plugin<Project> {

    val log = Logging.getLogger(BoxfusePlugin::class.java.simpleName)

    override fun apply(project: Project) {


        val extension = project.extensions.create("boxFuse", PluginExtension::class.java)

        project.task("reportBoxFuseVariants") {
            it.doLast {
                log.lifecycle("${extension.variant.name} from ${extension.variant.getDecimal()}")
            }
        }

    }

}

open class PluginExtension @javax.inject.Inject constructor(objectFactory: ObjectFactory) {

    val variant : Variant = objectFactory.newInstance(Variant::class.java)

    fun variant(action: Action<in Variant>) {
        action.execute(variant)
    }
}

