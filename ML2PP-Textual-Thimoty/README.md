# ML-Quadrat (ML2)
Fork of [ML-Quadrat](https://github.com/arminmoin/ML-Quadrat), consolidate their documentation regarding the project.

This project holds the code fixes for predictions and classification predictions and the code extension to support visualizations, metrics and reporting capabilities for DAML.

# How to compile?
The project has a lot of issues regarding dependencies. Download a maven artifact for: io.github.arminmoin:mlquadrat.compilers.bundle🫙2.0.0-SNAPSHOT in [Maven Central Repository](https://oss.sonatype.org/), search for the id "io.github.arminmoin", then install it locally with:

```
mvn install:install-file \
  -DgroupId=io.github.arminmoin \
  -DartifactId=mlquadrat.compilers.bundle \
  -Dversion=2.0.0-SNAPSHOT \
  -Dpackaging=jar \
  -Dfile=/path/to/mlquadrat.compilers.bundle-2.0.0-SNAPSHOT.jar
```

Afterwards run the following commands starting from the root (Ignore errors regarding IDE's, tests, etc. the most important is the "language" compiling for the Model, and the "compilers" compilers):
```
mvn clean install -DskipTests
cd ML2
mvn clean install -DskipTests

cd ML2/language
mvn clean install -DskipTests

cd ML2/compilers
mvn clean install -DskipTests
cd ML2/compilers/registery
mvn clean install -DskipTests
```
The last command will give you the .jar file location that you can use to compile projects with.


