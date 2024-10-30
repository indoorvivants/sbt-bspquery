# BPS Query - limit the projects imported in Metals

This project attempts to solve an existing problem in large SBT 
projects that use sbt-projectmatrix - large matrices of platforms and scala versions
can make Metals barely usable, because a single file is used for compiling multiple projects.

The way we can work around this issue is by filtering down the set of projects that Metals
will consider, by setting `bspEnabled := false` on projects we don't want to import.

This SBT plugin simplifies this procedure by using a very basic query syntax (`platform=jvm && scalaBinary=2.13`) which you can put in a `.bspquery` file in 
the root of your projects to only import JVM projects using Scala 2.13

## Installation

The SBT plugin only works on projects using sbt-projectmatrix.

add to your `project/plugins.sbt` (find version from badge above):

```scala
addSbtPlugin("com.indoorvivants" % "sbt-bspquery" % "<VERSION>")
```

create your query file to test:

**.bspquery**
```
platform=jvm && scalaBinary=2.13
```

## Configuration

The plugin automatically detects a `.bspquery` file at the root folder.
Otherwise, you can use `bspQuery := "..."` setting to change the query.

