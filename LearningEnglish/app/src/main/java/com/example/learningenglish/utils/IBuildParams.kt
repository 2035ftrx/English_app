package com.example.learningenglish.utils
interface IBuildParams {
    fun buildType(): String
}

sealed interface BuildType {
    object Dev : BuildType      // 开发用的
    object Test : BuildType     // 测试用的

    fun isDebug(): Boolean {
        return this is Dev
    }
}

object BuildParams {

    private lateinit var params: IBuildParams

    fun injectionParams(params: IBuildParams) {
        this.params = params
    }


    fun getBuildType(): BuildType {
        return when (params.buildType()) {
            "Dev" -> {
                BuildType.Dev
            }

            "QA" -> {
                BuildType.Test
            }

            else -> {
                BuildType.Test
            }
        }
    }

}