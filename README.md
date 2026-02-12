<!--
 ___ _            _ _    _ _    __
/ __(_)_ __  _ __| (_)__(_) |_ /_/
\__ \ | '  \| '_ \ | / _| |  _/ -_)
|___/_|_|_|_| .__/_|_\__|_|\__\___|
            |_| 
-->
![](https://platform.simplicite.io//logos/logo250.png)
* * *

`Demo` module definition
========================

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
	"origin": {
		"uri": "https://github.com/simplicitesoftware/module-demo.git"
	}
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

`DemoContact` business object definition
----------------------------------------

The **contact** object holds the interactions with the clients.

A contact can be linked or not a an order of the selelcted client
(when linked to an order the `demoCtcOrdId` field is set).

### Fields

| Name                                                         | Type                                     | Required | Updatable | Personal | Description                                                                      |
|--------------------------------------------------------------|------------------------------------------|----------|-----------|----------|----------------------------------------------------------------------------------|
| `demoCtcDatetime`                                            | datetime                                 | yes*     |           |          | Contcat date and time                                                            |
| `demoCtcType`                                                | enum(10) using `DEMO_CTC_TYPE` list      | yes      | yes       |          | Contact type                                                                     |
| `demoCtcSubType`                                             | enum(10) using `DEMO_CTC_SUBTYPE` list   |          | yes       |          | Contact sub type                                                                 |
| `demoCtcCanal`                                               | enum(10) using `DEMO_CTC_CANAL` list     | yes      | yes       |          | Canal used for contact                                                           |
| `demoCtcPriority`                                            | boolean                                  | yes      | yes       |          | Contact priority                                                                 |
| `demoCtcStatus`                                              | enum(1) using `DEMO_CTC_STATUS` list     | yes      | yes       |          | Contact status                                                                   |
| `demoCtcCliId` link to **`DemoClient`**                      | id                                       | yes*     | yes       |          | Contact customer                                                                 |
| _Ref. `demoCtcCliId.demoCliCode`_                            | _regexp(10)_                             |          |           |          | _Customer code_                                                                  |
| _Ref. `demoCtcCliId.demoCliFirstname`_                       | _char(100)_                              |          |           | yes      | _Customer first name_                                                            |
| _Ref. `demoCtcCliId.demoCliLastname`_                        | _char(100)_                              |          |           | yes      | _Customer last name_                                                             |
| _Ref. `demoCtcCliId.demoCliEmail`_                           | _email(50)_                              |          |           | yes      | _Customer email address_                                                         |
| _Ref. `demoCtcCliId.demoCliHomePhone`_                       | _phone(20)_                              |          |           | yes      | _Customer home phone number_                                                     |
| _Ref. `demoCtcCliId.demoCliWorkPhone`_                       | _phone(20)_                              |          |           | yes      | _Customer work phone number_                                                     |
| _Ref. `demoCtcCliId.demoCliMobilePhone`_                     | _phone(20)_                              |          |           | yes      | _Customer mobile phone number_                                                   |
| _Ref. `demoCtcCliId.demoCliFax`_                             | _phone(20)_                              |          |           | yes      | _Customer fax number_                                                            |
| `demoCtcOrdId` link to **`DemoOrder`**                       | id                                       |          | yes       |          | Contact order                                                                    |
| _Ref. `demoCtcOrdId.demoOrdNumber`_                          | _int(11)_                                |          |           |          | _Order number (automatically calculated at creation)_                            |
| _Ref. `demoCtcOrdId.demoOrdDate`_                            | _date_                                   |          |           |          | _Order date_                                                                     |
| _Ref. `demoCtcOrdId.demoOrdStatus`_                          | _enum(30) using `DEMO_ORD_STATUS` list_  |          |           |          | _Order status_                                                                   |
| _Ref. `demoCtcOrdId.demoOrdCliId`_                           | _id_                                     |          |           |          | _Order customer_                                                                 |
| _Ref. `demoOrdCliId.demoCliCode`_                            | _regexp(10)_                             |          |           |          | _Customer code_                                                                  |
| _Ref. `demoCtcOrdId.demoOrdPrdId`_                           | _id_                                     |          |           |          | _Order product_                                                                  |
| _Ref. `demoOrdPrdId.demoPrdSupId`_                           | _id_                                     |          |           |          | _Product supplier_                                                               |
| _Ref. `demoPrdSupId.demoSupCode`_                            | _regexp(50)_                             |          |           |          | _Supplier unique code (e.g. `MYSUP`)_                                            |
| _Ref. `demoPrdSupId.demoSupUsrId`_                           | _id_                                     |          |           |          | _User responsible of supplier_                                                   |
| _Ref. `demoOrdPrdId.demoPrdReference`_                       | _regexp(10)_                             |          |           |          | _Product reference_                                                              |
| _Ref. `demoOrdPrdId.demoPrdName`_                            | _char(100)_                              |          |           |          | _Product name_                                                                   |
| _Ref. `demoCtcOrdId.demoOrdQuantity`_                        | _int(11)_                                |          |           |          | _Product quantity ordered_                                                       |
| `demoCtcFile`                                                | document                                 |          | yes       |          | Contact attached file                                                            |
| `demoCtcPicture`                                             | image                                    |          | yes       |          | -                                                                                |
| `demoCtcMessages`                                            | notepad(50000)                           | yes      | yes       |          | Messages                                                                         |

### Lists

* `DEMO_CTC_TYPE`
    - `INF` Information
    - `REQ` Request
    - `CMP` Complaint
    - `OTH` Other
* `DEMO_CTC_SUBTYPE`
    - `EMPTY` 
* `DEMO_CTC_CANAL`
    - `PHONE` Code PHONE
    - `EMAIL` Code EMAIL
    - `CHAT` Code CHAT
    - `WEB` Code WEB
* `DEMO_CTC_STATUS`
    - `O` Open
    - `C` Closed
    - `P` Processing
* `DEMO_ORD_STATUS`
    - `P` Pending status
    - `H` On hold
    - `V` Validated status
    - `D` Shipped status
    - `C` Canceled status

`DemoContactHistoric` business object definition
------------------------------------------------

**Contact** object history, tracks changes on:

- Status
- Type and/or subtype
- Comments

### Fields

| Name                                                         | Type                                     | Required | Updatable | Personal | Description                                                                      |
|--------------------------------------------------------------|------------------------------------------|----------|-----------|----------|----------------------------------------------------------------------------------|
| `row_ref_id` link to **`DemoContact`**                       | id                                       | yes*     |           |          | Record row ID                                                                    |
| `row_idx`                                                    | int(11)                                  | yes*     | yes       |          | History record index                                                             |
| `created_by_hist`                                            | char(100)                                | yes*     |           |          | Created by                                                                       |
| `created_dt_hist`                                            | datetime                                 | yes*     |           |          | Created date                                                                     |
| `demoCtcStatus`                                              | enum(1) using `DEMO_CTC_STATUS` list     | yes      | yes       |          | Contact status                                                                   |
| `demoCtcType`                                                | enum(10) using `DEMO_CTC_TYPE` list      | yes      | yes       |          | Contact type                                                                     |
| `demoCtcSubType`                                             | enum(10) using `DEMO_CTC_SUBTYPE` list   |          | yes       |          | Contact sub type                                                                 |

### Lists

* `DEMO_CTC_STATUS`
    - `O` Open
    - `C` Closed
    - `P` Processing
* `DEMO_CTC_TYPE`
    - `INF` Information
    - `REQ` Request
    - `CMP` Complaint
    - `OTH` Other
* `DEMO_CTC_SUBTYPE`
    - `EMPTY` 

`DemoOrder` business object definition
--------------------------------------

The **order** business object corresponds to the
product orders placed by clients.

An order is for one single product.

### Fields

| Name                                                         | Type                                     | Required | Updatable | Personal | Description                                                                      |
|--------------------------------------------------------------|------------------------------------------|----------|-----------|----------|----------------------------------------------------------------------------------|
| `demoOrdNumber`                                              | int(11)                                  | yes*     |           |          | Order number (automatically calculated at creation)                              |
| `demoOrdDate`                                                | date                                     |          |           |          | Order date                                                                       |
| `demoOrdStatus`                                              | enum(30) using `DEMO_ORD_STATUS` list    | yes      | yes       |          | Order status                                                                     |
| `demoOrdDeliveryDate`                                        | datetime                                 |          | yes       |          | Order delivery date                                                              |
| `demoOrdCliId` link to **`DemoClient`**                      | id                                       | yes*     | yes       |          | Order customer                                                                   |
| _Ref. `demoOrdCliId.demoCliCode`_                            | _regexp(10)_                             |          |           |          | _Customer code_                                                                  |
| _Ref. `demoOrdCliId.demoCliFirstname`_                       | _char(100)_                              |          |           | yes      | _Customer first name_                                                            |
| _Ref. `demoOrdCliId.demoCliLastname`_                        | _char(100)_                              |          |           | yes      | _Customer last name_                                                             |
| _Ref. `demoOrdCliId.demoCliEmail`_                           | _email(50)_                              |          |           | yes      | _Customer email address_                                                         |
| _Ref. `demoOrdCliId.demoCliAddress1`_                        | _char(100)_                              |          |           | yes      | _Customer address (line 1)_                                                      |
| _Ref. `demoOrdCliId.demoCliAddress2`_                        | _char(100)_                              |          |           | yes      | _Customer address (line 2)_                                                      |
| _Ref. `demoOrdCliId.demoCliZipCode`_                         | _char(10)_                               |          |           | yes      | _Customer postal code_                                                           |
| _Ref. `demoOrdCliId.demoCliCity`_                            | _char(50)_                               |          |           | yes      | _Customer city_                                                                  |
| _Ref. `demoOrdCliId.demoCliCountry`_                         | _enum(30) using `DEMO_COUNTRY` list_     |          |           | yes      | _Customer country_                                                               |
| `demoOrdPrdId` link to **`DemoProduct`**                     | id                                       | yes*     | yes       |          | Order product                                                                    |
| _Ref. `demoOrdPrdId.demoPrdReference`_                       | _regexp(10)_                             |          |           |          | _Product reference_                                                              |
| _Ref. `demoOrdPrdId.demoPrdName`_                            | _char(100)_                              |          |           |          | _Product name_                                                                   |
| _Ref. `demoOrdPrdId.demoPrdType`_                            | _enum(50) using `DEMO_PRD_TYPE` list_    |          |           |          | _Product type_                                                                   |
| _Ref. `demoOrdPrdId.demoPrdPicture`_                         | _image_                                  |          |           |          | _Product picture_                                                                |
| _Ref. `demoOrdPrdId.demoPrdSupId`_                           | _id_                                     |          |           |          | _Product supplier_                                                               |
| _Ref. `demoPrdSupId.demoSupCode`_                            | _regexp(50)_                             |          |           |          | _Supplier unique code (e.g. `MYSUP`)_                                            |
| _Ref. `demoPrdSupId.demoSupName`_                            | _char(100)_                              |          |           |          | _Supplier name_                                                                  |
| _Ref. `demoPrdSupId.demoSupLogo`_                            | _image_                                  |          |           |          | _Supplier logo_                                                                  |
| _Ref. `demoPrdSupId.demoSupUsrId`_                           | _id_                                     |          |           |          | _User responsible of supplier_                                                   |
| _Ref. `demoOrdPrdId.demoPrdStock`_                           | _int(11)_                                |          |           |          | _Current stock for product_                                                      |
| _Ref. `demoOrdPrdId.demoPrdUnitPrice`_                       | _float(11, 2)_                           |          |           |          | _Unit price of product_                                                          |
| `demoOrdUnitPrice`                                           | float(11, 2)                             |          |           |          | Product unit price for order                                                     |
| `demoOrdQuantity`                                            | int(11)                                  | yes      | yes       |          | Product quantity ordered                                                         |
| `demoOrdTotal`                                               | float(11, 2)                             |          |           |          | Total order amount                                                               |
| `demoOrdVAT`                                                 | float(11, 2)                             |          |           |          | VAT for order                                                                    |
| `demoOrdComments`                                            | notepad(50000)                           |          | yes       |          | Comments on order                                                                |

### Lists

* `DEMO_ORD_STATUS`
    - `P` Pending status
    - `H` On hold
    - `V` Validated status
    - `D` Shipped status
    - `C` Canceled status
* `DEMO_COUNTRY`
    - `FR` France
    - `UK` United Kingdom
    - `SP` Spain
* `DEMO_PRD_TYPE`
    - `LAPTOP` Laptop
    - `DESKTOP` Desktop
    - `TABLET` Tablet
    - `SMARTPHONE` Smartphone
    - `OTHER` Other

`DemoOrderHistoric` business object definition
----------------------------------------------

**Order** object history, tracks changes on:

- Status
- Quantity

### Fields

| Name                                                         | Type                                     | Required | Updatable | Personal | Description                                                                      |
|--------------------------------------------------------------|------------------------------------------|----------|-----------|----------|----------------------------------------------------------------------------------|
| `row_ref_id` link to **`DemoOrder`**                         | id                                       | yes*     |           |          | Record row ID                                                                    |
| `row_idx`                                                    | int(11)                                  | yes*     | yes       |          | History record index                                                             |
| `created_by_hist`                                            | char(100)                                | yes*     |           |          | Created by                                                                       |
| `created_dt_hist`                                            | datetime                                 | yes*     |           |          | Created date                                                                     |
| `demoOrdStatus`                                              | enum(30) using `DEMO_ORD_STATUS` list    | yes      | yes       |          | Order status                                                                     |
| `demoOrdQuantity`                                            | int(11)                                  | yes      | yes       |          | Product quantity ordered                                                         |

### Lists

* `DEMO_ORD_STATUS`
    - `P` Pending status
    - `H` On hold
    - `V` Validated status
    - `D` Shipped status
    - `C` Canceled status

`DemoProductHistoric` business object definition
------------------------------------------------

**Product** object history, tracks changes on:

- Product name
- Unit price
- Availability

### Fields

| Name                                                         | Type                                     | Required | Updatable | Personal | Description                                                                      |
|--------------------------------------------------------------|------------------------------------------|----------|-----------|----------|----------------------------------------------------------------------------------|
| `row_ref_id` link to **`DemoProduct`**                       | id                                       | yes*     |           |          | Record row ID                                                                    |
| `row_idx`                                                    | int(11)                                  | yes*     | yes       |          | History record index                                                             |
| `created_by_hist`                                            | char(100)                                | yes*     |           |          | Created by                                                                       |
| `created_dt_hist`                                            | datetime                                 | yes*     |           |          | Created date                                                                     |
| `demoPrdReference`                                           | regexp(10)                               | yes*     | yes       |          | Product reference                                                                |
| `demoPrdName`                                                | char(100)                                | yes      | yes       |          | Product name                                                                     |
| `demoPrdUnitPrice`                                           | float(11, 2)                             | yes      | yes       |          | Unit price of product                                                            |
| `demoPrdAvailable`                                           | boolean                                  | yes      | yes       |          | Available product?                                                               |

`DemoSupplier` business object definition
-----------------------------------------

The **supplier** business object corresponds to the
suppliers of products that can be ordered.

### Fields

| Name                                                         | Type                                     | Required | Updatable | Personal | Description                                                                      |
|--------------------------------------------------------------|------------------------------------------|----------|-----------|----------|----------------------------------------------------------------------------------|
| `demoSupCode`                                                | regexp(50)                               | yes*     | yes       |          | Supplier unique code (e.g. `MYSUP`)                                              |
| `demoSupName`                                                | char(100)                                | yes      | yes       |          | Supplier name                                                                    |
| `demoSupDescription`                                         | html(1000000)                            |          | yes       |          | Supplier description                                                             |
| `demoSupPhone`                                               | phone(20)                                |          | yes       |          | Supplier phone number                                                            |
| `demoSupFax`                                                 | phone(20)                                |          | yes       |          | Supplier fax number                                                              |
| `demoSupWebsite`                                             | url(100)                                 |          | yes       |          | Supplier website URL                                                             |
| `demoSupEmail`                                               | email(20)                                |          | yes       |          | Supplier email address                                                           |
| `demoSupLogo`                                                | image                                    |          | yes       |          | Supplier logo                                                                    |
| `demoSupUsrId` link to **`User`**                            | id                                       |          | yes       |          | User responsible of supplier                                                     |
| _Ref. `demoSupUsrId.usr_login`_                              | _regexp(100)_                            |          |           | yes      | _Login_                                                                          |
| `demoSupComments`                                            | notepad(50000)                           |          | yes       |          | Comments on supplier                                                             |

`DemoProduct` business object definition
----------------------------------------

The **product** business object corresponds to the
products that can be ordered.

Its reference is unique per supplier.

### Fields

| Name                                                         | Type                                     | Required | Updatable | Personal | Description                                                                      |
|--------------------------------------------------------------|------------------------------------------|----------|-----------|----------|----------------------------------------------------------------------------------|
| `demoPrdSupId` link to **`DemoSupplier`**                    | id                                       | yes*     | yes       |          | Product supplier                                                                 |
| _Ref. `demoPrdSupId.demoSupCode`_                            | _regexp(50)_                             |          |           |          | _Supplier unique code (e.g. `MYSUP`)_                                            |
| _Ref. `demoPrdSupId.demoSupName`_                            | _char(100)_                              |          |           |          | _Supplier name_                                                                  |
| _Ref. `demoPrdSupId.demoSupLogo`_                            | _image_                                  |          |           |          | _Supplier logo_                                                                  |
| _Ref. `demoPrdSupId.demoSupUsrId`_                           | _id_                                     |          |           |          | _User responsible of supplier_                                                   |
| `demoPrdReference`                                           | regexp(10)                               | yes*     | yes       |          | Product reference                                                                |
| `demoPrdName`                                                | char(100)                                | yes      | yes       |          | Product name                                                                     |
| `demoPrdType`                                                | enum(50) using `DEMO_PRD_TYPE` list      | yes      | yes       |          | Product type                                                                     |
| `demoPrdDescription`                                         | text(1000000)                            |          | yes       |          | Product description                                                              |
| `demoPrdPicture`                                             | image                                    |          | yes       |          | Product picture                                                                  |
| `demoPrdEan13`                                               | char(13)                                 |          | yes       |          | EAN13 code                                                                       |
| `demoPrdEan13Image`                                          | image                                    |          |           |          | EAN13 code image                                                                 |
| `demoPrdStock`                                               | int(11)                                  | yes      | yes       |          | Current stock for product                                                        |
| `demoPrdUnitPrice`                                           | float(11, 2)                             | yes      | yes       |          | Unit price of product                                                            |
| `demoPrdAvailable`                                           | boolean                                  | yes      | yes       |          | Available product?                                                               |
| `demoPrdFeatured`                                            | boolean                                  | yes      | yes       |          | Featured product?                                                                |
| `demoPrdDocumentation`                                       | html(1000000)                            |          | yes       |          | Product documentation                                                            |
| `demoPrdBrochure`                                            | document                                 |          | yes       |          | Product brochure                                                                 |
| `demoPrdOnlineDoc`                                           | url(255)                                 |          | yes       |          | Online product documentation URL                                                 |
| `demoPrdComments`                                            | notepad(50000)                           |          | yes       |          | Comments on product                                                              |

### Lists

* `DEMO_PRD_TYPE`
    - `LAPTOP` Laptop
    - `DESKTOP` Desktop
    - `TABLET` Tablet
    - `SMARTPHONE` Smartphone
    - `OTHER` Other

### Custom actions

* `DEMO_DECSTOCK`: Product stock **decrement** triggered by the order
state transition to _shipped_ status.
* `DEMO_PRD_EMAIL`: Send product information by email
* `DEMO_INCSTOCK`: Sample action for product stock **increment**
(by `N` specified in the product business object code).

`DemoClient` business object definition
---------------------------------------

The **client** business object corresponds
to the customer who places order.

His address is geolocalized using GoogleMaps&reg; API.

### Fields

| Name                                                         | Type                                     | Required | Updatable | Personal | Description                                                                      |
|--------------------------------------------------------------|------------------------------------------|----------|-----------|----------|----------------------------------------------------------------------------------|
| `demoCliCode`                                                | regexp(10)                               | yes*     | yes       |          | Customer code                                                                    |
| `demoCliFirstname`                                           | char(100)                                | yes      | yes       | yes      | Customer first name                                                              |
| `demoCliLastname`                                            | char(100)                                | yes      | yes       | yes      | Customer last name                                                               |
| `demoCliAddress1`                                            | char(100)                                | yes      | yes       | yes      | Customer address (line 1)                                                        |
| `demoCliAddress2`                                            | char(100)                                |          | yes       | yes      | Customer address (line 2)                                                        |
| `demoCliZipCode`                                             | char(10)                                 | yes      | yes       | yes      | Customer postal code                                                             |
| `demoCliCity`                                                | char(50)                                 | yes      | yes       | yes      | Customer city                                                                    |
| `demoCliCountry`                                             | enum(30) using `DEMO_COUNTRY` list       | yes      | yes       | yes      | Customer country                                                                 |
| `demoCliCoords`                                              | geocoords                                |          | yes       | yes      | Customer geoccordinates                                                          |
| `demoCliEmail`                                               | email(50)                                |          | yes       | yes      | Customer email address                                                           |
| `demoCliHomePhone`                                           | phone(20)                                |          | yes       | yes      | Customer home phone number                                                       |
| `demoCliWorkPhone`                                           | phone(20)                                |          | yes       | yes      | Customer work phone number                                                       |
| `demoCliMobilePhone`                                         | phone(20)                                |          | yes       | yes      | Customer mobile phone number                                                     |
| `demoCliFax`                                                 | phone(20)                                |          | yes       | yes      | Customer fax number                                                              |
| `demoCliType`                                                | enum(30) using `DEMO_CLI_TYPE` list      | yes      | yes       |          | Customer type                                                                    |
| `demoCliComments`                                            | html(1000000)                            |          | yes       |          | Useful comments on customer                                                      |
| `demoCliPlacemapLabel`                                       | char(100)                                |          | yes       |          | Customer place map label                                                         |

### Lists

* `DEMO_COUNTRY`
    - `FR` France
    - `UK` United Kingdom
    - `SP` Spain
* `DEMO_CLI_TYPE`
    - `T1` Code T1
    - `T2` Code T2
    - `T3` Code T3

`DemoOrderCreate` business process definition
---------------------------------------------

**Order entry** activity workflow

### Activities

* `Begin`: Begin activity
* `ClientSelect`: Customer selection activity
* `SupplierSelect`: Supplier selection activity
* `ProductSelect`: Selected supplier's product selection activity
* `OrderCreate`: Order creation form activity
* `End`: End activity

`DemoCatalog` external object definition
----------------------------------------

Custom JSON web service for getting the product catalog


`DemoCounters` external object definition
-----------------------------------------

Counters


`DemoOrderAgenda` external object definition
--------------------------------------------

Order delivery agenda


`DemoPlaceNewOrder` external object definition
----------------------------------------------

Place new order internal page


