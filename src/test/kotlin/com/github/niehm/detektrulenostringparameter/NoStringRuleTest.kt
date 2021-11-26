package com.github.niehm.detektrulenostringparameter

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Finding
import io.gitlab.arturbosch.detekt.test.compileAndLint
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.groups.Tuple.tuple
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class NoStringRuleTest {

    @ValueSource(
        strings = [CONSTRUCTOR, TOP_LEVEL_FUNCTION, METHOD, MULTI_PARAM_CONSTRUCTOR,
            MULTI_PARAM_TOP_LEVEL_FUNCTION, EXTENSION_FUNCTION]
    )
    @ParameterizedTest
    internal fun `reports parameter`(code: String) {
        val findings = NoStringParameterRule(Config.empty).compileAndLint(code)
        assertThat(findings)
            .hasSize(1)
            .extracting(Finding::message)
            .containsExactly(tuple("No string parameters."))
    }

    @ValueSource(
        strings = [PUBLIC_PROPERTY, INTERNAL_PROPERTY, PROTECTED_PROPERTY]
    )
    @ParameterizedTest
    internal fun `reports visible properties`(code: String) {
        val findings = NoStringParameterRule(Config.empty).compileAndLint(code)
        assertThat(findings)
            .hasSize(1)
            .extracting(Finding::message)
            .containsExactly(tuple("No non-private string properties."))
    }

    @Test
    internal fun `reports multiple occurrences`() {
        val code = """
            class MyClass(val constructorProperty: String) {
                fun test(something: String, something2: String) {}
            }
        """
        val findings = NoStringParameterRule(Config.empty).compileAndLint(code)

        assertThat(findings)
            .hasSize(3)
            .extracting(Finding::message)
            .containsExactly(
                tuple("No string parameters."),
                tuple("No string parameters."),
                tuple("No string parameters.")
            )
    }

    @ValueSource(
        strings = [TYPE_ALIAS, NO_STRING_PARAMETER, GENERIC_TYPE, LOCAL_VARIABLES, PRIVATE_PROPERTY]
    )
    @ParameterizedTest
    internal fun `does not report`(code: String) {
        val findings = NoStringParameterRule(Config.empty).compileAndLint(code)
        assertThat(findings).isEmpty()
    }
}

@Language("kotlin")
private const val CONSTRUCTOR = """
    class MyClass(val constructorProperty: String)
"""

@Language("kotlin")
private const val MULTI_PARAM_CONSTRUCTOR = """
    class MyClass(val number: Int, val constructorProperty: String)
"""

@Language("kotlin")
private const val TOP_LEVEL_FUNCTION = """
    fun test(something: String) {}
"""

@Language("kotlin")
private const val MULTI_PARAM_TOP_LEVEL_FUNCTION = """
    fun test(number: Int, something: String) {}
"""

@Language("kotlin")
private const val METHOD = """
    class MyClass {
        fun test(something: String) {}
    }
"""

@Language("kotlin")
private const val EXTENSION_FUNCTION = """
    fun Test.test(something: String) {}
"""

@Language("kotlin")
private const val TYPE_ALIAS = """
    typealias MyType = String
    fun test(something: MyType) {}
"""

@Language("kotlin")
private const val NO_STRING_PARAMETER = """
    class MyClass(val constructorProperty: Int) {
        fun test(something: Boolean, something2: Array<Double>) {}
    }
"""

@Language("kotlin")
private const val GENERIC_TYPE = """
    class MyClass(val constructorProperty: List<String>)
"""

@Language("kotlin")
private const val LOCAL_VARIABLES = """
    fun test() {
        val test = "Something"
    }
"""

@Language("kotlin")
private const val PRIVATE_PROPERTY = """
    class MyClass {
        private val test: String = "Something"
    }
"""

@Language("kotlin")
private const val PUBLIC_PROPERTY = """
    class MyClass {
        val test: String = "Something"
    }
"""

@Language("kotlin")
private const val INTERNAL_PROPERTY = """
    class MyClass {
        internal val test: String = "Something"
    }
"""

@Language("kotlin")
private const val PROTECTED_PROPERTY = """
    class MyClass {
        protected val test: String = "Something"
    }
"""

