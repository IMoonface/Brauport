package com.example.brauportv2.model.brew

data class StepList(
    val sId: Int,
    val rId: Int,
    val steps: List<StepItem>
)
