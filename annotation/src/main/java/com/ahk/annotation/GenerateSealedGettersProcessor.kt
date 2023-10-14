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
        val elements = roundEnv.getElementsAnnotatedWith(GenerateSealedGetters::class.java)
        for (element in elements) {
            val interfaceElement = element as TypeElement
            generateFunctionsForInterface(interfaceElement)
        }
        return true
    }

    private fun generateFunctionsForInterface(interfaceElement: TypeElement) {
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
        writer.println("import ${packageName}.${className};")
        varTypes.iterator().forEach {
            writer.println("import ${it};")
        }

        writer.println("class ${className}Functions {")


        for (element in classNameElements) {
            writer.println("        public static boolean is${element.key}(${className} obj) {")
            writer.println("            return obj instanceof ${className}.${element.key};")
            writer.println("        }")

            for (enclosedElement in element.value) {
                val firstLetter = enclosedElement.simpleName.toString().first().uppercaseChar()
                val simpleName =
                    enclosedElement.simpleName.toString().replaceFirstChar { firstLetter }
                writer.println(
                    "        public static ${
                        enclosedElement.asType().toString()
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

    }
}

/*private fun generateFunctionsForInterface(interfaceElement: TypeElement) {
    val packageName = processingEnv.elementUtils.getPackageOf(interfaceElement).toString()
    val className = interfaceElement.simpleName.toString()
    var classNameElements = emptyMap<String, List<Element>>()
    interfaceElement.enclosedElements
        .map {
            classNameElements =
                classNameElements.plus(it.simpleName.toString() to it.enclosedElements)
        }


    val file = FileSpec.builder(packageName, "${className}Functions")

    file.addType(
        TypeSpec.classBuilder(
            "${className}Functions"
        ).build()
    )

    for (element in classNameElements) {
        if (element.key != "DefaultImpls") {
            file.addFunction(
                FunSpec.builder("is${element.key}")
                    .receiver(interfaceElement.asType().asTypeName())
                    .returns(Boolean::class)
                    .addStatement("return this is ${className}.${element.key}")
                    .build()
            )
        }
        for (enclosedElement in element.value) {
            val firstLetter = enclosedElement.simpleName.toString().first().uppercaseChar()
            val simpleName =
                enclosedElement.simpleName.toString().replaceFirstChar { firstLetter }
            if (enclosedElement.kind == ElementKind.FIELD && enclosedElement.simpleName.toString() != "INSTANCE") {
                /*if (enclosedElement.isPrimitive()) {
                    processingEnv.messager.printMessage(
                        Diagnostic.Kind.NOTE,
                        "${enclosedElement.asType().asTypeName().toString()} Primitive types are not supported"
                    )
                    continue
                }*/

                file.addFunction(
                    FunSpec.builder("get${element.key}$simpleName")
                        .receiver(interfaceElement.asType().asTypeName().getCorrectType())
                        .returns(
                            enclosedElement.asType().asTypeName().getCorrectType()
                                .copy(nullable = true)
                        )
                        .addStatement("return if(this is ${className}.${element.key}) this.${enclosedElement.simpleName} else null")
                        .build()
                )
            }
        }
    }
    file.build().writeTo(processingEnv.filer)
}

private fun TypeName.getCorrectType(): TypeName {
    return when(this.toString()) {
        "java.lang.String" -> ClassName("kotlin", "String")
        "java.lang.Integer" -> ClassName("kotlin", "Int")
        "java.lang.Long" -> ClassName("kotlin", "Long")
        "java.lang.Float" -> ClassName("kotlin", "Float")
        "java.lang.Double" -> ClassName("kotlin", "Double")
        "java.lang.Boolean" -> ClassName("kotlin", "Boolean")
        "java.lang.Byte" -> ClassName("kotlin", "Byte")
        "java.lang.Short" -> ClassName("kotlin", "Short")
        "java.lang.Character" -> ClassName("kotlin", "Char")
        "java.util.List" -> ClassName("kotlin.collections", "List")
        "java.util.ArrayList" -> ClassName("kotlin.collections", "ArrayList")
        "java.util.Map" -> ClassName("kotlin.collections", "Map")
        "java.util.HashMap" -> ClassName("kotlin.collections", "HashMap")
        "java.util.Set" -> ClassName("kotlin.collections", "Set")
        "java.util.HashSet" -> ClassName("kotlin.collections", "HashSet")
        else -> this
    }
}


private fun Element.isPrimitive(): Boolean {
    return this.asType().asTypeName().toString().startsWith("java.util")
            || this.asType().asTypeName().toString().startsWith("kotlin.collections")
            || this.asType().asTypeName().toString().startsWith("kotlin.Array")

}

 */
