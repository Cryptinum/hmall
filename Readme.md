# 项目说明

本项目是一个Java微服务技术栈的学习项目，其中涉及到的业务技术栈包括Mybatis Plus、Spring Boot、Spring Cloud、OpenFeign、Sentinel、RabbitMQ、Elasticsearch、Redis等，辅助技术包括Linux、Maven、Nginx、Docker等。

项目文档：https://b11et3un53m.feishu.cn/wiki/FYNkwb1i6i0qwCk7lF2caEq5nRe

# Day 1 - Mybatis Plus

见本地项目 `mybatisplus` ，此处省略。

# Day 2 - 环境搭建

由于教程中提供的CentOS 7已经于2024年6月30日停止维护，因此使用CentOS Stream 9进行搭建。

搭建教程：https://b11et3un53m.feishu.cn/wiki/MWQIw4Zvhil0I5ktPHwcoqZdnec

# Day 3 - 微服务 上 - 服务治理与远程调用

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
MYSQL_SERVICE_HOST=192.168.168.168
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
      server-addr: 192.168.168.168:8848
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

