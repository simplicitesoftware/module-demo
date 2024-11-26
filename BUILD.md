![](https://docs.simplicite.io//logos/logo250.png)
* * *

Apache Maven
============

Build
-----

``` text
mvn -U -DskipTests=true clean package
```

Sonar analysis
--------------

``` text
mvn sonar:sonar
```

Checkstyle
----------

``` text
mvn checkstyle:check
```

JSHint (requires node.js)
-------------------------

``` text
npm install
npm run jshint
```

