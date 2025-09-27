<p align="center"><img src="./logo.png" height="300" width="400" alt="logo"/></p>
<p align="center">快速、规范、优雅的 Spring Boot 开发套件</p>

---

<p align="center">
    <a href="https://spring.io/projects/spring-boot" target="_blank"><img src="https://img.shields.io/badge/Spring%20Boot-3.5.6-blue.svg?logo=spring" alt="Spring Boot 3.5.6"></a>
</p>
<p align="center">
    <a href="#" target="_blank"><img src="https://img.shields.io/badge/Version-3.5.6.1-red.svg?logo=spring" alt="Version 3.5.6.1"></a>
    <a href="https://bell-sw.com/pages/downloads/#downloads" target="_blank"><img src="https://img.shields.io/badge/JDK-17%2B-green.svg?logo=openjdk" alt="Java 17"></a>
    <a href="./LICENSE"><img src="https://shields.io/badge/License-Apache--2.0-blue.svg?logo=apache" alt="License Apache 2.0"></a>
</p>


---

## Momentum Suite

**Momentum Suite**是一个基于 Spring Boot 的、精心设计的单体应用脚手架。它严格遵循阿里巴巴开发规范与领域驱动设计（DDD）思想，整合了一系列业界最佳实践，帮助开发者快速搭建一个结构清晰、代码规范、可维护、可演进的现代化后端项目。

### 1.核心特性

🚀 快速启动: 开箱即用，跳过繁琐的初始配置，直接进入业务开发。

🏛️ 优雅架构: 采用模块化的单体设计，逻辑分层清晰，易于理解和维护。

📜 最佳实践: 深度整合业界广泛认可的开发规范（如阿里巴巴Java开发手册），代码质量有保障。

🌱 平滑演进: 精心设计的结构为未来向微服务架构迁移预留了可能性。

🛠️ 完整套件: 内置统一响应、全局异常、多环境配置等，提供一站式解决方案。


---

## 2.架构详解

### 依赖关系图

```text
momentum-suite (父项目，统一管理依赖和插件)
│
├── momentum-suite-app -- 应用主入口和Web层 (负责启动、Controller、全局配置)
│
├── momentum-suite-client -- 对外契约和数据模型 (定义API接口、DTO、通用响应)
│
├── momentum-suite-service -- 核心业务逻辑实现 (实现client接口，编排业务流程)
│
├── momentum-suite-domain -- 领域模型和核心规则 (定义领域对象和核心业务行为)
│
└── momentum-suite-infrastructure -- 基础设施和技术实现 (负责数据库、缓存等具体技术)

```

### 模块职责清单

---

#### 📦 `momentum-suite-app`
*   **角色**: **应用入口**
*   **职责**: 包含项目启动类、`Controller` 层、全局配置（AOP、异常处理）和多环境配置文件。
*   **依赖清单**:
    *   `momentum-suite-service` (为了调用业务逻辑的实现)
    *   `momentum-suite-client` (为了在 Controller 中使用 DTO 和 Facade 接口)
---

#### 📦 `momentum-suite-service`
*   **角色**: **业务逻辑**
*   **职责**: 实现 `client` 模块的 `Facade` 接口，编排 `domain` 和 `infrastructure` 的能力，完成具体业务流程。
*   **依赖清单**:
    *   `momentum-suite-client` (因为它必须**实现** `client` 中定义的接口)
    *   `momentum-suite-domain` (为了使用领域对象来执行核心业务规则)
    *   `momentum-suite-infrastructure` (为了调用 `Mapper` 来访问数据库或调用其他基础设施)
---

#### 📦 `momentum-suite-client`
*   **角色**: **数据模型**
*   **职责**: 定义 `Facade` 接口、数据传输对象 (`DTO`, `VO`)、通用响应体 (`ApiResponse`) 和全局异常。
*   **依赖**: **(无)**
---

#### 📦 `momentum-suite-domain`
*   **角色**: **业务核心**
*   **职责**: 定义包含业务行为的领域对象（Domain Object）和最核心、最纯粹的业务规则。
*   **依赖**: **(无)**

---

#### 📦 `momentum-suite-infrastructure`
*   **角色**: **技术实现**
*   **职责**: 提供所有技术相关的具体实现，如数据库访问（`Mapper`, `PO`）、缓存操作、消息队列等。
*   **依赖清单**:
    *   `momentum-suite-domain` (因为它需要知道要为哪个领域对象提供持久化能力，并进行 `PO` 与 `Domain Object` 的转换)
---

