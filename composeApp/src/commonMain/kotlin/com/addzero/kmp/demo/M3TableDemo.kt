//package com.addzero.kmp.demo
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import com.addzero.kmp.processor.annotation.Route
//import com.seanproctor.datatable.DataColumn
//import com.seanproctor.datatable.TableColumnWidth
//import com.seanproctor.datatable.material3.PaginatedDataTable
//import com.seanproctor.datatable.paging.rememberPaginatedDataTableState
//
///**
// * 用户数据模型
// */
//data class User(
//    val id: Int,
//    val name: String,
//    val age: Int,
//    val email: String,
//    val status: String
//)
//
///**
// * 表格状态管理
// */
//@Composable
//fun rememberTableState() {
//    var sortColumnIndex by remember { mutableStateOf<Int?>(null) }
//    var sortAscending by remember { mutableStateOf(true) }
//    var selectedRows by remember { mutableStateOf(setOf<Int>()) }
//
//    // 模拟数据
//    val users = remember {
//        List(100) { index ->
//            User(
//                id = index + 1,
//                name = "User ${index + 1}",
//                age = 20 + (index % 40),
//                email = "user${index + 1}@example.com",
//                status = if (index % 2 == 0) "Active" else "Inactive"
//            )
//        }
//    }
//
//    // 分页状态
//    val paginatedState = rememberPaginatedDataTableState(
//        initialPageSize = 10,
//        initialPageIndex = 0,
//        initialCount = users.size
//    )
//
//    // 排序后的数据
//    val sortedUsers = remember(users, sortColumnIndex, sortAscending) {
//        when (sortColumnIndex) {
//            0 -> users.sortedBy { it.id }
//            1 -> users.sortedBy { it.name }
//            2 -> users.sortedBy { it.age }
//            3 -> users.sortedBy { it.email }
//            4 -> users.sortedBy { it.status }
//            else -> users
//        }.let { if (!sortAscending) it.reversed() else it }
//    }
//
//    // 当前页数据
//    val currentPageData = remember(sortedUsers, paginatedState.pageIndex, paginatedState.pageSize) {
//        val start = paginatedState.pageIndex * paginatedState.pageSize
//        sortedUsers.drop(start).take(paginatedState.pageSize)
//    }
//
//    // 处理排序
//    val handleSort = { columnIndex: Int, ascending: Boolean ->
//        sortColumnIndex = columnIndex
//        sortAscending = ascending
//    }
//
//    // 表格列定义
//    val columns = listOf(
//        // ID列
//        DataColumn(
////            alignment = Alignment.Start,
//            width = TableColumnWidth.Fixed(80.dp),
////            onSort = { ascending -> handleSort(0, ascending) },
//            isSortIconTrailing = true
//        ) {
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.spacedBy(4.dp)
//            ) {
//                Text("ID")
//                if (sortColumnIndex == 0) {
//                    Icon(
//                        if (sortAscending) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
//                        contentDescription = "Sort"
//                    )
//                }
//            }
//        },
//
//        // 名称列
//        DataColumn(
////            alignment = Alignment.Start,
//            width = TableColumnWidth.Flex(2f),
////            onSort = { ascending -> handleSort(1, ascending) },
//            isSortIconTrailing = true
//        ) {
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.spacedBy(4.dp)
//            ) {
//                Text("Name")
//                if (sortColumnIndex == 1) {
//                    Icon(
//                        if (sortAscending) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
//                        contentDescription = "Sort"
//                    )
//                }
//            }
//        },
//
//        // 年龄列
//        DataColumn(
//            alignment = Alignment.Center,
//            width = TableColumnWidth.Fixed(80.dp),
////            onSort = { ascending -> handleSort(2, ascending) },
//            isSortIconTrailing = true
//        ) {
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.spacedBy(4.dp)
//            ) {
//                Text("Age")
//                if (sortColumnIndex == 2) {
//                    Icon(
//                        if (sortAscending) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
//                        contentDescription = "Sort"
//                    )
//                }
//            }
//        },
//
//        // 邮箱列
//        DataColumn(
////            alignment = Alignment.Start,
//            width = TableColumnWidth.Flex(2f),
////            onSort = { ascending -> handleSort(3, ascending) },
//            isSortIconTrailing = true
//        ) {
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.spacedBy(4.dp)
//            ) {
//                Text("Email")
//                if (sortColumnIndex == 3) {
//                    Icon(
//                        if (sortAscending) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
//                        contentDescription = "Sort"
//                    )
//                }
//            }
//        },
//
//        // 状态列
//        DataColumn(
//            alignment = Alignment.Center,
//            width = TableColumnWidth.Fixed(100.dp),
////            onSort = { ascending -> handleSort(4, ascending) },
//            isSortIconTrailing = true
//        ) {
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.spacedBy(4.dp)
//            ) {
//                Text("Status")
//                if (sortColumnIndex == 4) {
//                    Icon(
//                        if (sortAscending) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
//                        contentDescription = "Sort"
//                    )
//                }
//            }
//        }
//    )
//
//    // 表格组件
//    PaginatedDataTable(
//        columns = columns,
//        state = paginatedState,
//        sortColumnIndex = sortColumnIndex,
//        sortAscending = sortAscending,
//        modifier = Modifier.fillMaxSize(),
//        rowBackgroundColor = { index ->
//            if (selectedRows.contains(currentPageData[index].id)) {
//                MaterialTheme.colorScheme.primaryContainer
//            } else {
//                MaterialTheme.colorScheme.surface
//            }
//        }
//    ) {
//        currentPageData.forEach { user ->
//            row {
//                onClick = {
//                    selectedRows = if (selectedRows.contains(user.id)) {
//                        selectedRows - user.id
//                    } else {
//                        selectedRows + user.id
//                    }
//                }
//
//                cell {
//                    Text(
//                        text = user.id.toString(),
//                        modifier = Modifier.fillMaxWidth(),
//                        textAlign = TextAlign.Start
//                    )
//                }
//                cell {
//                    Text(
//                        text = user.name,
//                        modifier = Modifier.fillMaxWidth(),
//                        textAlign = TextAlign.Start
//                    )
//                }
//                cell {
//                    Text(
//                        text = user.age.toString(),
//                        modifier = Modifier.fillMaxWidth(),
//                        textAlign = TextAlign.Center
//                    )
//                }
//                cell {
//                    Text(
//                        text = user.email,
//                        modifier = Modifier.fillMaxWidth(),
//                        textAlign = TextAlign.Start
//                    )
//                }
//                cell {
//                    Text(
//                        text = user.status,
//                        modifier = Modifier.fillMaxWidth(),
//                        textAlign = TextAlign.Center,
//                        color = if (user.status == "Active")
//                            MaterialTheme.colorScheme.primary
//                        else
//                            MaterialTheme.colorScheme.error
//                    )
//                }
//            }
//        }
//    }
//
//    // 工具栏
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp),
//        horizontalArrangement = Arrangement.SpaceBetween,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Text(
//            text = "Selected: ${selectedRows.size} items",
//            style = MaterialTheme.typography.bodyLarge
//        )
//
//        Row(
//            horizontalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            Button(
//                onClick = { selectedRows = emptySet() },
//                enabled = selectedRows.isNotEmpty()
//            ) {
//                Icon(Icons.Default.Clear, contentDescription = "Clear Selection")
//                Spacer(Modifier.width(4.dp))
//                Text("Clear Selection")
//            }
//
//            Button(
//                onClick = { /* 处理删除操作 */ },
//                enabled = selectedRows.isNotEmpty(),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = MaterialTheme.colorScheme.error
//                )
//            ) {
//                Icon(Icons.Default.Delete, contentDescription = "Delete Selected")
//                Spacer(Modifier.width(4.dp))
//                Text("Delete Selected")
//            }
//        }
//    }
//}
//
//@Composable
//@Route
//fun M3TableDemo() {
//    Surface(
//        modifier = Modifier.fillMaxSize(),
//        color = MaterialTheme.colorScheme.background
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp)
//        ) {
//            Text(
//                text = "Advanced Data Table Demo",
//                style = MaterialTheme.typography.headlineMedium,
//                modifier = Modifier.padding(bottom = 16.dp)
//            )
//
//            rememberTableState()
//        }
//    }
//}
//
//@Composable
//@Route
//fun M3PageTablej(): Unit {
//    PaginatedDataTable(
//        columns = listOf(
//            DataColumn {
//                Text("Column1")
//            },
//            DataColumn {
//                Text("Column2")
//            },
//            DataColumn {
//                Text("Column3")
//            },
//        ),
//        state = rememberPaginatedDataTableState(5),
//    ) {
//        for (rowIndex in 0 until 100) {
//            row {
//                onClick = { println("Row clicked: $rowIndex") }
//                cell {
//                    Text("Row $rowIndex, column 1")
//                }
//                cell {
//                    Text("Row $rowIndex, column 2")
//                }
//                cell {
//                    Text("Row $rowIndex, column 3")
//                }
//            }
//        }
//    }
//
//}
