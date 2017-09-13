# renga-graph
Renga Knowledge Graph and related packages

Documentation: https://renga.readthedocs.io/en/latest/developer/knowledge_graph.html

## Project structure
This project is defined using [sbt](http://www.scala-sbt.org/),
and is a [multi-project build](http://www.scala-sbt.org/0.13/docs/Multi-Project.html).

```
renga-graph
├── build.sbt            // root build definition
├── core/                // definitions for graph elements, typing, etc.
├── init/                // graph initilization service
├── mutation/
│   ├── implementation/  // mutation-related code
│   └── service/         // graph mutation service
├── navigation/
│   └── service/         // graph navigation service
└── typesystem/
    ├── implementation/  // type system-related code
    └── service/         // graph type system service
```

To create all docker images:
```bash
$ sbt docker:publishLocal
[...]
[info] Successfully tagged renga-graph-init:<version>
[info] Built image renga-graph-init:<version>
[...]
[info] Successfully tagged renga-graph-typesystem-service:<version>
[info] Built image renga-graph-typesystem-service:<version>
```

Image name and tag can be manipulated with sbt settings, see
[sbt-native-packager](https://sbt-native-packager.readthedocs.io/en/v1.2.2/formats/docker.html).

To test/build a single project:
```bash
$ sbt
> project core
[info] Set current project to renga-graph-core (in build file:[...]/renga-graph/)
> compile
[success] Total time: 0 s, completed 12-Sep-2017 09:03:24
```
