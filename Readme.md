# 项目说明

本项目是一个Java微服务技术栈的学习项目，其中涉及到的业务技术栈包括Mybatis Plus、Spring Boot、Spring Cloud、OpenFeign、Sentinel、RabbitMQ、Elasticsearch、Redis等，辅助技术包括Linux、Maven、Nginx、Docker等。

项目文档：https://b11et3un53m.feishu.cn/wiki/FYNkwb1i6i0qwCk7lF2caEq5nRe

# Day 1 - Mybatis Plus

见本地项目 `mybatisplus` ，此处省略。

# Day 2 - 环境搭建

由于教程中提供的CentOS 7已经于2024年6月30日停止维护，因此使用CentOS Stream 9进行搭建。

搭建教程：https://b11et3un53m.feishu.cn/wiki/MWQIw4Zvhil0I5ktPHwcoqZdnec

# Day 3 - 微服务 上 - 服务治理与远程调用

本节主要解决基础的微服务架构问题，包括服务注册与发现、跨服务调用等。

## 单体架构和微服务架构

### 单体架构

单体架构是指将所有功能模块**集中开发**，并打包在**一个**应用程序中进行部署和运行的架构模式。对黑马商城的原始项目来说，商品管理、用户管理、交易管理、购物车管理等模块都在同一个模块下开发。

#### 优势

单体架构的优点包括：

- 简单易用：结构简单，部署方便，适合小型应用。
- 性能较好：由于所有模块在同一进程中运行，通信开销较低。
- 易于测试：可以在本地环境中进行完整的测试。

#### 劣势

随着业务的发展、项目规模的扩大，单体架构逐渐暴露出一些缺点：

- **难以协作**：多人协作开发时，代码冲突频繁，影响开发效率。
- 难以维护：代码量庞大，模块间耦合度高，修改一个模块可能影响其他模块。
- 难以扩展：无法针对不同模块进行独立扩展，资源利用率低。
- **难以部署**：每次发布都需要重新打包整个应用，部署周期长。
- **难以差异化**：不同的功能模块可能有不同的访问量、安全性、稳定性等要求，单体架构难以满足这些差异化需求。
- 难以适应变化：技术栈和框架的更新困难，无法灵活应对业务需求的变化。

#### 性能测试

使用Apache JMeter对黑马商城的单体架构进行性能测试，发现其在高并发（1000个线程）访问带有延迟的接口时，无延迟的接口响应时间也变得较长（~2s）。

这说明单体架构在高并发场景下存在性能瓶颈，当某一个接口（此处为 `/hi` 访问计数接口）占用资源并发访问量太高时，Tomcat的线程池处于一种饱和状态，导致访问其他接口（此处为商品分页查询接口）的请求需要等待高并发接口的请求处理完毕才能得到响应。

注：还测试了200个线程访问SQL查询的接口，响应时间为3s左右，说明SQL查询本身也存在性能问题。

### 微服务架构

微服务架构是经服务化改造后的一套最佳实践架构模式，将单体应用拆分为多个**独立部署**、**独立运行**的服务，每个服务负责特定的业务功能模块。各个服务通过轻量级的通信机制（如HTTP、消息队列等）进行交互。

#### 优势

对于一个单体项目拆分成微服务的过程，指导的思想是小粒度、团队自治、服务自治，具体而言：

- 小粒度：每个微服务应尽量小，专注于单一业务功能，避免过度复杂化（但也并不意味着越小越好）。
- 团队自治：每个微服务由独立的团队负责开发、测试、部署和运维，团队可以选择适合自己的技术栈和工具。
- 服务自治：每个微服务应具备独立的数据库和数据存储，避免与其他服务共享数据库。

#### 解决单体架构的劣势

微服务架构通过以下方式解决了单体架构的劣势：

- 协作开发：不同团队可以独立开发各自负责的微服务，减少代码冲突，提高开发效率。
- 独立扩展：可以根据不同服务的安全性、稳定性、访问量和性能等需求，独立设计，独立扩展资源，提高资源利用率。
- 独立部署：每个微服务可以独立部署和发布，缩短部署周期。

## Spring Cloud概念入门

Spring Cloud是一个基于Spring Boot的微服务开发框架，提供了一整套解决方案来简化微服务的开发、部署和运维。它集成了众多开源组件，涵盖了服务注册与发现、配置管理、负载均衡、断路器、消息总线等功能。这些组件都遵循了Spring Cloud的设计标准，可以通过Spring Boot进行自动装配和依赖管理，大大简化了微服务的开发过程。

### 核心组件

| 组件类型   | 组件名称                            | 功能描述                         |
|--------|---------------------------------|------------------------------|
| 服务注册发现 | Eureka、Nacos、Consul             | 实现服务的注册与发现，支持服务实例的动态注册和注销。   |
| 统一配置管理 | Spring Cloud Config、Nacos       | 集中管理微服务的配置文件，支持配置的动态刷新。      |
| 服务远程调用 | OpenFeign、Dubbo、RestTemplate    | 简化服务间的HTTP调用，支持负载均衡和熔断机制。    |
| 统一网关路由 | Spring Cloud Gateway、Zuul       | 提供API网关功能，实现请求路由、负载均衡、安全认证等。 |
| 服务链路监控 | Zipkin、Sleuth、SkyWalking        | 实现分布式链路追踪，帮助定位性能瓶颈和故障。       |
| 断路器与熔断 | Hystrix、Resilience4j、Sentinel   | 提高系统的容错能力，防止级联故障，实现流控、降级、保护。 |
| 消息总线   | Spring Cloud Bus、RabbitMQ、Kafka | 实现微服务间的异步通信和事件驱动。            |

### 版本设置

Spring Cloud的版本与Spring Boot、Spring、JDK的版本密切相关，必须确保它们之间的兼容性。Spring 2.7.x版本对应的Spring Cloud版本为2021.0.x，JDK版本要求为11及以上。

在Maven中可以通过`spring-cloud-dependencies`来管理Spring Cloud的版本，其中定义了各个Spring Cloud组件的版本号。以下是一个示例配置：

```xml

<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>2021.0.3</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

## 黑马商城项目概览

### JWT校验功能

#### 校验过程

在登录过程中，首先通过用户名和密码，向数据库中查询用户信息条目。如果查询到的用户不为空且不为禁用状态，则进入密码校验。使用Spring Security中的 `PasswordEncoder` 的 `matches` 方法对用户输入的密码和数据库中存储的加密密码进行比对。如果密码匹配成功，则生成JWT令牌，并设置过期时间为30分钟。最后，将生成的JWT令牌返回给客户端浏览器。

当每次访问需要权限校验的接口时，客户端需要在请求头的 `authorization` 属性中携带JWT令牌。而后端通过拦截器对请求进行拦截，提取请求头中的JWT令牌，并对令牌进行了多重校验，包括Token的完整性、合法性、时效性、载荷 `user` 的有效性、载荷可转为 `Long` 类型等。只有当所有校验都通过后，才允许访问受保护的接口。后端会将转换格式后的载荷信息 `userId` 存入 `ThreadLocal` 变量中，供后续业务逻辑使用。

在生成Token的 `createToken` 方法中，使用的是Hutool的JWT工具包进行创建。且在创建中只将 `user` 的ID存入了载荷中，避免将敏感信息暴露在Token中。签名使用的是 `SHA256withRSA` 算法，确保Token的完整性和防篡改。签名时使用私钥，而在校验时使用公钥进行验证。

#### 细节解析

与苍穹外卖不同的是，在黑马商城中，拦截器在 `afterCompletion` 方法中清除了 `ThreadLocal` 变量，避免内存泄漏的问题。具体来说，Tomcat使用线程池处理请求，一个请求处理完以后，线程不会被销毁，而是被放回线程池中等待下一个请求。如果不清除 `ThreadLocal` 变量，那么 `userId` 数据会被该线程一直持有，并被下一个请求访问到，导致数据泄漏和错误。

如果不进行实现，考虑以下场景：用户A（id=123）登录并访问了一个接口，线程A处理了这个请求，并将 `userId=123` 存入了 `ThreadLocal` 变量中。请求处理完毕后，线程A被放回线程池中，带有 `userId=123` 。随后，用户B（id=456）登录并访问了另一个接口，线程A被再次分配给用户B的请求。如果用户B的请求因为某些原因（例如Token错误，考虑 `parseToken` 中的流程）在拦截器中校验失败，没有执行 `UserContext.setUser()` 覆盖 `ThreadLocal` 变量，那么 `ThreadLocal` 变量中仍然保存着 `userId=123`。这样，用户B的请求在后续的业务逻辑中可能会错误地使用用户A的ID，导致用户A的数据被读取甚至修改，引发严重的数据泄漏和权限问题。

#### 改进建议

在原本的实现中，在密码匹配成功后，一旦签发了一个Token，在过期之前就是一直有效的。那么如果用户更改了密码，或者管理员想强制让某用户下线，那么服务器是做不到的。有两种解决方案：

1. 在登录时，可以将Token存储在Redis中，以便后续的会话管理和权限校验
2. 在登出或被禁用时，则将Token移入一个黑名单中，并设置与Token相同的TTL，禁止其继续使用。拦截器在校验完Token以后，还需要检查该Token是否在黑名单中，如果在则拒绝访问。

另外，针对自动失效的问题，也可以推出自动续签功能增强体验，将访问Token和刷新Token结合使用。访问Token的过期时间设置较短（如30分钟），而刷新Token的过期时间设置较长（如7天）。当访问Token过期时，客户端可以使用刷新Token向服务器请求一个新的访问Token，而无需重新登录。

### 其他功能

其他功能包括商品管理、用户管理、交易管理、购物车管理等，均在单体架构中实现，具体细节见项目代码。

## 微服务拆分基础

### 服务拆分的原则

针对不同的开发阶段，可以采用不同的服务拆分策略：

1. 创业型项目：此阶段业务需求变化较快，团队规模较小，建议先采用单体架构，快速迭代和验证业务模型，随着规模扩大再逐渐拆分。
2. 确定的大型项目：资金充足，目标明确，可以直接采用微服务架构，按照业务领域进行拆分，确保每个服务职责单一，避免后续拆分麻烦。

从拆分目标而言，需要做到：

1. 高内聚：每个微服务的职责需要尽量单一，包含的业务相互关联度高、完整度高。例如将来在功能迭代时，修改一个微服务的代码时几乎都是在服务内部的，几乎不会影响到其他服务。
2. 低耦合：每个微服务的功能要相对独立，避免与其他服务有过多的依赖关系。不过一些复杂业务或者不可避免的跨服务依赖关系是可以接受的。

从拆分方式而言，一般分为两种方式：

1. 纵向拆分：分布式的思想，按照业务模块进行拆分，例如商品服务、用户服务、订单服务等。每个服务负责特定的业务功能，具有独立的数据库和数据存储。
2. 横向拆分：AOP的思想，抽取公共服务，按照技术栈进行拆分，提高复用性，例如认证服务、网关服务、配置中心等。每个服务提供通用的功能，供其他服务调用。

### 黑马商城的拆分方案

根据黑马商城的复杂程度，初步采用纵向拆分的方案，将单体应用拆分为：用户模块、商品模块、购物车模块、订单模块、支付模块五个微服务。

在创建项目时，有两种工程结构：

1. 完全解耦：每一个微服务都是一个独立的项目，拥有自己的目录结构。
    - 优点：每个服务可以独立开发、测试、部署和运维，甚至可以用不同的语言
    - 缺点：每个项目都需要创建独立仓库，管理多个项目较为复杂。
    - 应用场景：适合大型项目，团队规模较大，服务之间依赖较少的场景。
2. Maven聚合：整个项目是一个独立的Project，然后每个微服务是其中的一个Module，将来独立打包和部署。
    - 优点：所有服务都在同一个仓库中，便于管理和协作，且可以共享一些公共配置和依赖。
    - 缺点：所有服务的代码都在同一个仓库中，可能会导致代码量庞大，影响开发效率。
    - 应用场景：适合中小型项目，团队规模较小，服务之间依赖较多的场景。

为方便起见，直接采用Maven聚合的方式进行拆分

### 初步拆分步骤

保留 `hmall` 父项目，在下面创建不同的子模块。创建后需要做的修改包括：

1. 修改子模块的名称为 `xxx-service`，例如 `item-service`、`cart-service` 等。
2. 创建好模块后，分别在源代码目录下创建包 `com.hmall.xxx` ，并在其中分别创建 `controller`、`service`、`mapper`、`domain`、`config` 等包。
3. 在模块下创建启动类，命名为 `XxxApplication`，并添加 `@SpringBootApplication` 和 `@MapperScan("com.hmall.xxx.mapper")` 注解。
4. **注意真实业务场景下需要针对每个服务创建独立的数据库实例**。此处为方便起见，直接在单体SQL容器中创建不同的数据库，例如 `hm-item`、`hm-cart` 等，并将提供的SQL脚本分别导入到不同的数据库中。
5. 将原本的yaml配置复制到 `resources` 目录下，并修改端口号为不同的值，例如 `item-service` 为8081，`cart-service` 为8082等。修改`spring.application.name` 为对应的服务名称，例如 `item-service`。修改数据库连接为对应的数据库。修改 `knife4j` 配置的 `title` 和 `description` 为对应的服务名称，修改扫描的包路径为 `com.hmall.xxx.controller`。
6. 修改 `pom.xml` 文件，将原本的依赖复制到各个子模块中，并删除与业务无关的依赖。
7. 将与业务相关的代码复制并重构到各个子模块中，例如 `ItemController`、`ItemService`、`ItemMapper`、`Item` 等类复制到 `item-service` 中，其他模块同理。重构时需要注意包名和类名的修改。

### 自动装配与lombok的安全性问题

#### 为什么Idea不推荐自动装配注入（字段注入）

需要重申IoC（控制反转）的核心思想，即是将对象的控制权（创建、管理、装配）从代码本身转移到外部Spring容器，而不是在代码中手动创建对象和管理依赖关系。这样可以实现松耦合、易测试和易维护。

但字段注入存在以下缺点：

1. 隐藏了类的依赖关系：一个类的必要依赖最好在创建时就明确指出，**而不是通过反射在运行时注入**。字段注入使得类的依赖关系不明显，增加了代码的复杂度和维护难度。
2. 降低了代码的可测试性：在进行单元测试时，字段注入使得测试类难以模拟和替换依赖对象，需要通过反射或Spring的特定测试工具才能实现。
3. 无法保证不变性：字段注入在对象创建后通过反射注入，无法声明为 `final`，无法保证依赖对象在整个对象生命周期内不变，可能导致线程安全问题，和不可预期的行为。
4. 可能导致循环依赖：Spring解决循环依赖使用了**三级缓存**机制，而字段注入可以支持循环依赖，但会增加复杂性和潜在的错误风险。

#### 为什么Idea更推荐使用构造器注入

构造器注入通过构造函数参数来传递依赖对象，具有以下优点：

1. 明确依赖关系：构造器的参数列表清晰地显示了类的依赖关系，便于理解和维护，增强生成的类的确定性。
2. 提高可测试性：可以通过Mockito等工具模拟和替换依赖对象，简化单元测试，防止Spring的接入。
3. 保证依赖不变性：可以将依赖对象声明为 `final`，确保在对象生命周期内不变，提升线程安全性。
4. 提前暴露循环依赖：如果存在循环注入，Spring会在创建Bean时（启动过程中）抛出异常，避免运行时错误。

#### 一种实践 - 使用Lombok的@RequiredArgsConstructor

Lombok在Java编译期间作为注解处理器运行，当类上使用 `@RequiredArgsConstructor` 注解时，Lombok会查找所有包含 `final` 修饰且未被初始化的字段，并生成一个包含这些字段的构造函数。这样可以简化代码，避免手动编写构造函数，同时保持构造器注入的优点。

另一方面，Spring容器在启动时扫描到有 `@Service` 注解的服务类，并发现该类有一个构造函数（由Lombok生成），Spring会自动识别该构造函数，并尝试通过构造函数参数来注入所需的依赖对象。

注意：Spring 4.3.x后，如果一个类只有一个构造函数，且该构造函数的参数类型都是Spring容器中已有的Bean类型，那么Spring会自动使用该构造函数进行依赖注入，而无需显式添加 `@Autowired` 注解。

#### Lombok的隐患

尽管Lombok使用起来非常方便，但也存在一些隐患：

1. 编译依赖：Lombok通过注解在背后生成了代码，这些代码在编译后才会存在于字节码中，导致看到的源代码和实际运行的字节码不一致，可能会影响代码的可读性，例如无法通过 `Ctrl+鼠标左键` 跳转到生成的构造函数。
2. IDE支持：所有团队成员的IDE都需要安装并正确配置Lombok插件，否则可能会导致编译错误或代码提示异常。
3. 调试困难：尽管不常见，调试时的堆栈信息或断点可能回定位到与源代码不完全对应的位置，增加调试难度。
4. 增加冲突和脆弱性：引入第三方库增加了项目的复杂性和潜在的兼容性问题，尤其是在Lombok版本更新或与其他库冲突时，或是Lombok本身存在bug。

## 服务治理 - 解决购物车服务的跨服务调用

在拆分购物车服务时，发现其需要调用商品服务的接口来获取商品信息。由于微服务之间是独立部署和运行的，无法直接通过方法调用来实现跨服务调用。因此，需要使用**远程过程调用**（RPC）的方法来实现跨服务调用。有两种方案，基于HTTP协议和基于Dubbo协议。

### 方案对比

### 方案一：HttpClient包

实现这个方法的第一感很简单，既然每个服务已经在独立的端口上运行，那么直接通过HttpClient，发送HTTP请求调用商品服务的接口不就行了么？这种方式虽然可行，但存在一些问题：

1. 硬编码问题：需要在购物车服务中硬编码商品服务的地址和端口，如果商品服务的地址发生变化，购物车服务也需要修改代码并重新部署。如果有新的商品服务实例加入或退出，购物车服务也需要手动更新地址列表。
2. 负载均衡问题：如果商品服务有多个实例，购物车服务需要实现负载均衡的逻辑，选择一个可用的实例进行调用。
3. 容错问题：如果商品服务不可用，购物车服务需要处理调用失败的情况，避免影响用户体验。

### 方案二：RestTemplate

另一个比较原始的方法是使用RestTemplate实现HTTP的调用。具体步骤如下：

1. 注入一个 `RestTemplate` 对象，可以通过 `@Bean` 注解创建一个单例的 `RestTemplate` 实例。
2. 使用 `exchange` 方法发送HTTP请求，指定请求路径、请求方式、请求实体、返回值Class、请求参数，发送并获取响应结果。

```java
ResponseEntity<List<ItemDTO>> response = restTemplate.exchange(
        "http://localhost:8081/items?ids={ids}",
        HttpMethod.GET,  // 指定为GET方法
        null,  // GET方法没有请求体，因此传入null
        new ParameterizedTypeReference<List<ItemDTO>>() {
        },  // 此处由于泛型擦除不能直接使用List<ItemDTO>.class，需要使用参数化类型引用
        Map.of("ids", CollUtil.join(itemIds, ","))
);
```

这种方法虽然比HttpClient稍微简化了一些代码，但仍然存在硬编码、负载均衡和容错等问题。

### ※方案三：OpenFeign

为了解决这些问题，可以使用Spring Cloud提供的OpenFeign组件来简化跨服务调用。OpenFeign是一个声明式的HTTP客户端，可以通过接口定义来描述HTTP请求，并自动生成实现类。它还集成了Ribbon和Hystrix，支持负载均衡和熔断机制。

## Nacos - 服务注册中心

### 基本原理

在商品服务中，如果将来需要做多实例部署的话，每个实例即便端口不一样，它们也有相同的服务名称 `item-service` 和相同的对外提供服务的接口地址，此时将这些同质的服务实例称为**服务提供者**，那么反过来，需要RPC的 `cart-service` 即是**服务调用者**。根据不同的业务需求，**一个服务实例可能既是服务提供者也是服务调用者**。

对于微服务项目，如何匹配服务调用者和服务提供者呢？这就需要一个**服务注册中心**（类似一个中介公司），它的作用是**维护一个服务实例的清单**，记录每个服务实例的名称、地址、端口等信息。有了注册中心，服务间就可以通过以下过程进行相互调用：

1. 注册服务信息：服务提供者在启动时将自己的信息注册到注册中心。
2. 订阅注册信息：服务调用者在调用时从注册中心获取可用的服务实例列表，称为**服务发现**。
3. 进行负载均衡：获取到服务列表后，服务调用者会通过某些算法选择一个服务实例进行调用，称为**负载均衡**。
4. 远程调用服务：服务调用者通过HTTP等协议远程调用选中的服务实例，完成业务逻辑。

Nacos就是一个开源的，用于构建云原生应用的动态服务发现、配置管理和服务管理平台，角色定位类似于项目基础设施的总管，提供**服务注册中心、配置中心和服务管理中心**三大核心功能。

### 安装与部署

本项目中使用docker进行部署，具体见 `custom.env` 文件，其中配置了Nacos与MySQL连接的一些信息：

```env
PREFER_HOST_MODE=hostname
MODE=standalone
SPRING_DATASOURCE_PLATFORM=mysql
MYSQL_SERVICE_HOST=192.168.*.*
MYSQL_SERVICE_DB_NAME=nacos
MYSQL_SERVICE_PORT=3306
MYSQL_SERVICE_USER=root
MYSQL_SERVICE_PASSWORD=root
MYSQL_SERVICE_DB_PARAM=characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai
```

运行docker命令

```bash
docker run -d \
    --name nacos \
    --env-file ./nacos/custom.env \
    -p 8848:8848 \
    -p 9848:9848 \
    -p 9849:9849 \
    --restart=always \
    --network hm-net \
    nacos/nacos-server:v2.1.0-slim
```

然后访问 `http://localhost:8848/nacos` ，使用默认账号密码 `nacos/nacos` 登录，即可进入Nacos控制台。

### 服务注册 - 服务提供端的使用

需要在 `pom.xml` 中添加Nacos的依赖：

```xml

<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>
```

然后在 `application.yaml` 中添加Nacos的配置：

```yaml
spring:
  application:
    name: trade-service  # 服务名称
  cloud:
    nacos:
      server-addr: 192.168.*.*:8848
```

针对一个服务配置多个实例的情况，可以在Idea的启动项中复制某个服务的运行配置，然后配置修改端口号的虚拟机选项 `-Dserver.port=xxxx` 即可，该选项只对当前运行配置生效。

随后即可在Nacos控制台的服务列表中看到注册的服务实例。

### 服务发现 - 服务调用端的使用

引入依赖，配置Nacos地址和服务名称参考上文。然后在需要调用远程服务的接口中创建一个 `private final DiscoveryClient discoveryClient` 字段，并使用构造器注入。然后通过 `discoveryClient.getInstances("item-service")` 获取到所有可用的服务实例列表，通过 `instances.get(...)` 进行负载均衡选择一个实例进行调用。

```java
private final DiscoveryClient discoveryClient;

List<ServiceInstance> instances = discoveryClient.getInstances("trade-service");
ServiceInstance serviceInstance = instances.get(RandomUtil.randomInt(instances.size()));
URI uri = serviceInstance.getUri();
```

这样就可以将硬编码在代码中的服务地址和端口号解耦，改为动态获取。而且如果某个服务节点不可用，Nacos管理端会即时感知，调用端也不会再选择该节点进行调用。新服务节点加入时，调用端也会自动发现并使用（前提是已经正确配置好了Nacos的各项配置）。

> 总结：引入依赖、配置Nacos地址、配置服务名称、启动服务实例、使用DiscoveryClient获取服务实例列表、进行负载均衡选择实例、远程调用。（最后三项是针对服务调用端的）

### 相关问题

#### 可用性感知

当服务状态变更，例如某个服务不可用时，注册中心如何进行感知？根据服务实例的持久性不同，有两种处理方式：

1. 临时实例
    - 微服务实例在启动时会向注册中心发起注册请求，并将自己标记为临时实例。注册成功过后，服务实例会启动一个定时任务（默认为5s），**定期发送心跳包，告诉注册中心自己还活着**。如果注册中心在一定时间内没有收到心跳包，会将该实例标记为不健康（默认为15s）、甚至不可用（默认为30s），并从服务列表中移除。
    - 使用UDP或HTTP发送心跳包，健康检查的责任在客户端（实例端）。
    - 优点为响应迅速，能够在几十秒之内感知服务实例的不可用，适合动态变化频繁的云原生场景。
2. 永久实例
    - 服务实例通过API或控制台北注册为永久实例，这些实例信息会被持久化存储到注册中心的数据库中。注册中心会定期对永久实例发起健康检查请求。根据探测结果（例如TCP是否畅通，HTTP是否返回200），注册中心会更新实例的健康状态。而**即便探测到某个实例不健康，注册中心也不会将其从服务列表中移除，而是继续保留该实例的信息**。实例的删除必须通过API或控制台手动操作。
    - 使用TCP或HTTP、甚至SQL查询等方式进行健康检查，健康检查的责任在服务端（注册中心端）。
    - 优点为能够接管任何网络可达的服务实例，即便对方没有集成注册中心的客户端SDK，适合传统的单体应用场景，提供了对基础设施层面（例如数据库、缓存）的统一视图管理。

| 特性 (Feature) | 临时实例 (Ephemeral Instance)   | 永久实例 (Persistent Instance)       |
|--------------|-----------------------------|----------------------------------|
| 注册标识         | ephemeral=true              | ephemeral=false                  |
| 健康检查责任方      | 客户端 (Client)                | 服务端 (Server)                     |
| 核心机制         | 客户端定时上报心跳 (Push)            | 服务端定时主动探测 (Pull/Probe)           |
| 服务下线         | 心跳超时后，自动从列表中剔除              | 探测失败后，仅标记为不健康，永不自动剔除             |
| 数据存储         | 仅存于Nacos Server内存和服务端缓存     | 持久化到Nacos后端数据库 (如MySQL)          |
| 网络分区容忍度      | 客户端与服务端网络断开后，实例会因心跳超时而被快速剔除 | 实例仅被标记为不健康，网络恢复后，探测成功即可自动恢复健康状态  |
| 适用场景         | 业务微服务、需要弹性伸缩的云原生应用          | 数据库、Redis缓存、Nginx、无法集成SDK的第三方服务等 |

#### Nacos vs Eureka

Eureka是Netflix开源的一个服务注册与发现组件，主要用于亚马逊AWS云环境下的微服务架构。它提供了服务注册、服务发现、客户端负载均衡等功能。

相比于Eureka的只提供服务注册发现功能，Nacos还提供了**配置管理**和**服务管理**功能，能够集中管理微服务的配置文件，支持配置的动态刷新；同时提供了服务的权重调整、流量控制等功能，提升了微服务的可用性和稳定性。Eureka需要集成其他组件（如Spring Cloud Config）来实现配置管理，而Nacos则集成了这些功能，简化了微服务的架构设计。

| 特性      | Nacos                               | Eureka                      |
|---------|-------------------------------------|-----------------------------|
| 功能范围    | 服务发现 + 配置管理 (二合一)                   | 仅服务发现 (纯粹)                  |
| 数据一致性协议 | CP (Raft) 或 AP (Distro) 可切换         | AP (最终一致性)                  |
| 健康检查    | TCP/HTTP/MySQL/客户端心跳 (方式多样，服务端主动探测) | 客户端心跳 (Client Beat，服务端被动接收) |
| 配置管理    | 内置，且支持动态刷新                          | 无 (需要额外集成Config组件)          |
| 命名空间/分组 | 支持 (可用于环境隔离，如开发/测试/生产)              | 不支持 (隔离能力弱)                 |
| 社区与生态   | 阿里巴巴主导，社区活跃，云原生生态整合好                | Netflix已宣布停止维护，由社区托管，更新缓慢   |
| 部署与运维   | 相对重一些（依赖MySQL），但功能强大                | 非常轻量，无外部依赖                  |

#### 服务掉线的原因和排查

## OpenFeign - 简化跨服务调用

OpenFeign是一个声明式的HTTP客户端，是Spring Cloud在Eureka开源的Feign的基础上进行改造得到的包，作用是基于Spring MVC的常见注解，方便地调用HTTP接口。它支持负载均衡和熔断机制。其中负载均衡是通过集成Spring Cloud LoadBalancer实现的，较早期的版本则是通过集成Netflix Ribbon实现的。

> 总结：引入OpenFeign和Spring Cloud LoadBalancer依赖、利用 `@EnableFeignClients` 开启OpenFeign功能、创建Feign客户端接口、注入Feign客户端接口并调用方法。

### 基本操作

首先引入依赖，包括OpenFeign以及负载均衡组件Spring Cloud LoadBalancer：

```xml  

<dependencies>
    <!--openFeign-->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-openfeign</artifactId>
    </dependency>
    <!--负载均衡器-->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-loadbalancer</artifactId>
    </dependency>
</dependencies>
```

然后在主启动类上添加 `@EnableFeignClients` 注解，启用Feign客户端功能。

接着创建一个接口，使用 `@FeignClient` 注解标记该接口为Feign客户端，并指定服务名称 `name` 和可选的 `contextId`（用于区分同一服务的不同Feign客户端）。在接口中定义方法，并使用Spring MVC的注解（如 `@GetMapping`、`@PostMapping` 等）来描述HTTP请求的路径、方法、参数等信息。

```java

@FeignClient("trade-service")
public interface ItemClient {
    @GetMapping("/items")
    List<ItemDTO> queryItemsByIds(@RequestParam("ids") Collection<Long> ids);
}
```

最后，在需要调用远程服务的类中，注入Feign客户端接口，并直接调用接口方法即可。Feign会自动处理HTTP请求的发送、响应的接收和数据的转换。

### 作用原理

Feign在底层使用动态代理，在运行时生成接口的实现类。这个实现是通过Spring CLoud Feign提供的 `FeignClientFactoryBean` 类来完成的。`FeignClientFactoryBean` 实现了 `FactoryBean` 接口，负责创建Feign客户端的代理对象。而调用时则走的是 `ReflectiveFeign` 类中的内部类 `FeignInvocationHandler` 的 `invoke` 方法。

这个 `invoke` 方法中主要是为了过滤掉 `Object` 类中的方法（如 `equals`、`toString`、`hashCode` 等），然后调用Feign的 `SynchronousMethodHandler` 的 `invoke` 方法。

这个 `invoke` 方法中主要是为了构建HTTP请求，传入的是一个 `Object[]` 数组，表示方法的参数列表。首先通过参数列表构建一个 `RequestTemplate` 对象，表示HTTP请求的模板，模板的构建是通过获取在Client接口中定义的方法上的注解信息来完成的。然后进入 `executeAndDecode` 方法，这个方法首先通过Client接口上的 `@FeignClient` 注解获取服务名称，然后进入 `FeignBlockingLoadBalancerClient` 的 `execute` 方法

这个方法主要是为了获取服务实例列表，并进行负载均衡选择一个实例。其中

```java
ServiceInstance instance = loadBalancerClient.choose(serviceId, lbRequest);
```

一行就是负载均衡的核心代码，`loadBalancerClient` 是 `Spring Cloud LoadBalancer` 提供的负载均衡客户端，`choose` 方法会根据服务ID和请求信息选择一个合适的服务实例，并通过 `reconstructURI` 得到真正的远程调用地址。与[前文](#服务发现---服务调用端的使用)手动实现的效果是一致的。

最终的HTTP请求发送是通过一个 `Client` 对象（代理客户端）来完成的，默认情况下使用 `HttpURLConnectionClient` 类，这个类是对Java原生的 `HttpURLConnection` 进行了一层封装，简化了HTTP请求的发送和响应的处理。

### 连接池 - 提高连接性能

默认的 `HttpURLConnectionClient` 实现在每次发送HTTP请求时，都会**创建一个新的HTTP连接**，使用完毕后立即关闭连接。这样虽然简单，但会带来较大的性能开销，尤其是在高并发场景下，频繁创建和关闭连接会导致资源浪费和响应时间增加。

可以通过选择其他的框架实现的 `Client` 接口，来实现连接池功能。常见的选择有Apache HttpClient和OkHttp。具体源码参考 `FeignBlockingLoadBalancerClient` 中的 `delegate` 字段。

使用方法很简单，以Apache HttpClient为例，只需要引入依赖，并在配置文件中开启连接池功能即可。

```xml

<dependencies>
    <!--Apache HttpClient 5连接池-->
    <dependency>
        <groupId>org.apache.httpcomponents.client5</groupId>
        <artifactId>httpclient5</artifactId>
    </dependency>
    <!--Feign对HC5的支持组件-->
    <dependency>
        <groupId>io.github.openfeign</groupId>
        <artifactId>feign-hc5</artifactId>
    </dependency>
</dependencies>

```

```yaml
feign:
  httpclient:
    hc5:
      enabled: true # 这是2.x的写法
  client:
    config:
      default:
        connectTimeout: 2000
        readTimeout: 2000
```

### 抽取客户端 - 最佳实践

在实际项目中，可能会有多个服务需要调用同一个远程服务的接口，例如 `item-service` 的接口可能会被 `cart-service`、`order-service` 等多个服务调用。为了避免代码重复和提高维护性，可以将Feign客户端接口抽取到一个独立的模块中，供其他服务依赖和使用。

具体实践中有两种方案，一种是抽取所有的Feign客户端接口到一个公共模块中，结构更清晰但耦合度相对较高。另一种是每个服务单独创建一个模块，存放该服务的Feign客户端接口、DTO和服务等，结构更复杂但耦合度更低，**不需要一个团队既要维护自己的服务代码，又要维护公共的客户端接口**。

为方便起见，使用第一种方案。考虑建立一个新的模块 `hm-api`，专门用于存放所有的公共API和DTO类，于是就可以把Feign客户端接口集成进去。这样做有以下好处：

1. 代码复用：多个服务可以共享同一个Feign客户端接口，避免重复编写相同的代码。
2. 统一管理：所有的Feign客户端接口集中在一个模块中，便于管理和维护。
3. 版本控制：可以为 `hm-api` 模块单独进行版本控制，确保各个服务使用的Feign客户端接口版本一致。

首先在 `hm-api` 模块的 `pom.xml` 中引入OpenFeign和负载均衡器的依赖，然后将对应的Client和DTO包导入到 `com.hmall.api` 包中。接着在需要调用远程服务的模块中（如 `cart-service`），在 `pom.xml` 中引入对 `hm-api` 模块的依赖，并在主启动类上添加 `@EnableFeignClients(basePackages = "com.hmall.api.client")` 注解，指定Feign客户端接口所在的包路径。最后在需要调用远程服务的类中，注入Feign客户端接口并调用其方法即可。

### 日志输出

OpenFeign默认是不输出日志的，需要添加一个配置类 `DefaultFeignConfig`，并在其中创建一个 `Logger.Level` 类型的Bean，指定日志级别为 `FULL`，表示输出完整的请求和响应信息。注意不需要添加 `@Configuration` 注解，否则会导致每个Feign客户端都使用这个配置类，无法实现按需配置。

```java
public class DefaultFeignConfig {
    @Bean
    public Logger.Level feignLogLevel() {
        return Logger.Level.FULL;
    }
}
```

然后在需要使用Feign的模块的启动类上，为 `@EnableFeignClients` 注解添加 `defaultConfiguration` 属性，指定默认的配置类为 `DefaultFeignConfig`。

配置好之后，每次调用远程服务时，都会在控制台输出详细的日志信息，包括请求方法、URL、请求头、请求体、响应状态码、响应头、响应体等。

# Day 4 - 微服务 中 - 网关与配置中心

本节主要解决前端访问入口、统一权限校验与配置管理的问题。

## 网关

网关负责请求的路由、转发、鉴权等功能。常见的网关有Netflix Zuul（早期实现）、Spring Cloud Gateway（当前主流）等。

Spring Cloud Gateway官网：https://spring.io/projects/spring-cloud-gateway

## 统一前端访问入口

将网关的端口设置为8080，并在网关内部配置路由规则（通过Nacos的注册中心列表拉取服务列表），将不同的请求路径转发和负载均衡到不同的微服务，这样就能实现统一前端的访问 入口。微服务的地址只需要暴露网关地址而无需暴露微服务本身的地址，前端只知道网关地址也可以将后端视为一个整体的单体架构，一定程度上保护了后端的服务，也提升了前端的开发和交互体验。

### 基本配置

首先引入commmon模块、Spring Cloud Gateway、Nacos和负载均衡器的依赖：

```xml

<dependencies>
    <!--common-->
    <dependency>
        <groupId>com.heima</groupId>
        <artifactId>hm-common</artifactId>
        <version>1.0.0</version>
    </dependency>
    <!--网关-->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-gateway</artifactId>
    </dependency>
    <!--nacos discovery-->
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
    </dependency>
    <!--负载均衡-->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-loadbalancer</artifactId>
    </dependency>
</dependencies>
```

然后创建启动类 `GatewayApplication`，并添加 `@SpringBootApplication` 注解。

最后添加配置类，配置具体的路由规则：

```yaml
server:
  port: 8080
spring:
  application:
    name: gateway
  cloud:
    nacos:
      server-addr: ${hm.nacos.host}:8848
    gateway:
      routes:
        - id: item  # 路由规则id，自定义，唯一
          uri: lb://item-service  # 路由的目标服务，lb代表负载均衡，会从注册中心拉取服务列表
          predicates: # 路由断言，判断当前请求是否符合当前规则，符合则路由到目标服务
            - Path=/items/**,/search/**  # 这里是以请求路径作为判断规则
        - id: cart
          uri: lb://cart-service
          predicates:
            - Path=/carts/**
        - id: user
          uri: lb://user-service
          predicates:
            - Path=/users/**,/addresses/**
        - id: trade
          uri: lb://trade-service
          predicates:
            - Path=/orders/**
        - id: pay
          uri: lb://pay-service
          predicates:
            - Path=/pay-orders/**
```

### 路由过滤

`spring.cloud.gateway.routes` 下的每一个元素都表示一条路由规则，最终由 `RouteDefinition` 类进行读取，包含以下几个重要属性：

- `id`：路由规则的唯一标识，可以自定义。
- `uri`：路由的目标地址，可以是HTTP URL，也可以是服务名称（以 `lb://` 开头表示通过负载均衡访问）。
- `predicates`：路由断言，用于判断请求是否符合当前路由规则。
    - 常见的断言有 `Path`（请求路径）、`Method`（请求方法）、`Header`（请求头）等，共12种。
- `filters`：路由过滤器，用于对请求和响应进行特殊处理。
    - 常见的过滤器有 `AddRequestHeader` （添加请求头）、`AddResponseHeader`（添加响应头）、`RewritePath`（重写请求路径）等，共33种。

上一部分的路由规则中，只使用了 `Path` 断言来判断请求路径是否符合当前路由规则。实际上，可以根据业务需求，添加更多的断言和过滤器来实现更复杂的路由逻辑。例如，可以添加Method断言来限制请求方法为GET或POST，添加 `Header` 断言来检查请求头中是否包含某个特定的值，添加过滤器来修改请求路径或添加请求头等。下面的路由规则中，添加了 `Method` 断言和 `AddRequestHeader` 过滤器：

```yaml
- id: item
  uri: lb://item-service
  predicates:
    - Path=/items/**,/search/**
    - Method=GET
  filters:
    - AddRequestHeader=X-Request-Source,Gateway
```

针对默认对所有请求生效的过滤器，可以通过与 `routes` 同级的 `default-filters` 选项进行配置。

更多原理见[动态路由](#动态路由)一节。

## 统一权限校验

> 总结：在本节中，需要实现三个内容：
> 1. 网关的登录校验并向微服务传递用户信息，见 `hm-gateway` 模块下的 `AuthGlobalFilter` 过滤器，它是一个 `GlobalFilter`
> 2. 微服务拦截并校验用户信息，见 `hm-common` 模块下的 `UserInfoInterceptor` 拦截器，它是一个 `HandlerInterceptor`
> 3. 微服务之间的相互调用传递用户信息，见 `hm-api` 模块下的 `DefaultFeignConfig` 配置类中的 `userInfoRequestInterceptor` 拦截器，它是一个 `RequestInterceptor`

在拆分后的微服务架构中，登录功能被拆分到了用户服务中，由于这是首次颁发Session的JWT Token的服务，因此用户服务不需要进行登录校验，直接放在用户服务中是可接受的。

而其他服务（如商品服务、购物车服务、订单服务等）都需要对Token进行登录校验，以确保用户身份的合法性和安全性。以前实现的校验逻辑是通过 `UserContext` 中的 `ThreadLocal` 变量来存储当前请求的用户信息。然而这个实现依赖于Tomcat客户端的线程一致性，当服务分散至各个微服务之后，每个服务都属于不同的Tomcat客户端和进程，无法共享 `ThreadLocal` 变量，因此每个服务都需要单独实现登录校验的逻辑。

为了避免在每个服务中重复编写登录校验的代码，可以将登录校验逻辑集中到网关中进行处理，**需要先校验合法性，然后才转发**，将校验通过的用户信息向后传递给各个服务。

在这个过程中，需要解决三个问题：

1. 网关路由是通过配置文件进行的，具体处理的底层逻辑是什么？如何在转发之前做登录校验？
2. 网关在校验JWT Token之后，如何将用户信息传递给后端的各个服务？
3. 微服务之间的相互调用不经过网关，这种情况下如何传递用户信息？

### 网关过滤器的底层逻辑

网关的底层显然是没有业务逻辑的，其只需要根据配置文件中的路由规则进行判断和过滤，然后转发给对应的微服务，类似责任链模式。

首先，对路由规则的判断通过 `HandlerMapping` 接口完成。这是一个路由映射器，默认实现是 `RoutePredicateHandlerMapping` 类。它会**读取配置文件中的路由断言进行规则匹配**，将匹配的路由规则存储进上下文，然后把请求交给 `WebHandler` 进行处理。

`WebHandler` 的默认实现是 `FilteringWebHandler` 类。它会**读取配置文件中的路由过滤器**，找到当前请求对应路由生效的哪些过滤器，并将这些过滤器与默认过滤器合并，然后将这些过滤器放入集合并排序，组成一个**过滤器链**，最后依次执行这些过滤器。

特别地，在这个过滤器链的最后，还有一个 `NettyRoutingFilter` （Netty路由过滤器）。这个过滤器的作用是将**请求转发给对应的微服务**。它会从上下文中获取路由规则，得到目标服务的地址，然后发送HTTP请求，将请求转发给目标服务，并将封装后的响应存回上下文，逐级返回给客户端。

逐级意味着在微服务接收请求前和返回响应后，每个过滤器都被调用了两次，一次是请求前，一次是响应后。这是因为过滤器内部可以包含PRE和POST两部分逻辑。**只有上一个过滤器的PRE执行成功的情况下，才会调用下一个过滤器**，因此只有当所有过滤器的PRE逻辑都执行通过后，请求才会被路由到微服务，后续过滤器不再执行。另一方面，在微服务返回结果后，就会倒序执行过滤器的POST逻辑。这样就可以在请求前对请求进行修改（如添加请求头、重写路径等），在响应后对响应进行处理（如添加响应头、修改响应体等）。

### 在网关中实现登录校验

根据以上网关请求处理流程，不难发现，想要实现登录校验的功能，只需要**创建一个自定义的过滤器**，并将其添加到路由规则的过滤器链中即可。这个过滤器的**PRE逻辑**需要从请求头中获取JWT Token，然后进行解析和校验，如果校验通过，则**将用户信息存入请求头中**，并调用 `chain.filter(exchange)` 将请求传递给下一个过滤器；如果校验失败，则直接返回错误响应，阻止请求继续传递。

#### 自定义过滤器

网关过滤器有两种类型：

- 路由过滤器 `GatewayFilter` ：只对某个路由规则生效，可以通过配置文件中的 `filters` 选项进行配置，默认不生效，**配置到路由后才生效**。
- 全局过滤器 `GlobalFilter` ：对所有路由规则生效，可以通过实现 `GlobalFilter` 接口来创建，**类实现后自动生效**。

两个类中都有一个 `Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain)` 方法，两个参数分别表示当前请求的**上下文**和**过滤器链**。`ServerWebExchange` 类中包含了过滤器链中的共享对象，如请求和响应对象，可以通过 `exchange.getRequest()` 获取请求对象，通过 `exchange.getResponse()` 获取响应对象。以下是一个继承 `GlobalFilter` 的示例：

```java

@Component
public class MyGlobalFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 获取请求对象
        ServerHttpRequest request = exchange.getRequest();
        // 获取请求头
        HttpHeaders headers = request.getHeaders();
        System.out.println("headers = " + headers);
        // 继续调用过滤器链中的下一个过滤器
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        // 设置为最高优先级
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
```

路由过滤器 `GatewayFilter` 可以自由指定作用范围，以及自定义参数，但实现相对来说更复杂。自定义一个 `GatewayFilter` 不能直接实现接口，而是需要继承一个抽象工厂类 `AbstractGatewayFilterFactory` ，并指定一个配置类作为泛型参数。由于配置中过滤器可以添加自定义参数，因此对不同参数，过滤器的效果也是不同的，工厂类就实现了通过读取配置，并创建定制化的过滤器对象。实现时，需要在过滤器类中重写 `apply` 方法，返回一个 `GatewayFilter` 对象。在这个对象的 `filter` 方法中实现具体的过滤逻辑。以下是一个继承 `AbstractGatewayFilterFactory` 的示例：

```java
// 注意定义时类名需要以 "GatewayFilterFactory" 结尾，前缀则以后作为过滤器名称使用，即 "MyPrint"
@Component
public class MyPrintGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {
    @Override
    public GatewayFilter apply(Object config) {
        return new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                System.out.println("经过了MyPrintGatewayFilter");
                return chain.filter(exchange);
            }
        };
    }
}
```

#### 实现登录校验

首先将公钥jks文件放入网关模块的 `resources` 目录下，将原始项目中的 `jwt` 选项复制到网关模块的 `application.yaml` 中。然后将原始项目中的JWT配置文件和工具类复制到网管模块的对应目录下。接着创建一个 `AuthGlobalFilter` 类，继承 `GlobalFilter`，并在 `apply` 方法中实现登录校验的逻辑，见 `AuthGlobalFilter` 类。

1. 获取请求和响应： `exchange.getRequest()` 和 `exchange.getResponse()`。
2. 判断放行路径：通过遍历 `authProperties.getExcludePaths()` 判断当前请求路径是否在放行列表中，如果是则直接调用 `chain.filter(exchange)` 放行。
3. 获取请求头中的Token： `request.getHeaders().get("Authorization")`
4. 校验Token：使用 `JwtUtils` 工具类进行解析和校验，如果校验失败，返回错误响应。
5. 传递用户信息：如果校验通过，从Token中获取用户信息，并将其存入请求头中.
6. 放行至下一个过滤器：如果全部通过，则调用 `chain.filter(exchange)` 继续传递请求。

#### 传递用户信息

在网关中校验通过后，需要将用户信息传递给后端的各个微服务。由于HTTP请求是无状态的，因此无法直接共享内存中的变量（如ThreadLocal）。一种常见的做法是将用户信息存储在请求头中，然后在后端服务中从请求头中获取用户信息。

问题在于，即便已经实现了将用户信息传递至后端，得到用户信息的逻辑可能会在多个服务中重复出现，导致代码冗余和维护困难。为了解决这个问题，可以使用Spring MVC的拦截器进行实现。拦截器可以在请求到达控制器之前进行处理，适合用于统一的请求预处理逻辑，如登录校验、用户信息提取等。

因此需要做两件事，首先在网关中将用户信息存入请求头，然后在各个微服务中创建一个拦截器，从请求头中提取用户信息，并存入 `UserContext` 的 `ThreadLocal` 变量中，供后续的业务逻辑使用。

针对存入请求头，使用 `exchange.mutate()` 方法，创建一个新的请求对象，然后最终放行这个新的exchange：

```java
ServerWebExchange mutatedExchange = exchange.mutate()  // 下游请求修改
        .request(builder -> builder.header("user-info", userId.toString()))
        .build();  // 构建新的exchange
```

针对提取用户信息，在 `hm-common` 模块中创建一个 `UserInfoInterceptor` 类，实现 `HandlerInterceptor` 接口。由于登录拦截已经在网关中实现了，因此不需要再拦截一次，可以全部放行，只起到传递用户信息的作用。实现时需要重写 `preHandle` 方法，从请求头中获取用户信息，并存入 `UserContext` 的 `ThreadLocal` 变量中。

编写完拦截器之后，还需要将拦截器注册到Spring MVC中。可以创建一个配置类 `MvcConfig`，实现 `WebMvcConfigurer` 接口，添加 `@Configuration` 注解，并重写 `addInterceptors` 方法，将拦截器添加到注册表中。

注册完拦截器之后，由于拦截器是在 `hm-common` 模块中编写的，要想让各个微服务都能使用这个拦截器，还需要在 `META-INF/spring.factories` 文件中添加以下内容：

```properties
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
  com.hmall.common.config.MvcConfig
```

### 问题详解 - Spring Boot的条件化装配原理

> 注意，由于网关模块的技术栈是Spring Cloud Gateway（基于Spring Webflux），其中是没有Spring MVC的，因此如果此时就启动服务，WebFlux因为找不到 `WebMvcConfigurer` 接口这种MVC的核心类，导致启动失败。可以在MVC配置类上添加 `@ConditionalOnClass(DispatcherServlet.class)` 注解，表示只有在类路径下存在 `DispatcherServlet` 类时，才加载该配置类。由于 `DispatcherServlet` 是Spring MVC的核心类，这样就能避免网关模块启动失败的问题。

### 微服务之间传递用户信息

随着微服务的数量逐渐增加，项目中的很多业务需要多个微服务共同合作完成，而这个过程中需要微服务之间互相传递用户信息。但是在微服务之间的相互调用中，通常不会经过网关，因此无法通过网关传递用户信息。对于使用 `ThreadLocal` 变量存储的思路，前文也提到过，这种方式无法跨进程共享内存，**原因有二**：

1. 分布式系统中，每个微服务有不同的线程实例， `ThreadLocal` 线程私有
2. 前文的实现中，用户信息 `User-Info` 请求头只能通过网关向微服务传递，微服务之间的相互调用无法传递

为解决这个问题，OpenFeign提供了一个 `RequestInterceptor` 接口，可以在每次Feign客户端发送请求之前，对请求进行预处理，例如添加请求头、修改请求参数等。通过实现这个接口，可以在每次Feign客户端发送请求之前，从 `UserContext` 中获取用户信息，并将其添加到请求头中，从而实现微服务之间的用户信息传递。

直接在 `hm-api` 模块的Feign配置类中编写，注册一个返回 `RequestInterceptor` 类型的Bean即可：

```java

@Bean
public RequestInterceptor userInfoRequestInterceptor() {
    return new RequestInterceptor() {
        @Override
        public void apply(RequestTemplate template) {
            Long user = UserContext.getUser();
            if (user == null) {
                return;
            }
            template.header("User-Info", user.toString());
        }
    };
}
```

> 注意，虽然此处没有添加任何加载Bean的注解，但是在各个微服务的启动类上，添加了 `@EnableFeignClients(basePackages = "com.hmall.api.client")` 注解，指定了Feign客户端接口所在的包路径。当使用Feign客户端发起调用时，Feign会创建一个 `RequestTemplate` 对象来准备请求，在请求发送之前，Feign会遍历所有已经注册的 `RequestInterceptor` 拦截器Bean，并调用它们的 `apply` 方法，对请求进行预处理。

## 统一配置中心

目前在微服务中有大量重复的配置，我们希望引入一个统一的配置中心，来集中管理和分发这些配置。常见的配置中心有Spring Cloud Config（早期实现）、Nacos（当前主流）等。通过统一的配置管理服务，可以实现配置的集中存储、版本控制、动态刷新等功能，无需重启即可更新配置，提升微服务的可维护性和灵活性。

### 配置共享

首先把共享的配置添加到Nacos中，包括JDBC、Mybatis Plus、日志、Swagger、OpenFeign等配置。然后编写一个配置类 `NacosConfig` 用于拉取共享配置代替微服务的本地配置。

这样，在Spring Boot应用程序启动时，会先尝试拉取Nacos的配置，然后初始化对应的应用程序上下文，接着再加载本地的配置文件并初始化上下文。由于Spring Boot的配置加载是有优先级的，后加载的配置会覆盖先加载的配置，因此本地配置文件中的配置会覆盖Nacos中的共享配置，剩余的配置进行合并。

但是这样的话，程序启动时并不知道Nacos的地址，如何拉取Nacos的配置？Nacos提供了一种机制，让程序在本地先读取一个 `bootstrap.yaml` 引导配置文件，这个文件会在应用程序启动的最早阶段被加载。可以在这个文件中配置Nacos的地址和命名空间等信息，这样程序就能在启动时连接到Nacos，并拉取共享配置。以购物车服务为例：

```yaml
spring:
  application:
    name: cart-service  # 微服务名称
  profiles:
    active: dev
  cloud:
    nacos:
      server-addr: ${hm.nacos.host}:8848
      config:
        file-extension: yaml # 文件后缀名
        shared-configs: # 共享配置
          - data-id: shared-jdbc.yaml # 共享mybatis配置
          - data-id: shared-log.yaml # 共享日志配置
          - data-id: shared-swagger.yaml # 共享日志配置
```

### 配置热更新

配置热更新指的是，当配置中心中的配置发生变化时，能够自动刷新应用程序中的配置，而**无需重启应用程序**。Nacos支持配置的动态刷新，可以通过监听配置的变化事件来实现热更新。例如用户登录的超时时长、登录失败的最大尝试次数、购买上限等等。

首先引入依赖：

```xml

<dependencies>
    <!--nacos配置管理-->
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
    </dependency>
    <!--读取bootstrap文件-->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-bootstrap</artifactId>
    </dependency>
</dependencies>
```

然后在Nacos中添加一个名称格式如下的配置：

```
{spring.application.name}-{spring.profiles.active}（可选）.{file.extension}
```

例如对于 `cart-service` 服务，在 `dev` 环境下，文件后缀名为 `yaml`，则配置的名称应为 `cart-service-dev.yaml`。然后在微服务中配置需要以特定方式读取需要热更新的配置属性，有两种方法：

1. 在类上添加 `@ConfigurationProperties` 注解，配置参数 `prefix` 指定配置前缀
2. 在字段上添加 `@Value` 注解，指定配置的完整路径，然后在类上添加 `@RefreshScope` 注解，指定需要热更新

### 动态路由

> 总结：创建一个配置类，为 `ConfigService` 注册一个监听器 `Listener`，当路由更新时则会自动调用回调函数，其中需要实现的逻辑有将Nacos的远程路由配置解析为 `RouteDefinition` 列表、删除旧的路由信息、添加新的路由信息。

目前我们的网关路由规则是通过配置文件进行的，无法动态修改和更新。Nacos支持动态路由，要实现动态路由，首先要将路由配置保存到Nacos，当Nacos中的路由配置变更时，推送最新配置到网关，实时更新网关中的路由信息。问题在于**Nacos只能保存并推送配置**，而网关内部的路由更新，Nacos是无法进行帮助的。**因此我们自己需要实现的是：监听Nacos配置的变化事件，然后在事件中更新网关的路由信息。**

Nacos提供了一系列SDK用于操作配置和服务注册发现，特别地，监听配置的写法可以参见[这个链接](https://nacos.io/docs/v2/guide/user/sdk/#%E8%AF%B7%E6%B1%82%E7%A4%BA%E4%BE%8B-1)。其中的 `getConfig` 方法就是从Nacos中获取配置，`addListener` 方法用于监听配置的变化事件。这两个方法可以合并成直接调用 `getConfigAndSignListener` 方法来完成。

#### Nacos的自动配置

Nacos的自动配置类是 `NacosConfigBootstrapConfiguration` 和 `NacosConfigAutoConfiguration`，它们会在Spring Boot应用程序启动时被加载。其中都注册了 `NacosConfigProperties` 和 `NacosConfigManager` 两个Bean。前者用于存储Nacos的配置信息，后者用于管理Nacos的配置操作。那么我们就可以通过注入这两个Bean，来获取Nacos的配置，并监听配置的变化事件。

`NacosConfigManager` 的构造函数调用了该类的 `createConfigService` 方法（本质上是一个初始化方法），该方法会创建一个 `ConfigService` 对象，这个对象是通过调用 `NacosFactory.createConfigService` 方法来创建的，这与官方SDK中的创建方式一致。

#### 实现动态路由

引入Nacos配置管理和Bootstrap读取依赖，见[配置热更新](#配置热更新)一节引入的依赖。

首先创建一个类用于动态路由读取，见 `hm-gateway` 模块下的 `DynamicRouteLoader` 类。该类中注入了 `NacosConfigManager` 和 `RouteDefinitionWriter` 两个Bean。前者用于获取Nacos的配置，后者用于更新网关的路由信息。

`NacosConfigManager` 调用 `getConfigService().getConfigAndSignListener()` 方法，传入服务名称、命名空间、超时时间和监听器对象。其中监听器对象是一个 `Listener` 接口的实现类，可以是匿名内部类或自己实现的类。该接口中有两个方法，`receiveConfigInfo` 和 `getExecutor`。前者用于接收配置变化的通知，当Nacos中的配置发生变化时，该方法会被调用，并传入最新的配置内容。后者用于指定监听器的执行器，可以返回null，表示使用默认的执行器。

在 `receiveConfigInfo` 方法中和调用 `getConfigAndSignListener` 后，都需要更新路由配置信息，分为三步：

1. 将接收到的配置内容转换为 `List<RouteDefinition>` 对象（在[本节](#路由过滤)的yaml配置中，spring.cloud.gateway.routes配置的实际上就是这个），直接使用Hutool工具包的 `JSONUtil.toList` 方法进行转换。
2. 删除旧的路由信息，调用 `RouteDefinitionWriter` 的 `delete` 方法，传入路由ID，循环遍历，需要在配置类中定义一个 `Set` 进行存储 `routeIds` 。
3. 添加新的路由信息，调用 `RouteDefinitionWriter` 的 `save` 方法，传入新的路由定义对象，循环遍历，同时将新的 `routeDefinition.getId()` 保存到 `routeIds` 中。

具体实现见 `DynamicRouteLoader` 类。

# Day 5 - 微服务 下 - 服务保护与分布式事务

本节中主要了解Sentinel用于请求限流、线程隔离、服务熔断降级等功能，以及Seata用于分布式事务管理。

## 服务保护的基本概念

在分布式系统中，服务的调用与服务本身的相互调用都可能会产生大量异常和性能问题，如果在数据库查询时发生一些故障，可能会导致整个服务不可用，进而影响到整个系统的稳定性和可用性。考虑一下几个场景：

1. 业务健壮性问题：查询购物车时，需要调用商品查询，如果商品查询服务出现异常，那么就会导致购物车查询也失败，从而影响用户体验。从用户体验角度来看，即便商品查询失败，购物车查询也应该成功，只是商品信息无法显示。
2. 级联失效问题：商品服务的查询接口被恶意攻击导致并发量激增，占用过多Tomcat连接，这可能会导致商品服务的所有接口阻塞，响应时间增加甚至不可用，进而影响到整个系统的稳定性和可用性。

为了解决这些问题，需要引入一些服务保护技术，如限流、熔断、降级等。

### 雪崩问题

雪崩问题指的是在分布式系统中，如果链路中某个服务节点出现故障或性能问题，而调用该服务的调用者也没有做好异常处理，导致大量线程被阻塞，资源耗尽，进而引发整个系统的连锁反应，调用链中的所有服务级联失效，导致整个微服务集群不可用。

### 解决方案

在实际开发中尽量避免服务出现故障或阻塞，保证代码的健壮性和高可用性，保障网络畅通减小网络抖动，提升服务的性能和稳定性，提高并发性能，减少服务的故障率和响应时间。在出现故障的情况下，做好应对处理，服务调用者需要做好异常处理和容错，准备后备方案，避免异常扩散。

从保护服务提供者角度出发，就是**限制进入服务提供者的流量**。可以通过**请求限流**进行管理，限制访问微服务请求的并发量，避免服务因为流量激增，超过服务器的性能上限而出现故障。实现时需要让请求先经过一个**限流器**，不管到达限流器的流量有多大，限流器都只会按预设的流量上限放行请求，超过限流器的流量上限的请求则会被拒绝或延迟处理，将波动的QPS变得平缓。常见的限流算法有漏桶算法、令牌桶算法、计数器算法等。

从保护服务调用者角度出发，就是**限制出服务调用者的流量**。可以通过**线程隔离**进行管理，线程隔离也称为舱壁模式，通过模拟船舱隔板的防水原理（破坏了某个隔间不会影响其他隔间）。通过限定每个业务所能使用的线程数量而将故障业务隔离，避免故障扩散。实现时需要为每个业务创建一个独立的线程池，限制调用每个业务所能使用的线程数量，当某个业务的线程池满了之后，新的请求就会被拒绝或延迟处理，从而避免该业务的故障影响到其他业务。但是只进行线程隔离，无法解决被调用服务本身的故障问题，我们不能在知道服务不可用的情况下还继续调用它，这样只会浪费资源。

可以使用**服务熔断**进行管理，使用一个**断路器**统计请求的**异常比例或慢调用比例**，当该比例超过预设的阈值时，断路器就会打开，进入熔断状态，所有请求快速失败，会进入一个fallback逻辑，作为**服务降级**方案。等到经过一段时间后，断路器会进入**半开状态**，允许少量请求通过，如果这些请求成功率较高，则断路器会关闭，恢复对该服务的调用；如果这些请求仍然失败率较高，则断路器会重新打开，继续阻止对该服务的调用。这样就能在服务出现故障时，及时切断对该服务的调用。

### 常用中间件

常见的服务保护中间件有Hystrix（早期实现）、Resilience4j（当前主流）、Sentinel（阿里巴巴开源）等。

| 特性              | Alibaba Sentinel                                                                    | Resilience4j                                   | Netflix Hystrix                               |
|:----------------|:------------------------------------------------------------------------------------|:-----------------------------------------------|:----------------------------------------------|
| 项目状态            | 积极开发 (社区活跃)                                                                         | 积极开发 (官方推荐)                                    | 维护模式 (Legacy)                                 |
| 核心思想/定位         | 以流量为切入点，进行全方位流量控制和服务保护                                                              | 轻量级的、模块化的通用容错库                                 | 以隔离和熔断为核心的容错框架                                |
| 线程隔离            | 仅支持信号量隔离（通过线程数限流实现）                                                                 | 仅支持信号量隔离（通过Bulkhead模块）。更轻量，开销小                 | 支持线程池隔离（核心特性）和信号量隔离。隔离彻底，但线程池开销大              |
| 熔断策略            | 功能最强大，支持慢调用比例、异常比例、异常数等多种策略，且支持熔断恢复的预热（Warm Up）                                     | 功能更丰富，支持基于错误率和慢调用率的熔断。状态机模型更严谨                 | 基于错误率的滑动窗口统计                                  |
| 限流              | 核心优势功能。支持QPS、线程数等多种流控模式；支持直接拒绝、Warm Up、匀速排队等多种流控效果；支持针对调用来源、调用链路的精细化限流              | 提供专门的RateLimiter模块，但功能相对基础                     | 功能简陋。只能通过线程池大小或信号量大小间接实现，不够灵活                 |
| Fallback        | 支持。通过 `@SentinelResource` 注解，且区分了 `fallback`（处理业务异常）和 `blockHandler`（处理限流和熔断）。设计更精细 | 支持。通过 `@CircuitBreaker` 等注解指定 `fallbackMethod` | 支持。通过 `@HystrixCommand` 注解指定 `fallbackMethod` |
| 控制台 (Dashboard) | 提供功能强大的Sentinel Dashboard，支持实时监控和动态修改规则，无需重启应用。运维极其便利                               | 无官方控制台。依赖将指标暴露给 Prometheus + Grafana 等第三方监控系统  | 提供 Hystrix Dashboard 和 Turbine 进行聚合监控         |
| 配置方式            | 支持注解、配置文件，但最大优势是支持动态数据源，可与 Nacos, Apollo, Zookeeper 等配置中心联动，实现规则的动态化、持久化和集群共享       | 主要通过注解和配置文件。规则基本是静态的                           | 主要通过注解和配置文件。规则基本是静态的                          |
| 依赖              | 核心库无额外依赖，但与Nacos、Spring Cloud Gateway等生态组件结合紧密                                      | 无任何外部依赖，非常轻量                                   | 依赖 `archaius` 等多个Netflix库，相对较重                |

## Sentinel - 服务保护技术

官方文档：https://sentinelguard.io/zh-cn/docs/introduction.html

### 快速入门

Sentinel提供了一个核心库作为客户端，包含了服务保护的各项功能。规则配置有两种方式，一是通过在代码中使用注解或API进行配置，二是通过一个独立的**控制台**（Dashboard）进行配置。控制台既可以**实现服务监控**，也可以**动态修改规则**，并将规则下发到客户端，无需重启应用程序。

首先从官方Github仓库中下载jar包，放在一个非中文路径，然后在路径下通过如下命令启动控制台：

```bash
java -Dserver.port=8090 -Dcsp.sentinel.dashboard.server=localhost:8090 -Dproject.name=sentinel-dashboard -jar sentinel-dashboard-1.8.6.jar
```

然后就可以通过访问 http://localhost:8090 进入控制台，默认用户名和密码均为 `sentinel`。首次登录后控制台显示的是Sentinel自己的监控信息。

也可以使用docker部署，直接在docker中运行以下命令：

```bash
docker run -d \
    --name sentinel \
    -p 8858:8858 \
    --network hm-net \
    herodotus/sentinel-dashboard:1.8.6
```

同时为Spring Boot启动添加虚拟机选项 `-Dcsp.sentinel.heartbeat.client.ip=XXX`，填入虚拟机的网关地址。

接下来需要将微服务与控制台进行连接。首先引入 `spring-cloud-starter-alibaba-sentinel` 依赖，然后在 `application.yaml` 中添加如下配置：

```yaml
spring:
  cloud:
    sentinel:
      eager: true  # 开启饥饿模式，主动检测
      http-method-specify: true  # 开启基于请求方法的限流
      transport:
        dashboard: XXX:8858
```

在**簇点链路**选项卡中可以看到**基于请求方法+请求路径**的请求统计。簇点链路就是单机调用链路，是一次请求进入服务后经过的每一个被Sentinel监控的资源链。默认Sentinel会监控Spring MVC的每一个HTTP接口。限流和熔断等都是针对簇点链路中的资源（簇点）设置的，资源名称默认就是接口的请求路径，例如购物车接口使用的就是 `@RequestMapping` 中的路径 `/carts` 作为资源名称，通过打开 `http-method-specify` 才能区分请求方法。

### 请求限流

在Sentinel控制台中，选择一个服务，进入簇点链路选项卡，在右边的“操作”栏中选择一个资源的“流控”按钮，即可新增流控规则。

**阈值类型**有两个选项，分别是QPS和并发线程数，确定好选项后可以在**单机阈值**框中填写上限。

1. **QPS**：每秒请求数，表示每秒允许通过的请求数量，超过该数量的请求将被限流。
2. **并发线程数**：同时处理的请求数，表示在同一时间内允许通过的请求数量，超过该数量的请求将被限流。

**流控模式**有三个选项，分别是直接、关联和链路。

1. **直接**：最简单的方式，直接对当前资源进行限流。
2. **关联**：关联限流，适用于对某个资源的访问依赖于另一个资源的场景，例如订单服务依赖于库存服务，如果库存服务不可用，那么订单服务也应该被限流。
3. **链路**：链路限流，适用于对某个调用链路的请求进行限流，例如购物车服务依赖于商品服务，如果商品服务不可用，那么购物车服务也应该被限流。

**流控效果**有三个选项，分别是快速失败、Warm Up和排队等待。

1. **快速失败**：最简单的方式，当请求超过阈值时，直接拒绝请求。
2. **Warm Up**：预热模式，适用于突发流量的场景，在流量突然增大时，先以较低的速率放行请求，然后逐渐提升到设定的阈值，避免瞬间流量过大导致系统崩溃。
3. **排队等待**：平滑模式，当请求超过阈值时，将多余的请求放入等待队列中，按照设定的速率依次处理，适用于对延迟不敏感但需要保证一定吞吐量的场景。

实现限流，需要在控制台中新增流控规则，选择资源名称，**设置阈值类型为QPS**，流控模式为直接，流控效果为快速失败。然后保存规则。

可以使用JMeter进行压测，可以看到被拒绝的请求会返回HTTP状态码429，表示请求过多，被流量控制了。

### 线程隔离

实现线程隔离，需要在控制台中新增流控规则，选择资源名称，**设置阈值类型为并发线程数**，流控模式为直接，流控效果为快速失败。然后保存规则。

通过线程隔离，一个业务的线程池满了之后，新的请求就会被拒绝或延迟处理，从而避免该业务的故障影响到其他业务。

### 服务降级

在配置线程隔离时，如果某个业务的线程池满了，这个业务就会变得不可用，虽然不会影响到其他业务，但是用户体验会变差。为了提升用户体验，可以为每个业务配置一个**降级方案**，当业务不可用时，调用降级方案来处理请求。

首先需要对Feign的请求启用Sentinel监控，在 `application.yaml` 中设置 `feign.sentinel.enabled=true`。然后对Feign客户端添加Fallback逻辑，有两种配置方式，分别是 `FallbackClass` 和 `FallbackFactory`，前者无法对远程调用的异常做处理，后者可以，推荐后者。

编写Fallback逻辑分为三步，创建Fallback逻辑、注册为Bean、注册到Feign客户端接口：

1. 创建一个实现 `FallbackFactory<T>` 接口的类，重写 `create` 方法，返回一个Feign客户端接口（也就是泛型中的 `T` ）的实现类。**这个返回的对象就是对这个Feign客户端接口的后备方案，即降级处理逻辑**。
2. 将这个类作为一个Bean注册到Spring容器中，可以添加 `@Component` 注解，或者在配置类中添加一个返回该类对象的Bean方法。推荐在Feign的配置类中注册，这样在 `@FeignClient` 注解中就可以直接引用这个配置类，防止扫描不到。
3. 在Feign客户端接口上添加 `@FeignClient` 注解，指定 `fallbackFactory` 属性为上一步创建的类。

具体实现见 `hm-api` 模块下的 `ItemClientFallbackFactory` 类和 `ItemClient` 接口。

最后在Sentinel控制台中**删除之前的线程隔离规则**，新增流控规则，选择资源名称，**设置阈值类型为并发线程数**，流控模式为直接，流控效果为快速失败。然后保存规则。

### 服务熔断

服务降级是针对调用者的保护，而服务熔断是针对被调用者的保护。我们想在知道服务不可用的情况下，就直接拒绝这个远程调用，不再调用它，然后再去走Fallback逻辑，从而进一步节省资源。当服务恢复正常时，还能自动取消熔断，恢复对该服务的调用。这个过程统称为熔断降级。**熔断降级**的解决雪崩问题的重要手段，思路是由**断路器**统计服务调用的异常比例、慢请求比例。

熔断机制的原理如下：当服务正常时，断路器处于**Closed**状态；如果超过阈值，则会进入**Open**状态，熔断该服务，拦截访问该服务的一切请求并快速失败；当服务恢复时，断路器会先进入**Half-Open**模式，允许少量请求通过，如果这些请求成功率较高，则断路器会Closed，恢复对该服务的调用；如果这些请求仍然失败率较高，则断路器会重新Open，继续阻止对该服务的调用。

实现熔断降级，需要在控制台中新增熔断降级规则，选择资源名称，其中有三种熔断策略：

1. **慢调用比例**：一段时间内，如果请求的平均响应时间超过设定的阈值，并且慢调用比例超过设定的阈值，则触发熔断。
2. **异常比例**：一段时间内，如果请求的异常比例超过设定的阈值，则触发熔断。
3. **异常数**：一段时间内，如果请求的异常数超过设定的阈值，则触发熔断。

后面还有多个配置项：

1. **最大RT**：慢调用的响应时间阈值，单位为毫秒。
2. **比例阈值**：触发熔断的比例阈值，单位为百分比。
3. **熔断时长**：熔断器进入熔断状态后打开的时间，单位为秒。
4. **最小请求数**：统计的请求数达到该值后，才会进行熔断判断。
5. **统计时长**：统计的时间窗口，单位为毫秒。

## 分布式事务的基本概念

数据库管理系统（DBMS）在写入或更新资料的过程中，为保证事务（transaction）是正确可靠的，所必须具备的四个特性：原子性（atomicity，或称不可分割性）、一致性（consistency）、隔离性（isolation，又称独立性）、持久性（durability）。

在分布式系统中，如果一个业务需要多个服务合作完成，而且每一个服务都有事务，那么多个事务必须同时成功或失败，这样的事务就是分布式事务。其中的每个服务的事务就是一个**分支事务**，而多个分支事务组成的整体事务就是一个**全局事务**。

### CAP理论

CAP理论指出，在分布式系统中，不可能同时满足一致性（Consistency）、可用性（Availability）和分区容错性（Partition Tolerance）这三个特性，只能同时满足其中的两个特性。

1. 分区容错性：分布式系统由多个通过网络连接的节点组成。当节点之间的网络发生故障时，系统被分割为多个无法相互通信的分区，而系统仍能继续提供服务。**由于网络故障是不可避免的，因此分区容错性是分布式系统必须具备的特性**。
2. 可用性：对于任何来自客户端的请求，系统中的每个**非故障节点**总能在**合理的时间内**返回一个**有效的响应**（非错误、非超时）。这个响应**不保证是最新数据**。可用性强调系统的**响应能力和服务质量**。
3. 一致性：指强一致性或线性一致性。任何读操作，总能读取到在此之前已完成的最新一次写操作的结果。在任意时刻，所有节点上的数据都是完全相同的。一致性强调数据的**准确性和完整性**。

保证可用性的例子：

- 社交媒体：你在微博上发了一张图，你的朋友可能几秒后才能看到。系统优先保证你能发出去（可用性），而不是全球用户同时看到（一致性）。
- 电商库存：高并发下，商品库存的显示可能不是100%精确。系统优先保证用户能下单（可用性），后续再通过其他流程处理超卖问题。

保证一致性的例子：

- 银行转账：系统宁可让转账交易失败或长时间等待，也绝不允许出现数据错乱（比如钱转出去了，但对方账户没收到）。
- 分布式数据库/锁：当主节点无法确认从节点状态时，可能会拒绝所有写操作，以防止“脑裂”导致数据不一致。

### BASE理论

BASE理论是对CAP理论的补充，提出了在分布式系统中可以通过基本可用（**B**asically **A**vailable）、软状态（**S**oft state）和最终一致性（**E**ventual consistency）来实现系统的高可用性和可扩展性。

1. 基本可用：系统在出现故障时，仍然能够提供基本的服务，保证系统的可用性。
2. 软状态：系统的状态可以是临时的，不需要强制要求所有节点的状态都一致，可以允许一定时间内、一定程度的状态不一致。
3. 最终一致性：系统中的所有数据副本，在经过一段时间后，最终会达到一致的状态。

分布式事务的解决方案主要针对**一致性问题**进行处理，被分为两大类，分别是**刚性事务**（追求强一致性）和**柔性事务**（追求最终一致性）。

### 刚性事务

严格遵循ACID原则，确保分布式事务的强一致性。常见的实现方式为**两阶段提交**（2PC）方案，通过引入**事务协调者**（Transaction Coordinator）对所有参与事务的服务进行统一管理和协调。2PC方案分为两个阶段：

1. 准备阶段/投票阶段：
    - 事务协调者向所有参与事务的服务发送 `prepare` 请求，询问它们是否可以提交事务。
    - 每个服务执行本地事务但只提交是否成功，而**不提交服务逻辑**，并将结果（成功或失败）返回给协调者。
2. 提交阶段/回滚阶段：
    - 如果所有服务都返回成功，协调者发送 `commit` 请求，要求所有服务提交事务。
    - 如果有任何服务返回失败，协调者发送 `rollback` 请求，要求所有服务回滚事务。

刚性事务提供了强一致性，保证了跨服务、跨数据库的操作原子性，对业务代码透明，开发起来也相对简单。但是同步阻塞的方式会导致性能低下，且对网络和数据库的要求较高，容易出现单点故障。如果在第二阶段 `commit` 过程中，协调者发生故障，可能会导致部分服务提交事务，而其他服务回滚事务，从而引发数据不一致的问题。

**刚性事务适合用于不追求高并发、业务链路短、且对一致性要求极高的场景，例如银行转账、支付等。**

XA协议是刚性事务的典型实现，MySQL、Oracle等主流数据库都支持XA协议。Java实现的标准接口是Java JTA（Java Transaction API），Spring框架提供了对JTA的支持，开源实现包括Atomikos、Bitronix、Narayana等。

### 柔性事务

柔性事务是一种基于最终一致性原则的分布式事务解决方案，允许在一定时间内数据不一致，但最终会达到一致状态，追求系统的高可用性和可扩展性。常见的实现方式有三种。

#### TCC模式

TCC（Try-Confirm-Cancel）模式是一种**基于业务补偿**的分布式事务解决方案，将一个分布式事务划分为三个阶段：

1. Try阶段：在这个阶段，事务协调者会调用所有参与事务的服务的 `try` 方法，尝试预留资源或锁定数据。此时并不进行实际的业务操作，只是做一些准备工作。
2. Confirm阶段：如果所有服务的 `try` 方法都成功返回，协调者会调用所有服务的 `confirm` 方法，正式提交事务，完成业务操作。
3. Cancel阶段：如果有任何服务的 `try` 方法失败，协调者会调用所有服务的 `cancel` 方法，回滚之前的预留操作，释放资源。

这种方法由于锁定的是业务逻辑资源而非数据库资源，因此不会长时间占用数据库连接，适合高并发场景，且能保证最终一致性。但是需要开发人员编写额外的业务逻辑代码来实现 `try`、`confirm` 和 `cancel` 方法，增加了开发复杂度，业务侵入性非常强。另外，TCC模式对网络和服务的可靠性要求较高，如果在 `confirm` 或 `cancel` 阶段发生故障，可能会导致数据不一致的问题。

适用于对一致性要求较高、业务逻辑复杂、需要预留资源、且能接受一定开发复杂度的场景，例如电商订单处理、库存管理等。

常见的实现包括Seata、Hmily、ByteTCC等。

#### Saga模式

Saga模式是一种**基于事件驱动**的分布式事务解决方案，将一个分布式事务划分为一系列的本地事务，每个本地事务完成后会发布一个事件，触发下一个本地事务的执行。如果某个本地事务失败，则会触发一系列的补偿操作，反向回滚之前已经完成的本地事务。

Saga模式对于耗时较长或者需要人工参与的业务流程适用性较好，每个事务提交快，系统吞吐量高，且基于事件驱动，使得系统耦合度较低。但事务执行过程中，系统处于很多中间态，可能会导致数据不一致的问题，且补偿操作的编写和维护都较为复杂。

适用于业务流程长、需要高并发、且允许数据短暂不一致的场景。例如，电商下单（创建订单 -> 扣减库存 -> 生成物流单）。

常见的实现包括Seata、ServiceComb Pack、Eventuate、Axon Framework等。也可以通过消息队列组件例如Kafka、RabbitMQ等来自定义实现编排Sage流程。

#### AT模式

基于2PC思想的柔性事务，见Seata一节中的[AT模式](#AT模式-1)介绍。

#### 事务性发件箱

事务性发件箱，也称本地消息表，实际是**一种设计模式**。是在微服务中实现可靠事件投递、保证最终一致性的事实标准方案。它确保了“执行本地业务”和“发送消息”这两个操作的原子性。

1. 在一个本地数据库事务中，同时完成业务数据的修改和“待发送消息”的入库（插入到一个专门的消息表/发件箱表）。
2. 因为这两个操作在同一个本地事务中，所以它们要么都成功，要么都失败，保证了原子性。
3. 一个独立的、可靠的后台服务（或定时任务）轮询这个消息表。
4. 将消息表中的消息投递到消息队列（MQ）。
5. 消息队列确保将消息投递给下游的消费者服务。
6. 消费者服务处理消息，完成后续业务。

事务性发件箱从根本上同时实现了高可靠、高性能、低耦合三个优点。解决了本地事务和分布式消息发送之间的原子性问题，消息异步发送，业务逻辑执行快，服务间通过消息队列解耦，生产者不关心消费者的状态。但实现有复杂度，需要额外维护消息表和轮询/投递服务，另外存在一定的消息延迟，只能保证最终一致性。

适用于几乎所有**需要通过异步事件来保证最终一致性**的微服务场景。

实际需要手动实现，有三大要素：

1. 消息表：在业务数据库中自行创建。可以设计一个专门的消息表，存储待发送的消息，包含消息内容、状态（待发送、已发送、发送失败）、重试次数、创建时间等字段。
2. 消息队列：使用现有的消息队列中间件，例如Kafka、RabbitMQ、RocketMQ等，负责消息的传递和存储。
3. 消息投递服务：实现一个独立的服务或定时任务，定期扫描消息表，读取待发送的消息，发送到消息队列，并更新消息状态。可以通过Spring的 `@Scheduled` 注解或Quartz等定时任务框架手动实现，也可以使用Debezium等CDC工具监听数据库变更来触发消息发送，将新数据自动推送到Kafka。

### 在业务中如何取舍？

首先是确定业务对强一致性的需求。如果业务对一致性要求极高，例如银行转账、支付等场景，建议使用刚性事务（2PC）。如果业务允许一定程度的数据不一致，可以考虑柔性事务。

其次是根据不同的业务场景选择合适的柔性事务模式。

- 如果业务需要资源预留，例如库存锁定、订单预处理等，且能接受较高的开发复杂度，可以选择TCC模式。
- 如果业务流程较长，涉及多个服务的协作，且允许数据短暂不一致，可以选择Saga模式。
- 如果只是需要可靠地通知下游服务完成某个操作，可以选择事务性发件箱模式。

## Seata - 分布式事务管理

官方文档：https://seata.apache.org/zh-cn/docs/overview/what-is-seata

Seata是阿里巴巴开源的分布式事务解决方案。提供TCC和Sage两种柔性事务模式的实现，同时也支持AT模式（基于二阶段提交的柔性事务）。

Seata通过引入一个**事务协调者**（Transaction Coordinator，TC）维护全局和分支事务的状态，协调全局事务提交或回滚。另有一个**事务管理器**（Transaction Manager，TM）定义全局事务的范围，也就是开始、提交或回滚全局事务的节点，一般来讲就是**全局事务方法的入口和出口**。**资源管理器**（Resource Manager，RM）用于管理分支事务的生命周期，与TC建立联系，注册分支事务和报告分支事务的状态，**数据源代理**（DataSource Proxy）用于拦截和管理分支事务。

Seata的Web控制台是一个基于Spring Boot的应用，提供了一个用户界面来监控和管理分布式事务。可以查看全局事务和分支事务的状态、日志等信息。

### 配置

将提供的 `seata` 目录整体拷贝到虚拟机中，修改 `application.yaml` 中的数据库用户名和密码配置，运行如下命令进行docker部署

```bash
docker run --name seata \
-p 8099:8099 \  # 微服务访问使用的端口
-p 7099:7099 \  # Seata控制台端口
-e SEATA_IP=192.168.*.* \  # 填入虚拟机地址
-v ./seata:/seata-server/resources \
--privileged=true \
--network hm-net \  # 使用自定义网络保证能连接到数据库
--ulimit nofile=65535:65535 \  # 防止文件描述符不够，Java 8的bug
-d \
seataio/seata-server:1.5.2
```

然后就可以访问 `http://192.168.*.*:7099` 进入Seata控制台，用户名和密码已经在 `application.yaml` 中配置好，均为 `admin`。

### 部署TC服务

首先在项目中引入 `spring-cloud-starter-alibaba-seata` 依赖，然后在Nacos中添加配置，内容有

1. Seata注册中心配置：尽管我们前面使用的是Nacos，但是Seata支持多种注册中心，不知道项目使用的是什么注册中心，因此需要再配置一遍，包括地址、命名空间等。Nacos通过自上而下查找命名空间、组、服务、集群实例的方式来定位服务，因此需要配置命名空间ID、组名和服务名。此处的服务名为Seata的服务名称。
2. 事务组名称：用于标识一组相关的分布式事务，所有参与同一全局事务的分支事务都应该属于同一个事务组。事务组名称可以自定义，但需要确保在整个分布式系统中唯一且一致。`tx-service-group` 属性用于指定事务组名称，所有参与同一全局事务的分支事务都应该使用相同的事务组名称。
3. 集群映射关系：用于将事务组名称映射到具体的Seata服务器集群。这样，当一个分支事务需要与事务协调者通信时，它可以通过事务组名称找到对应的Seata服务器集群，从而进行注册和状态报告。`vgroup-mapping` 属性用于配置事务组名称与Seata服务器集群的映射关系。集群名称需要在Seata服务器的配置文件中定义。

在购物车、商品、交易模块的启动配置文件中加入Nacos配置，然后在对应的启动配置的虚拟机选项中添加 `--add-opens=java.base/java.lang=ALL-UNNAMED` 选项，解决Java 9及以上版本的cglib不兼容问题。

在docker的log中看到三个服务均被注册到Seata服务器中，说明配置成功，需要清空Idea的缓存并重启防止配置失效。

### XA模式

在[刚性事务](#刚性事务)一节中已经介绍过，Seata的XA模式是基于二阶段提交（2PC）协议实现的，确保分布式事务的原子性和一致性。一阶段锁定数据库资源，二阶段结束后释放锁。

Seata通过引入一个**事务协调者**（Transaction Coordinator，TC）来管理全局事务的状态，并协调各个参与事务的服务（**资源管理器**，Resource Manager，RM）进行提交或回滚操作。

当请求进入了全局事务对应的方法，TM就会创建一个全局事务ID（XID），将其绑定到当前线程上，并将ID注册到TC中。然后在这个全局事务方法中调用的每一个微服务，TM都会调用RM管理的分支。RM会拦截对于数据库的操作，并将这些操作注册到TC中，形成分支事务。每个分支事务都会有一个唯一的分支事务ID（Branch ID），与全局事务ID一起组成完整的事务标识（XID + Branch ID）。

注册之后，RM会执行本地事务SQL，但不提交（通过一个**排他锁**来锁定这些数据），仅将是否成功的结果返回给TC。所有的分支执行完之后，TM告知TC这一信息，TC会根据所有分支的结果来决定是提交还是回滚全局事务。

如果TC在检查过后所有分支都成功，TC会发送 `commit` 请求，要求所有RM提交事务；如果有任何分支执行途中就发生异常，TM会立即通知TC进行回滚操作，TC会发送 `rollback` 请求，要求所有RM回滚事务。最终所有事务要么都提交，要么都回滚，并释放锁，从而保证了分布式事务的原子性和一致性。

实现时，只需要配置 `seata.data-source-proxy-mode: XA`，然后在每个需要分布式事务的方法上添加 `@GlobalTransactional` 注解即可。

测试时，如果全局事务方法对应的Service控制台和Seata控制台中都能看到回滚日志，说明测试成功。

### AT模式

AT模式是Seata的默认模式，是一种基于2PC的柔性事务解决方案。与传统的2PC不同，AT模式通过**数据源代理**（DataSource Proxy）来拦截和管理分支事务，避免了传统2PC中对数据库资源的长时间锁定，提高了系统的性能和可用性。通过对每个数据源设置一个 `undo_log` 表作为**快照**来记录数据变更的回滚信息，从而实现分支事务的回滚操作。

与XA模式不同的是，注册之后，RM会执行本地事务SQL，**并且提交**，但会将数据变更记录到 `undo_log` 表中，而不是通过锁定数据来实现事务的隔离。这样，其他事务仍然可以访问和修改这些数据，提高了系统的并发性能。

后续的向TC报告分支事务结果、TC决定提交或回滚全局事务的流程与XA模式类似。如果所有RM提交成功，那么TC只需要让各个RM删除 `undo_log` 表中的记录即可；如果有任何RM提交失败，TC会发送 `rollback` 请求，要求所有RM根据 `undo_log` 表中的记录来回滚事务。

实现时，需要先对各个需要分布式事务的数据库创建 `undo_log` 表，配置 `seata.data-source-proxy-mode: AT`，然后在**每个**需要分布式事务的方法上添加 `@GlobalTransactional` 注解即可。Seata官网的[快速入门](https://seata.apache.org/zh-cn/docs/user/quickstart/)中提供了创建 `undo_log` 表的SQL脚本。

测试时，如果全局事务方法对应的Service控制台和Seata控制台中都能看到回滚日志，说明测试成功。由于回滚成功后会自动清除快照信息，需要打断点才能看到数据。

### XA模式和AT模式的区别

XA模式一阶段不提交事务，而是锁定资源；AT模式一阶段直接提交，不锁定资源。

XA模式依赖数据库机制实现回滚；AT模式利用数据快照实现回滚。

XA模式强一致；AT模式最终一致。

# Day 6 - 消息队列 上 - 微服务间的异步通信

在前面实现远程调用时使用的是OpenFeign，它是一种同步调用方式。也就是说，调用方在发起请求后会一直等待，直到接收到响应结果或者发生超时异常。当在调用链路中有多个服务时，这种同步调用方式会导致请求的响应时间变长，甚至可能因为某个服务的故障而导致整个调用链路的失败，同时也存在并发效率低下的问题。

为了针对性地优化一些业务的调用链，可以使用消息队列（Message Queue，MQ）来实现异步通信。消息队列是一种基于发布/订阅模式的中间件，允许一个服务将消息发送到队列中，而另一个服务从队列中接收消息并进行处理。这样，发送方和接收方可以在不同的时间进行操作，从而实现异步通信。

## 基本概念

通过 `pay-service` 模块中的余额支付功能（`tryPayOrderByBalance`）来说明消息队列的使用场景。项目为了了简化支付流程，省略了第三方支付平台的对接，直接在交易服务中实现了余额支付功能。当发起支付请求时，支付服务会调用用户服务来扣减用户的余额，然后再**更新支付订单**的支付状态，最后还要调用交易服务**更新交易订单**的状态。

### 同步调用

为了保障余额安全，需要等待扣减余额成功后才能更新支付订单状态，因此这两个操作是同步调用的。这也是同步调用的典型场景，当一个操作的结果直接影响到另一个操作时，就需要使用同步调用，**时效性最强**。

但是之后的调用，或者更新的无关业务需求，如果都是同步调用的话，就会产生新的问题：

1. 许多新业务都需要在支付服务的方法中实现，导致支付服务的代码臃肿，**拓展性差**。
2. 多个同步调用的延迟积累，导致**性能下降**，影响用户体验。
3. 当某个节点出现故障时，可能会导致**级联失效**，影响系统的稳定性。

### 异步调用

由于交易订单的修改与支付订单的修改之间没有直接的业务关系，因此可以将交易订单的修改操作放到一个异步任务中进行处理。

考虑新的需求，如果需要在支付成功过后，发送通知、增加积分、更新用户等级等操作，这些操作都可以通过消息队列来实现异步处理，而无需在支付服务中实现。

这样，支付服务在完成余额扣减和支付订单状态更新后，就可以立即返回响应结果，而不需要等待其他无关业务操作完成，从而提高了系统的响应速度和并发处理能力。

在异步调用中，服务调用者和提供者的角色，转变为消息发送者和接收者。消息发送者将消息**发送到消息队列中**（**消息代理，用于管理、暂存、转发消息**），而消息接收者从消息队列中接收消息并进行处理。消息发送者和接收者之间通过消息队列进行解耦，彼此独立，可以在不同的时间进行操作。

优势：解除耦合、拓展性强，无需等待、提高性能，故障隔离，缓存消息、削峰填谷

问题：不能立即得到结果、**时效性低**，不确定下游业务的执行结果，业务安全依赖消息代理的可靠性

### MQ的技术选择

| 指标/维度      | RabbitMQ                                      | ActiveMQ                           | RocketMQ                                       | Kafka                                                                 |
|------------|-----------------------------------------------|------------------------------------|------------------------------------------------|-----------------------------------------------------------------------|
| **公司/社区**  | Pivotal/VMware, 社区/生态成熟                       | Apache, 社区活跃度一般                    | Alibaba, Apache, 国内社区极活跃                       | LinkedIn, Confluent, Apache, 大数据生态霸主                                  |
| **开发语言**   | Erlang (OTP 平台, 高并发)                          | Java                               | Java                                           | Java                                                                  |
| **协议支持**   | AMQP (核心), MQTT, STOMP (插件)                   | JMS, AMQP, MQTT, OpenWire 等 (协议最全) | 私有协议 (深度优化)                                    | 私有协议 (深度优化)                                                           |
| **可用性**    | 镜像队列 (数据完整复制)                                 | Master-Slave (依赖 ZK)               | 多 Master-多 Slave, Dledger (Raft)               | 分区副本机制 (Partition Replicas)                                           |
| **单机吞吐量**  | **万级/秒** (优秀)                                 | **千级/秒** (较低)                      | **十万级/秒** (非常高)                                | **百万级/秒** (最高)                                                        |
| **消息延迟**   | **毫秒级** (低延迟)                                 | 相对较高                               | **毫秒级** (低延迟)                                  | **毫秒级** (高负载下可能略高)                                                    |
| **消息可靠性**  | 事务, 发送方确认, ACK                                | JMS 标准持久化, 事务, ACK                 | 同步刷盘/复制, **事务消息**                              | `acks=all`, 分区多副本                                                     |
| **核心功能特性** | **灵活的路由策略**, 插件丰富                             | 遵循 JMS 标准, 协议兼容性好                  | **事务消息, 延迟消息, 顺序消息, 消息积压**                     | **流处理平台, 消息可回溯/重放, 长期持久化**                                            |
| **架构模型**   | 传统的 Broker-Consumer 模型 (Broker 负责路由)          | 传统的 Broker-Consumer 模型             | 传统的 Broker-Consumer 模型                         | 日志流模型 (Broker 是日志, Consumer 自行管理位移)                                   |
| **适用场景**   | 1. 企业级应用集成<br>2. 业务逻辑复杂, 需灵活路由<br>3. 对延迟敏感的业务 | 1. 遗留系统或遵循 JMS 规范的项目<br>2. 中小型系统   | 1. **金融、电商**等对可靠性要求极高的场景<br>2. 需要事务/延迟/顺序消息的业务 | 1. **大数据管道** (日志聚合, ELT)<br>2. 事件溯源, CQRS<br>3. 流式计算 (配合 Flink/Spark) |

## RabbitMQ

官方文档：https://www.rabbitmq.com/tutorials

### 基本架构

RabbitMQ的核心组件包括**消息发送者**（Publisher）、**消息消费者**（Consumer）、**消息队列**（Queue）、**交换机**（Exchange）、**绑定**（Binding）和**虚拟主机**（Virtual Host）。

1. **消息发送者**：负责将消息发送到交换机。
2. **消息消费者**：负责从队列中接收和处理消息。
3. **消息队列**：用于缓存消息，直到被消费者处理。
4. **交换机**：负责接收来自消息发送者的消息，并根据路由规则将消息路由到一个或多个队列。
5. **绑定**：用于将交换机和队列连接起来，定义消息从交换机到队列的路由规则。
6. **虚拟主机**：用于隔离不同的应用或用户，每个虚拟主机都有自己的交换机、队列和绑定。

### 安装与部署

在虚拟机中运行如下命令进行docker部署

```bash
docker run \
 -e RABBITMQ_DEFAULT_USER=rabbitmq \
 -e RABBITMQ_DEFAULT_PASS=rabbitmq \
 -v mq-plugins:/plugins \
 --name rabbitmq \
 --hostname rabbitmq \
 -p 15672:15672 \
 -p 5672:5672 \
 --network hm-net\
 -d \
 rabbitmq:3.8-management
```

然后就可以访问 `http://192.168.*.*:15672` 进入RabbitMQ管理界面，用户名和密码均为 `rabbitmq`。

## RabbitMQ控制台基础操作

### 建立绑定

需求：在RabbitMQ的控制台完成下列操作

- 新建队列hello.queue1和hello.queue2
- 向默认的amp.fanout交换机发送一条消息
- 查看消息是否到达hello.queue1和hello.queue2
- 总结规律

1. 登录RabbitMQ控制台，进入“Queues”页面，点击“Add a new queue”按钮，创建两个队列，分别命名为 `hello.queue1` 和 `hello.queue2`，其他配置保持默认，然后点击“Add queue”按钮完成创建。
2. 进入“Exchanges”页面，找到默认的 `amq.fanout` 交换机，点击进入。在“Publish message”部分，输入消息内容，例如 `Hello, RabbitMQ!`，然后点击“Publish message”按钮发送消息。
    - 由于此时没有将交换机与任何队列绑定，因此消息不会被路由到任何队列中
    - 也没有任何消费者来接收消息，因此消息会被丢弃，交换机和队列中都不会有任何消息
    - **交换机只起到路由的作用，而无法存储消息**
3. 进入“Queues”页面，查看 `hello.queue1` 和 `hello.queue2` 队列的消息数，应该都是0。
4. 回到“Exchanges”页面，点击 `amq.fanout` 交换机，进入“Bindings”部分，将 `hello.queue1` 和 `hello.queue2` 队列分别绑定到 `amq.fanout` 交换机上。
5. 再次发送消息，进入“Exchanges”页面，点击 `amq.fanout` 交换机，进入“Publish message”部分，输入消息内容，例如 `Hello, RabbitMQ!`，然后点击“Publish message”按钮发送消息。
6. 回到“Queues”页面，查看 `hello.queue1` 和 `hello.queue2` 队列的消息数，应该都是1。
7. 点击 `hello.queue1` 队列，进入队列详情页面，点击“Get messages”按钮，可以看到刚才发送的消息。
    - 可见消息队列中的数据走向是：消息发送者 -> 交换机 -> 队列 -> 消费者
    - **必须将交换机和队列绑定，消息才能被路由到队列中**

### 数据隔离

RabbitMQ通过虚拟主机（Virtual Host，vhost）来实现数据隔离。每个虚拟主机都有自己的交换机、队列和绑定，可以将不同的应用或用户的数据隔离开来，防止相互干扰。

需求：在RabbitMQ的控制台完成下列操作

- 新建一个用户hmall
- 为hmall用户创建一个虚拟主机
- 测试不同虚拟主机之间的数据隔离现象

1. 登录RabbitMQ控制台，进入“Admin”页面，点击“Add a new user”按钮，创建一个新用户，用户名为 `hmall`，密码为 `hmall`，角色选择 `administrator`，然后点击“Add user”按钮完成创建。
2. 在“Virtual Hosts”部分，点击“Add a new virtual host”按钮，创建一个新虚拟主机，命名为 `/hmall`，然后点击“Add virtual host”按钮完成创建。
3. 在“Permissions”部分，选择刚才创建的用户 `hmall` 和虚拟主机 `/hmall`，然后点击“Set permission”按钮，设置该用户对该虚拟主机的权限，选择“Configure”、“Write”、“Read”权限，然后点击“Set permission”按钮完成设置。
4. 退出当前用户，重新登录，用户名和密码均为 `hmall`，进入“Queues”页面，尝试通过“Get messages”按钮查看之前创建的 `hello.queue1` 和 `hello.queue2` 队列，应该会提示“Access refused”，因为这些队列属于默认的虚拟主机 `/`，而当前用户 `hmall` 只能访问 `/hmall` 虚拟主机。这样就实现了不同虚拟主机之间的数据隔离。

## Java客户端 - Spring AMQP

Spring AMQP是Spring框架提供的对AMQP协议（Advanced Message Queueing Protocol）的支持，简化了RabbitMQ的使用。Spring AMQP提供了模板用于发送和接收消息，分为两个部分，其中spring-amqp是基础抽象，而spring-rabbit是对RabbitMQ的具体实现。

官方文档：https://spring.io/projects/spring-amqp#learn

RabbitMQ常用的以下几种模式：

1. 队列
    - 简单队列（Simple Queue）
    - 工作队列（Work Queue）
2. 交换机
    - Fanout交换机（发布订阅模式）
    - Direct交换机（路由模式）
    - Topic交换机（通配符模式）
    - Headers交换机（属性匹配模式，使用较少）

### Simple Queue

需求：在 `mq-demo` 项目中使用Spring AMQP实现消息的发送和接收

- 利用控制台创建队列simple.queue
- 在publisher服务中，利用SpringAMQP直接向simple.queue发送消息
- 在consumer服务中，利用SpringAMQP编写消费者，监听simple.queue队列

在 `mq-demo` 项目父工程中引入 `spring-boot-starter-amqp` 依赖，然后在配置文件中添加RabbitMQ的连接配置

```yaml
spring:
  rabbitmq:
    host: 192.168.*.* # 你的虚拟机IP
    port: 5672 # 端口
    virtual-host: /hmall # 虚拟主机
    username: hmall # 用户名
    password: hmall # 密码
```

创建一个测试 `com.itheima.publisher.amqp.SpringAmqpTest` ，使用 `RabbitTemplate` 发送消息

```java

@SpringBootTest
public class SpringAmqpTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSimpleQueue() {
        // 队列名称
        String queueName = "simple.queue";
        // 消息
        String message = "hello, spring amqp!";
        // 发送消息
        rabbitTemplate.convertAndSend(queueName, message);
    }
}
```

在 `consumer` 模块中创建一个消费者 `com.itheima.consumer.listener.SpringRabbitListener`，使用 `@RabbitListener` 注解监听队列

```java

@Component
public class SpringRabbitListener {
    // 利用RabbitListener来声明要监听的队列信息
    // 将来一旦监听的队列中有了消息，就会推送给当前服务，调用当前方法，处理消息。
    // 可以看到方法体中接收的就是消息体的内容
    @RabbitListener(queues = "simple.queue")
    public void listenSimpleQueueMessage(String msg) throws InterruptedException {
        System.out.println("spring 消费者接收到消息：【" + msg + "】");
    }
}
```

### Work Queue

让多个消费者绑定到同一个队列，共同消费队列中的消息。

需求：

- 在RabbitMO的控制台创建一个队列，名为work.queue
- 在publisher服务中定义测试方法，发送50条消息到work.queue
- 在consumer服务中定义两个消息监听者，都监听work.queue队列

类似地，创建两个消费者监听器方法，发现控制台的输出中，两个消费者交替接收并处理消息，说明RabbitMQ**默认使用轮询**的方式将消息分发给多个消费者。

实际部署时，是部署同一个服务的不同实例，而非在同一个服务实例中编写多个监听器方法，这样就实现了**负载均衡**。

如果想根据消费者的处理能力来分发消息，可以在 `application.yaml` 中配置 `spring.rabbitmq.listener.simple.prefetch` 属性，表示每个消费者在处理完当前消息之前，最多可以接收多少条未确认的消息。这样，处理能力强的消费者可以接收更多的消息，而处理能力弱的消费者则会接收较少的消息，从而实现根据处理能力分发消息。

如果设置为1，那么每个消费者在处理完当前消息之前，只能接收1条未确认的消息。这样，RabbitMQ会等待消费者处理完当前消息并发送确认后，才会将下一条消息分发给该消费者，从而实现**公平分发**。

### Fanout交换机

也称为发布订阅模式（Publish/Subscribe），让多个队列绑定到同一个交换机，使得消息可以**广播**到所有绑定的队列。

一般是用于需要同时处理的场景，例如前文提到的场景，在订单状态修改为已支付后，另外的几个服务分别进行积分增加、发送通知、日志收集等，这些业务之间彼此是不相关的，可以通过发布订阅模式来实现异步处理。

首先在RabbitMQ控制台创建一个交换机，类型选择 `fanout`，命名为 `hmall.fanout`，然后创建两个队列，分别命名为 `fanout.queue1` 和 `fanout.queue2`，最后将这两个队列绑定到 `hmall.fanout` 交换机上。

代码方面，在 `publisher` 模块中创建一个测试类 `com.itheima.publisher.amqp.FanoutTest`，使用 `RabbitTemplate` 发送消息到交换机。在 `consumer` 模块中创建两个消费者监听器方法，分别监听 `fanout.queue1` 和 `fanout.queue2` 队列。

发送消息的方法与队列模式的有区别，为三个参数，其中 `exchange` 参数指定交换机名称，`routingKey` 参数在 `fanout` 交换机中不起作用，可以传入空字符串。

```java

@Test
public void testFanoutExchange() {
    // 交换机名称
    String exchangeName = "hmall.fanout";
    // 消息
    String message = "hello, everyone!";
    rabbitTemplate.convertAndSend(exchangeName, "", message);
}
```

### Direct交换机

也称为路由模式（Routing），根据消息的路由键（Routing Key）将消息路由到不同的队列。每一个队列都绑定到交换机上，并**指定一个路由键**。当发布者发送消息时，也需要指定一个路由键，交换机会将消息路由到所有消息和队列的路由键**完全匹配**的队列中。

仍然考虑前面的场景，假设用户下订单后没有支付，而是取消，那么有的服务需要处理这个取消订单的消息，而有的服务则不需要处理。此时就可以使用路由模式来实现。

首先在RabbitMQ控制台创建一个交换机，类型选择 `direct`，命名为 `hmall.direct`，然后创建两个队列，分别命名为 `direct.queue1` 和 `direct.queue2`，最后将这两个队列绑定到 `hmall.direct` 交换机上，并分别指定路由键 `red`, `blue` 和 `red`, `yellow`。

代码方面，与Fanout交换机写法类似，不过发送消息时需要指定路由键。

```java

@Test
public void testSendDirectExchange() {
    // 交换机名称
    String exchangeName = "hmall.direct";
    // 消息
    String message = "红色警报！";
    // 发送消息
    rabbitTemplate.convertAndSend(exchangeName, "red", message);
}
```

### Topic交换机

也称为通配符模式（Wildcard），根据消息的路由键和队列绑定的路由模式将消息路由到不同的队列。

路由模式支持两种通配符：

- `*`：匹配一个单词，例如 `item.#` 能够匹配 `item.spu.insert` 或者 `item.spu`
- `#`：匹配零个或多个单词，例如 `item.*` 只能匹配 `item.spu`，但不能匹配 `item.spu.insert`

首先在RabbitMQ控制台创建一个交换机，类型选择 `topic`，命名为 `hmall.topic`，然后创建两个队列，分别命名为 `topic.queue1` 和 `topic.queue2`，最后将这两个队列绑定到 `hmall.topic` 交换机上，并分别指定路由模式 `china.#` 和 `#.weather`。

代码方面，与Direct交换机写法相同，交换机会自动根据通配符进行路由。

## 使用API声明队列和交换机

Spring AMQP提供了几个接口用于声明队列、交换机和绑定关系，分别是 `Queue`、`Exchange` 和 `Binding`。可以通过@Bean的方式将这些接口的实现类注入到Spring容器中，Spring AMQP会在应用启动时自动创建这些组件。也可以通过工厂类 `XxxBuilder` 构建这些组件。

### 基于类声明

例如，以下代码可以用于生成一个Fanout交换机：

```java

@Configuration
public class FanoutConfig {
    /**
     * 声明交换机
     * @return Fanout类型交换机
     */
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("hmall.fanout");
    }

    /**
     * 第1个队列
     */
    @Bean
    public Queue fanoutQueue1() {
        return new Queue("fanout.queue1");
    }

    /**
     * 绑定队列和交换机
     */
    @Bean
    public Binding bindingQueue1(Queue fanoutQueue1, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(fanoutQueue1).to(fanoutExchange);
    }

    /**
     * 第2个队列
     */
    @Bean
    public Queue fanoutQueue2() {
        return new Queue("fanout.queue2");
    }

    /**
     * 绑定队列和交换机
     */
    @Bean
    public Binding bindingQueue2(Queue fanoutQueue2, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(fanoutQueue2).to(fanoutExchange);
    }
}
```

以下代码可以用于生成一个Direct交换机：

```java

@Configuration
public class DirectConfig {

    /**
     * 声明交换机
     * @return Direct类型交换机
     */
    @Bean
    public DirectExchange directExchange() {
        return ExchangeBuilder.directExchange("hmall.direct").build();
    }

    /**
     * 第1个队列
     */
    @Bean
    public Queue directQueue1() {
        return new Queue("direct.queue1");
    }

    /**
     * 绑定队列和交换机
     */
    @Bean
    public Binding bindingQueue1WithRed(Queue directQueue1, DirectExchange directExchange) {
        return BindingBuilder.bind(directQueue1).to(directExchange).with("red");
    }

    /**
     * 绑定队列和交换机
     */
    @Bean
    public Binding bindingQueue1WithBlue(Queue directQueue1, DirectExchange directExchange) {
        return BindingBuilder.bind(directQueue1).to(directExchange).with("blue");
    }

    /**
     * 第2个队列
     */
    @Bean
    public Queue directQueue2() {
        return new Queue("direct.queue2");
    }

    /**
     * 绑定队列和交换机
     */
    @Bean
    public Binding bindingQueue2WithRed(Queue directQueue2, DirectExchange directExchange) {
        return BindingBuilder.bind(directQueue2).to(directExchange).with("red");
    }

    /**
     * 绑定队列和交换机
     */
    @Bean
    public Binding bindingQueue2WithYellow(Queue directQueue2, DirectExchange directExchange) {
        return BindingBuilder.bind(directQueue2).to(directExchange).with("yellow");
    }
}
```

### 基于注解声明

也可以使用 `@RabbitListener` 注解的 `bindings` 属性来声明队列、交换机和绑定关系。

以下代码可以用于生成一个Direct交换机：

```java

@RabbitListener(bindings = @QueueBinding(
        value = @Queue(name = "direct.queue1"),
        exchange = @Exchange(name = "hmall.direct", type = ExchangeTypes.DIRECT),
        key = {"red", "blue"}
))
public void listenDirectQueue1(String msg) {
    System.out.println("消费者1接收到direct.queue1的消息：【" + msg + "】");
}

@RabbitListener(bindings = @QueueBinding(
        value = @Queue(name = "direct.queue2"),
        exchange = @Exchange(name = "hmall.direct", type = ExchangeTypes.DIRECT),
        key = {"red", "yellow"}
))
public void listenDirectQueue2(String msg) {
    System.out.println("消费者2接收到direct.queue2的消息：【" + msg + "】");
}
```

以下代码可以用于生成一个Topic交换机：

```java

@RabbitListener(bindings = @QueueBinding(
        value = @Queue(name = "topic.queue1"),
        exchange = @Exchange(name = "hmall.topic", type = ExchangeTypes.TOPIC),
        key = "china.#"
))
public void listenTopicQueue1(String msg) {
    System.out.println("消费者1接收到topic.queue1的消息：【" + msg + "】");
}

@RabbitListener(bindings = @QueueBinding(
        value = @Queue(name = "topic.queue2"),
        exchange = @Exchange(name = "hmall.topic", type = ExchangeTypes.TOPIC),
        key = "#.news"
))
public void listenTopicQueue2(String msg) {
    System.out.println("消费者2接收到topic.queue2的消息：【" + msg + "】");
}
```

### 消息转换器

`RabbitTemplate` 中的 `convertAndSend` 方法为什么叫做 `convert` 而不是 `send` 呢？因为它会将传入的对象转换为消息体，然后发送到队列中。AMQP默认的消息体是字节数组，默认的序列化方式是JDK序列化，但是JDK的序列化有数据体积大、安全性低、可读性差等缺点，因此Spring AMQP提供了多种消息转换器（Message Converter）来满足不同的需求。

#### 测试默认转换器

需求：测试利用SpringAMQP发送对象类型的消息

- 声明一个队列，名为object.queue
- 编写单元测试，向队列中直接发送一条消息，消息类型为Map
- 在控制台查看消息，总结发现的问题

首先在RabbitMQ控制台创建一个队列，命名为 `object.queue`。然后在 `publisher` 模块中创建一个测试方法：

```java

@Test
public void testDefaultMessageConverter() {
    String queueName = "object.queue";
    HashMap<String, Object> msg = new HashMap<>();
    msg.put("name", "itheima");
    msg.put("age", 6);
    msg.put("address", "北京");
    rabbitTemplate.convertAndSend(queueName, msg);
}
```

在控制台中获取消息，发现消息体是乱码的字节数组，无法查看具体的消息内容，且占用空间较大（201字节）。

```text
rO0ABXNyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRocmVzaG9sZHhwP0AAAAAAAAx3CAAAABAAAAADdAAHYWRkcmVzc3QABuWMlS6rHQABG5hbWV0AAdpdGhlaW1hdAADYWdlc3IAEWphdmEubGFuZy5JbnRlZ2VyEuKgpPeBhzgCAAFJAAV2YWx1ZXhyABBqYXZhLmxhbmcuTnVtYmVyhqyVHQuU4IsCAAB4cAAAAAZ4
```

属性中的数据类型是 `content_type: application/x-java-serialized-object
` ，这说明默认使用的是JDK序列化方式，通过跟踪代码，这个默认的消息转换器是 `SimpleMessageConverter`，具体操作是其中的 `createMessage` 方法，它会根据传入对象的类型选择合适的序列化方式，对于 `String` 类型使用UTF-8编码，对于 `byte[]` 类型直接使用，对于其他类型则使用JDK序列化，即实现了 `Serializable` 接口的对象，使用的是 `ObjectOutputStream` 进行序列化。

#### 使用JSON转换器

推荐使用JSON转换器，既能保证消息体积小、可读性强，又能避免JDK序列化的安全性问题（主要是代码注入）。

首先在 `publisher` 和 `consumer` 模块中都引入 `jackson-databind` 依赖（对于Java 8时间类型，还需要额外引入 `jackson-datatype-jsr310` 并配置 `ObjectMapper`），然后在配置类中配置 `Jackson2JsonMessageConverter` 消息转换器：

```xml

<dependencies>
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
    </dependency>
    <dependency>
        <groupId>com.fasterxml.jackson.datatype</groupId>
        <artifactId>jackson-datatype-jsr310</artifactId>
    </dependency>
</dependencies>
```

然后在两个模块的配置类中添加如下代码：

```java

@Bean
public MessageConverter messageConverter() {
    return new Jackson2JsonMessageConverter();
}
```

再向队列中发送消息，发现消息体变成了JSON格式，且占用空间较小（45字节），可读性强。

```text
{"address":"北京","name":"itheima","age":6}
```

同时可见属性中的数据类型变成了 `content_type: application/json`，且有多个header属性指明了消息的类型，例如 `__TypeId__: java.util.HashMap`。

## 业务改造

在 `pay-service` 模块的 `tryPayOrderByBalance` 方法中，将支付成功后更新交易订单的操作，改为发送一条消息到RabbitMQ，由交易服务异步接收并处理。这个操作调用了 `TradeClient.markOrderPaySuccess`，对应到Controller就是 `trade-service` 模块的 `OrderController.markOrderPaySuccess` 方法。

### 配置RabbitMQ

为生产者和消费者均引入 `spring-boot-starter-amqp` 依赖：

```xml

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
```

在 `application.yaml` 中配置RabbitMQ的连接信息，建议抽取到Nacos然后配置到 `bootstrap.yaml` 中：

```yaml
spring:
  rabbitmq:
    host: 192.168.*.* # 你的虚拟机IP
    port: 5672 # 端口
    virtual-host: /hmall # 虚拟主机
    username: hmall # 用户名
    password: hmall # 密码
```

在 `hm-common` 模块中创建一个配置类 `com.itheima.common.config.MqConfig`，用于声明消息转换器：

```java

@Configuration
public class MqConfig {
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
```

由于配置类处于 `hm-common` 模块中，因此需要在 `pay-service` 和 `trade-service` 模块的启动类上添加 `@ComponentScan` 注解，扫描 `com.hmall.common.config` 包。或者在 `hm-common` 模块中的 `META-INF/spring.factories` 文件中添加如下配置：

```properties
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
    com.hmall.common.config.MqConfig
```

### 编写消息监听器

在 `trade-service` 模块中创建一个消息监听器类 `com.hmall.trade.listener.PayStatusListener`，用于监听支付成功的消息：

```java

@Component
@Slf4j
@RequiredArgsConstructor  // 注入使用构造器注入
public class PayStatusListener {

    // 注入订单服务
    private final IOrderService orderService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "trade.pay.success.queue", durable = "true"),
            exchange = @Exchange(name = "pay.direct", durable = "true", type = "direct"),
            key = {"pay.success"}
    ))
    public void listenPaySuccess(Long orderId) {
        log.info("接收到支付成功的消息，订单ID：{}", orderId);
        // 调用订单服务，更新订单状态，逻辑与markOrderPaySuccess一致
        orderService.markOrderPaySuccess(orderId);
    }
}
```

### 编写消息发送者

在 `pay-service` 模块的 `PayOrderServiceImpl` 类中，注入 `RabbitTemplate`，并在 `tryPayOrderByBalance` 方法中发送支付成功的消息：

```java

@Service
@RequiredArgsConstructor
public class PayOrderServiceImpl extends ServiceImpl<PayOrderMapper, PayOrder> implements IPayOrderService {
    // private final TradeClient tradeClient;
    private final RabbitTemplate rabbitTemplate;

    @Override
    @Transactional
    public void tryPayOrderByBalance(PayOrderFormDTO payOrderFormDTO) {
        // ...扣减余额和修改支付单状态的代码...

        // 原本的修改订单状态逻辑
        // tradeClient.markOrderPaySuccess(po.getBizOrderNo());

        // 发送支付成功的消息
        try {
            rabbitTemplate.convertAndSend("pay.direct", "pay.success", po.getBizOrderNo());
        } catch (Exception e) {
            log.error("支付成功消息发送失败，订单ID：{}", po.getBizOrderNo(), e);
        }
    }
}
```

> 这里使用try-catch是为了不影响主要业务逻辑，**防止消息发送失败导致整个支付流程回滚**，实际项目中可以根据业务需求决定是否需要重试或者补偿机制，在catch代码块中实现具体逻辑。

### 使用技巧 - 用POJO类封装发送的消息

在实际项目中，发送的消息往往是一个复杂的对象，而不仅仅是一个简单的ID或者字符串。为了提高代码的可读性和维护性，建议使用POJO类来封装发送的消息。

例如在 `trade-service` 的 `OrderServiceImpl.createOrder` 方法中，订单创建成功后，需要发送一条消息通知购物车服务清空购物车，可以创建一个POJO类 `com.hmall.trade.message.CartCleanMessage` 来封装消息内容：

```java

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartCleanMessage implements Serializable {
    private Long userId;
    private Collection<Long> itemIds;
}
```

然后在 `OrderServiceImpl` 中发送消息：

```java

@Override
@GlobalTransactional  // seata分布式事务
public Long createOrder(OrderFormDTO orderFormDTO) {
    // ...订单处理逻辑...

    // 发送消息，清理购物车
    CartCleanMessage msg = CartCleanMessage.builder()
            .userId(UserContext.getUser())
            .itemIds(itemIds).build();
    try {
        rabbitTemplate.convertAndSend("trade.topic", "order.create", msg);
    } catch (Exception e) {
        log.error("订单微服务-发送清理购物车消息失败，用户id：{}，商品id集合：{}", UserContext.getUser(), itemIds, e);
    }

    // ...返回订单ID的逻辑
}
```

# Day 7 - 消息队列 下 - 可靠性与延迟消息

仍然考虑上一章中的业务，在支付服务中，第一步调用用户服务尝试扣减用户余额，第二步修改支付订单状态，如果订单已支付，那么第三步支付服务向消息代理发送一条支付成功的消息，交易服务异步接收并处理该消息，修改交易订单状态。

现在考虑这样一个场景，前两步扣减余额和修改支付订单状态都成功了，但是在第三步发送消息时，网络出现故障，导致消息发送失败，那么交易订单的状态就不会被更新，用户会看到订单一直处于未支付状态，导致同步出现问题，影响用户体验。

为了确保用户体验的一致性，那么就需要第三步中支付服务发出的消息能够**可靠地**到达交易服务，交易服务也能够**可靠地**处理该消息。这就需要我们在设计和实现时，考虑消息的可靠性问题。

那么从数据流向的角度来看，消息可靠性可以分为三个环节：

- **发送者可靠性**：确保消息能够成功发送到消息代理
- **消息队列可靠性**：确保消息在消息代理中不会丢失
- **消费者可靠性**：确保消费者能够成功接收并处理消息

即便已经采用了多种手段对消息可靠性进行保障，消息仍然有可能处理失败，这时需要设计兜底策略进行处理，具体分为

> 总结：如何保证支付服务与交易服务之间的消息可靠性？
> - 首先，我们使用了MQ消息通知机制，对交易服务通知，并进行异步处理，缩短主要业务的流程和处理时间，提高了系统的可用性
> - 其次，我们从发送者、消息队列和消费者三个环节对MQ消息通知的可靠性进行了保障。发送者方面提供了发送者确认和发送者重连机制；消费者方面提供了消费者确认和消费者重试机制；消息队列方面提供了消息的持久化，保障了数据安全
> - 最后，我们还针对特定业务场景，设计了幂等性处理，在保障消息**至少被处理一次**的同时，**避免了重复处理**带来的数据不一致问题

> 总结：RabbitMQ延迟消息插件是如何管理不同延迟时间的消息的？
> - RabbitMQ本身不支持延迟队列功能，但是可以通过TTL和死信队列来实现延迟消息，也可以通过延迟消息插件来实现，以下是延迟消息插件的处理方案
> - 插件中通过将交换机设置为延迟交换机，消息发送到交换机时，可以指定一个延迟时间，交换机会将消息存储在内部的延迟队列中，直到延迟时间到达后，再将消息路由到绑定的队列中
> - 针对不同的延迟时间，插件内部会维护一个**优先级队列**，通常用最小堆实现，所有被发送到延迟交换机的消息，都会被放入这个优先级队列中，并根据它们的**绝对投递时间戳**（也就是 `当前时间 + 投递时间`）进行排序
> - 插件内部有一个轻量级的Erlang进程充当计时器，工作原理是**计算出下一次需要投递消息的确切时间点，然后设置一个闹钟，并一直睡到那个时间点再醒来**。因此该进程大部分时间都是在睡眠状态，不会占用过多的CPU资源
> - 死信队列的问题在于它是FIFO的，如果一条延迟60分钟的消息先进队，后面又有一条延迟1分钟的消息，那么1分钟的消息就会被60分钟的消息阻塞，称为队头阻塞

## 可靠性 - 发送者

发送者可靠性主要包括两个方面：

1. 发送者重连：关注消息是否能被MQ正确接收
2. 发送确认机制：关注消息是否能被正确路由到队列

> 注意：当网络不稳定的时候，利用**发送者重连机制**可以有效提高消息发送的成功率，然而Spring AMQP提供的重试机制是**阻塞式**的，在多次重试期间，当前线程会一直被阻塞，极大影响业务性能。

> 如果对于业务性能有要求，建议**禁用重试机制**。如果一定要使用的话，就需要合理配置等待时长、等待乘数和最大重试次数，避免频繁重试影响业务性能。另一种方法是使用异步发送消息，发送失败后再异步重试。

> 注意：**发送者确认机制**同样比较消耗MQ的性能，一般不建议开启，另外我们考虑一些触发的情况
> - 路由失败：一般是Routing Key错误，或者交换机没有绑定队列，这种情况重发消息没有意义，是编程错误
> - 交换机名称错误：也是编程错误，重发消息没有意义
> - MQ内部故障：这种情况可以重发消息，但是概率非常低，因此只有在业务对消息可靠性要求非常高的情况下，才考虑开启发送确认机制。而且一般仅开启 `ConfirmCallback` 回调函数处理NACK就够了，进入 `ReturnCallback` 一般是编程错误，禁用来辅助提供调试信息

### 发送者重连

有时因为网络波动，可能会出现发送者连接MQ失败的情况。通过配置可以让发送者在连接失败时自动重连，默认是关闭的。发送者重连机制类似以太网的截断二进制指数退避，即每次连接失败后，等待一段时间再重试，等待时间逐渐增加，直到达到最大重试次数。

```yaml
spring:
  rabbitmq:
    connection-timeout: 1s # 设置MQ的连接超时时间，如果超时则失败
    template:
      retry:
        enabled: true # 开启超时重试机制，超时失败时自动重试，默认false
        initial-interval: 1000ms # 失败后的初始等待时间，降低长时间网络波动的影响，默认是1000ms
        multiplier: 1 # 失败后下次的等待时长倍数，下次等待时长 = initial-interval * multiplier，默认是1
        max-attempts: 3 # 最大重试次数，默认是3
```

在 `mq-demo` 项目中添加配置，docker暂时关闭RabbitMQ，然后通过测试类发送消息，观察控制台日志，可以看到发送者在连接失败后，进行了重试，最终还是失败了，这里的时间间隔为2s是因为连接超时的1s加上了发送重试间隔1s：

```text
09-23 15:12:06:879  INFO 464 --- [           main] o.s.a.r.c.CachingConnectionFactory       : Attempting to connect to: [192.168.*.*:5672]
09-23 15:12:08:901  INFO 464 --- [           main] o.s.a.r.c.CachingConnectionFactory       : Attempting to connect to: [192.168.*.*:5672]
09-23 15:12:10:921  INFO 464 --- [           main] o.s.a.r.c.CachingConnectionFactory       : Attempting to connect to: [192.168.*.*:5672]

org.springframework.amqp.AmqpIOException: java.net.SocketTimeoutException: Connect timed out

```

### 发送确认机制

Spring AMQP提供了两种发送确认机制：

1. **发布确认**（Publisher Confirms）：确保消息成功发送到交换机
2. **发布回退**（Publisher Returns）：确保消息成功路由到队列

开启确认机制之后，当发送者发送消息给MQ后，MQ会在内部判断消息的处理情况，如果被正确处理，那么就会返回确认结果给发送者，反之返回失败结果，具体来说有以下几种情况：

1. 消息投递到了MQ，但是路由失败：MQ通过Publisher Return返回路由异常原因，并通过Publisher Confirm返回ACK，告知投递成功
    - 这一般是因为路由管理员没有给交换机绑定对应的队列，导致交换机无法路由消息
    - **此时做消息重发没有意义，因为路由信息本身就是错误的**
2. 临时消息投递到了MQ，并且入队成功：返回ACK，告知投递成功
    - 投递到的是非持久化队列，消息是保存在内存中的，MQ宕机后消息会丢失
3. 持久消息投递到了MQ，并且入队成功**并完成持久化**：返回ACK，告知投递成功
    - 投递到的是持久化队列，消息是保存在磁盘中的，直到消息进入到磁盘才会返回ACK
4. 除了以上三种情况以外，其他任何情况都视为投递失败：返回NACK，告知投递失败
    - 例如网络异常、MQ宕机等

为了开启发送确认机制，需要在配置文件中添加如下配置：

```yaml
spring:
  rabbitmq:
    publisher-confirm-type: correlated # 开启publisher confirm机制，并设置confirm类型
    publisher-returns: true # 开启publisher return机制
```

其中 `publisher-confirm-type` 属性有三个可选值：

1. `none`：不开启发送确认机制，默认值
2. `simple`：同步阻塞等待MQ的回执，等待期间后面的代码不会执行，**不常用**
3. `correlated`：MQ异步回调返回回执，不影响后面代码的执行

然后配置 `RabbitTemplate` 的回调函数，首先是返回回调，确认消息是否成功到达队列：

```java

@Slf4j
@AllArgsConstructor
@Configuration
public class MqConfig {
    // 每个RabbitTemplate实例只能设置一个回调函数
    private final RabbitTemplate rabbitTemplate;

    // 在类初始化后设置回调函数
    @PostConstruct
    public void init() {
        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            @Override
            public void returnedMessage(ReturnedMessage returned) {
                log.error("触发return callback,");
                log.debug("exchange: {}", returned.getExchange());
                log.debug("routingKey: {}", returned.getRoutingKey());
                log.debug("message: {}", returned.getMessage());
                log.debug("replyCode: {}", returned.getReplyCode());
                log.debug("replyText: {}", returned.getReplyText());
            }
        });
    }
}
```

接着是确认回调，确认消息是否成功到达交换机。由于每次发消息时的逻辑不一定相同，因此ConfirmCallback需要在每次发消息时都定义，这就需要传进 `convertAndSend` 方法第四个参数 `CorrelationData`，用于携带当前消息的唯一标识ID和回调结果对象，它是一个 `SettableListenableFuture`，可以通过它来设置回调结果。

```java

@Test
void testPublisherConfirm() {
    // 1. 创建CorrelationData，指定一个唯一ID
    // 该ID用于标识当前消息，方便在回调函数中根据ID来处理对应的消息
    CorrelationData cd = new CorrelationData(UUID.randomUUID().toString());

    // 2. 给Future添加ConfirmCallback
    // 这是异步任务完成时的回调函数，只拿到Future是无法获取回执结果的，必须要手动添加回执逻辑
    cd.getFuture().addCallback(
            new ListenableFutureCallback<CorrelationData.Confirm>() {

                // 内部处理Future发生异常时的处理逻辑
                // 一般不会到这里，因为内部已经做了很多异常捕获
                @Override
                public void onFailure(Throwable ex) {
                    // 2.1.Future发生异常时的处理逻辑，基本不会触发
                    log.error("send message fail", ex);
                }

                // Future成功时的处理逻辑
                // 需要根据返回的结果是否是ACK进行后续的逻辑处理
                // 如果是ACK，记录日志即可
                // 如果是NACK，说明消息发送失败，需要进行补偿处理，例如重试或者记录到数据库中
                // 注意：这里的onSuccess方法是在另一个线程中执行的，不要在这里执行耗时的业务逻辑
                @Override
                public void onSuccess(CorrelationData.Confirm result) {
                    // 2.2.Future接收到回执的处理逻辑，参数中的result就是回执内容
                    if (result.isAck()) { // result.isAck()，boolean类型，true代表ack回执，false 代表 nack回执
                        log.debug("发送消息成功，收到 ack!");
                    } else { // result.getReason()，String类型，返回nack时的异常描述
                        log.error("发送消息失败，收到 nack, reason : {}", result.getReason());
                    }
                }
            });

    // 3. 发送消息
    // 传入四个参数：交换机、路由键、消息体、CorrelationData
    rabbitTemplate.convertAndSend("hmall.direct", "q", "hello", cd);
}
```

也可以通过创建实现 `ApplicationContextAware` 接口的类来实现确认回调：

```java

@Configuration
@Slf4j
@RequiredArgsConstructor
public class MqConfig implements ApplicationContextAware {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        log.info("初始化 RabbitTemplate Callbacks");

        rabbitTemplate.setReturnsCallback(returnedMessage -> {
            log.error("触发了return callback");
            log.debug("exchange: {}", returnedMessage.getExchange());
            log.debug("routingKey: {}", returnedMessage.getRoutingKey());
            log.debug("message: {}", returnedMessage.getMessage());
            log.debug("replyCode: {}", returnedMessage.getReplyCode());
            log.debug("replyText: {}", returnedMessage.getReplyText());
        });

        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                log.info("消息成功发送到交换机, 消息ID: {}", correlationData != null ? correlationData.getId() : "null");
            } else {
                log.error("消息发送到交换机失败, 消息ID: {}, 失败原因: {}", correlationData != null ? correlationData.getId() : "null", cause);
            }
        });
    }
}
```

如果在发送消息时还定义了其他确认回调函数，那么两个回调函数都会被调用。

## 可靠性 - 消息队列

Spring AMQP创建的交换机、消息队列以及消息，**默认都是非持久化的**，也就是存储在内存中。只有当MQ内存满的时候，才会将部分**较早的**消息写入磁盘，称为**Page Out**，这个过程是阻塞式的，较为耗时，MQ在这个过程中无法处理其他请求，且当消费者故障或者处理过慢时，会导致严重的消息积压。

总结来说，RabbitMQ会通过以下几种方式来保证消息的可靠性：

- 持久化：首先**通过配置**可以使得交换机、队列和消息都变成持久化的，这样即使MQ宕机，重启后也能恢复数据
- LazyQueue：RabbitMQ在3.6版本中引入了Lazy Queue的概念，**将所有消息直接持久化**，只有在消费者消费消息时，才会将消息从磁盘中读取到内存中进行处理。3.12后是队列的默认行为
- 发送者回执：开启持久化和发送者确认机制时，只有**当消息被成功持久化后，才会返回ACK**给发送者

> 持久化和惰性队列的区别是：持久化是接收到消息后先存入内存，然后异步写入磁盘；惰性队列是接收到消息后直接存入磁盘，不经过内存。

### 消息持久化

消息持久化包括三个方面：交换机、队列和消息本身。

在控制台中，交换机和队列在创建时选择 `Durable` 选项，消息在发送时设置 `Delivery Mode` 为 `Persistent`。这样就能实现消息的持久化。

在未使用持久化时，当高并发时，内存空间被占满，RabbitMQ会采用被动持久化的策略，将部分较早的消息写入磁盘，此时会导致流量的突然降低，影响效率。主动设置持久化之后，消息直接写入磁盘，防止流量的突然降低，反而会提高效率。

### LazyQueue

实际上，使用持久化后，由于数据需要在磁盘保存副本，整体的并发效率会有所下降，尤其是在高并发的场景下，磁盘I/O会成为瓶颈。

> 为了提高高并发场景下的消息处理能力，RabbitMQ在3.6.0版本中引入了Lazy Queue（惰性队列）的概念，有如下特征：
> - 接收到消息后直接存入磁盘，不再存储到内存，无需Page Out，从而避免了阻塞
> - 消费者当要消费消息时才会从磁盘中读取，并加载到内存（可以提前缓存部分消息，最多2048条）
> - 声明为惰性队列后，被声明为非持久化的消息也会被直接写入磁盘，而不是存储在内存中

> 在3.12版本后，Lazy Queue成为了队列的默认行为，无法更改。

在控制台中，创建队列时，添加参数 `x-queue-mode`，值为 `lazy` 即可，或者直接点击下方的 `Lazy mode` 选项，可以自动添加参数。

使用代码声明时，需要使用 `QueueBuilder` 来创建队列：

```java

@Bean
public Queue lazyQueue() {
    return QueueBuilder
            .durable("lazy.queue")
            .lazy() // 设置为惰性队列，或者.withArgument("x-queue-mode", "lazy")
            .build();
}
```

使用 `@RabbitListener` 注解声明时，可以通过 `arguments` 属性来添加参数：

```java

@RabbitListener(queuesToDeclare = @Queue(
        name = "lazy.queue",
        durable = "true",
        arguments = @Argument(name = "x-queue-mode", value = "lazy")  // 设置为惰性队列
))
public void listenLazyQueue(String msg) {
    log.info("消费者接收到lazy.queue的消息: {}", msg);
}
```

## 可靠性 - 消费者

消费者可靠性主要包括三个方面：

1. 消费者确认机制：确保消息被成功接收
2. 消息重试与补偿：确保消息被成功处理
3. 幂等性设计：确保消息被重复处理时不会产生副作用

### 消费者确认机制

类似发送者确认机制的MQ给发送者回执，消费者确认机制是当消费者处理消息成功后，给MQ发送一个回执。

具体来说，交换机广播给消息队列消息后，消费者从队列中获取消息进行处理，此时队列先不移除消息，而是会将该消息标记为“未确认”（unacknowledged），消费者**处理消息产生结果后**，发送的回执有三种情况：

1. ACK：如果消费者处理成功，那么就**发送一个ACK**给队列，队列就会将该消息移除
2. NACK：如果消费者处理失败，那么就**发送一个NACK**给队列，队列就会将该消息重新发给消费者尝试再次处理
3. REJECT：如果消费者处理失败，并且不想再处理该消息，那么就**发送一个REJECT**给队列，队列就会将该消息**丢弃或者进入死信交换机**。这种情况一般是因为**消息本身有问题**，处理失败也没有意义

Spring AMQP已经实现了消息确认功能，并可以通过配置文件选择确认模式：

1. NONE：不处理，消息投递给消费者后，立即返回ACK，消息立即从队列中移除，非常不安全，不推荐使用
2. MANUAL：手动模式，手动在业务代码中调用API发送ACK或REJECT，有一定侵入性，但更灵活
3. AUTO：自动模式，Spring AMQP通过AOP对消息处理逻辑做了环绕增强
    - 业务正常执行，返回ACK
    - 业务出现异常，返回NACK
    - 消息处理或校验异常，返回REJECT（`MessageConversionException`、`AmqpRejectAndDontRequeueException` 等）

在配置文件中添加如下配置，开启消费者确认机制：

```yaml
spring:
  rabbitmq:
    listener:
      simple:
        acknowledge-mode: auto # 设置消费者确认模式，auto为自动模式，manual为手动模式，none为不处理，默认是auto
```

开启AUTO模式之后，RabbitMQ在消费者正式处理消息前，会**先将消息标记为“未确认”（Unacked）**，然后调用消费者的处理方法，根据处理结果来决定发送哪种回执，如前文所述。

### 消息重试与补偿

在默认情况下，开启AUTO模式之后，消费者处理消息失败时，RabbitMQ会自动返回NACK，并且**立即重新投递该消息**，这种重试机制是**无限次的**，直到消费者处理成功为止。

但是如果消费者长时间无法从故障中恢复，那么消费者一直返回NACK，MQ也会一直重新消息入队并重试，这会导致消息积压，影响其他消息的处理，甚至可能导致MQ内存被占满，最终宕机。

Spring AMQP提供了消费者失败重试机制，在消费者出现异常时，使用**本地重试**，可以通过配置文件来设置重试的参数：

```yaml
spring:
  rabbitmq:
    listener:
      simple:
        prefetch: 1 # 每次从队列中获取的消息数量，默认是1
        retry:
          enabled: true # 开启重试机制，默认false
#          initial-interval: 1000ms # 失败后的初始等待时间，降低长时间网络波动的影响，默认是1000ms
#          multiplier: 1 # 失败后下次的等待时长倍数，下次等待时长 = initial-interval * multiplier，默认是1
#          max-attempts: 3 # 最大重试次数，默认是3
#          stateless: true # 是否使用无状态重试，默认true，如果业务中包含事务，这里必须是false
```

在默认情况下（仅开启 `retry.enabled: true`），消费者消息重试机制在达到最大重试次数之后，抛出一个 `RejectAndDontRequeueRecoverer`，并进入 `ConditionalRejectingErrorHandler`，这样就会触发REJECT回执，消息会被丢弃或者进入死信交换机。默认策略下显然会降低消息的可靠性。

消息重试失败后，需要 `MessageRecoverer` 来处理失败的消息，Spring AMQP提供了三种实现：

1. `RejectAndDontRequeueRecoverer`：默认实现，发送REJECT回执，消息被丢弃，不推荐
2. `ImmediateRequeueMessageRecoverer`：发送NACK回执，消息重新入队等待下次消费，比起消费者确认NACK机制**频率更低，较安全**
3. `RepublishMessageRecoverer`：将失败的消息重新发布到指定交换机，适合做日志记录或警告通知

考虑第三种实现，首先定义接受失败消息的交换机、队列以及绑定关系。然后定义一个 `RepublishMessageRecoverer` 的Bean，指定失败消息的交换机和路由键：

```java

@Configuration
public class ErrorMessageConfiguration {
    @Bean
    public DirectExchange errorExchange() {
        return new DirectExchange("error.direct", true, false);
    }

    @Bean
    public Queue errorQueue() {
        return new Queue("error.queue", true, false, false);
    }

    @Bean
    public Binding errorQueueBinding(Queue errorQueue, DirectExchange errorExchange) {
        return BindingBuilder.bind(errorQueue).to(errorExchange).with("error");
    }

    @Bean
    public MessageRecoverer messageRecoverer(RabbitTemplate rabbitTemplate) {
        return new RepublishMessageRecoverer(rabbitTemplate, "error.direct", "error");
    }
}
```

实现之后，消息经过本地重试之后，就会自动发送至 `error.direct` 交换机，路由键为 `error`，然后进入 `error.queue` 队列中，信息中的属性会包含异常信息，以及全部的异常堆栈，方便后续排查问题。

```text
spring 消费者接收到消息：【hello, spring amqp!】
spring 消费者接收到消息：【hello, spring amqp!】
spring 消费者接收到消息：【hello, spring amqp!】
09-24 15:11:20:653  WARN 97712 --- [ntContainer#0-1] o.s.a.r.retry.RepublishMessageRecoverer : Republishing failed message to exchange 'error.direct' with routing key error
```

### 幂等性设计

在分布式系统中，消息的重复投递是不可避免的，例如网络抖动、消费者宕机等，都会导致消息被重复消费，**多为消费者回执没有正常被MQ接收的情形**。如果消费者处理消息的逻辑不是幂等的，那么就会产生副作用，影响系统的一致性。业务的幂等性就是指同一操作执行多次，产生的结果与执行一次的结果相同。

一般来说，GET和DELETE请求是幂等的，而POST和PUT请求不是幂等的。对于非幂等的操作，可以通过以下几种方式来实现幂等性：

1. **唯一请求ID**：利用ID区分是否是重复消息
    - 每一条消息都生成一个唯一的ID（如UUID），与消息一起投递给消费者
    - 消费者接收到消息后处理业务，业务处理成功后将ID存入数据库
    - 下次接收到相同ID的消息时，去数据库查询是否已存在，存在则视为重复消息，放弃处理
    - **额外的存储空间、影响性能、业务复杂度增加、侵入性增加、判断逻辑与主要业务不相关**
2. **状态检查**：**结合业务本身逻辑**，利用业务状态区分是否是重复消息（推荐，但不一定对所有业务有效）
    - 例如余额支付业务中，最终MQ通知交易服务修改订单状态为已支付
    - 第一次消息推送，交易服务没有收到消息，打回队列重试
    - 此时用户通过交易服务取消订单，订单状态改为已取消
    - 这个时候网络恢复，MQ重试，交易服务接收到，那么此时就不应该修改状态订单为已支付
    - 通过业务逻辑，既然发现**订单状态不再是未支付，那么就不应该再处理该消息**
    - 这种情况下使用的是**业务本身的状态**来区分是否是重复消息，也**不用额外增加存储空间**

唯一请求ID法需要为消息转换器添加ID属性，Jackson2JsonMessageConverter默认是不创建消息ID的，因此需要手动设置：

```java

@Bean
public MessageConverter messageConverter() {
    Jackson2JsonMessageConverter jjmc = new Jackson2JsonMessageConverter();
    jjmc.setCreateMessageIds(true);
    return jjmc;
}
```

先关闭消费者，然后发送一条消息，在控制台中可见消息的属性中包含 `message_id`，这样消费者就可以根据这个ID进行进一步处理，此时需要使用 `Message` 对象来接收消息：

```java

@RabbitListener(queues = "simple.queue")
public void listenSimpleQueueMessage(Message msg) throws InterruptedException {
    System.out.println("msg的ID=" + msg.getMessageProperties().getMessageId());
    System.out.println("spring 消费者接收到消息：" + msg);
}
```

状态检查法需要修改 `trade-service` 模块中的 `PayStatusListener.listenPaySuccess` 方法：

```java
public void listenPaySuccess(Long orderId) {
    log.info("接收到支付成功的消息，订单ID：{}", orderId);
    orderService.listenPaySuccess(orderId);
}
```

然后将业务逻辑封装进 `OrderServiceImpl.listenPaySuccess` 方法中：

```java

@Override
public void listenPaySuccess(Long orderId) {
    // 1. 首先查询订单
    Order order = getById(orderId);

    // 2. 判断订单状态是否是未支付
    if (order == null || order.getStatus() != 1) {
        return;
    }

    // 3. 标记订单状态为已支付
    markOrderPaySuccess(orderId);
}
```

## 延迟消息 - 兜底方案

仍然考虑用户下单的业务场景，用户下单调用交易服务，交易服务首先保存订单，然后远程调用商品服务**锁定库存**，保证用户下单成功后，商品库存不会被其他用户抢占。

另一方面，就是前文分析的场景，用户在下单后，会根据是否成功支付通过MQ向交易服务发送一条消息，如果MQ消息没有成功发送，那么交易服务不会更新订单状态，根据成功支付与否分为两种情况：

- 如果成功支付，更新付款订单的状态为已支付，商品库存被正确扣除，只有订单状态不一致，只会影响用户体验，可以通过前文讨论的消息可靠性进行消息重试等处理
- 如果未支付，那么看似付款订单和交易订单的状态是同步的，但是商品服务的库存并没有恢复，实际上也是不一致的，这种情况会严重影响商品的销量准确性，需要特殊处理

其中一个解决方案是使用苍穹外卖中使用的**Spring Task**，创建一个定时任务，定期扫描未支付的订单，如果发现某个订单已经超过了支付时间，那么就将该订单取消，并且调用商品服务恢复库存。

另一个更优雅的解决方案是通过MQ的**延迟消息**功能：

- 当用户通过交易服务下单时，由交易服务向支付服务发送一条延迟消息，延迟时间为订单的支付超时时间（例如30分钟）
- 在已实现的功能中，正常情况下，交易服务和支付服务的数据同步是在短时间内达成的。以下基于短时间内两个服务出现不一致的情况，分析延迟消息作为兜底策略的行为：
    - 如果用户在30分钟内成功支付，那么延迟消息到达时，支付服务会向交易服务发送一条支付成功的消息，交易服务更新订单状态为已支付
    - 如果用户在30分钟内没有支付，那么延迟消息到达时，支付服务发现订单仍然是未支付状态，那么就发送消息，通知交易服务取消订单，并且调用商品服务恢复库存

### 基本概念

**延迟消息**是指，发送者发送消息时指定一个**延迟时间**，消费者不会立刻受到消息，而是**在指定的延迟时间后才会收到消息**并处理。这样即便网络出现故障，消息无法发送成功，那么在延迟时间内网络恢复后，消息仍然可以被发送成功，从而保障数据的一致性。

**延迟任务**是指，设置在延迟时间之后才会去执行的任务，可以通过延迟消息实现。

### 死信交换机

RabbitMQ本身是不支持延迟消息的，但是可以通过**死信交换机**（Dead Letter Exchange，DLX）来实现延迟消息的功能。

当一个队列中的消息满足以下情况之一时，就会称为死信：

- **重入队失败**：消费者拒绝或不重新入队
    - `basic.reject` （返回REJECT），或者
    - `basic.nack` （返回NACK）声明消费失败，并且 `requeue` 参数被设置为 `false`
- **消息过期**：消息是过期消息，超时无消费者进行消费
    - 达到了队列或消息本身设置的的TTL（队列也可以设置TTL）
- **队列已满**：队列达到最大长度，无法容纳更多消息
    - 要投递的队列消息堆积满了，最早的消息可能会被丢弃并成为死信

如果队列通过 `dead-letter-exchange` 参数绑定了一个死信交换机，那么当队列中的消息成为死信时，消息会被重新发布到该死信交换机中，然后根据路由键路由到绑定的队列中。

首先分别创建一组普通/死信的交换机/队列，分别为 `normal.direct`、`normal.queue` 和 `dlx.direct`、`dlx.queue`，其中需要做几件事：

- 为普通队列设置TTL，例如30秒
- 将发送者绑定到普通交换机
- 将消费者绑定到死信队列
- 将普通队列通过 `dead-letter-exchange = dlx.direct` 参数绑定到死信交换机：

这样就能实现延迟消息的功能：发送者发送消息到普通交换机，路由到普通队列。由于普通队列没有绑定消费者，那么消息会在队列中等待30秒。30秒后，消息过期，成为死信，普通队列将死信发送到死信交换机 -> 路由到死信队列 -> 推送给消费者。

```text
=== 步骤 1: 生产者发送一条带 TTL 的消息到普通交换机 ===

+-----------+   msg(TTL=30s, routing_key="order.delay") 
| Publisher | ------------------------------------------> [Exchange: normal.direct]
+-----------+
                                                                     |
                                                                     | Binding (key="order.delay")
                                                                     v

=== 步骤 2: 消息进入普通队列，该队列配置了死信交换机，消息在此等待过期 ===

                                             +----------------------------------------------+
                                             | Queue: normal.queue                          |
                                             | * Properties:                                |
                                             |   - x-dead-letter-exchange: dlx.direct       |
                                             |   - x-dead-letter-routing-key: order.process |
                                             +----------------------------------------------+
                                                                     |
                                                                     | (30秒后, 消息过期变成“死信”, 被自动转发到 DLX)
                                                                     v

=== 步骤 3: 死信交换机(DLX)接收到死信，并根据路由键将其路由到真正的消费队列 ===

                                                          [Exchange: dlx.direct]
                                                                     |
                                                                     | Binding (key="order.process")
                                                                     v

=== 步骤 4: 消费者从最终队列中获取到“延迟”后的消息进行处理 ===

                                                           [Queue: dlx.queue] ---------> +----------+
                                                                                         | Consumer |
                                                                                         +----------+
```

首先定义死信交换机、死信队列、接收者：

```java

@RabbitListener(bindings = @QueueBinding(
        value = @Queue(name = "dlx.queue", durable = "true"),
        exchange = @Exchange(name = "dlx.direct", durable = "true", type = "direct"),
        key = {"order.process"}
))
public void listenDlxQueue(String msg) {
    log.info("消费者接收到dlx.queue的消息：{}", msg);
}
```

然后定义普通交换机、普通队列，并将普通队列绑定到死信交换机：

```java

@Configuration
public class NormalMessageConfiguration {

    @Bean
    public DirectExchange normalExchange() {
        return ExchangeBuilder.directExchange("normal.direct").durable(true).build();
    }

    @Bean
    public Queue normalQueue() {
        return QueueBuilder
                .durable("normal.queue")  // 普通队列
                .deadLetterExchange("dlx.direct")  // 绑定死信交换机
                .deadLetterRoutingKey("order.process")  // 设置在死信交换机中的路由键
                .build();
    }

    @Bean
    public Binding normalBinding(Queue normalQueue, DirectExchange normalExchange) {
        return BindingBuilder.bind(normalQueue).to(normalExchange).with("order.delay");
    }
}
```

最后定义发送者，发送一条消息到普通交换机，同时指定一个过期时间：

```java
public void testDlx() {
    rabbitTemplate.convertAndSend("normal.direct", "order.delay", "hello", message -> {
                // 设置消息的过期时间为30秒
                message.getMessageProperties().setExpiration("30000");
                return message;
            }
    );
    log.info("消息发送完成");
}
```

### 延迟消息插件

使用死信交换机实现延迟消息，虽然逻辑清晰，但是实现起来过于繁琐。从RabbitMQ 3.7.x版本开始，官方提供了一个**延迟消息插件**（Github: [rabbitmq-delayed-message-exchange](https://github.com/rabbitmq/rabbitmq-delayed-message-exchange)），可以直接使用。

这个插件可以将普通交换机改造为支持延迟消息功能的交换机，当消息投递到交换机后可以暂存一段时间，到期后再投递到队列。当发送了一条设置TTL的消息后，交换机内部有一个计时器，等到TTL时间到达后，交换机再将消息路由到绑定的队列中。

在控制台使用时只需要在创建交换机时指定类型为 `x-delayed-message`，并且设置一个参数 `x-delayed-type`，值为普通的交换机类型（如 `direct`、`topic` 等）。

首先查看RabbitMQ插件存放的数据卷挂载地址：

```bash 
docker volume inspect mq-plugins
```

然后将下载的插件复制 `Mountpoint` 属性对应的目录下，最后执行以下命令启用插件，等待一段时间即可：

```bash
docker exec -it rabbitmq rabbitmq-plugins enable rabbitmq_delayed_message_exchange
```

然后定义一个延迟交换机和队列，并将队列绑定到交换机：

```java
// 通过注解的方式声明
@RabbitListener(bindings = @QueueBinding(
        value = @Queue(name = "delay.queue", durable = "true"),
        exchange = @Exchange(name = "delay.direct", delayed = "true"),
        key = "delay"
))
public void listenDelayMessage(String msg) {
    log.info("接收到delay.queue的延迟消息：{}", msg);
}

// 或者基于Bean的方式声明
@Bean
public DirectExchange delayExchange() {
    return ExchangeBuilder
            .directExchange("delay.direct")  // 指定交换机类型和名称
            .delayed()  // 设置delay的属性为true
            .durable(true)  // 持久化
            .build();
}
```

发送延迟消息时，需要在消息属性中设置 `x-delay` 参数，指定延迟的时间，单位为毫秒：

```java

@Test
public void testDelayExchange() {
    rabbitTemplate.convertAndSend("delay.direct", "delay", "hello, spring amqp delay message!", message -> {
                // 新版本中为setDelayLong
                message.getMessageProperties().setDelay(10000);
                return message;
            }
    );
}
```

## 业务改造 - 取消超时订单

考虑上一节中，用户下单的业务场景，用户下单调用交易服务，交易服务首先保存订单，然后远程调用商品服务锁定库存，保证用户下单成功后，商品库存不会被其他用户抢占。另一方面，用户在下单后，会根据是否成功支付通过MQ向交易服务发送一条消息，如果MQ消息没有成功发送，那么交易服务不会更新订单状态，根据成功支付与否分为两种情况：

- 如果成功支付，更新付款订单的状态为已支付，商品库存被正确扣除，只有订单状态不一致，只会影响用户体验，可以通过前文讨论的消息可靠性进行消息重试等处理
- 如果未支付，那么看似付款订单和交易订单的状态是同步的，但是商品服务的库存并没有恢复，实际上也是不一致的，这种情况会严重影响商品的销量准确性，需要特殊处理

本节通过MQ的延迟消息插件进行业务改造：

- 当用户通过交易服务下单时，由交易服务向支付服务发送一条延迟消息，延迟时间为订单的支付超时时间15分钟
- 如果短时间内，交易服务已经收到了支付服务发送的支付成功消息，那么交易服务更新订单状态为已支付，无需理会延迟消息
- 以下基于短时间内两个服务出现不一致的情况：
    - 如果用户在15分钟内成功支付，那么延迟消息到达时，支付服务会向交易服务发送一条支付成功的消息，交易服务更新订单状态为已支付
    - 如果用户在15分钟内没有支付，那么延迟消息到达时，支付服务发现订单仍然是未支付状态，那么就发送消息，通知交易服务关闭订单，并且调用商品服务恢复库存

### 定义交换机、队列和消费者

```java

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderDelayMessageListener {

    private final IOrderService orderService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MQConstants.DELAY_QUEUE_NAME, durable = "true"),
            exchange = @Exchange(value = MQConstants.DELAY_EXCHANGE_NAME, delayed = "true"),
            key = MQConstants.DELAY_ROUTING_KEY
    ))
    public void listenOrderDelayMessage(Long orderId) {
        orderService.listenOrderDelayMessage(orderId);
    }
}
```

### 实现业务方法

```java

@Override
public void listenOrderDelayMessage(Long orderId) {
    // 1. 查询订单
    Order order = getById(orderId);

    // 2. 检测订单状态，如果已支付，那么直接返回
    if (order == null || order.getStatus() != 1) {
        return;
    }

    // 3. 如果未支付，那么需要查询支付流水
    PayOrderDTO payOrder = payClient.queryPayOrderByBizOrderNo(orderId);

    // 4. 判断支付流水是否已支付
    if (payOrder == null) {
        return;
    }
    if (payOrder.getStatus() == 3) {
        // 4.1 如果已支付，那么更新订单状态为已支付
        markOrderPaySuccess(orderId);
    } else {
        // 4.2 如果未支付，那么更新订单状态为已取消，同时恢复商品库存
        cancelOrder(orderId);
    }
}
```

```java

@GlobalTransactional
public void cancelOrder(Long orderId) {
    // 1.更新订单状态为已取消
    Order order = new Order();
    order.setId(orderId);
    order.setStatus(5);
    order.setUpdateTime(LocalDateTime.now());
    order.setCloseTime(LocalDateTime.now());
    updateById(order);

    // 2.恢复商品库存
    List<OrderDetail> details = detailService.lambdaQuery().eq(OrderDetail::getOrderId, orderId).list();
    List<OrderDetailDTO> orderDetailDTOS = BeanUtils.copyList(details, OrderDetailDTO.class);
    itemClient.restoreStock(orderDetailDTOS);
}
```

其他业务逻辑主要涉及到Feign的远程调用，略。