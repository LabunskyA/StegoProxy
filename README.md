# StegoProxy
Java library and application to create staganography proxy connections over the Internet.

# Usage
Simply download client and/or server .jar files from releases page and run them with java 8 or later. <br>
Run it without arguments if you want to know what to do next.

At you client machine you should use steganography <b>ProxyClient.jar</b> as proxy server. <br>
It incapsulates your data into steganography channel and sends it to server with proxy server.

<b>ProxyServer.jar</b> should be running at external server and connected to real proxy server, like Squid. <br>
It receives incapsulated data, decodes and retranslates in to real proxy, so data could reach it's real destination.

# How it works
<b>StegoProxy</b> is consisted of ProxyServer and ProxyClient modules.

<b>ProxyClient</b> is client-side of StegoProxy. <br>
You can interract with it like with any other proxies.
It sends any incoming data throught steganography chanel to ProxyServer.

<b>StegoServer</b> is server-side of StegoProxy. <br> 
It automaticaly interracting with incoming steganography data and resending it to Real Proxy (like Squid).

<b>The scheme:</b> <br>
Browser (or any other app) ---> ProxyClient [--->] ProxyServer --> Real Proxy

# Contribute
You can create your own steganography algorithms and containers factories. <br>
All you need is to implement <b><a href="https://github.com/LabunskyA/StegoProxy/blob/master/src/pw/stego/network/container/steganography/Steganography.java">Steganography</a></b> and <b><a href="https://github.com/LabunskyA/StegoProxy/blob/master/src/pw/stego/network/container/util/ContainerFactory.java">ContainerFactory</a></b> interfaces.

I'll be glad to see your pull requests!

# Help project
You can help or thank me with developing it in several ways:
<ul>
  <li><a href="https://www.paypal.me/labunsky">PayPal</a></li>
  <li><a href="https://qiwi.me/stegoproxy">QIWI</a></li>
  <li>BTC: 3PKGGwo5Ke2zm1yiBnuowZLEbmawz9YpJ8</li>
  <li>LTC: LLSdGK7Y8fWHpC833SufMqqF4HcpU5nXVP</li>
  <li>ETH: 0x332b2f5e23e5f9e89d7531d7e5e95a7616ec8bb0</li>
  <li>DOGE: DNpFyA9jSQYJyVYfLKetdNufYFgLimVezK</li>
  <li>XMR: 4AbP6jGakTXMvqZYkqDGWLhssFwHmRXQ5QNWCMH4hMWEckj3sU7949qd2PrzpawBQpD38VkcMTXqNf46HPFU4YZzH5vetQa</li>
<ul>
