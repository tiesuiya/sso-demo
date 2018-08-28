## 手动实现[CAS](https://apereo.github.io/cas/4.2.x/protocol/CAS-Protocol.html)协议

## host config
```$xslt
127.0.0.1 www.sso.com
127.0.0.1 www.crm.com
127.0.0.1 www.erp.com
```

## how to use
- 启动 sso-server sso-client-demo-crm sso-client-demo-erp
- 登录 `http://www.crm.com:9001/` `http://www.erp.com:9002/` 其中任意一个，会发现另一个已经登录  

#### 已知BUG
- 访问`http://www.crm.com:9001/data/page`，再访问`http://www.crm.com:9002/data/page`这样的话会陷入循环，无意间碰到的，现实中不应该可能出现