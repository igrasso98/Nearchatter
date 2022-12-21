package ar.edu.itba.pam.nearchatter.dao

import android.database.Cursor
import ar.edu.itba.pam.nearchatter.db.room.NearchatterDb

object DaoUtils {
  fun queryFirst(db: NearchatterDb, query: String): Cursor {
    var cursor = db.query(query, null)
    cursor.moveToFirst()
    return cursor
  }
}
