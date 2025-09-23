import com.vanniktech.maven.publish.MavenPublishBaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.kotlin.dsl.configure
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Publish Convention Plugin
 *
 * 该插件提供了一种简便的方式来配置Maven发布，只需提供几个参数即可：
 * - projectDescription: 项目描述
 * - gitUrl: Git仓库URL
 * - licenseName: 许可证名称
 * - licenseUrl: 许可证URL
 * - developerEmail: 开发者邮箱
 *
 * 使用示例:
 * ```
 * publishConfig {
 *     projectDescription = "你的项目描述"
 *     gitUrl = "https://github.com/user/repo.git"
 *     licenseName = "The Apache License, Version 2.0"
 *     licenseUrl = "http://www.apache.org/licenses/LICENSE-2.0.txt"
 *     developerEmail = "developer@example.com"
 * }
 * ```
 */
class PublishConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        // 应用Maven Publish插件
        project.pluginManager.apply("com.vanniktech.maven.publish")

        // 创建扩展配置
        val extension = PublishPluginExtension.create(project)
        // 配置Maven发布
        val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
        val currentDate = dateFormat.format(Date())
        
        // 计算当天发布次数
        val publishCount = getPublishCount(project, currentDate)
        val version = "$currentDate.$publishCount"
        
        println("项目版本为:::$version")
        
        project.extensions.configure<MavenPublishBaseExtension> {
            publishToMavenCentral(true)
            signAllPublications()
            coordinates(
                project.group.toString(), project.name, version
            )

            pom {
                name.set(project.name)
                description.set(extension.projectDescription)
                inceptionYear.set(project.provider { Calendar.getInstance().get(Calendar.YEAR).toString() })

                // 从gitUrl解析相关参数
                val gitBaseUrl = extension.gitUrl.map { it.removeSuffix(".git") }
                val gitHost = extension.gitUrl.map { it.substringAfter("://").substringBefore("/") }
                val gitRepoName =
                    extension.gitUrl.map { it.substringAfter("://").substringAfter("/").removeSuffix(".git") }

                url.set(gitBaseUrl)

                licenses {
                    license {
                        name.set(extension.licenseName)
                        url.set(extension.licenseUrl)
                        distribution.set(extension.licenseUrl)
                    }
                }

                developers {
                    developer {
                        // 从邮箱提取用户名作为开发者ID和名称
                        val authName = extension.developerEmail.map { it.substringBefore("@") }
                        id.set(authName)
                        name.set(authName)
                        email.set(extension.developerEmail)
                    }
                }

                scm {
                    connection.set(gitHost.zip(gitRepoName) { host, repo -> "scm:git:git://$host/$repo.git" })
                    developerConnection.set(gitHost.zip(gitRepoName) { host, repo -> "scm:git:ssh://$host/$repo.git" })
                    url.set(gitBaseUrl)
                }
            }
        }
        
        // 发布完成后打印工件信息
        project.afterEvaluate {
            project.gradle.taskGraph.whenReady {
                project.tasks.matching { it.name.startsWith("publish") }.forEach { task ->
                    task.doLast {
                        printArtifactInfo(project, version)
                    }
                }
            }
        }
    }
    
    /**
     * 获取当天发布次数
     */
    private fun getPublishCount(project: Project, currentDate: String): Int {
        val publishLogFile = File(project.rootDir, ".publish-log")
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        
        // 如果日志文件不存在，创建它
        if (!publishLogFile.exists()) {
            publishLogFile.createNewFile()
            publishLogFile.writeText("$today,1\n")
            return 1
        }
        
        // 读取日志文件
        val lines = publishLogFile.readLines()
        if (lines.isEmpty()) {
            publishLogFile.writeText("$today,1\n")
            return 1
        }
        
        // 查找今天的记录
        val todayLine = lines.find { it.startsWith("$today,") }
        return if (todayLine != null) {
            // 今天已有发布记录，次数加1
            val count = todayLine.substringAfter(",").toInt() + 1
            // 更新日志文件
            val updatedLines = lines.map { line ->
                if (line.startsWith("$today,")) "$today,$count" else line
            }
            publishLogFile.writeText(updatedLines.joinToString("\n") + "\n")
            count
        } else {
            // 今天没有发布记录，新增一条
            publishLogFile.appendText("$today,1\n")
            1
        }
    }
    
    /**
     * 打印工件信息
     */
    private fun printArtifactInfo(project: Project, version: String) {
        val group = project.group.toString()
        val artifactId = project.name
        
        println("🚀 发布完成！")
        println("📦 工件信息:")
        println("   组: $group")
        println("   工件ID: $artifactId")
        println("   版本: $version")
        println("   完整依赖声明 (Groovy):")
        println("   implementation '$group:$artifactId:$version'")
        println("   完整依赖声明 (Kotlin DSL):")
        println("   implementation(\"$group:$artifactId:$version\")")
    }
}