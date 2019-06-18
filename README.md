<!--
 ___ _            _ _    _ _    __
/ __(_)_ __  _ __| (_)__(_) |_ /_/
\__ \ | '  \| '_ \ | / _| |  _/ -_)
|___/_|_|_|_| .__/_|_\__|_|\__\___|
            |_| 
-->
![](https://www.simplicite.io/resources//logos/logo250.png)
* * *

`Demo` module definition
========================

**Order management** demo application

Sample data available [here](https://www.simplicite.io/resources/modules/demo-data-4.0.xml)

Settings from [GitHub](https://github.com/simplicitesoftware/module-demo):

```json
{
	"type": "git",
	"origin": { "uri": "https://github.com/simplicitesoftware/module-demo.git" }
}
```

`DemoClient` business object definition
---------------------------------------

The **client** business object corresponds
to the customer who places order.

His address is geolocalized using GoogleMaps&reg; API.

### Fields

| Name                                                         | Type                                     | Required | Updatable | Personal | Description                                                                      | 
| ------------------------------------------------------------ | ---------------------------------------- | -------- | --------- | -------- | -------------------------------------------------------------------------------- |
| `demoCliCode`                                                | char(10)                                 | yes*     | yes       |          | Customer code                                                                    |
| `demoCliFirstname`                                           | char(100)                                | yes      | yes       | yes      | Customer first name                                                              |
| `demoCliLastname`                                            | char(100)                                | yes      | yes       | yes      | Customer last name                                                               |
| `demoCliAddress1`                                            | char(100)                                | yes      | yes       | yes      | Customer address (line 1)                                                        |
| `demoCliAddress2`                                            | char(100)                                |          | yes       | yes      | Customer address (line 2)                                                        |
| `demoCliAddress3`                                            | char(100)                                |          | yes       | yes      | Customer address (line 3)                                                        |
| `demoCliZipCode`                                             | char(10)                                 | yes      | yes       | yes      | Customer postal code                                                             |
| `demoCliCity`                                                | char(50)                                 | yes      | yes       | yes      | Customer city                                                                    |
| `demoCliCountry`                                             | enum(7) using `DEMO_COUNTRY` list        | yes      | yes       | yes      | Customer country                                                                 |
| `demoCliCoords`                                              | geocoords                                |          |           | yes      | Customer geoccordinates                                                          |
| `demoCliEmail`                                               | email(50)                                |          | yes       | yes      | Customer email address                                                           |
| `demoCliHomePhone`                                           | phone(20)                                |          | yes       | yes      | Customer home phone number                                                       |
| `demoCliWorkPhone`                                           | phone(20)                                |          | yes       | yes      | Customer work phone number                                                       |
| `demoCliMobilePhone`                                         | phone(20)                                |          | yes       | yes      | Customer mobile phone number                                                     |
| `demoCliFax`                                                 | phone(20)                                |          | yes       | yes      | Customer fax number                                                              |
| `demoCliType`                                                | enum(7) using `DEMO_CLI_TYPE` list       | yes      | yes       |          | Customer type                                                                    |
| `demoCliComments`                                            | text(1000000)                            |          | yes       |          | Useful comments on customer                                                      |
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

### Custom actions

No custom action

`DemoContact` business object definition
----------------------------------------

The **contact** object holds the interactions with the clients.

A contact can be linked or not a an order of the selelcted client
(when linked to an order the `demoCtcOrdId` field is set).

### Fields

| Name                                                         | Type                                     | Required | Updatable | Personal | Description                                                                      | 
| ------------------------------------------------------------ | ---------------------------------------- | -------- | --------- | -------- | -------------------------------------------------------------------------------- |
| `demoCtcDatetime`                                            | datetime                                 | yes*     |           |          | Contcat date and time                                                            |
| `demoCtcType`                                                | enum(7) using `DEMO_CTC_TYPE` list       | yes      | yes       |          | Contact type                                                                     |
| `demoCtcSubType`                                             | enum(7) using `DEMO_CTC_SUBTYPE` list    |          | yes       |          | Contact sub type                                                                 |
| `demoCtcCanal`                                               | enum(7) using `DEMO_CTC_CANAL` list      | yes      | yes       |          | Canal used for contact                                                           |
| `demoCtcPriority`                                            | boolean                                  | yes      | yes       |          | Contact priority                                                                 |
| `demoCtcStatus`                                              | enum(7) using `DEMO_CTC_STATUS` list     | yes      | yes       |          | Contact status                                                                   |
| `demoCtcCliId` link to **`DemoClient`**                      | id                                       | yes*     | yes       |          | Contact customer                                                                 |
| _Ref. `demoCtcCliId.demoCliCode`_                            | _char(10)_                               |          |           |          | _Customer code_                                                                  |
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
| _Ref. `demoCtcOrdId.demoOrdStatus`_                          | _enum(7) using `DEMO_ORD_STATUS` list_   |          |           |          | _Order status_                                                                   |
| _Ref. `demoCtcOrdId.demoOrdCliId`_                           | _id_                                     |          |           |          | _Order customer_                                                                 |
| _Ref. `demoCtcOrdId.demoOrdPrdId`_                           | _id_                                     |          |           |          | _Order product_                                                                  |
| _Ref. `demoOrdPrdId.demoPrdSupId`_                           | _id_                                     |          |           |          | _Product supplier_                                                               |
| _Ref. `demoPrdSupId.demoSupUsrId`_                           | _id_                                     |          |           |          | _User responsible of supplier_                                                   |
| _Ref. `demoOrdPrdId.demoPrdReference`_                       | _char(10)_                               |          |           |          | _Product reference_                                                              |
| _Ref. `demoOrdPrdId.demoPrdName`_                            | _char(100)_                              |          |           |          | _Product name_                                                                   |
| _Ref. `demoCtcOrdId.demoOrdQuantity`_                        | _int(11)_                                |          |           |          | _Product quantity ordered_                                                       |
| `demoCtcComments`                                            | html(50000)                              |          | yes       |          | Comments on contact                                                              |
| `demoCtcFile`                                                | document                                 |          | yes       |          | Contact attached file                                                            |

### Lists

* `DEMO_CTC_TYPE`
    - `INF` Code INF
    - `REQ` Code REQ
    - `CMP` Code CMP
    - `OTH` Code OTH
* `DEMO_CTC_SUBTYPE`
    - `DEFAULT` Code DEFAULT
* `DEMO_CTC_CANAL`
    - `PHONE` Code PHONE
    - `FAX` Code FAX
    - `EMAIL` Code EMAIL
    - `CHAT` Code CHAT
    - `WEB` Code WEB
* `DEMO_CTC_STATUS`
    - `O` Open
    - `C` Closed
    - `P` Processing
* `DEMO_ORD_STATUS`
    - `P` Pending status
    - `V` Validated status
    - `D` Shipped status
    - `C` Canceled status

### Custom actions

No custom action

`DemoContactHistoric` business object definition
------------------------------------------------



### Fields

| Name                                                         | Type                                     | Required | Updatable | Personal | Description                                                                      | 
| ------------------------------------------------------------ | ---------------------------------------- | -------- | --------- | -------- | -------------------------------------------------------------------------------- |
| `row_ref_id` link to **`DemoContact`**                       | id                                       | yes*     |           |          | -                                                                                |
| `row_idx`                                                    | int(11)                                  | yes*     | yes       |          | -                                                                                |
| `created_by_hist`                                            | char(100)                                | yes*     |           |          | -                                                                                |
| `created_dt_hist`                                            | datetime                                 | yes*     |           |          | -                                                                                |
| `demoCtcComments`                                            | html(50000)                              |          | yes       |          | Comments on contact                                                              |
| `demoCtcFile`                                                | document                                 |          | yes       |          | Contact attached file                                                            |
| `demoCtcStatus`                                              | enum(7) using `DEMO_CTC_STATUS` list     | yes      | yes       |          | Contact status                                                                   |

### Lists

* `DEMO_CTC_STATUS`
    - `O` Open
    - `C` Closed
    - `P` Processing

### Custom actions

No custom action

`DemoOrder` business object definition
--------------------------------------

The **order** business object corresponds to the
product orders placed by clients.

An order is for one single product.

### Fields

| Name                                                         | Type                                     | Required | Updatable | Personal | Description                                                                      | 
| ------------------------------------------------------------ | ---------------------------------------- | -------- | --------- | -------- | -------------------------------------------------------------------------------- |
| `demoOrdNumber`                                              | int(11)                                  | yes*     |           |          | Order number (automatically calculated at creation)                              |
| `demoOrdDate`                                                | date                                     |          |           |          | Order date                                                                       |
| `demoOrdStatus`                                              | enum(7) using `DEMO_ORD_STATUS` list     | yes      | yes       |          | Order status                                                                     |
| `demoOrdDeliveryDate`                                        | datetime                                 |          | yes       |          | Order delivery date                                                              |
| `demoOrdCliId` link to **`DemoClient`**                      | id                                       | yes*     | yes       |          | Order customer                                                                   |
| _Ref. `demoOrdCliId.demoCliCode`_                            | _char(10)_                               |          |           |          | _Customer code_                                                                  |
| _Ref. `demoOrdCliId.demoCliFirstname`_                       | _char(100)_                              |          |           | yes      | _Customer first name_                                                            |
| _Ref. `demoOrdCliId.demoCliLastname`_                        | _char(100)_                              |          |           | yes      | _Customer last name_                                                             |
| _Ref. `demoOrdCliId.demoCliEmail`_                           | _email(50)_                              |          |           | yes      | _Customer email address_                                                         |
| _Ref. `demoOrdCliId.demoCliAddress1`_                        | _char(100)_                              |          |           | yes      | _Customer address (line 1)_                                                      |
| _Ref. `demoOrdCliId.demoCliAddress2`_                        | _char(100)_                              |          |           | yes      | _Customer address (line 2)_                                                      |
| _Ref. `demoOrdCliId.demoCliAddress3`_                        | _char(100)_                              |          |           | yes      | _Customer address (line 3)_                                                      |
| _Ref. `demoOrdCliId.demoCliZipCode`_                         | _char(10)_                               |          |           | yes      | _Customer postal code_                                                           |
| _Ref. `demoOrdCliId.demoCliCity`_                            | _char(50)_                               |          |           | yes      | _Customer city_                                                                  |
| _Ref. `demoOrdCliId.demoCliCountry`_                         | _enum(7) using `DEMO_COUNTRY` list_      |          |           | yes      | _Customer country_                                                               |
| `demoOrdPrdId` link to **`DemoProduct`**                     | id                                       | yes*     | yes       |          | Order product                                                                    |
| _Ref. `demoOrdPrdId.demoPrdReference`_                       | _char(10)_                               |          |           |          | _Product reference_                                                              |
| _Ref. `demoOrdPrdId.demoPrdName`_                            | _char(100)_                              |          |           |          | _Product name_                                                                   |
| _Ref. `demoOrdPrdId.demoPrdPicture`_                         | _image_                                  |          |           |          | _Product picture_                                                                |
| _Ref. `demoOrdPrdId.demoPrdSupId`_                           | _id_                                     |          |           |          | _Product supplier_                                                               |
| _Ref. `demoPrdSupId.demoSupCode`_                            | _char(50)_                               |          |           |          | _Supplier unique code (e.g. `MYSUP`)_                                            |
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
    - `V` Validated status
    - `D` Shipped status
    - `C` Canceled status
* `DEMO_COUNTRY`
    - `FR` France
    - `UK` United Kingdom
    - `SP` Spain

### Custom actions

No custom action

`DemoOrderHistoric` business object definition
----------------------------------------------



### Fields

| Name                                                         | Type                                     | Required | Updatable | Personal | Description                                                                      | 
| ------------------------------------------------------------ | ---------------------------------------- | -------- | --------- | -------- | -------------------------------------------------------------------------------- |
| `row_ref_id` link to **`DemoOrder`**                         | id                                       | yes*     |           |          | -                                                                                |
| `row_idx`                                                    | int(11)                                  | yes*     | yes       |          | -                                                                                |
| `created_by_hist`                                            | char(100)                                | yes*     |           |          | -                                                                                |
| `created_dt_hist`                                            | datetime                                 | yes*     |           |          | -                                                                                |
| `demoOrdStatus`                                              | enum(7) using `DEMO_ORD_STATUS` list     | yes      | yes       |          | Order status                                                                     |
| `demoOrdQuantity`                                            | int(11)                                  | yes      | yes       |          | Product quantity ordered                                                         |

### Lists

* `DEMO_ORD_STATUS`
    - `P` Pending status
    - `V` Validated status
    - `D` Shipped status
    - `C` Canceled status

### Custom actions

No custom action

`DemoProduct` business object definition
----------------------------------------

The **product** business object corresponds to the
products that can be ordered.

Its reference is unique per supplier.

### Fields

| Name                                                         | Type                                     | Required | Updatable | Personal | Description                                                                      | 
| ------------------------------------------------------------ | ---------------------------------------- | -------- | --------- | -------- | -------------------------------------------------------------------------------- |
| `demoPrdSupId` link to **`DemoSupplier`**                    | id                                       | yes*     | yes       |          | Product supplier                                                                 |
| _Ref. `demoPrdSupId.demoSupCode`_                            | _char(50)_                               |          |           |          | _Supplier unique code (e.g. `MYSUP`)_                                            |
| _Ref. `demoPrdSupId.demoSupName`_                            | _char(100)_                              |          |           |          | _Supplier name_                                                                  |
| _Ref. `demoPrdSupId.demoSupLogo`_                            | _image_                                  |          |           |          | _Supplier logo_                                                                  |
| _Ref. `demoPrdSupId.demoSupUsrId`_                           | _id_                                     |          |           |          | _User responsible of supplier_                                                   |
| `demoPrdReference`                                           | char(10)                                 | yes*     | yes       |          | Product reference                                                                |
| `demoPrdName`                                                | char(100)                                | yes      | yes       |          | Product name                                                                     |
| `demoPrdDescription`                                         | text(1000000)                            |          | yes       |          | Product description                                                              |
| `demoPrdPicture`                                             | image                                    |          | yes       |          | Product picture                                                                  |
| `demoPrdStock`                                               | int(11)                                  | yes      | yes       |          | Current stock for product                                                        |
| `demoPrdUnitPrice`                                           | float(11, 2)                             | yes      | yes       |          | Unit price of product                                                            |
| `demoPrdAvailable`                                           | boolean                                  | yes      | yes       |          | Available product?                                                               |
| `demoPrdDocumentation`                                       | html(1000000)                            |          | yes       |          | Product documentation                                                            |
| `demoPrdBrochure`                                            | document                                 |          | yes       |          | Product brochure                                                                 |
| `demoPrdOnlineDoc`                                           | url(255)                                 |          | yes       |          | Online product documentation URL                                                 |
| `demoPrdComments`                                            | notepad(50000)                           |          | yes       |          | Comments on product                                                              |

### Custom actions

* `DEMO_DECSTOCK`: Product stock **decrement** triggered by the order
state transition to _shipped_ status.
* `DEMO_INCSTOCK`: Sample action for product stock **increment**
(by `N` specified in the product business object code).

`DemoProductHistoric` business object definition
------------------------------------------------



### Fields

| Name                                                         | Type                                     | Required | Updatable | Personal | Description                                                                      | 
| ------------------------------------------------------------ | ---------------------------------------- | -------- | --------- | -------- | -------------------------------------------------------------------------------- |
| `row_ref_id` link to **`DemoProduct`**                       | id                                       | yes*     |           |          | -                                                                                |
| `row_idx`                                                    | int(11)                                  | yes*     | yes       |          | -                                                                                |
| `created_by_hist`                                            | char(100)                                | yes*     |           |          | -                                                                                |
| `created_dt_hist`                                            | datetime                                 | yes*     |           |          | -                                                                                |
| `demoPrdReference`                                           | char(10)                                 | yes*     | yes       |          | Product reference                                                                |
| `demoPrdName`                                                | char(100)                                | yes      | yes       |          | Product name                                                                     |
| `demoPrdUnitPrice`                                           | float(11, 2)                             | yes      | yes       |          | Unit price of product                                                            |
| `demoPrdAvailable`                                           | boolean                                  | yes      | yes       |          | Available product?                                                               |

### Custom actions

No custom action

`DemoSupplier` business object definition
-----------------------------------------

The **supplier** business object corresponds to the
suppliers of products that can be ordered.

### Fields

| Name                                                         | Type                                     | Required | Updatable | Personal | Description                                                                      | 
| ------------------------------------------------------------ | ---------------------------------------- | -------- | --------- | -------- | -------------------------------------------------------------------------------- |
| `demoSupCode`                                                | char(50)                                 | yes*     | yes       |          | Supplier unique code (e.g. `MYSUP`)                                              |
| `demoSupName`                                                | char(100)                                | yes      | yes       |          | Supplier name                                                                    |
| `demoSupDescription`                                         | text(1000000)                            |          | yes       |          | Supplier description                                                             |
| `demoSupPhone`                                               | phone(20)                                |          | yes       |          | Supplier phone number                                                            |
| `demoSupFax`                                                 | phone(20)                                |          | yes       |          | Supplier fax number                                                              |
| `demoSupWebsite`                                             | url(100)                                 |          | yes       |          | Supplier website URL                                                             |
| `demoSupEmail`                                               | email(20)                                |          | yes       |          | Supplier email address                                                           |
| `demoSupLogo`                                                | image                                    |          | yes       |          | Supplier logo                                                                    |
| `demoSupUsrId` link to **`User`**                            | id                                       |          | yes       |          | User responsible of supplier                                                     |
| _Ref. `demoSupUsrId.usr_login`_                              | _regexp(100)_                            |          |           | yes      | _Login_                                                                          |
| `demoSupComments`                                            | notepad(50000)                           |          | yes       |          | Comments on supplier                                                             |

### Custom actions

No custom action

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

`demo` external object definition
---------------------------------

Demo website using simple Mustache templating


`DemoCatalog` external object definition
----------------------------------------

Custom JSON web service for getting the product catalog


`DemoOrderAgenda` external object definition
--------------------------------------------

Order delivery agenda


`DemoPlaceNewOrder` external object definition
----------------------------------------------

Place new order internal page


`DemoWebSite` external object definition
----------------------------------------

Demo website developed in Javascript & Bootstrap


`demows` external object definition
-----------------------------------

Custom REST web services


