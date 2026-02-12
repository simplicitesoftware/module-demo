![Logo](https://platform.simplicite.io/logos/standard/logo250.png)
* * *

Introduction
------------

This is a demo **order management** application.

Import
------

To import this module:

- Create a module named `Demo`
- Set the settings as:

```json
{
	"type": "git",
	"branch": "v6",
	"origin": { "uri": "https://github.com/simplicitesoftware/module-demo.git" }
}
```

- Click on the _Import module_ button

Configure
---------

In order to have the frontend examples working the password for the
webservices-only user `website` must be `simplicite`.

This can be achieved by importing the following XML:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<simplicite xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://www.simplicite.fr/base" xsi:schemaLocation="http://www.simplicite.fr/base https://www.simplicite.io/resources/schemas/base.xsd">
<object>
	<name>UserPwd</name>
	<action>update</action>
	<data>
		<usr_login_read>website</usr_login_read>
		<usr_password>simplicite</usr_password>
	</data>
</object>
</simplicite>
```

Load data
---------

Some sample data is provided as a module's dataset. It contains:

- Sample providers
- Sample products
- Sample customers

Open this dataset and click on the _Apply_ button after having imported the module and made a full clear cache.

