package com.addzero.kmp.component.assist_fun

import androidx.compose.runtime.Composable
import com.addzero.kmp.ui.infra.model.navigation.RecentTabsManager
import org.koin.compose.viewmodel.koinViewModel

object RecentTableCache {
   lateinit var viewModel: RecentTabsManager

    fun init(model: RecentTabsManager) {
        viewModel = model
    }

    @Composable
    fun cleanViewModel() {
        viewModel = koinViewModel<RecentTabsManager>()
    }

}
