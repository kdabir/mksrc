package com.kdabir.mksrc

import org.gradle.api.Plugin
import org.gradle.api.Project

class MakeSourceDirsPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.tasks.create('makeSourceDirs', MakeSourceDirsTask)
        project.tasks.create('generateSettingsFile', GenerateSettingsFileTask)
    }
}
