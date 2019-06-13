package com.ins.gradle.plugin.boxfuse

import com.boxfuse.client.gradle.BoxfuseExtension
import com.boxfuse.client.gradle.task.RunTask
import groovy.lang.Closure
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.internal.reflect.Instantiator
import javax.inject.Inject
import org.gradle.api.tasks.Nested





/**
 * Created by Jonathan DA ROS on 11/06/2019.
 */
class BoxfusePlugin @Inject constructor(val instantiator: Instantiator) : Plugin<Project> {


    private val PLUGIN_EXTENSION_NAME = "boxFuse"

    override fun apply(project: Project) {
        // Create the 'greeting' extension

        // Create a container of Book instances
        val extension = project.extensions.create(PLUGIN_EXTENSION_NAME, PluginExtension::class.java)
        extension.variants = project.container(Variant::class.java)
        extension.config = BoxfuseExtension()

        var boxfuseExtension = project.extensions.findByName("boxfuse") as BoxfuseExtension?



        project.afterEvaluate{
            val ext =project.extensions.findByName("boxFuse") as PluginExtension

            if(boxfuseExtension == null){
                project.extensions.add("boxfuse",ext.config)
            }
            ext.variants?.all{ variant ->

                variant.config = BoxfuseExtension()
                val envs = arrayOf("test","prod", "dev")
                for(env in envs){
                    project.tasks.create(
                        "boxFuse" +
                                variant.name.capitalize() + env?.capitalize(), RunTask::class.java) {task ->

                        task.app= variant.name
                        task.env= env
                        task.payload =  variant.config?.payload?:ext.config?.payload
                        task.secret =  variant.config?.secret?:ext.config?.secret
                        task.user =  variant.config?.user?:ext.config?.user


                        task.doFirst{
                            println("Deploying ${variant.name} to $env with payload : ${task.payload}, user ${task.user} - secret ${task.secret}");
                        }
                        task.doLast {
                            println("${variant.name} is deployed to $env")
                        }
                    }
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
    var config : BoxfuseExtension?= null

    fun config( closure : Closure<BoxfuseExtension>) {
        org.gradle.util.ConfigureUtil.configure(closure, config)
    }

}
open class PluginExtension @javax.inject.Inject constructor() {


    var config : BoxfuseExtension?= null
    var variants : NamedDomainObjectContainer<Variant>?= null
    var payload : String?= null

    fun config( closure : Closure<BoxfuseExtension>) {
        org.gradle.util.ConfigureUtil.configure(closure, config)
    }
//    fun config(action: Action<in BoxfuseExtension?>) {
//        action.execute(config)
//    }

    fun variants( configuration : Closure<Variant>) {
        variants?.configure(configuration)
    }

}
