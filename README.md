# Example android webview integration project

### Installation
All of the dependencies are in the gradle file

### Running
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

### Server requirements

To run the example, your API must expose two endpoints, ```/access_token``` and ```/user```. These
endpoints must return the access_token that was retrieved from Basiq's API with the CLIENT_CREDENTIALS
scope, and a user_id that will be used to create the connection.

You can find an example server implementation [here](https://github.com/basiqio/basiq-blink-server-example)