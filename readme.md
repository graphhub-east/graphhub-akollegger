Github in a Graph - akollegger
==============================

This is [akollegger](http://github.com/akollegger)'s implementation of an importer
which crawls Github starting with an authorized user, creating a social graph in Neo4j.

Generate your own Github sub-graph
----------------------------------

You can use this to create your own sub-graph by following these steps:

1. Create a file called github.sbt, with contents like:

    githubUser := "akollegger"
    
    githubPassword := "password-for-github"
 
2. `./bin/sbt`
   - will launch the `sbt` console

    test-only org.neo4j.contrib.github.importer.Neo4jImportVisitorSpec


Github in a Graph
-----------------

This project is part of a workshop for learning Neo4j by using Github as a data source. 

* [Github in a Graph Workshop](https://github.com/graphhub-east/graph-github/wiki)
* [Github API](http://developer.github.com)
* [Github API Libraries](http://developer.github.com/v3/libraries/)