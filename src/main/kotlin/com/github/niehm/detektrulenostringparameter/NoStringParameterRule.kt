package com.github.niehm.detektrulenostringparameter

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import io.gitlab.arturbosch.detekt.rules.isInternal
import org.jetbrains.kotlin.psi.KtParameter
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.KtTypeReference
import org.jetbrains.kotlin.psi.psiUtil.isProtected
import org.jetbrains.kotlin.psi.psiUtil.isPublic

class NoStringParameterRule(config: Config) : Rule(config) {
    override val issue = Issue(
        javaClass.simpleName,
        Severity.CodeSmell,
        "No string parameters or public string properties are allowed.",
        Debt.FIVE_MINS,
    )

    override fun visitParameter(parameter: KtParameter) {
        super.visitParameter(parameter)
        repeat(parameter.children.filterIsInstance<KtTypeReference>().filterStringType().size) {
            report(CodeSmell(issue, Entity.from(parameter), "No string parameters."))
        }
    }

    override fun visitProperty(property: KtProperty) {
        super.visitProperty(property)
        if (property.isPublic || property.isInternal() || property.isProtected()) {
            repeat(property.children.filterIsInstance<KtTypeReference>().filterStringType().size) {
                report(CodeSmell(issue, Entity.from(property), "No non-private string properties."))
            }
        }
    }

    private fun List<KtTypeReference>.filterStringType() =
        filter { it.text == String::class.simpleName }
}
