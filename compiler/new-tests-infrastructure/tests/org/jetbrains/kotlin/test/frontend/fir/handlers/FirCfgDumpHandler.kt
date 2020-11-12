/*
 * Copyright 2010-2020 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.test.frontend.fir.handlers

import org.jetbrains.kotlin.fir.resolve.dfa.cfg.FirControlFlowGraphRenderVisitor
import org.jetbrains.kotlin.test.directives.DirectivesContainer
import org.jetbrains.kotlin.test.directives.FirDiagnosticsDirectives
import org.jetbrains.kotlin.test.frontend.fir.FirSourceArtifact
import org.jetbrains.kotlin.test.model.TestModule
import org.jetbrains.kotlin.test.services.TestServices

// TODO: adapt to multifile and multimodule tests
class FirCfgDumpHandler(testServices: TestServices) : FirAnalysisHandler(testServices) {
    override val directivesContainers: List<DirectivesContainer>
        get() = listOf(FirDiagnosticsDirectives)

    private val builder = StringBuilder()
    private var alreadyDumped: Boolean = false

    override fun processModule(module: TestModule, info: FirSourceArtifact) {
        if (alreadyDumped || FirDiagnosticsDirectives.dumpCfg !in module.directives) return
        val file = info.firFiles.values.first()
        file.accept(FirControlFlowGraphRenderVisitor(builder))
    }

    override fun processAfterAllModules() {
        if (!alreadyDumped) return
        val testDataFile = moduleStructure.originalTestDataFiles.first()
        val expectedFile = testDataFile.parentFile.resolve("${testDataFile.nameWithoutFirExtension}.dot")
        assertions.assertEqualsToFile(expectedFile, builder.toString())
    }
}
