package com.kdabir.mksrc

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class MakeSourceDirsTask extends DefaultTask {

    @TaskAction
    def makeSourceDirs() {
        project.logger.info('creating source dirs...')

        if (project.hasProperty('sourceSets')) { // comes via any language plugin like java, groovy, scala etc
            project.sourceSets*.allSource.srcDirs.flatten()*.mkdirs()

            if (project.hasProperty('webAppDirName')) { // applied by war plugin
                project.file(project.webAppDirName).mkdirs()
            }

        } else {
            project.logger.warn("No sourceSets found. Please apply a language plugin")
        }

    }
}
