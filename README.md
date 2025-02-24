# license-server

---

## 介绍

许可证（授权码）服务器是一个基于 Java 语言和 Spring 框架的 Web 项目，用于提供软件许可证（授权码）的生成、验证和管理服务。

同时提供了一套基于 TypeScript 语言和 Vue 框架的 Web 界面，用于产品管理，客户管理，许可证（授权码）管理等。

最后，提供了一组基于 Java，Python，Go，Rust 等多种语言的客户端 SDK，用于在客户端验证许可证（授权码）。

---

## 软件架构

软件架构包含三部分：

### server

服务器

- 采用 Java 语言和 Spring 框架开发
- 采用 MySQL 数据库存储数据
- 采用 Redis 缓存数据
- 提供对接 Web admin 的 API 服务
- 提供软件许可证（授权码）的生成、验证和管理服务

### admin

管理平台

- 采用 TypeScript 语言和 Vue 框架开发
- 采用 Element UI 组件库和 pure-admin 模板
- 提供产品管理，客户管理，许可证（授权码）管理等模块功能

---

## 安装教程

推荐使用 Docker 安装，具体步骤如下：

compose.yaml 文件示例：

```yaml
services:

  redis:
    image: redis:latest
    container_name: license-redis
    # ports:
      # - "6379:6379"
    volumes:
      - ./data/license-redis:/data
    environment:
      - REDIS_MODE=standalone
      - REDIS_PORT=6379

  mysql:
    image: mysql:latest
    container_name: license-mysql
    # ports:
      # - "3306:3306"
    volumes:
      - ./data/license-mysql:/var/lib/mysql
      # - ./data/init-db.sql:/docker-entrypoint-initdb.d/init-db.sql
    environment:
      - MYSQL_ROOT_PASSWORD=root
      - MYSQL_DATABASE=license_server
      # - MYSQL_USER=license
      # - MYSQL_PASSWORD=license
      - MYSQL_CHARSET=utf8mb4
      - MYSQL_COLLATION=utf8mb4_bin
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
      interval: 5s
      timeout: 10s
      retries: 5

  license-server:
    image: registry.cn-beijing.aliyuncs.com/daodao-bot/license-server:t-20250224-0
    container_name: license-server
    # ports:
    #   - "8080:8080"
    volumes:
      - ./data/license-server:/data
    environment:
      - SERVER_PORT=8080
      - MYSQL_URL=jdbc:mysql://mysql:3306/license_server
      - MYSQL_USERNAME=root
      - MYSQL_PASSWORD=root
      - REDIS_HOST=redis
      - REDIS_PORT=6379
      - ADMIN_PASSWORD=123456
    depends_on:
      - redis
      - mysql
    restart: always

  license-admin:
    image: registry.cn-beijing.aliyuncs.com/daodao-bot/license-admin:t-20250224-2
    container_name: license-admin
    ports:
      - "8848:80"
    volumes:
      - ./data/license-admin:/data
    environment:
      - LICENSE_SERVER=http://license-server:8080

```

执行启动命令

```bash
docker compose up -d
```

## 使用说明

...

---

## 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request
