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

...

## 使用说明

...

---

## 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request
