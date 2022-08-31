# 构建指南

## 系统要求

- OpenJDK 17（可以是任意版本，推荐官方版和Temurin）

### 安装 OpenJDK

#### Ubuntu

运行以下命令：

```shell
sudo apt install openjdk-17-jdk
```

#### Windows/MacOS

我们建议使用 Eclipse Temurin。

## 构建

在项目根目录运行 `./mvnw package` （或者如果你电脑里面安装了 Maven，直接运行 `mvn package`）即可。Maven
会自动下载所需要的依赖，编译代码并打包为 `jar`。

编译出的 jar 位于 `target` 目录下。