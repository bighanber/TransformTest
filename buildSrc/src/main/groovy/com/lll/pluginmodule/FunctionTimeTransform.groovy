package com.lll.pluginmodule

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformInvocation
import com.android.build.api.transform.TransformOutputProvider
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod
import javassist.bytecode.AnnotationsAttribute
import javassist.bytecode.MethodInfo
import javassist.bytecode.annotation.Annotation
import javassist.bytecode.annotation.StringMemberValue
import org.apache.commons.codec.digest.DigestUtils
import org.gradle.api.Project

import java.util.jar.JarEntry
import java.util.jar.JarFile

class FunctionTimeTransform extends Transform {

    private static final String DEFAULT_NAME = "FunctionTimeTransform"
    private Project mProject

    FunctionTimeTransform(Project project) {
        mProject = project
        println "hhahaha"
    }

    @Override
    String getName() {
        return DEFAULT_NAME
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {

        println "enter transform"
        def inputs = transformInvocation.inputs
        def outputProvider = transformInvocation.outputProvider
        outputProvider.deleteAll()
        def routeJarInput
        for (TransformInput input : inputs) {
            for (DirectoryInput dirInput : input.directoryInputs) {
                def root = dirInput.file.absolutePath
                dirInput.file.eachFileRecurse { File file ->
                    def filePath = file.absolutePath
                    if (!filePath.endsWith(".class")) return
                    def className = getClassName(root, filePath)
                    if (isSystemClass(className)) return
                    insertMethod(root, className)
                }

                File dest = outputProvider.getContentLocation(dirInput.name, dirInput.contentTypes, dirInput.scopes, Format.DIRECTORY)
                FileUtils.copyDirectory(dirInput.file, dest)
            }

            for (JarInput jarInput : input.jarInputs) {
                if (jarInput.name.contains("")) {
                    routeJarInput = jarInput
                }
                JarFile jarFile = new JarFile(jarInput.file)
                Enumeration<JarEntry> enumeration = jarFile.entries()
                while (enumeration.hasMoreElements()) {
                    JarEntry entry = enumeration.nextElement()
                    String entryName = entry.name
                    if (!entryName.endsWith(".class")) continue
                    String classBame = entryName.substring(0, entryName.length() - 6).replaceAll("/", ".")
                    InputStream is = jarFile.getInputStream(entry)
//                    insertMethodWithJar(jarInput, transformInvocation.outputProvider)
                }
                copyFile(jarInput, outputProvider)
            }
        }
    }

    void insertMethod(String filePath, String className) {
//        InsertMethod(new FileInputStream(new File(filePath)), className)
        ClassPool pool = ClassPool.getDefault()
        println("类名字====" + className + "  " + filePath)
        pool.insertClassPath(filePath)
        CtClass ct = pool.get(className)
        CtMethod[] cms = ct.getDeclaredMethods()
        for (CtMethod cm : cms) {
            println "方法名字====" + cm.getName()

            MethodInfo info = cm.getMethodInfo()
            AnnotationsAttribute attr = (AnnotationsAttribute)info.getAttribute(AnnotationsAttribute.invisibleTag)
            println attr
            if (attr != null) {
                Annotation annotation = attr.getAnnotation("com.lll.transformtest.FuncConst")
                println annotation
                if (annotation != null) {
                    String text = ((StringMemberValue)annotation.getMemberValue("value")).getValue()
                    println text
                    cm.insertBefore("System.out.println(\"MethodName: $className - $text; startFuncTime: \" + System.currentTimeMillis());")
                    cm.insertAfter("System.out.println(\"MethodName: $className - $text; endFuncTime: \" + System.currentTimeMillis());")
                }
            }
        }
        ct.writeFile(filePath)
        ct.defrost()
        ct.detach()
    }


    void InsertMethod(InputStream is, String className) {
//        ClassPool pool = ClassPool.getDefault()
//        println("方法名字====" + className)
//        pool.insertClassPath(className)
//        CtClass ct = pool.get(className)
//        CtMethod[] cms = ct.getDeclaredMethods()
//        for (CtMethod cm : cms) {
//            System.out.println("方法名字====" + cm.getName())
//            MethodInfo info = cm.getMethodInfo()
//            AnnotationsAttribute attribute = info.getAttribute(AnnotationsAttribute.visibleTag)
//            System.out.println(attribute)
//
//        }
    }

    void insertMethodWithJar(JarInput jarInput, TransformOutputProvider out) {
//        File jarFile = jarInput.file
//        ClassPool pool = ClassPool.getDefault()
//        println("方法名字====" + jarFile)

    }


    String getClassName(String root, String classPath) {
        return classPath.substring(root.length() + 1, classPath.length() - 6).replaceAll("/", ".")
    }

    //默认排除
    static final DEFAULT_EXCLUDE = [
            '.*\\.R$',
            '.*\\.R\\$.*$',
            '.*\\.BuildConfig$',
    ]

    boolean isSystemClass(String fileName) {
        for (def exclude : DEFAULT_EXCLUDE) {
            if (fileName.matches(exclude)) return true
        }
        return false
    }

    void copyFile(JarInput jarInput, TransformOutputProvider outputProvider) {
        def dest = getDestFile(jarInput, outputProvider)
        FileUtils.copyFile(jarInput.file, dest)
    }

    static File getDestFile(JarInput jarInput, TransformOutputProvider outputProvider) {
        def destName = jarInput.name
        // 重名名输出文件,因为可能同名,会覆盖
        def hexName = DigestUtils.md5Hex(jarInput.file.absolutePath)
        if (destName.endsWith(".jar")) {
            destName = destName.substring(0, destName.length() - 4)
        }
        // 获得输出文件
        File dest = outputProvider.getContentLocation(destName + "_" + hexName, jarInput.contentTypes, jarInput.scopes, Format.JAR)
        return dest
    }
}
