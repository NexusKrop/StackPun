# StackPun 更改日志

本文件记录所有 StackPun 的用户可见更改（至少自 2022/9/13 起，此日之前的更改可能不准确）。

## Unreleased

### 修改

* 现在聊天消息是由 ChatRenderer 来渲染了（使用原版信道）

### 移除

* 黑名单功能已被废弃，且不再可用
* 移除 `troll` 命令
* 移除 ProtocolLib 依赖

## [0.1.3-alpha] - 2022/9/14

### 新增

* 新增 `/anvil` 命令快速打开铁砧页面
* 新增 `/workbench` 命令快速打开9x9工作台页面
* 史莱姆不会再生成了
* 现在可以拦截并阻止部分爆炸了

### 修复

* 修复帮助信息未正确本地化的问题

### 修改

* 现在插件会从 `plugin.yml` 中获取插件本身的版本了
* 使用注解自动生成 `plugin.yml`

## [0.1.2-alpha] - 2022/9/13

### 修复

* 修复 `/mute`、`/unmute` 等命令不能使用的问题
* 修复 `/unmute` 无法解除禁言的问题

### 移除

* 移除 `/unmute` 命令自我解除禁言检查

## [0.1.1-alpha] - 2022/9/13

### 新增

* 新增 `/stackpun reload` 子命令重载配置文件。
* 新增 `/stackpun version` 子命令查看 StackPun 版本。
* 新增自定义格式服务器列表描述。

### 修改

* 将玩家的档案文件分离。

## 0.1.0-alpha

* 最初版本。