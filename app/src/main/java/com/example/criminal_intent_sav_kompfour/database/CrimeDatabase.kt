package com.example.criminal_intent_sav_kompfour.database

import android.provider.Telephony.Mms.Part.TEXT
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.criminal_intent_sav_kompfour.Crime
import java.sql.Types.NULL

@Database(entities = [ Crime::class ],
    version=2, exportSchema = false)
@TypeConverters(CrimeTypeConverters::class)
abstract class CrimeDatabase : RoomDatabase() {
    abstract fun crimeDao(): CrimeDao
}
val migration_1_2 = object : Migration(1, 2) {
    override fun migrate(database:
                         SupportSQLiteDatabase
    ) {
        database.execSQL(
            "ALTER TABLE Crime ADD COLUMNsuspect TEXT NOT NULL DEFAULT ''"
        )
    }
}