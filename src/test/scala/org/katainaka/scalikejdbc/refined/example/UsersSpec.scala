package org.katainaka.scalikejdbc.refined.example

import eu.timepit.refined.auto._
import org.scalatest._
import scalikejdbc._
import scalikejdbc.scalatest.AutoRollback
import org.katainaka.scalikejdbc.refined._

class UsersSpec extends fixture.FlatSpec with Matchers with BeforeAndAfterAll with AutoRollback {

  override protected def beforeAll(): Unit = {
    Class.forName("org.h2.Driver")
    ConnectionPool.singleton("jdbc:h2:mem:test;INIT=CREATE SCHEMA IF NOT EXISTS TEST\\;SET SCHEMA TEST;",
                             "user",
                             "pass")

    DB.autoCommit { implicit session =>
      sql"""
        CREATE TABLE USERS (
        id SERIAL NOT NULL PRIMARY KEY,
        name VARCHAR(64),
        age INT,
        )
      """.execute.apply()

      sql"""
        INSERT INTO USERS(id, name, age) VALUES (1, 'testuser', 20)
      """.execute().apply()
    }
  }

  val u = Users.syntax("u")

  behavior of "Users"

  it should "find by primary keys" in { implicit session =>
    val maybeFound = Users.find(1L)
    maybeFound.isDefined should be(true)
  }
  it should "find by where clauses" in { implicit session =>
    val maybeFound = Users.findBy(sqls.eq(u.id, 1L))
    maybeFound.isDefined should be(true)
  }
  it should "find all records" in { implicit session =>
    val allResults = Users.findAll()
    allResults.size should be > (0)
  }
  it should "count all records" in { implicit session =>
    val count = Users.countAll()
    count should be > (0L)
  }
  it should "find all by where clauses" in { implicit session =>
    val results = Users.findAllBy(sqls.eq(u.id, 1L))
    results.size should be > (0)
  }
  it should "count by where clauses" in { implicit session =>
    val count = Users.countBy(sqls.eq(u.id, 1L))
    count should be > (0L)
  }
  it should "create new record" in { implicit session =>
    val created = Users.create(name = "MyString", age = 123)
    created should not be (null)
  }
  it should "save a record" in { implicit session =>
    val entity = Users.findAll().head
    val modified = entity.copy(name = "testuserrefined")
    val updated = Users.save(modified)
    updated should not equal (entity)
  }
  it should "destroy a record" in { implicit session =>
    val entity = Users.findAll().head
    val deleted = Users.destroy(entity)
    deleted should be(1)
    val shouldBeNone = Users.find(1L)
    shouldBeNone.isDefined should be(false)
  }
  it should "perform batch insert" in { implicit session =>
    val entities = Users.findAll()
    entities.foreach(e => Users.destroy(e))
    val batchInserted = Users.batchInsert(entities)
    batchInserted.size should be > (0)
  }
}
