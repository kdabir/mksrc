package com.kdabir.mksrc

import org.gradle.api.Plugin
import org.gradle.api.Project

class MakeSourceDirsPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.tasks.register('makeSourceDirs', MakeSourceDirsTask) {
            group = "Build Setup"
            description = "Generates source directories for the project."

        }
        project.tasks.register('generateSettingsFile', GenerateSettingsFileTask) {
            group = "Build Setup"
            description = "Generates settings.gradle for the project"
        }
    }
}
