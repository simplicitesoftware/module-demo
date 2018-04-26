![](https://www.simplicite.io/resources//logos/logo250.png)
* * *

`Demo` module definition
========================

Order management demo

`DemoClient` business object definition
---------------------------------------

The **client** business object corresponds
to the customer who places order.

His address is geolocalized using GoogleMaps&reg; API.

### Fields

| Name                                                         | Type                                     | Req | Upd | Description                                                                      | 
| ------------------------------------------------------------ | ---------------------------------------- | --- | --- | -------------------------------------------------------------------------------- |
| `demoCliCode`                                                | char(10)                                 | x*  | x   | -                                                                                |
| `demoCliFirstname`                                           | char(100)                                | x   | x   | -                                                                                |
| `demoCliLastname`                                            | char(100)                                | x   | x   | -                                                                                |
| `demoCliAddress1`                                            | char(100)                                | x   | x   | -                                                                                |
| `demoCliAddress2`                                            | char(100)                                |     | x   | -                                                                                |
| `demoCliAddress3`                                            | char(100)                                |     | x   | -                                                                                |
| `demoCliZipCode`                                             | char(10)                                 | x   | x   | -                                                                                |
| `demoCliCity`                                                | char(50)                                 | x   | x   | -                                                                                |
| `demoCliCountry`                                             | enum(7) using `DEMO_COUNTRY` list        | x   | x   | -                                                                                |
| `demoCliCoords`                                              | geocoords                                |     |     | -                                                                                |
| `demoCliEmail`                                               | email(50)                                |     | x   | -                                                                                |
| `demoCliHomePhone`                                           | phone(20)                                |     | x   | -                                                                                |
| `demoCliWorkPhone`                                           | phone(20)                                |     | x   | -                                                                                |
| `demoCliMobilePhone`                                         | phone(20)                                |     | x   | -                                                                                |
| `demoCliFax`                                                 | phone(20)                                |     | x   | -                                                                                |
| `demoCliType`                                                | enum(7) using `DEMO_CLI_TYPE` list       | x   | x   | -                                                                                |
| `demoCliComments`                                            | text(1000000)                            |     | x   | -                                                                                |
| `demoCliPlacemapLabel`                                       | char(100)                                |     | x   | Place map label                                                                  |

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

| Name                                                         | Type                                     | Req | Upd | Description                                                                      | 
| ------------------------------------------------------------ | ---------------------------------------- | --- | --- | -------------------------------------------------------------------------------- |
| `demoCtcDatetime`                                            | datetime                                 | x*  |     | -                                                                                |
| `demoCtcType`                                                | enum(7) using `DEMO_CTC_TYPE` list       | x   | x   | -                                                                                |
| `demoCtcSubType`                                             | enum(7) using `DEMO_CTC_SUBTYPE` list    |     | x   | -                                                                                |
| `demoCtcCanal`                                               | enum(7) using `DEMO_CTC_CANAL` list      | x   | x   | -                                                                                |
| `demoCtcPriority`                                            | boolean                                  | x   | x   | -                                                                                |
| `demoCtcStatus`                                              | enum(7) using `DEMO_CTC_STATUS` list     | x   | x   | -                                                                                |
| `demoCtcCliId` link to **`DemoClient`**                      | id                                       | x*  | x   | -                                                                                |
| _Ref. `demoCtcCliId.demoCliCode`_                            | _char(10)_                               |     |     | -                                                                                |
| _Ref. `demoCtcCliId.demoCliFirstname`_                       | _char(100)_                              |     |     | -                                                                                |
| _Ref. `demoCtcCliId.demoCliLastname`_                        | _char(100)_                              |     |     | -                                                                                |
| _Ref. `demoCtcCliId.demoCliEmail`_                           | _email(50)_                              |     |     | -                                                                                |
| _Ref. `demoCtcCliId.demoCliHomePhone`_                       | _phone(20)_                              |     |     | -                                                                                |
| _Ref. `demoCtcCliId.demoCliWorkPhone`_                       | _phone(20)_                              |     |     | -                                                                                |
| _Ref. `demoCtcCliId.demoCliMobilePhone`_                     | _phone(20)_                              |     |     | -                                                                                |
| _Ref. `demoCtcCliId.demoCliFax`_                             | _phone(20)_                              |     |     | -                                                                                |
| `demoCtcOrdId` link to **`DemoOrder`**                       | id                                       |     | x   | -                                                                                |
| _Ref. `demoCtcOrdId.demoOrdNumber`_                          | _int(11)_                                |     |     | _Order **number**<br/>Automatically calculated at creation_                      |
| _Ref. `demoCtcOrdId.demoOrdDate`_                            | _date_                                   |     |     | -                                                                                |
| _Ref. `demoCtcOrdId.demoOrdStatus`_                          | _enum(7) using `DEMO_ORD_STATUS` list_   |     |     | -                                                                                |
| _Ref. `demoCtcOrdId.demoOrdCliId`_                           | _id_                                     |     |     | -                                                                                |
| _Ref. `demoCtcOrdId.demoOrdPrdId`_                           | _id_                                     |     |     | -                                                                                |
| _Ref. `demoOrdPrdId.demoPrdSupId`_                           | _id_                                     |     |     | _Product **supplier** identifier._                                               |
| _Ref. `demoPrdSupId.demoSupUsrId`_                           | _id_                                     |     |     | -                                                                                |
| _Ref. `demoOrdPrdId.demoPrdReference`_                       | _char(10)_                               |     |     | -                                                                                |
| _Ref. `demoOrdPrdId.demoPrdName`_                            | _char(100)_                              |     |     | -                                                                                |
| _Ref. `demoCtcOrdId.demoOrdQuantity`_                        | _int(11)_                                |     |     | -                                                                                |
| `demoCtcComments`                                            | html(50000)                              |     | x   | -                                                                                |
| `demoCtcFile`                                                | document                                 |     | x   | -                                                                                |

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

| Name                                                         | Type                                     | Req | Upd | Description                                                                      | 
| ------------------------------------------------------------ | ---------------------------------------- | --- | --- | -------------------------------------------------------------------------------- |
| `row_ref_id` link to **`DemoContact`**                       | id                                       | x*  |     | -                                                                                |
| `row_idx`                                                    | int(11)                                  | x*  | x   | -                                                                                |
| `created_by_hist`                                            | char(100)                                | x*  |     | -                                                                                |
| `created_dt_hist`                                            | datetime                                 | x*  |     | -                                                                                |
| `demoCtcComments`                                            | html(50000)                              |     | x   | -                                                                                |
| `demoCtcFile`                                                | document                                 |     | x   | -                                                                                |
| `demoCtcStatus`                                              | enum(7) using `DEMO_CTC_STATUS` list     | x   | x   | -                                                                                |

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

| Name                                                         | Type                                     | Req | Upd | Description                                                                      | 
| ------------------------------------------------------------ | ---------------------------------------- | --- | --- | -------------------------------------------------------------------------------- |
| `demoOrdNumber`                                              | int(11)                                  | x*  |     | Order **number**<br/>Automatically calculated at creation                        |
| `demoOrdDate`                                                | date                                     |     |     | -                                                                                |
| `demoOrdStatus`                                              | enum(7) using `DEMO_ORD_STATUS` list     | x   | x   | -                                                                                |
| `demoOrdDeliveryDate`                                        | datetime                                 |     | x   | -                                                                                |
| `demoOrdCliId` link to **`DemoClient`**                      | id                                       | x*  | x   | -                                                                                |
| _Ref. `demoOrdCliId.demoCliCode`_                            | _char(10)_                               |     |     | -                                                                                |
| _Ref. `demoOrdCliId.demoCliFirstname`_                       | _char(100)_                              |     |     | -                                                                                |
| _Ref. `demoOrdCliId.demoCliLastname`_                        | _char(100)_                              |     |     | -                                                                                |
| _Ref. `demoOrdCliId.demoCliEmail`_                           | _email(50)_                              |     |     | -                                                                                |
| _Ref. `demoOrdCliId.demoCliAddress1`_                        | _char(100)_                              |     |     | -                                                                                |
| _Ref. `demoOrdCliId.demoCliAddress2`_                        | _char(100)_                              |     |     | -                                                                                |
| _Ref. `demoOrdCliId.demoCliAddress3`_                        | _char(100)_                              |     |     | -                                                                                |
| _Ref. `demoOrdCliId.demoCliZipCode`_                         | _char(10)_                               |     |     | -                                                                                |
| _Ref. `demoOrdCliId.demoCliCity`_                            | _char(50)_                               |     |     | -                                                                                |
| _Ref. `demoOrdCliId.demoCliCountry`_                         | _enum(7) using `DEMO_COUNTRY` list_      |     |     | -                                                                                |
| `demoOrdPrdId` link to **`DemoProduct`**                     | id                                       | x*  | x   | -                                                                                |
| _Ref. `demoOrdPrdId.demoPrdReference`_                       | _char(10)_                               |     |     | -                                                                                |
| _Ref. `demoOrdPrdId.demoPrdName`_                            | _char(100)_                              |     |     | -                                                                                |
| _Ref. `demoOrdPrdId.demoPrdPicture`_                         | _image_                                  |     |     | -                                                                                |
| _Ref. `demoOrdPrdId.demoPrdSupId`_                           | _id_                                     |     |     | _Product **supplier** identifier._                                               |
| _Ref. `demoPrdSupId.demoSupCode`_                            | _char(10)_                               |     |     | _Supplier unique **code** (e.g. `MYSUP`)_                                        |
| _Ref. `demoPrdSupId.demoSupName`_                            | _char(100)_                              |     |     | _Supplier **name** (e.g. `My supplier`)_                                         |
| _Ref. `demoPrdSupId.demoSupLogo`_                            | _image_                                  |     |     | -                                                                                |
| _Ref. `demoPrdSupId.demoSupUsrId`_                           | _id_                                     |     |     | -                                                                                |
| _Ref. `demoOrdPrdId.demoPrdStock`_                           | _int(11)_                                |     |     | -                                                                                |
| _Ref. `demoOrdPrdId.demoPrdUnitPrice`_                       | _float(11, 2)_                           |     |     | -                                                                                |
| `demoOrdUnitPrice`                                           | float(11, 2)                             |     |     | -                                                                                |
| `demoOrdQuantity`                                            | int(11)                                  | x   | x   | -                                                                                |
| `demoOrdTotal`                                               | float(11, 2)                             |     |     | -                                                                                |
| `demoOrdVAT`                                                 | float(11, 2)                             |     |     | -                                                                                |
| `demoOrdComments`                                            | notepad(50000)                           |     | x   | -                                                                                |

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

| Name                                                         | Type                                     | Req | Upd | Description                                                                      | 
| ------------------------------------------------------------ | ---------------------------------------- | --- | --- | -------------------------------------------------------------------------------- |
| `row_ref_id` link to **`DemoOrder`**                         | id                                       | x*  |     | -                                                                                |
| `row_idx`                                                    | int(11)                                  | x*  | x   | -                                                                                |
| `created_by_hist`                                            | char(100)                                | x*  |     | -                                                                                |
| `created_dt_hist`                                            | datetime                                 | x*  |     | -                                                                                |
| `demoOrdStatus`                                              | enum(7) using `DEMO_ORD_STATUS` list     | x   | x   | -                                                                                |
| `demoOrdQuantity`                                            | int(11)                                  | x   | x   | -                                                                                |

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

| Name                                                         | Type                                     | Req | Upd | Description                                                                      | 
| ------------------------------------------------------------ | ---------------------------------------- | --- | --- | -------------------------------------------------------------------------------- |
| `demoPrdSupId` link to **`DemoSupplier`**                    | id                                       | x*  | x   | Product **supplier** identifier.                                                 |
| _Ref. `demoPrdSupId.demoSupCode`_                            | _char(10)_                               |     |     | _Supplier unique **code** (e.g. `MYSUP`)_                                        |
| _Ref. `demoPrdSupId.demoSupName`_                            | _char(100)_                              |     |     | _Supplier **name** (e.g. `My supplier`)_                                         |
| _Ref. `demoPrdSupId.demoSupLogo`_                            | _image_                                  |     |     | -                                                                                |
| _Ref. `demoPrdSupId.demoSupUsrId`_                           | _id_                                     |     |     | -                                                                                |
| `demoPrdReference`                                           | char(10)                                 | x*  | x   | -                                                                                |
| `demoPrdName`                                                | char(100)                                | x   | x   | -                                                                                |
| `demoPrdDescription`                                         | text(1000000)                            |     | x   | -                                                                                |
| `demoPrdPicture`                                             | image                                    |     | x   | -                                                                                |
| `demoPrdStock`                                               | int(11)                                  | x   | x   | -                                                                                |
| `demoPrdUnitPrice`                                           | float(11, 2)                             | x   | x   | -                                                                                |
| `demoPrdAvailable`                                           | boolean                                  | x   | x   | -                                                                                |
| `demoPrdDocumentation`                                       | html(1000000)                            |     | x   | -                                                                                |
| `demoPrdBrochure`                                            | document                                 |     | x   | -                                                                                |
| `demoPrdComments`                                            | notepad(50000)                           |     | x   | -                                                                                |

### Custom actions

* `DEMO_DECSTOCK`: Product stock **decrement** triggered by the order
state transition to _shipped_ status.
* `DEMO_INCSTOCK`: Sample action for product stock **increment**
(by `N` specified in the product business object code).

`DemoProductHistoric` business object definition
------------------------------------------------



### Fields

| Name                                                         | Type                                     | Req | Upd | Description                                                                      | 
| ------------------------------------------------------------ | ---------------------------------------- | --- | --- | -------------------------------------------------------------------------------- |
| `row_ref_id` link to **`DemoProduct`**                       | id                                       | x*  |     | -                                                                                |
| `row_idx`                                                    | int(11)                                  | x*  | x   | -                                                                                |
| `created_by_hist`                                            | char(100)                                | x*  |     | -                                                                                |
| `created_dt_hist`                                            | datetime                                 | x*  |     | -                                                                                |
| `demoPrdReference`                                           | char(10)                                 | x*  | x   | -                                                                                |
| `demoPrdName`                                                | char(100)                                | x   | x   | -                                                                                |
| `demoPrdUnitPrice`                                           | float(11, 2)                             | x   | x   | -                                                                                |
| `demoPrdAvailable`                                           | boolean                                  | x   | x   | -                                                                                |

### Custom actions

No custom action

`DemoSupplier` business object definition
-----------------------------------------

The **supplier** business object corresponds to the
suppliers of products that can be ordered.

### Fields

| Name                                                         | Type                                     | Req | Upd | Description                                                                      | 
| ------------------------------------------------------------ | ---------------------------------------- | --- | --- | -------------------------------------------------------------------------------- |
| `demoSupCode`                                                | char(10)                                 | x*  | x   | Supplier unique **code** (e.g. `MYSUP`)                                          |
| `demoSupName`                                                | char(100)                                | x   | x   | Supplier **name** (e.g. `My supplier`)                                           |
| `demoSupDescription`                                         | text(1000000)                            |     | x   | -                                                                                |
| `demoSupPhone`                                               | phone(20)                                |     | x   | -                                                                                |
| `demoSupFax`                                                 | phone(20)                                |     | x   | -                                                                                |
| `demoSupWebsite`                                             | url(100)                                 |     | x   | -                                                                                |
| `demoSupEmail`                                               | email(20)                                |     | x   | -                                                                                |
| `demoSupLogo`                                                | image                                    |     | x   | -                                                                                |
| `demoSupUsrId` link to **`User`**                            | id                                       |     | x   | -                                                                                |
| _Ref. `demoSupUsrId.usr_login`_                              | _regexp(100)_                            |     |     | -                                                                                |
| `demoSupComments`                                            | notepad(50000)                           |     | x   | -                                                                                |

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
