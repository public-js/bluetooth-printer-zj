# @public-js/bluetooth-printer-zj

Bluetooth printer support for Capacitor

## Install

```bash
npm install @public-js/bluetooth-printer-zj
npx cap sync
```

## API

<docgen-index>

* [`discover()`](#discover)
* [`connect(...)`](#connect)
* [`disconnect()`](#disconnect)
* [`print(...)`](#print)
* [Interfaces](#interfaces)
* [Type Aliases](#type-aliases)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### discover()

```typescript
discover() => Promise<DiscoverResponse>
```

**Returns:** <code>Promise&lt;<a href="#discoverresponse">DiscoverResponse</a>&gt;</code>

--------------------


### connect(...)

```typescript
connect(data: ConnectRequest) => Promise<ConnectResponse>
```

| Param      | Type                                                      |
| ---------- | --------------------------------------------------------- |
| **`data`** | <code><a href="#connectrequest">ConnectRequest</a></code> |

**Returns:** <code>Promise&lt;<a href="#connectresponse">ConnectResponse</a>&gt;</code>

--------------------


### disconnect()

```typescript
disconnect() => Promise<void>
```

--------------------


### print(...)

```typescript
print(data: PrintRequest) => Promise<void>
```

| Param      | Type                                                  |
| ---------- | ----------------------------------------------------- |
| **`data`** | <code><a href="#printrequest">PrintRequest</a></code> |

--------------------


### Interfaces


#### DiscoverResponse

| Prop          | Type                            |
| ------------- | ------------------------------- |
| **`devices`** | <code>DiscoveredDevice[]</code> |


#### DiscoveredDevice

| Prop          | Type                 |
| ------------- | -------------------- |
| **`address`** | <code>string</code>  |
| **`name`**    | <code>string</code>  |
| **`bonded`**  | <code>boolean</code> |


#### ConnectResponse

| Prop         | Type                |
| ------------ | ------------------- |
| **`device`** | <code>string</code> |


#### ConnectRequest

| Prop          | Type                |
| ------------- | ------------------- |
| **`address`** | <code>string</code> |


#### PrintRequest

| Prop            | Type                      |
| --------------- | ------------------------- |
| **`blocks`**    | <code>PrintBlock[]</code> |
| **`pageWidth`** | <code>number</code>       |
| **`feed`**      | <code>number</code>       |


#### PrintBlock

| Prop       | Type                                                      | Default         |
| ---------- | --------------------------------------------------------- | --------------- |
| **`type`** | <code><a href="#printblocktype">PrintBlockType</a></code> | <code>lf</code> |


#### PrintBlockLf

| Prop       | Type              |
| ---------- | ----------------- |
| **`type`** | <code>'lf'</code> |


#### PrintBlockTextRaw

| Prop              | Type                   | Default          |
| ----------------- | ---------------------- | ---------------- |
| **`type`**        | <code>'textRaw'</code> |                  |
| **`encoding`**    | <code>string</code>    | <code>GBK</code> |
| **`codepage`**    | <code>number</code>    | <code>0</code>   |
| **`scaleWidth`**  | <code>number</code>    | <code>0</code>   |
| **`scaleHeight`** | <code>number</code>    | <code>0</code>   |
| **`fontStyle`**   | <code>number</code>    | <code>0</code>   |
| **`align`**       | <code>number</code>    | <code>0</code>   |


#### PrintBlockTextDraw

| Prop            | Type                    | Default         |
| --------------- | ----------------------- | --------------- |
| **`type`**      | <code>'textDraw'</code> |                 |
| **`lineCount`** | <code>number</code>     |                 |
| **`fontSize`**  | <code>number</code>     | <code>16</code> |
| **`mode`**      | <code>number</code>     | <code>0</code>  |


#### PrintBlockTextQr

| Prop            | Type                  | Default        |
| --------------- | --------------------- | -------------- |
| **`type`**      | <code>'textQr'</code> |                |
| **`codeWidth`** | <code>number</code>   |                |
| **`mode`**      | <code>number</code>   | <code>0</code> |


### Type Aliases


#### PrintBlockType

<code>'lf' | 'textRaw' | 'textDraw' | 'textQr'</code>


#### PrintBlock

<code><a href="#printblocklf">PrintBlockLf</a> | <a href="#printblocktextraw">PrintBlockTextRaw</a> | <a href="#printblocktextdraw">PrintBlockTextDraw</a> | <a href="#printblocktextqr">PrintBlockTextQr</a></code>

</docgen-api>
