package com.kdabir.mksrc

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class MakeSourceDirsPluginSpec extends Specification {

    Project project = ProjectBuilder.builder().build()

    def setup() {
        project.pluginManager.apply MakeSourceDirsPlugin
    }

    def "applying plugin adds tasks"() {
        expect:
        project.getTasksByName("makeSourceDirs", false).first() instanceof MakeSourceDirsTask
        project.getTasksByName("generateSettingsFile", false).first() instanceof GenerateSettingsFileTask
    }

}
