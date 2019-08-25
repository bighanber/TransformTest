package com.lll.pluginmodule

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class FunctionTimePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        println "===================================="
        println "Hello trans"
        println "===================================="
        def android = project.extensions.findByType(AppExtension.class)
        android.registerTransform(new FunctionTimeTransform(project))
    }
}