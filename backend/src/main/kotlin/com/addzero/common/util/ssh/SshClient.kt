//import com.jcraft.jsch.*
//import java.io.ByteArrayOutputStream
//import java.io.File
//import java.io.FileNotFoundException
//import java.util.*
//
//fun main() {
//    val sshClient = SshClient(
//        host = "82.157.77.120",
//        username = "root",
//        password = "Zhou9955"
//    )
//
//    try {
//        sshClient.use { client ->
//            // 执行命令并打印结果
////            val result = client.executeCommand("ls -la")
////            println("Command output:\n$result")
////
////            // 执行多个命令
//            val pwdResult = client.executeCommand("pwd")
//            println("Current directory: $pwdResult")
////
////            val whoamiResult = client.executeCommand("whoami")
////            println("Current user: $whoamiResult")
////            client.downloadFile("/root/.add_fn", "~/Desktop/test/")
//            client.uploadFile("~/Desktop/test/", "/root/test")
//        }
//    } catch (e: Exception) {
//        println("Error occurred: ${e.message}")
//        e.printStackTrace()
//    }
//}
//
//class SshClient(
//    private val host: String,
//    private val username: String,
//    private val password: String? = null,
//    private val privateKeyPath: String? = null,
//    private val port: Int = 22
//) {
//    private val jsch = JSch()
//    private var session: Session? = null
//
//    init {
//        privateKeyPath?.let {
//            try {
//                jsch.addIdentity(it)
//                println("SSH: Successfully loaded private key from $it")
//            } catch (e: Exception) {
//                println("SSH: Failed to load private key: ${e.message}")
//                throw e
//            }
//        }
//    }
//
//    /**
//     * 连接到服务器
//     */
//    fun connect() {
//        try {
//            println("SSH: Connecting to $host:$port as $username...")
//
//            session = jsch.getSession(username, host, port).apply {
//                password?.let { setPassword(it) }
//
//                val config = Properties()
//                config["StrictHostKeyChecking"] = "no"
//                setConfig(config)
//
//                timeout = 30000
//
//                connect()
//            }
//
//            println("SSH: Successfully connected to $host")
//        } catch (e: Exception) {
//            println("SSH: Connection failed: ${e.message}")
//            throw e
//        }
//    }
//
//    /**
//     * 执行远程命令
//     * @param command 要执行的命令
//     * @return 命令执行的输出结果
//     */
//    fun executeCommand(command: String): String {
//        println("SSH: Executing command: $command")
//
//        val channel = session?.openChannel("exec") as ChannelExec
//        val outputStream = ByteArrayOutputStream()
//        val errorStream = ByteArrayOutputStream()
//
//        try {
//            channel.apply {
//                setCommand(command)
//                // 重要：不要设置inputStream为null
//                setOutputStream(outputStream)
//                setErrStream(errorStream)
//                connect()
//            }
//
//            // 读取命令输出
//            val buffer = ByteArray(1024)
//            var read: Int
//
//            // 读取标准输出
//            while (true) {
//                read = channel.inputStream.read(buffer)
//                if (read == -1) break
//                outputStream.write(buffer, 0, read)
//            }
//
//            // 等待命令执行完成
//            while (!channel.isClosed) {
//                Thread.sleep(100)
//            }
//
//            val output = outputStream.toString()
//            val error = errorStream.toString()
//
//            val exitStatus = channel.exitStatus
//            println("SSH: Command completed with exit status: $exitStatus")
//
//            if (error.isNotEmpty()) {
//                println("SSH: Command error output: $error")
//            }
//
//
//            return if (exitStatus != 0) {
//                throw RuntimeException("Command failed with exit status $exitStatus: $error")
//            } else {
//                output
//            }
//        } catch (e: Exception) {
//            println("SSH: Command execution failed: ${e.message}")
//            throw e
//        } finally {
//            channel.disconnect()
//            println("SSH: Command channel disconnected")
//        }
//    }
//
//
//    /**
//     * 上传文件或目录到远程服务器
//     * @param localPath 本地路径（文件或目录，支持 ~）
//     * @param remotePath 远程目录路径
//     */
//    fun uploadFile(localPath: String, remotePath: String) {
//        // 展开本地路径中的 ~
//        val expandedLocalPath = localPath.replace("~", System.getProperty("user.home"))
//        val localFile = File(expandedLocalPath)
//
//        if (!localFile.exists()) {
//            throw FileNotFoundException("Local path not found: $expandedLocalPath")
//        }
//
//        // 确保远程路径以 / 结尾
//        val normalizedRemotePath = remotePath.trimEnd('/') + "/"
//        println("SSH: Uploading to remote directory: $normalizedRemotePath")
//
//        val channel = session?.openChannel("sftp") as ChannelSftp
//
//        try {
//            channel.connect()
//
//            // 创建远程目录（包括父目录）
//            try {
//                channel.mkdir(normalizedRemotePath)
//            } catch (e: SftpException) {
//                // 忽略目录已存在的错误
//                if (e.id != 4) {
//                    throw e
//                }
//            }
//
//            if (localFile.isFile) {
//                // 上传单个文件
//                channel.put(expandedLocalPath, normalizedRemotePath + localFile.name)
//            } else if (localFile.isDirectory) {
//                // 递归上传目录内容
//                localFile.walkTopDown().forEach { file ->
//                    if (file != localFile) {  // 跳过根目录
//                        val relativePath = file.relativeTo(localFile).path
//                        val remoteFilePath = normalizedRemotePath + relativePath
//
//                        if (file.isDirectory) {
//                            try {
//                                channel.mkdir(remoteFilePath)
//                            } catch (e: SftpException) {
//                                if (e.id != 4) throw e  // 忽略目录已存在错误
//                            }
//                        } else {
//                            channel.put(file.absolutePath, remoteFilePath)
//                        }
//                    }
//                }
//            }
//        } finally {
//            channel.disconnect()
//        }
//    }
//    /**
//     * 从远程服务器下载文件或目录
//     * @param remotePath 远程路径
//     * @param localPath 本地保存路径（支持 ~）
//     */
//    fun downloadFile(remotePath: String, localPath: String) {
//        // 展开本地路径中的 ~
//        val expandedLocalPath = localPath.replace("~", System.getProperty("user.home"))
//        val localFile = File(expandedLocalPath)
//
//        println("SSH: Downloading from $remotePath to $expandedLocalPath")
//
//        // 确保本地目录存在
//        localFile.parentFile?.mkdirs()
//
//        val channel = session?.openChannel("sftp") as ChannelSftp
//
//        try {
//            channel.connect()
//            println("SSH: SFTP channel connected")
//
//            try {
//                // 尝试作为目录处理
//                val files = channel.ls(remotePath) as Vector<ChannelSftp.LsEntry>
//
//                // 如果能列出文件，说明是目录
//                println("SSH: Remote path is a directory, downloading contents...")
//                localFile.mkdirs() // 确保本地目录存在
//
//                files.forEach { entry ->
//                    if (entry.filename != "." && entry.filename != "..") {
//                        val remoteFilePath = "$remotePath/${entry.filename}"
//                        val localFilePath = "${localFile.absolutePath}/${entry.filename}"
//
//                        if (entry.attrs.isDir) {
//                            // 递归下载子目录
//                            downloadFile(remoteFilePath, localFilePath)
//                        } else {
//                            // 下载文件
//                            channel.get(remoteFilePath, localFilePath)
//                            println("SSH: Downloaded file: ${entry.filename}")
//                        }
//                    }
//                }
//            } catch (e: SftpException) {
//                // 如果不是目录，尝试作为文件下载
//                channel.get(remotePath, expandedLocalPath)
//                println("SSH: Downloaded file successfully")
//            }
//        } catch (e: Exception) {
//            println("SSH: Download failed: ${e.message}")
//            throw e
//        } finally {
//            channel.disconnect()
//            println("SSH: SFTP channel disconnected")
//        }
//    }
//    /**
//     * 关闭连接
//     */
//    fun disconnect() {
//        try {
//            session?.let {
//                if (it.isConnected) {
//                    it.disconnect()
//                    println("SSH: Disconnected from $host")
//                }
//            }
//        } catch (e: Exception) {
//            println("SSH: Error during disconnect: ${e.message}")
//        }
//    }
//
//    /**
//     * 使用 use 函数自动关闭连接
//     */
//    inline fun <T> use(block: (SshClient) -> T): T {
//        try {
//            connect()
//            return block(this)
//        } finally {
//            disconnect()
//        }
//    }
//}
