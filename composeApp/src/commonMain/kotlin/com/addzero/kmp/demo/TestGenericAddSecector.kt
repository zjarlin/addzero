package com.addzero.kmp.demo

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import com.addzero.kmp.annotation.Route
import com.addzero.kmp.component.form.AddGenericSelector
import com.addzero.kmp.component.form.AddGenericSingleSelector
import com.addzero.kmp.form_mapping.Iso2DataProvider
import com.addzero.kmp.isomorphic.SysDeptIso


@Composable
@Route
fun daoisjdoiajsdoajsod(): Unit {
    val isoToDataProvider = Iso2DataProvider.isoToDataProvider

    var secectedDepts by remember {
        mutableStateOf(emptyList<SysDeptIso>())
    }
    var secectedDept by remember { mutableStateOf(null as SysDeptIso?) }

    val dataProviderFunction = isoToDataProvider[SysDeptIso::class]
    Column {
        AddGenericSingleSelector(
            value = secectedDept,
            onValueChange = { secectedDept = it as SysDeptIso? },
            dataProvider = {
                if (dataProviderFunction == null) {
                    emptyList()
                } else {
                    dataProviderFunction().invoke("") as List<SysDeptIso>
                }
            },
            getId = { it.id!! },
            getLabel = { it.name },
            { it.children }
        )
        Text("==============================")

        AddGenericSelector(
            value = secectedDepts,
            onValueChange = { secectedDepts = it as List<SysDeptIso>  },
            dataProvider = {
                if (dataProviderFunction == null) {
                    emptyList()
                } else {
                    dataProviderFunction().invoke("") as List<SysDeptIso>
                }
            },
            getId = { it.id!! },
            getLabel = { it.name },
            { it.children }
        )

    }


}
