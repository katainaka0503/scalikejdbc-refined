package org.katainaka.scalikejdbc.refined

import java.sql.{PreparedStatement, ResultSet}

import eu.timepit.refined.auto._
import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric.{Greater, Positive}
import eu.timepit.refined.string.{EndsWith, StartsWith}
import org.scalamock.scalatest.MockFactory
import org.scalatest.WordSpec
import org.scalatest.Matchers._
import scalikejdbc.{ParameterBinderFactory, TypeBinder}

class ScalikejdbcRefinedSupportSpec extends WordSpec with MockFactory {

  "scalikejdbc-refined" should {
    "defines TypeBinder[Int Refined Positive]" in {
      val resultSet = mock[ResultSet]
      (resultSet.getObject(_: Int)) expects 1 returning new Integer(1)

      val result = implicitly[TypeBinder[Int Refined Positive]].apply(resultSet, 1)

      result.value shouldBe 1
    }

    "defines TypeBinder[String Refined StartsWith]" in {
      val resultSet = mock[ResultSet]
      (resultSet.getString(_: Int)) expects 1 returning "prefixandsuffix"

      val result =
        implicitly[TypeBinder[String Refined StartsWith[W.`"prefix"`.T]]].apply(resultSet, 1)

      result.value shouldBe "prefixandsuffix"
    }

    "define ParameterBinderFactory[Double Refined Greater[3.2]]" in {
      val stmt = mock[PreparedStatement]
      val value: Double Refined Greater[W.`3.2`.T] = 4.5

      stmt.setDouble _ expects (1, value.value)

      implicitly[ParameterBinderFactory[Double Refined Greater[W.`3.2`.T]]].apply(value)(stmt, 1)
    }

    """define ParameterBinderFactory[String Refined EndsWith["suffix"]""" in {
      val stmt = mock[PreparedStatement]
      val value: String Refined EndsWith[W.`"suffix"`.T] = "foobarsuffix"

      stmt.setString _ expects (1, value.value)

      implicitly[ParameterBinderFactory[String Refined EndsWith[W.`"suffix"`.T]]]
        .apply(value)(stmt, 1)
    }
  }

}
