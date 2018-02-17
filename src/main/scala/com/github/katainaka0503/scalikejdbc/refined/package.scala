package com.github.katainaka0503.scalikejdbc

import eu.timepit.refined.api.{Refined, Validate}
import scalikejdbc.TypeBinder

package object refined {
  implicit def refinedTypeBinder[T, P](implicit validate: Validate[T, P],
                                       based: TypeBinder[T]): TypeBinder[Refined[T, P]] =
    based.map(Refined.unsafeApply)
}
