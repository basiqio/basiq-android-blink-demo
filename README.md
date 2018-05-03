# Example android webview integration project

## Installation
All of the dependencies are in the gradle file

## Running
Make sure to create a config.properties file in the following directory

```
$ touch app/src/main/res/raw/config.properties
```

The file should hold the values of the urls of the webview server, and your client server that will
be responsible for creating a user and fetching the access token.

Example:
```
api_url=http://0.0.0.0
views_url=http://js.basiq.io/index.html
```

## Server requirements

To run the example, your API must expose two endpoints, ```/access_token``` and ```/user```. These
endpoints must return the access_token that was retrieved from Basiq's API with the CLIENT_CREDENTIALS
scope, and a user_id that will be used to create the connection.

You can find an example server implementation [here](https://github.com/basiqio/basiq-blink-server-example)


## Communication

Your application can communicate with the basic webview app using custom protocols. You can track
these protocol invocations by implementing a custom ```WebViewClient``` and override the
```shouldOverrideUrlLoading ``` method. The ```shouldOverrideUrlLoading``` method is invoked
every time a url change is triggered in the webview with the change event data. Inside the
event data the url is available, and you parse the url to get the necessary info.

## Example WebViewClient

You can find the source code from the example [here](https://github.com/basiqio/basiq-android-blink-demo/blob/master/app/src/main/java/com/example/nlukic/webviewtest/utils/WebViewClientWithListener.java).

As you can see in the following code snippet:

```java
if (url != null && url.startsWith("basiq://")) {
    Integer payloadStart = url.indexOf("{");
    String payloadString;
    String event;
    if (payloadStart != -1) {
        payloadString = url.substring(payloadStart);
        event = url.substring(url.indexOf("//") + 2, payloadStart - 1);
    } else {
        payloadString = "{}";
        event = url.substring(url.indexOf("//") + 2);
    }
    try {
        JSONObject payload = new JSONObject(payloadString);
        parseEventData(event, payload);
    } catch (Exception ex) {
        Log.v("ReceivedProtocolError", "JSON exception: "+ex.getMessage());
    }
    Log.v("ReceivedProtocol", "URI: " + url + " | Event: " + event + " | Payload: " + payloadString);
    return true;
} else {
    return false;
}
```

We detect if the url starts with the "basiq" protocol, and in that case parse it. The
payload is a JSON string, and it comes after the event name. Example:

```
basiq://connection/{"id":"klqwd-qwdoijjoqd102wq-djkqw"}
```

In the example, *connection* is the event name, and the payload contains the connection id
in the JSON. We can parse the payload by detecting if any payload exists by checking for the
opening brace, and then trying to parse the JSON using ```JSONObject```