package com.ins.gradle.plugin.boxfuse

import com.boxfuse.client.gradle.task.RunTask
import groovy.lang.Closure
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.internal.reflect.Instantiator
import javax.inject.Inject


/**
 * Created by Jonathan DA ROS on 11/06/2019.
 */
class BoxfusePlugin @Inject constructor(val instantiator: Instantiator) : Plugin<Project> {



    override fun apply(project: Project) {
        // Create the 'greeting' extension

        // Create a container of Book instances
        val persons = project.container(Variant::class.java)

        project.extensions.add("boxFuse", persons)

        persons.whenObjectAdded{ variant ->
            project.tasks.create(
                "boxFuse" +
                        variant.name.capitalize() + variant.env?.capitalize(), RunTask::class.java) {

                it.app= variant.name
                it.env= variant.env

                it.doLast {
                    println("Deployed ${variant.name} to ${variant.env}")
                }
            }
        }

    }


}

open class Variant constructor(var name : String){
    var env : String? = null

    fun env(closure : Closure<String>){
        closure.call(env)
    }
}
open class GreetingPluginExtension @javax.inject.Inject constructor(objectFactory: ObjectFactory) {
    var message: String? = null
    val greeter: Variant = objectFactory.newInstance(Variant::class.java)

    fun greeter(action: Action<in Variant>) {
        action.execute(greeter)
    }
}
