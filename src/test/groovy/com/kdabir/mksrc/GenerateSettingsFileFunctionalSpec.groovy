package com.kdabir.mksrc

import directree.DirTree
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class GenerateSettingsFileFunctionalSpec extends Specification {

    @Rule
    TemporaryFolder projectDir = new TemporaryFolder()
    File testProject

    def setup() {
        testProject = projectDir.newFolder("test-project")
    }

    def teardown() {
        testProject.deleteDir()
    }

    def "generate settings.gradle"() {
        given: "project without settings.gradle"

        DirTree.create(testProject.absolutePath) {
            file 'build.gradle', """
                plugins {
                    id "com.kdabir.mksrc"
                }
            """.stripIndent()
            dir("subproject1") {
                file "subproject1.gradle", ""
            }
            dir("subproject2") {
                file "subproject2.gradle", ""
            }
            dir("not_a_project") {
                file "readme.txt", "this is not a project dir"
            }
        }

        when: "running the task"
        def result = runTask()

        then:
        def settingsGradleFile = new File(testProject, "settings.gradle")
        def settingsGradleContent = settingsGradleFile.text

        result.task(":generateSettingsFile").getOutcome() == TaskOutcome.SUCCESS
        settingsGradleFile.exists()

        settingsGradleContent.contains GenerateSettingsFileTask.warning
        settingsGradleContent.contains "include(':subproject1')"
        settingsGradleContent.contains "project(':subproject1').setBuildFileName('subproject1.gradle')"

        settingsGradleContent.contains "include(':subproject2')"
        settingsGradleContent.contains "project(':subproject2').setBuildFileName('subproject2.gradle')"
    }

    def "updates settings.gradle"() {
        given: "project wit settings.gradle"

        DirTree.create(testProject.absolutePath) {
            file 'build.gradle', """
                plugins {
                    id "com.kdabir.mksrc"
                }
            """.stripIndent()
            file 'settings.gradle', """
                pluginManagement {
                    // custom content
                }
            """.stripIndent()
            dir("subproject1") {
                file "subproject1.gradle", ""
            }
            dir("not_a_project") {
                file "readme.txt", "this is not a project dir"
            }
        }

        when: "running the task"
        def result = runTask()

        then:
        def settingsGradleFile = new File(testProject, "settings.gradle")
        def settingsGradleContent = settingsGradleFile.text

        result.task(":generateSettingsFile").getOutcome() == TaskOutcome.SUCCESS
        settingsGradleFile.exists()

        settingsGradleContent.contains  "pluginManagement"
        settingsGradleContent.contains  "// custom content"
        settingsGradleContent.contains GenerateSettingsFileTask.warning
        settingsGradleContent.contains "include(':subproject1')"
        settingsGradleContent.contains "project(':subproject1').setBuildFileName('subproject1.gradle')"
    }


    private BuildResult runTask() {
        GradleRunner.create()
            .withProjectDir(testProject)
            .withArguments('generateSettingsFile')
            .withPluginClasspath()
            .build()
    }


}
