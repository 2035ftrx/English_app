这里下载单词的逻辑，会出现问题

因为单词量的问题，需要非常多的请求才能把数据下载下来

所以会使用循环访问接口的方式

但是这样会导致 okhttp 出现问题。

okhttp 的连接池并未对同一个接口频繁访问作出合理的处理。

这样会出现当频繁出现请求同一个接口时，出现大概以下问题：

java.net.ProtocolException: expected chunk size and optional extensions

java.net.ProtocolException: Expected leading [0-9a-fA-F] character but was 0x20

java.io.EOFException: \n not found: limit=1 content=0d…

