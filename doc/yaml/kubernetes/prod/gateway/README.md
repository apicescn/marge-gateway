# gateWay说明
因为本项目采用统一gateway对外暴露，应用于panther小程序所用，故而此处gateway别名为panther-gateway,
而对于traefik是必须要配置SERVER_CONTEXT_PATH的，否则会无法访问。
此处gateway采用DaemonSet(守护线程),保证在每个Node上都运行一个容器副本以便于方便管理应用。