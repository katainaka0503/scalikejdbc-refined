package jp.katainaka.scalikejdbc

import eu.timepit.refined._
import eu.timepit.refined.api.{RefType, Refined, Validate}
import scalikejdbc.{ParameterBinderFactory, TypeBinder}

import scala.language.higherKinds

package object refined {
  implicit def refinedTypeBinder[T, P](implicit validate: Validate[T, P],
                                       based: TypeBinder[T]): TypeBinder[Refined[T, P]] =
    based.map(refineV[P].unsafeFrom(_))

  implicit def refinedParameterBinderFactory[T, P, F[_, _]](implicit
                                                            based: ParameterBinderFactory[T],
                                                            refType: RefType[F]): ParameterBinderFactory[F[T, P]] =
    based.contramap(refType.unwrap)
}
