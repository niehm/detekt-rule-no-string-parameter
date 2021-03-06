package com.github.niehm.detektrulenostringparameter

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider

class NoStringRuleSetProvider : RuleSetProvider {
    override val ruleSetId: String = "MyRuleSet"

    override fun instance(config: Config): RuleSet {
        return RuleSet(
            ruleSetId,
            listOf(
                NoStringParameterRule(config),
            ),
        )
    }
}
