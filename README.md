# Scalikejdbc-refined

Scalikejdbc-refined is a small scala library that enables boilerplate-free integration of refinement types using the Refined library with Scalikejdbc.

The library defines `TypeBinder[Refined[T, P]]` , and enables get refinement type from ResultSet without boilerplate.

## Usage

You first need to add the following dependency to your SBT dependencies:

libraryDependencies += "jp.katainaka" %% "scalikejdbc-refined" % x.x.x

## Licence

This library is released under the MIT License, see LICENSE.txt.

