package ar.edu.itba.pam.nearchatter.dao

import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import ar.edu.itba.pam.nearchatter.db.room.NearchatterDb
import ar.edu.itba.pam.nearchatter.db.room.user.UserDao
import ar.edu.itba.pam.nearchatter.db.room.user.UserEntity
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserDaoTest : TestCase() {
  private lateinit var userDao: UserDao
  private lateinit var db: NearchatterDb
  private lateinit var dbWrite: SupportSQLiteDatabase

  @Before
  public override fun setUp() {
    db = Room.inMemoryDatabaseBuilder(
      ApplicationProvider.getApplicationContext(),
      NearchatterDb::class.java,
    ).allowMainThreadQueries().build()

    dbWrite = db.openHelper.writableDatabase

    userDao = db.userDao()
  }

  @After
  fun closeDb() {
    db.close()
  }

  @Test
  fun getUsernameById() = runBlocking {
    db.runInTransaction {
      dbWrite.execSQL("INSERT INTO users (user_id, username) VALUES ('1', 'user1');")
      dbWrite.execSQL("INSERT INTO users (user_id, username) VALUES ('2', 'user2');")
    }

    assert(userDao.getUsernameById("1") == "user1")
    assert(userDao.getUsernameById("2") == "user2")
    assert(userDao.getUsernameById("3") == null)
  }

  @Test
  fun insertOrUpdateUsernameInsert() = runBlocking {
    userDao.insertOrUpdateUsername(UserEntity("1", "user1", null))
    assert(DaoUtils.queryFirst(db, "SELECT COUNT(*) FROM users").getInt(0) == 1)
  }

  @Test
  fun insertOrUpdateUsernameUpdateExistent() = runBlocking {
    dbWrite.execSQL("INSERT INTO users (user_id, username) VALUES ('1', 'user1')")
    assert(DaoUtils.queryFirst(db, "SELECT COUNT(*) FROM users").getInt(0) == 1)
    userDao.insertOrUpdateUsername(UserEntity("1", "user11", null))
    assert(DaoUtils.queryFirst(db, "SELECT COUNT(*) FROM users").getInt(0) == 1)
    assert(DaoUtils.queryFirst(db, "SELECT username FROM users WHERE user_id = '1'").getString(0) == "user11")
  }

  @Test
  fun insertOrUpdateUsernameUpdateNew() = runBlocking {
    userDao.insertOrUpdateUsername(UserEntity("1", "user1", null))
    assert(DaoUtils.queryFirst(db, "SELECT COUNT(*) FROM users").getInt(0) == 1)
    assert(DaoUtils.queryFirst(db, "SELECT username FROM users WHERE user_id = '1'").getString(0) == "user1")
    userDao.insertOrUpdateUsername(UserEntity("1", "user11", null))
    assert(DaoUtils.queryFirst(db, "SELECT COUNT(*) FROM users").getInt(0) == 1)
    assert(DaoUtils.queryFirst(db, "SELECT username FROM users WHERE user_id = '1'").getString(0) == "user11")
  }

  @Test
  fun updateUserNonExistent() = runBlocking {
    userDao.updateUser("1", "user11")
    assert(DaoUtils.queryFirst(db, "SELECT COUNT(*) FROM users").getInt(0) == 0)
  }

  @Test
  fun updateUserExistent() = runBlocking {
    dbWrite.execSQL("INSERT INTO users (user_id, username) VALUES ('1', 'user1')")
    userDao.updateUser("1", "user11")
    assert(DaoUtils.queryFirst(db, "SELECT username FROM users WHERE user_id = '1'").getString(0) == "user11")
  }

  @Test
  fun setLastMessage() = runBlocking {
    db.runInTransaction {
      dbWrite.execSQL("INSERT INTO users (user_id, username) VALUES ('1', 'user1')")
      dbWrite.execSQL("INSERT INTO users (user_id, username) VALUES ('2', 'user2')")
      dbWrite.execSQL("INSERT INTO users (user_id, username) VALUES ('3', 'user3')")
    }

    dbWrite.execSQL("INSERT INTO messages (message_id, sender_id, receiver_id, payload, send_at) VALUES ('1', '1', '2', 'hola', 1)")
    userDao.setLastMessage("1", "2", "1")

    assert(DaoUtils.queryFirst(db, "SELECT last_message_id FROM users WHERE user_id = '1'").getString(0) == "1")
    assert(DaoUtils.queryFirst(db, "SELECT last_message_id FROM users WHERE user_id = '2'").getString(0) == "1")

    dbWrite.execSQL("INSERT INTO messages (message_id, sender_id, receiver_id, payload, send_at) VALUES ('2', '2', '3', 'chau', 1)")
    userDao.setLastMessage("2", "3", "2")
    assert(DaoUtils.queryFirst(db, "SELECT last_message_id FROM users WHERE user_id = '2'").getString(0) == "2")
    assert(DaoUtils.queryFirst(db, "SELECT last_message_id FROM users WHERE user_id = '3'").getString(0) == "2")
  }
}
