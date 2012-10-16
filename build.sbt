name <<= (githubUser) { (username) =>  username+"-github" }

version := "1.0"

organization := "org.neo4j.contrib.github"

// add compile dependencies on some dispatch modules
libraryDependencies ++= Seq(
    "org.neo4j" % "neo4j-community" % "1.8",
    "org.neo4j" % "neo4j-shell" % "1.8",
    "org.eclipse.mylyn.github" % "org.eclipse.egit.github.core" % "2.1.0",
    "org.specs2" %% "specs2" % "1.12.2" % "test"
)

// append several options to the list of options passed to the Java compiler
javacOptions ++= Seq("-source", "1.6", "-target", "1.6")

testOptions in Test <+= (githubUser, githubPassword) map {
    (u,p) => Tests.Argument("github.user", u, "github.password", p)
    }

parallelExecution in Test := false

fork := true

retrieveManaged := true

offline := true

