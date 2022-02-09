# Storaging
[简介](#简介) | [部署](#部署)

## 简介
对象储存与配置服务

## 部署

### Helm 部署
选择此部署方式必须先安装 [Helm](https://helm.sh)。  
请查看 Helm 的 [文档](https://helm.sh/docs) 获取更多信息。

当 Helm 安装完毕后，使用下面命令添加仓库：

    helm repo add storaging https://dustlight-cn.github.io/storaging

若您已经添加仓库，执行命令 `helm repo update` 获取最新的包。
您可以通过命令 `helm search repo storaging` 来查看他们的 charts。

创建配置文件 values.yaml：
```yaml
ingress:
  enabled: true
  className: "nginx" # Ingress Class Name
  host: "storaging.dustlight.cn" # 服务域名
  tls:
    enabled: false # 是否开启 HTTPS
    crt: ""
    key: ""

storage:
  type: tencent # 云对象储存类型： tencent 或 alibaba
  tencent:
    secretKey: "xx"
    secretId: "xx"
    bucket: "xx"
    region: "xx"
  alibaba:
    accessKeyId: ""
    secretAccessKey: ""
    bucket: ""
    endpoint: ""

config:
  mongo:
    url: "mongodb+srv://username:password@mongo-svc/storaging?ssl=false" # MongoDB 连接 URL
    collection:
      config: "config" # 配置集合
      object: "object" # 对象集合
  auth:
    clientId: "test" #替换为自己的 ClientID
    clientSecret: "e6423085f5165a58f8949e763c6691ffe44e2f86" #替换为自己的 ClientSecret
```

安装：

    helm install -f values.yaml my-storaging storaging/storaging-service

卸载：

    helm delete my-storaging

