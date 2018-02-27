package jp.katainaka.scalikejdbc.refined.example

import eu.timepit.refined.api.Refined
import eu.timepit.refined.collection.NonEmpty
import eu.timepit.refined.numeric.Positive
import scalikejdbc._
import jp.katainaka.scalikejdbc.refined._

case class Users(id: Long, name: String Refined NonEmpty, age: Int Refined Positive) {

  def save()(implicit session: DBSession): Users = Users.save(this)(session)

  def destroy()(implicit session: DBSession): Int = Users.destroy(this)(session)

}

object Users extends SQLSyntaxSupport[Users] {

  override val schemaName = Some("TEST")

  override val tableName = "USERS"

  override val columns = Seq("ID", "NAME", "AGE")

  def apply(g: SyntaxProvider[Users])(rs: WrappedResultSet): Users = apply(g.resultName)(rs)
  def apply(g: ResultName[Users])(rs: WrappedResultSet): Users = Users(
    id = rs.get(g.id),
    name = rs.get(g.name),
    age = rs.get(g.age)
  )

  val u = Users.syntax("u")

  override val autoSession = AutoSession

  def find(id: Long)(implicit session: DBSession): Option[Users] = {
    withSQL {
      select.from(Users as u).where.eq(u.id, id)
    }.map(Users(u.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession): List[Users] = {
    withSQL(select.from(Users as u)).map(Users(u.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession): Long = {
    withSQL(select(sqls.count).from(Users as u)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession): Option[Users] = {
    withSQL {
      select.from(Users as u).where.append(where)
    }.map(Users(u.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession): List[Users] = {
    withSQL {
      select.from(Users as u).where.append(where)
    }.map(Users(u.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession): Long = {
    withSQL {
      select(sqls.count).from(Users as u).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(name: String Refined NonEmpty, age: Int Refined Positive)(implicit session: DBSession): Users = {
    val id = withSQL {
      insert
        .into(Users)
        .namedValues(
          column.name -> name,
          column.age -> age
        )
    }.updateAndReturnGeneratedKey().apply()

    Users(id = id, name = name, age = age)
  }

  def batchInsert(entities: Seq[Users])(implicit session: DBSession): List[Int] = {
    val params: Seq[Seq[(Symbol, Any)]] =
      entities.map(entity => Seq('id -> entity.id, 'name -> entity.name.value, 'age -> entity.age.value))
    SQL("""insert into USERS(
      ID,
      NAME,
      AGE
    ) values (
      {id},
      {name},
      {age}
    )""").batchByName(params: _*).apply[List]()
  }

  def save(entity: Users)(implicit session: DBSession): Users = {
    withSQL {
      update(Users)
        .set(
          column.id -> entity.id,
          column.name -> entity.name,
          column.age -> entity.age
        )
        .where
        .eq(column.id, entity.id)
    }.update.apply()
    entity
  }

  def destroy(entity: Users)(implicit session: DBSession): Int = {
    withSQL { delete.from(Users).where.eq(column.id, entity.id) }.update.apply()
  }

}
