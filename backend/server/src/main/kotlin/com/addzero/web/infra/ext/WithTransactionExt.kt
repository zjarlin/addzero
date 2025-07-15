package com.addzero.web.infra.ext

import com.addzero.kmp.exp.BizException
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.support.DefaultTransactionDefinition
import org.springframework.transaction.support.TransactionTemplate

@Component
final class TransactionService(private val transactionManager: PlatformTransactionManager) {

    private lateinit var transactionTemplate: TransactionTemplate

    @PostConstruct
    private fun init() {
        transactionTemplate = TransactionTemplate(transactionManager, createTransactionDefinition())
    }

    fun <T> executeInTransaction(block: () -> T): T? {
        return transactionTemplate.execute { status ->
            try {
                block()
            } catch (ex: Exception) {
                status.setRollbackOnly()
                throw ex
            }
        }
    }

    private fun createTransactionDefinition(): DefaultTransactionDefinition {
        return DefaultTransactionDefinition().apply {
            isolationLevel = TransactionDefinition.ISOLATION_READ_COMMITTED
        }
    }

    companion object {
        private lateinit var instance: TransactionService

        fun getInstance(): TransactionService {
            if (!::instance.isInitialized) {
                throw IllegalStateException("TransactionService has not been initialized")
            }
            return instance
        }
    }

    init {
        instance = this
    }
}

inline fun <T> withTransaction(crossinline block: () -> T): T {
    return TransactionService.getInstance().executeInTransaction { block() }
        ?: throw BizException("Transaction service not found")
}