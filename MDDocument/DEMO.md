![Logo](https://www.simplicite.io/resources/logos/logo250.png)
* * *

This document describes the business model of the **demo** order management application.

Business objects
================

This is the application's business model:

![]([MODEL:DemoObjects])

[OBJECTDOC:DemoSupplier]

[OBJECTDOC:DemoProduct]

[OBJECTDOC:DemoClient]

[OBJECTDOC:DemoOrder]

[OBJECTDOC:DemoContact]

State models workflows
======================

Orders
------

Orders go thru the following state model:

![]([MODEL:DemoOrderStates])

Activity workflows
==================

Order entry
-----------

The order entry workflow has the following model:

![]([MODEL:DemoWorkflow])

[PROCESSDOC:DemoOrderCreate]

Profiles
========

The application's user profiles are:

![]([MODEL:DemoUsers])

