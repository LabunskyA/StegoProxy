# StegoProxy
A Java library and an application to create staganography proxy connections over the Internet.

# Usage
Simply download client and/or server .jar files from the releases page and run them with java 8 or later.
Use -h or --help keys for more info.

At a client machine you should use steganography <b>ProxyClient.jar</b> as a proxy server.
It incapsulates your data into the steganographic channel and sends it to server with an active proxy server.

<b>ProxyServer.jar</b> should be running at the external server and connected to the Real Proxy server, like Squid.
It receives incapsulated data, decodes and retranslates in to the Real Proxy, so data could reach it's real destination.

# How it works
<b>StegoProxy</b> is a combination of ProxyServer and ProxyClient modules.

<b>ProxyClient</b> is a client side of StegoProxy.
You can interract with it like with any other proxies.
It sends any incoming data throught the steganography chanel to ProxyServer.

<b>StegoServer</b> is a server side of StegoProxy.
It automaticaly interracts with incoming steganography data and resends it to the Real Proxy (like Squid).

<b>The scheme looks like this:</b> <br>
Browser (or any other app) <-regular network-> ProxyClient [<-steganographic channel->] ProxyServer <-regular network-> Real Proxy

# Modify or contribute
You can use your own steganography algorithms and container types. <br>
All you need is to implement [Steganography](https://github.com/LabunskyA/StegoProxy/blob/master/src/pw/stego/network/container/steganography/Steganography.java) and [ContainerFactory](https://github.com/LabunskyA/StegoProxy/blob/master/src/pw/stego/network/container/util/ContainerFactory.java) interfaces.
