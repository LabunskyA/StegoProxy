# StegoProxy
Java library and application to create staganography proxy connections over the Internet.

# Usage
Simply download client and/or server .jar files from releases page and run them with JRE 8+. It will print you what to do next.

At you client machine you should use steganography ProxyClient.jar as proxy server.
It will incapsulate your data into steganography channel and send it to server with proxy server.

ProxyServer.jar should be running at external server and connected to real proxy server, like Squid.
It will receive incapsulated data, decode and retranslate in to real proxy so it could reach real destination.

# Contribute
You can create your own steganography algorithms and containers factories. 
All you need is to implement Steganography and ContainerFactory interfaces.

I'll be glad to see your pull requests!

# Help project
You can help or thank me with developing it in several ways:
<ul>
  <li><a href="https://www.paypal.me/labunsky">PayPal</a></li>
  <li><a href="https://qiwi.me/stegoproxy">QIWI</a></li>
  <li>Bitcoin: 3PKGGwo5Ke2zm1yiBnuowZLEbmawz9YpJ8</li>
  <li>Monero: 4AbP6jGakTXMvqZYkqDGWLhssFwHmRXQ5QNWCMH4hMWEckj3sU7949qd2PrzpawBQpD38VkcMTXqNf46HPFU4YZzH5vetQa</li>
<ul>
