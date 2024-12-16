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

Checkstyle (optional)
---------------------

``` text
mvn checkstyle:check
```

JSHint (optional, requires node.js)
-----------------------------------

``` text
npm install
npm run jshint
```

ESLint (optional, requires node.js)
-----------------------------------

``` text
npm install
npm run eslint
```

StyleLint (optional, requires node.js)
--------------------------------------

``` text
npm install
npm run stylelint
```

