package com.addzero.common.util.bash

interface DockerUtil {
    companion object {
        fun getContainterID(containerName: String): String {
            val containerID =
                CommandExecutor.runAndResult("docker ps  -a | grep $containerName | awk '{print $1}' | head -n 1")
            return containerID
        }

        fun runDockerCmd(containerID: String, cmd: String): String {
            return CommandExecutor.runAndResult("docker exec -i $containerID sh << 'EOF'\n$cmd\nEOF\n")
        }

        fun copyFileToContainer(containerID: String, src: String, tar: String): String {
            val cpCmd = "docker cp $src $containerID:$tar"
            val s2 = CommandExecutor.runAndResult(cpCmd)
            return s2
        }

        fun copyFileFromContainer(containerID: String, src: String, tar: String): String {
            val cpCmd = "docker cp $containerID:$src $tar"
            val s2 = CommandExecutor.runAndResult(cpCmd)
            if (s2.contains("Error response from daemon: Could not find the file")) {
                throw RuntimeException(s2)
            }

            return s2
        }
    }
}