package com.sportenth.data.database

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.UpdateBuilder

/**
 * PostgreSQL upsert (INSERT ... ON CONFLICT DO UPDATE).
 */
fun <T : Table> T.insertOrUpdate(
    conflictColumn: Column<*>,
    body: T.(UpdateBuilder<Number>) -> Unit
) {
    val insert = InsertOrUpdateStatement(this, conflictColumn, body)
    insert.execute(TransactionManager.current())
}

class InsertOrUpdateStatement<T : Table>(
    table: T,
    private val conflictColumn: Column<*>,
    body: T.(UpdateBuilder<Number>) -> Unit
) : InsertStatement<Number>(table, false) {
    init {
        body(table, this)
    }

    override fun prepareSQL(transaction: Transaction): String {
        val sql = super.prepareSQL(transaction)
        val updatePart = values.entries.joinToString(", ") { (col, value) ->
            "${transaction.identity(col)} = EXCLUDED.${transaction.identity(col)}"
        }
        return "$sql ON CONFLICT (${transaction.identity(conflictColumn)}) DO UPDATE SET $updatePart"
    }
}
