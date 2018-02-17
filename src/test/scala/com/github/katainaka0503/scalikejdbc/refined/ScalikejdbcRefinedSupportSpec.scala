package com.github.katainaka0503.scalikejdbc.refined

import java.sql.ResultSet

import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.numeric.Positive
import eu.timepit.refined.string.StartsWith
import org.scalamock.scalatest.MockFactory
import org.scalatest.WordSpec
import org.scalatest.Matchers._
import scalikejdbc.TypeBinder

class ScalikejdbcRefinedSupportSpec extends WordSpec with MockFactory{

  "scalikejdbc-refined" should {
    "defines TypeBinder[Int Refined Positive]" in {
      val resultSet = mock[ResultSet]
      (resultSet.getObject(_:Int)) expects 1 returning new Integer(1)

      val result = implicitly[TypeBinder[Int Refined Positive]].apply(resultSet, 1)

      result.value shouldBe 1
    }
  }

  "defines TypeBinder[String Refined StartsWith]" in {
    val resultSet = mock[ResultSet]
    (resultSet.getString(_:Int)) expects 1 returning "prefixandsuffix"

    val result = implicitly[TypeBinder[String Refined StartsWith[W.`"prefix"`.T]]].apply(resultSet, 1)

    result.value shouldBe "prefixandsuffix"
  }
}
