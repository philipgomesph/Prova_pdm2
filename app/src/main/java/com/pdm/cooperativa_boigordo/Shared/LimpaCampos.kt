package com.pdm.cooperativa_boigordo.Shared

import androidx.compose.runtime.MutableState
import androidx.compose.ui.text.input.TextFieldValue

public fun limparCampos(
    vararg textFields: MutableState<TextFieldValue>
) {
    textFields.forEach { it.value = TextFieldValue() }
}