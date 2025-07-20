package com.addzero.common.util.unuse

import java.io.File
import java.io.FileWriter
import java.net.URLClassLoader
import javax.tools.ToolProvider

// Define a custom ClassLoader to load the class from byte array
object InMemoryClassLoader /*extends ClassLoader*/ {
    //    private final Map<String, byte[]> classes = new HashMap<>();
    //    public void addClass(String name, byte[] byteCode) {
    //        classes.put(name, byteCode);
    //    }
    //
    //    @Override
    //    protected Class<?> findClass(String name) throws ClassNotFoundException {
    //        byte[] byteCode = classes.get(name);
    //        if (byteCode == null) {
    //            throw new ClassNotFoundException(name);
    //        }
    //        return defineClass(name, byteCode, 0, byteCode.length);
    //    }
    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        // Step 1: Source code
        val sourceCode = "public class DynamicClass { public String greet() { return \"Hello, World!\"; } }"

        // Step 2: Write source to file
        val sourceFile = File("DynamicClass.java")
        FileWriter(sourceFile).use { writer ->
            writer.write(sourceCode)
        }
        // Step 3: Compile source file
        val compiler = ToolProvider.getSystemJavaCompiler()
        compiler.run(null, null, null, sourceFile.path)

        // Step 4: Load compiled class
        val classLoader = URLClassLoader.newInstance(arrayOf(File("").toURI().toURL()))
        val dynamicClass = classLoader.loadClass("DynamicClass")

        // Step 5: Use the loaded class
        val instance = dynamicClass.getDeclaredConstructor().newInstance()
        val greet = dynamicClass.getMethod("greet").invoke(instance)
        println(greet) // Output: Hello, World!
    }
}