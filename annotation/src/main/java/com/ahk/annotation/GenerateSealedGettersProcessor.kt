package com.ahk.annotation

import com.google.auto.service.AutoService
import java.io.PrintWriter
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

var isWorked = false
@AutoService(Processor::class)
class GenerateSealedGettersProcessor : AbstractProcessor() {
    override fun getSupportedAnnotationTypes(): MutableSet<String> {

        return mutableSetOf(GenerateSealedGetters::class.java.name)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latest()
    }

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
    }

    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment
    ): Boolean {
        if (isWorked) {
            return true
        }
        isWorked = true
        processingEnv.messager.printMessage(Diagnostic.Kind.NOTE, "Hello")
        val elements = roundEnv.getElementsAnnotatedWith(GenerateSealedGetters::class.java)
        var writer = writeProviderHeader()
        for (element in elements) {
            val interfaceElement = element as TypeElement
            writer = generateFunctionsForInterface(interfaceElement, writer)
        }
        writeProviderFooter(writer)
        return true
    }

    private fun writeProviderFooter(providerWriter: PrintWriter) {
        providerWriter.println("null, null        );")
        providerWriter.println("}")
        providerWriter.close()
    }

    private fun writeProviderHeader(): PrintWriter {
        val filePath = "com.duhapp.dnotes.FunctionProvider"
        val providerFile = processingEnv.filer.createSourceFile(filePath)
        val providerWriter = PrintWriter(providerFile?.openWriter())
        providerWriter.println("package com.duhapp.dnotes;")
        providerWriter.println("public class FunctionProvider {")
        providerWriter.println("    public static com.duhapp.dnotes.features.base.ui.BaseStateFunctions getFunction(java.lang.reflect.Type functionType) {\n")
        providerWriter.println("        return map.get(functionType);\n")
        providerWriter.println("    }\n")
        providerWriter.println("    public static java.util.Map<java.lang.reflect.Type, com.duhapp.dnotes.features.base.ui.BaseStateFunctions> map = java.util.Map.of(")
        return providerWriter
    }

    private fun generateFunctionsForInterface(
        interfaceElement: TypeElement,
        providerWriter: PrintWriter
    ): PrintWriter {
        val packageName = processingEnv.elementUtils.getPackageOf(interfaceElement).toString()
        val className = interfaceElement.simpleName.toString()
        var classNameElements = emptyMap<String, List<Element>>()
        var varTypes: List<String> = emptyList()


        interfaceElement.enclosedElements
            .map {
                if (it.simpleName.toString() != "DefaultImpls") {
                    var enclosedElements = emptyList<Element>()
                    for (enclosedElement in it.enclosedElements) {
                        if (enclosedElement.kind == ElementKind.FIELD && enclosedElement.simpleName.toString() != "INSTANCE") {
                            varTypes = varTypes.plus(enclosedElement.asType().toString())
                            enclosedElements = enclosedElements.plus(enclosedElement)
                        }
                    }
                    classNameElements =
                        classNameElements.plus(it.simpleName.toString() to enclosedElements)
                }
            }

        val builderFile = processingEnv.filer.createSourceFile("$packageName.${className}Functions")
        val writer = PrintWriter(builderFile.openWriter())

        writer.println("package $packageName;")

        writer.println("public class ${className}Functions extends com.duhapp.dnotes.features.base.ui.BaseStateFunctions {")

        providerWriter.println("            ${packageName}.${className}Functions.class, new ${packageName}.${className}Functions(),")
        for (element in classNameElements) {
            if (element.key != "<init>") {
                writer.println("        public boolean is${element.key}(${className} obj) {")
                writer.println("            return obj instanceof ${className}.${element.key};")
                writer.println("        }")

            }
            for (enclosedElement in element.value) {
                val firstLetter = enclosedElement.simpleName.toString().first().uppercaseChar()
                val simpleName =
                    enclosedElement.simpleName.toString().replaceFirstChar { firstLetter }
                writer.println(
                    "        public ${
                        enclosedElement.asType()
                    } get${element.key}$simpleName(${className} obj) {"
                )
                writer.println("            if (obj instanceof ${className}.${element.key}) {")
                writer.println("                return ((${className}.${element.key}) obj).get${simpleName}();")
                writer.println("            } else {")
                writer.println("                return null;")
                writer.println("            }")
                writer.println("        }")
            }
        }
        writer.println("}")
        writer.close()
        return providerWriter
    }
}