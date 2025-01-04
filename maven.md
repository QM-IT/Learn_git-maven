# User Centre

## Install

参考：[maven download](https://maven.apache.org/download.cgi)，最好是3.8.x以上版本，JDK最好是17。



## Creating a Project

在一个空目录（目录名最好和`${artifactId}`相同）下执行：

```bash
mvn archetype:generate -DgroupId=com.mycompany.app -DartifactId=my-app -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.5 -DinteractiveMode=false
```

会生成一下文件树结构：

```tcl
my-app
|-- pom.xml
`-- src
    |-- main
    |   `-- java
    |       `-- com
    |           `-- mycompany
    |               `-- app
    |                   `-- App.java
    `-- test
        `-- java
            `-- com
                `-- mycompany
                    `-- app
                        `-- AppTest.java
```

src/main/java目录包含项目源代码，src/test/java目录包含测试源，pom. xml文件是项目的项目对象模型或POM，位于项目根目录下。



## POM文件

pom.xml文件位于项目根目录下。项目对象模型或POM是Maven的基本工作单元。它是一个XML文件，包含有关项目的信息和Maven用于构建项目的配置细节，它包含大多数项目的默认值。例如`the build directory`，即`target`；项目源码目录，即`src/main/java`；测试源码目录，即`src/test/java`；等等。当执行任务或目标时，Maven在当前目录中查找POM。它读取POM，获取所需的配置信息，然后执行目标。

POM中可以指定的一些配置是项目依赖项（dependency）、可以执行的插件（plugin）或目标（goal）、构建配置文件（build profile）等。其他信息，如`the project version, description, developers, mailing lists`等也可以指定

`maven工程 = pom.xml + pom-CommandLine`

> [Super POM](https://maven.apache.org/ref/3.9.9/maven-model-builder/super-pom.html)是Maven的默认POM。除非明确设置，否则所有POM都扩展了Super POM，这意味着Super POM中指定的配置由您为项目创建的POM继承。

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.mycompany.app</groupId>
  <artifactId>my-app</artifactId>
  <version>1.0-SNAPSHOT</version>
  <name>my-app</name>
  <!-- FIXME change it to the project's website -->
  <url>http://www.example.com</url>
  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <maven.compiler.release>17</maven.compiler.release>
  </properties>
  <dependencyManagement>
    <dependencies>
      <dependency>
        <groupId>org.junit</groupId>
        <artifactId>junit-bom</artifactId>
        <version>5.11.0</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>
    </dependencies>
  </dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter-api</artifactId>
      <scope>test</scope>
    </dependency>
    <!-- Optionally: parameterized tests support -->
    <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter-params</artifactId>
      <scope>test</scope>
    </dependency>
  </dependencies>
  <build>
      <!-- lock down plugins versions to avoid using Maven defaults (may be moved to parent pom) -->
    <pluginManagement>
       ... lots of helpful plugins
    </pluginManagement>
  </build>
</project>
```



### Minimal POM

在pom工程中最小配置包括以下几个元素：

- `project` root
- `modelVersion` - should be set to 4.0.0
- `groupId` - the id of the project's group.
- `artifactId` - the id of the artifact (project)
- `version` - the version of the artifact under the specified group

POM要求配置其groupId、artifactId和version。这三个值构成了项目的完全限定工件名称。这是<groupId>：<artifactId>：<version>的形式。至于上面的示例，它的完全限定工件名称是`com.mycompany.app:my-app:1.0-SNAPSHOT`。

此外，如第一部分所述，如果未指定配置细节，Maven将使用它们的默认值。这些默认值之一是`packaging type`。每个Maven项目都有一个打包类型。如果POM中未指定，则将使用默认值“jar”。此外，您可以看到在最小POM中没有指定存储库。如果您使用最小POM构建项目，它将继承超级POM中的存储库配置。因此，当Maven看到最小POM中的依赖项时，它会知道这些依赖项将从超级POM中指定的https://repo.maven.apache.org/maven2下载。



### Project Inheritance

POM继承中合并的元素（如果不显示指定就继承父工程）如下：

- dependencies
- developers and contributors
- plugin lists (including reports)
- plugin executions with matching ids
- plugin configuration
- resources

Super POM是项目继承的一个示例，但是您也可以通过在POM中指定父元素来引入您自己的父POM，示例参考[introduction-to-the-pom#{Example1、Example 2}](https://maven.apache.org/guides/introduction/introduction-to-the-pom.html)。



### Project Aggregation

项目聚合类似于项目继承。但是它没有指定模块中的父POM，而是指定父POM中的模块。通过这样做，父项目现在知道它的模块，如果对父项目调用Maven命令，则该Maven命令也将对父项目的模块执行。要执行项目聚合，您必须执行以下操作：

- Change the parent POMs packaging to the value "pom".
- Specify in the parent POM the directories path of its modules (children POMs).

Example：

```xml
<!-- com.mycompany.app:my-app:1's POM -->
<project>
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.mycompany.app</groupId>
  <artifactId>my-app</artifactId>
  <version>1</version>
</project>
--------------------------------------------------------------------------
<!-- com.mycompany.app:my-module:1's POM -->
<project>
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.mycompany.app</groupId>
  <artifactId>my-module</artifactId>
  <version>1</version>
</project>
--------------------------------------------------------------------------
<!--
directory structure:
.
 |-- my-module
 |   `-- pom.xml
 `-- pom.xml
-->
<!-- If we are to aggregate my-module into my-app, we would only have to modify my-app. -->
<project>
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.mycompany.app</groupId>
  <artifactId>my-app</artifactId>
  <version>1</version>
  <packaging>pom</packaging>
  <modules>
      <!-- The value of <module> is the relative path from the com.mycompany.app:my-app:1 to com.mycompany.app:my-module:1's POM (by practice, we use the module's artifactId as the module directory's name). -->
    <module>my-module</module>
  </modules>
</project>
```

现在，每当Maven命令处理com.mycompany.app： my-app：1时，同样的Maven命令也会针对com.mycompany.app：my-module：1运行。此外，一些命令（特别是goals）以不同的方式处理项目聚合。



### Project Inheritance vs Project Aggregation

如果您有几个Maven项目，并且它们都有相似的配置，您可以通过提取这些相似的配置并创建父项目来重构您的项目。因此，您所要做的就是让您的Maven项目继承该父项目，然后这些配置将应用于所有这些项目。

如果你有一组一起构建或处理的项目，您可以创建一个父项目，并让该父项目将这些项目声明为其模块。通过这样做，您只需要执行构建父项目，其余的就会随之而来。

但是当然，您可以同时拥有项目继承和项目聚合。这意味着，您可以让您的模块指定一个父项目，同时，让该父项目指定那些Maven项目作为其模块。您只需要应用所有三个规则：

- Specify in every child POM who their parent POM is.
- Change the parent POMs packaging to the value "pom" .
- Specify in the parent POM the directories of its modules (children POMs)

注意：配置文件继承与POM本身使用的继承策略相同。



### Project Interpolation and  Variables

Maven鼓励的做法之一是不要重复自己。但是，在某些情况下，您需要在几个不同的位置使用相同的值。为了帮助确保值只指定一次，Maven允许您在POM中使用自己的变量和预定义的变量。

例如，要访问`project. version`变量，您可以像这样引用它：

```xml
  <version>${project.version}</version>
```

需要注意的一个因素是，这些变量是在继承后处理的，如上所述。这意味着如果父项目使用预定义变量，那么如果又存在子项目中的定义，而不是父项目中的定义，将是子项目最终使用的定义。



## Running Apache Maven

可以通过`mvn -h`来查看命令语法，参考[maven command](https://maven.apache.org/run.html)



## Maven lifecycles 

生命周期由不同的构建阶段列表定义，构建阶段代表生命周期中的一个阶段。maven 内置的生命周期：

> The built-in lifecycles and their most used phases, in order, are:
>
> - clean - `clean`
> - default - `validate`, `compile`, `test`, `package`, `verify`, `install`, `deploy`
> - site - `site`, `site-deploy`

default介绍

- **validate**: validate the project is correct and all necessary information is available
- **compile**: compile the source code of the project
- **test**: test the compiled source code using a suitable unit testing framework. These tests should not require the code be packaged or deployed
- **package**: take the compiled code and package it in its distributable format, such as a JAR.
- **integration-test**: process and deploy the package if necessary into an environment where integration tests can be run
- **verify**: run any checks to verify the package is valid and meets quality criteria
- **install**: install the package into the local repository, for use as a dependency in other projects locally
- **deploy**: done in an integration or release environment, copies the final package to the remote repository for sharing with other developers and projects.

clean 和 site介绍

- **clean**: cleans up artifacts created by prior builds

- **site**: generates site documentation for this project

阶段（phase）实际上会映射到底层目标（goal）。每个阶段执行的具体目标取决于项目的打包类型。例如，如果项目类型是JAR，包执行jar： jar，如果项目类型是——你猜对了——WAR，包执行war：war。

阶段和目标可以按顺序执行：

```bash
mvn clean dependency:copy-dependencies package
```

此命令将清理项目、复制依赖项并打包项目（执行所有阶段直至打包——包含）。



### Buid Phase 和 Plugin Goal

构建阶段负责构建生命周期中的特定步骤，它执行这些职责的方式可能会有所不同。这是通过声明绑定到这些构建阶段的插件目标来完成的。插件目标(`Plugin Goal`)是构建和管理项目过程中的特定任务（比构建阶段更细化）。插件目标可以绑定到零个或多个构建阶段。不绑定到任何构建阶段的目标可以通过直接调用在构建生命周期之外执行。执行顺序取决于调用目标和构建阶段的顺序。例如，考虑下面的命令。`clean`和`package`是构建阶段，而`dependency:copy-dependencies`（插件的）目标。

```bash
mvn clean dependency:copy-dependencies package
```

如果要执行，将首先执行`clean phase`（这意味着它将运行清理生命周期的所有前置阶段，加上清理阶段本身），然后是`dependency:copy-dependencies goal`，最后执行`package phase`（及其默认生命周期的所有先前构建阶段）。此外，如果一个目标绑定到一个或多个构建阶段，则该目标将在所有这些阶段中调用。

此外，构建阶段也可以绑定零个或多个目标。如果构建阶段没有绑定目标，则该构建阶段将不会执行。但是如果它有一个或多个目标，它将执行所有这些目标。绑定到一个阶段的多个目标以在POM中声明的顺序执行。同一插件目标的多个实例被分组以一起执行。另外，插件可能包含多个插件目标。



### 配置 Build Lifecycle

配置生命周期、全部的生命周期的所有构建阶段、内置生命周期绑定：[introduction-to-the-lifecycle](https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html)



## Build Profiles

用于在不同的环境下改变maven对工程中的pom文件的解析结果，这里所指的环境因素可能是操作系统、jdk版本、系统变量等。对于其继承特性，只有被激活的profile的部分才会被继承。

### Details on profile activation

#### Explicit profile activation

可以使用-P命令行标志显式指定配置文件。

此标志后跟一个逗号分隔的配置文件ID列表，其中包含要使用的配置文件ID。除了任何由其`activation configuration`或`setting.xml`中的<activeProfiles>部分激活的配置文件，还会激活选项中指定的配置文件。从Maven 4开始，Maven将拒绝激活或停用无法解析的配置文件。为防止这种情况，请在配置文件标识符前加上`?`，将其标记为可选：

```bash
mvn groupId:artifactId:goal -P profile-1,profile-2,?profile-3
```

可以通过`<activeProfiles> section`在Maven setting.xml中激活配置文件。示例采用`<activeProfile>`元素的列表，每个元素都包含一个配置文件ID。

```xml
<settings>
  ...
  <activeProfiles>
    <activeProfile>profile-1</activeProfile>
  </activeProfiles>
  ...
</settings>
```

`<activeProfiles> tag`中列出的配置文件将在每次项目使用时默认激活。默认情况下，配置文件也可以在POM中使用如下配置来激活：

```xml
<profiles>
  <profile>
    <id>profile-1</id>
    <activation>
      <activeByDefault>true</activeByDefault>
    </activation>
    ...
  </profile>
</profiles>
```

除非使用前面描述的方法之一激活同一POM中的另一个配置文件，否则此配置文件将自动对所有构建生效。当POM中的配置文件在命令行上或通过其激活配置激活时，默认情况下处于活动状态的所有其他配置文件都会自动停用。

#### Implicit profile activation

可以根据检测到的构建环境状态自动触发配置文件。这些触发器是通过配置文件本身中的`<activation>`部分指定的。目前，这种检测仅限于`JDK版本匹配、操作系统匹配或系统属性的存在与否`。`Implicit profile activation`始终仅指容器配置文件（而不是具有相同id的其他模块中的配置文件）。下面是一些示例。

JDK

```xml
<!-- 
以下配置将在JDK的版本以1.4开头时触发配置文件（例如1.4.0_08、1.4.2_07、1.4），特别是对于较新的版本，如1.8或11，它不会激活： -->
<profiles>
  <profile>
    <activation>
      <jdk>1.4</jdk>
    </activation>
    ...
  </profile>
</profiles>
```

也可以使用[Ranges](https://maven.apache.org/enforcer/enforcer-rules/versionRanges.html)。范围值必须以[或（开头。否则，该值将被解释为前缀。以下配置接受版本1.3、1.4和1.5。

```xml
<profiles>
  <profile>
    <activation>
      <jdk>[1.3,1.6)</jdk>
    </activation>
    ...
  </profile>
</profiles>
```

注意：一个上限，如`1.5]`可能不包括1.5的大多数版本，如果它们将有一个额外的“补丁”版本，如_05，在上述范围内没有包含在内。



OS

这将根据检测到的操作系统不同而差异性激活。有关操作系统值的更多详细信息，请参阅 [Maven Enforcer Plugin](https://maven.apache.org/enforcer/enforcer-rules/requireOS.html) 插件。

```xml
<profiles>
  <profile>
    <activation>
      <os>
        <name>Windows XP</name>
        <family>Windows</family>
        <arch>x86</arch>
        <version>5.1.2600</version>
      </os>
    </activation>
    ...
  </profile>
</profiles>
```

这些值被解释为字符串，并与 [Java System properties](https://docs.oracle.com/javase/tutorial/essential/environment/sysprop.html)中的`os.name、os. arch、os.version`和派生自这些属性的系列相匹配。

每个值都可以以`!`为前缀以否定匹配。如果它们（不）等于实际的字符串值（不区分大小写），则这些值匹配。所有给定的操作系统条件都必须匹配才能考虑激活配置文件。在Maven3.9.7以后的maven中， version的值可能会以regex：为前缀。在这种情况下，正则表达式将应用于版本匹配并应用于小写的`os. version`值。执行`mvn --version`时会发出需要与给定值匹配的实际操作系统值。



Property

```xml
<!-- 当使用任何值指定系统属性“debug”时，以下配置文件将被激活： -->
<profiles>
  <profile>
    <activation>
      <property>
        <name>debug</name>
      </property>
    </activation>
    ...
  </profile>
</profiles>
---------------------------------------------------------
<!-- 当根本没有定义系统属性“debug”时，以下配置文件将被激活 -->
<profiles>
  <profile>
    <activation>
      <property>
        <name>!debug</name>
      </property>
    </activation>
    ...
  </profile>
</profiles>
---------------------------------------------------------
<!-- 当系统属性“debug”未定义或定义的值不是“true”时，将激活以下配置文件。 -->
<!--
要激活它，您可以在命令行上键入其中之一：
	mvn groupId:artifactId:goal
	mvn groupId:artifactId:goal -Ddebug=false
-->
<profiles>
  <profile>
    <activation>
      <property>
        <name>debug</name>
        <value>!true</value>
      </property>
    </activation>
    ...
  </profile>
</profiles>
---------------------------------------------------------
<!-- 下一个示例将在使用值“test”指定系统属性“environment”时触发配置文件： -->
<!--
要激活它，您可以在命令行上键入：
	mvn groupId:artifactId:goal -Denvironment=test
-->
<profiles>
  <profile>
    <activation>
      <property>
        <name>environment</name>
        <value>test</value>
      </property>
    </activation>
    ...
  </profile>
</profiles>
```

POM中的配置文件也可以根据`setting.xml`中`active profiles`的属性激活。

注意：像`FOO`这样的环境变量可用作`env.FOO`形式的属性。进一步注意，环境变量名称在Windows上被规范化为所有大写。

如果是Maven3.9.0+，还可以通过引用属性`packaging`来评估POM的打包类型。这仅在配置文件激活定义在公共父POM中时有用，该父POM在多个Maven项目之间重用。下一个示例将在构建打包类型为`war`的项目时触发配置文件：

```xml
<profiles>
  <profile>
    <activation>
      <property>
        <name>packaging</name>
        <value>war</value>
      </property>
    </activation>
    ...
  </profile>
</profiles>
```



Files

```xml
<!-- 此示例将在生成的文件 target/generated-sources/axistools/wsdl2java/org/apache/maven 丢失时触发配置文件。 -->
<profiles>
  <profile>
    <activation>
      <file>
        <missing>target/generated-sources/axistools/wsdl2java/org/apache/maven</missing>
      </file>
    </activation>
    ...
  </profile>
</profiles>
```

可以在标签`<exists>`和`<missing>`中放入变量引用。支持的变量引用可以是系统属性，如`${user. home}`和环境变量，如`${env.HOME}`。请注意，POM本身定义的属性和值在这里无法使用，例如上面的示例激活器不能使用`${project.build.directory}`，需要硬编码路径目标。

#### Multiple conditions

不同的隐式激活类型可以组合在一个profile中。只有满足所有条件时，profile才会激活（自Maven3.2.2， [MNG-4565](https://issues.apache.org/jira/browse/MNG-4565)起）。不支持在同一profile中多次使用相同类型（[MNG-5909](https://issues.apache.org/jira/browse/MNG-5909), [MNG-3328](https://issues.apache.org/jira/browse/MNG-3328)）。



### Deactivating a profile

可以使用命令行停用一个或多个配置文件，方法是在其标识符前面加上字符“！”或“-”，如下所示。

请注意！需要在Bash、ZSH和其他shell中使用\或引号进行转义，因为它具有特殊含义。还有一个已知的bug，命令行选项值以-（CLI-309）开头，因此建议将其与语法-P=-profile ilename一起使用。

```bash
mvn groupId:artifactId:goal -P \!profile-1,\!profile-2,\!?profile-3
// or
mvn groupId:artifactId:goal -P=-profile-1,-profile-2,-?profile-3
```

这可用于停用标记为activeByDefault的配置文件或通过其`activation config`激活的配置文件



### Customized profile areas of a POM

现在我们已经讨论了在哪里指定配置文件，以及如何激活它们。现在讨论您可以在配置文件中指定什么将是有用的。根据您选择配置配置文件的位置，您将可以访问不同的POM配置选项。

#### Profiles in external files

外部文件中指定的配置文件（即在`setting.xml`或`profiles.xml`中）在最严格的意义上是不可移植的。任何似乎很有可能改变构建结果的东西都仅限于POM中的内联配置文件。像repository 列表这样的东西可能只是获取artifact的专有存储库，不会改变构建的结果。对于构建好的pom.xml，您只能修改`<repositories>`和`<pluginRepositories>`部分，以及额外的`<properties>`部分。

`<properties>`部分允许您指定自由格式的键值对，这些键值对将包含在POM的插值过程中。这允许您以`${profile.provided.path}`的形式指定插件配置。



#### Profiles in POMs

另一方面，如果您的配置文件可以在POM中合理地指定，您就有更多的选择。当然，权衡是您只能修改该项目及其子模块。由于这些配置文件是内置指定在pom.xml中，因此更有可能保持可移植性，因此可以合理地说您可以向它们添加更多信息，而不会有其他用户无法获得该信息的风险。

POM中指定的配置文件可以修改以下[POM元素](https://maven.apache.org/ref/current/maven-model/maven.html)：

- `<repositories>`

- `<pluginRepositories>`

- `<dependencies>`

- `<plugins>`

- `<properties>` (not actually available in the main POM, but used behind the scenes)

- `<modules>`

- `<reports>`

- `<reporting>`

- `<dependencyManagement>`

- `<distributionManagement>`

  a subset of the `<build>` element, which consists of:

  - `<defaultGoal>`
  - `<resources>`
  - `<testResources>`
  - `<directory>`
  - `<finalName>`
  - `<filters>`
  - `<pluginManagement>`
  - `<plugins>`

### POM elements outside  profiles

`setting.xml`和`profile. xml`等外部文件也不支持POM配置文件之外的元素。

让我们详细说明这样一个场景，当有效的POM部署到远程存储库时，任何人都可以从存储库中获取它的信息，并使用它直接构建Maven项目。现在，想象一下，如果我们可以在依赖项中设置配置文件，这对构建非常重要，但是如果在`setting.xml`中、POM.XML文件之外的任何其他元素中设置配置属性，那么很可能我们不能期望其他人从存储库中使用该POM并能够直接构建它。我们还必须考虑如何与他人共享setings.xml。

请注意，要配置的文件太多会非常混乱并且很难维护。底线是，由于这是构建数据，因此它应该在POM中。



### Profile Order

来自激活的profile中的所有配置元素都会覆盖具有相同POM名称的全局元素，或者在集合的情况下扩展这些元素。如果同一POM或外部文件中有多个profile处于活动状态，则后定义的配置文件优先于前面定义的配置文件（与其配置文件ID和激活顺序无关）。

Example:

```xml
<project>
  ...
  <repositories>
    <!--  -->
    <repository>
      <id>global-repo</id>
      ...
    </repository>
  </repositories>
  ...
  <profiles>
    <profile>
      <id>profile-1</id>
      <activation>
        <activeByDefault>true</activeByDefault>
      </activation>
      <repositories>
        <!--  -->
        <repository>
          <id>profile-1-repo</id>
          ...
        </repository>
      </repositories>
    </profile>
    <profile>
      <id>profile-2</id>
      <activation>
        <activeByDefault>true</activeByDefault>
      </activation>
      <repositories>
          <!--  -->
        <repository>
          <id>profile-2-repo</id>
          ...
        </repository>
      </repositories>
    </profile>
    ...
  </profiles>
  ...
</project>
```

这将导致存储库优先级列表：profile e-2-repo（第一优先）， profile e-1-repo，global-repo。另外，setting.xml的profile优先级高于一个工程的pom.xml的profile。



### Profile Pitfalls

我们已经提到了这样一个事实，即在构建中添加profile说明有可能破坏项目的可移植性。我们甚至强调了profile元素可能破坏项目可移植性的情况。然而为了讨论更连贯，值得重申这些观点，涉及使用配置文件时要避免的一些陷阱。

在使用概要文件时，有两个主要问题需要记住。首先是`External Properties`，通常用于插件配置，这些会破坏项目的可移植性。另一个更微妙的领域是自然配置文件集的不完整规范(`Incomplete Specification of a Natural Profile Set`)。

#### External Properties

外部属性定义涉及在pom. xml之外定义但未在其内部的相应profile元素中定义的任何属性值。POM中属性最明显的用法是在插件配置中。虽然在没有`properties`元素集的情况下肯定会破坏项目可移植性，但这些灵活的信息可能会产生微妙影响，进而导致构建失败的。例如，在setting.xml中指定应该在配置文件中指定的`appserver path`属性可能会导致您的联调插件在团队中的其他用户尝试在没有类似设置的情况下构建时失败。考虑以下Web应用程序项目的pom.xml片段：

```xml
<project>
  ...
  <build>
    <plugins>
      <plugin>
        <groupId>org.myco.plugins</groupId>
        <artifactId>spiffy-integrationTest-plugin</artifactId>
        <version>1.0</version>
        <configuration>
          <appserverHome>${appserver.home}</appserverHome>
        </configuration>
      </plugin>
      ...
    </plugins>
  </build>
  ...
</project>
-------------------------------
<!-- Now, in your local ${user.home}/.m2/settings.xml, you have: -->
<settings>
  ...
  <profiles>
    <profile>
      <id>appserverConfig</id>
      <properties>
        <appserver.home>/path/to/appserver</appserver.home>
      </properties>
    </profile>
  </profiles>
  <activeProfiles>
    <activeProfile>appserverConfig</activeProfile>
  </activeProfiles>
  ...
</settings>
```



#### Incomplete Specification of a Natural Profile Set

除了上面的破坏可移植性样例之外，您的配置文件很容易无法涵盖所有情况。当您这样做时，您通常会让您的目标环境之一变得又高又干。让我们再举一次上面的示例pom. xml片段：

```xml
<project>
  ...
  <build>
    <plugins>
      <plugin>
        <groupId>org.myco.plugins</groupId>
        <artifactId>spiffy-integrationTest-plugin</artifactId>
        <version>1.0</version>
        <configuration>
          <appserverHome>${appserver.home}</appserverHome>
        </configuration>
      </plugin>
      ...
    </plugins>
  </build>
  ...
</project>
-----------------------------------------------------
<!-- 现在，考虑以下配置文件，它将在pom. xml中内联指定： -->
<project>
  ...
  <profiles>
    <profile>
      <id>appserverConfig-dev</id>
      <activation>
          <!-- 可以使用命令：mvn -Denv=dev-2 integration-test，进行构建 -->
        <property>
          <name>env</name>
          <value>dev</value>
        </property>
      </activation>
      <properties>
        <appserver.home>/path/to/dev/appserver</appserver.home>
      </properties>
    </profile>
    <profile>
      <id>appserverConfig-dev-2</id>
      <activation>
          <!-- 可以使用命令：mvn -Denv=dev integration-test，进行构建 -->
        <property>
          <name>env</name>
          <value>dev-2</value>
        </property>
      </activation>
      <properties>
        <appserver.home>/path/to/another/dev/appserver2</appserver.home>
      </properties>
    </profile>
  </profiles>
  ..
</project>
```

但是如果使用命令：`mvn -Denv=production integration-test`，进行构建会失败

因为，未替换为真实的`${appserver.home}`引用占位符将不是部署和测试您的Web应用程序的有效路径。在编写我们的配置文件时，我们还没有考虑正式生产环境的情况。“生产”环境（env=product）与“test”甚至可能是“local”一起构成了一组自然的目标环境，其实我们可能s是想为其构建集成-测试生命周期阶段。这个自然集的不完整规范意味着我们已经有效地将我们的有效目标环境限制在开发环境中。你的团队成员可能会很麻烦。当构建配置文件来处理此类情况时，请务必解决整个目标集合。

顺便说一句，用户特定的配置文件可以以类似的方式运行。这意味着当团队添加新的开发人员时，用于处理与用户相关的不同环境的配置文件可以运行。虽然我认为这对新手来说是有用的培训，但以这种方式把它们扔新团队成员。同样，一定要考虑完整的profiles。



### How to activate the specific profile

参考：[introduction-to-profiles#How can I tell which profiles are in effect during a build?](https://maven.apache.org/guides/introduction/introduction-to-profiles.html)



## Standard Directory Layout

拥有通用目录布局可以让熟悉一个Maven项目的用户立即熟悉另一个Maven项目结构。尽量符合这种结构。但是，如果不能，可以通过项目描述符覆盖这些设置。

| 目录                 | 说明                                                         |
| -------------------- | ------------------------------------------------------------ |
| `src/main/java`      | Application/Library sources                                  |
| `src/main/resources` | Application/Library resources                                |
| `src/main/filters`   | Resource filter files                                        |
| `src/main/webapp`    | Web application sources                                      |
| `src/test/java`      | Test sources                                                 |
| `src/test/resources` | Test resources                                               |
| `src/test/filters`   | Test resource filter files                                   |
| `src/it`             | Integration Tests (primarily for plugins)                    |
| `src/assembly`       | Assembly descriptors                                         |
| `src/site`           | Site                                                         |
| `LICENSE.txt`        | Project's license                                            |
| `NOTICE.txt`         | Notices and attributions required by libraries that the project depends on |
| `README.txt`         | Project's readme                                             |



## Dependency Mechanism

依赖管理是Maven的核心特性。管理单个项目的依赖很容易。也可以管理由数百个模块组成的多模块项目和应用程序的依赖。Maven在定义、创建和维护具有明确定义的类路径和库版本的可重现构建方面提供了很大帮助。

### Transitive Dependencies

Maven通过自动包含依赖传递特性来避免发现和指定您自己的依赖项所需的库。

通过从指定的远程存储库读取依赖项的项目文件来促进此功能。通常，这些项目的所有依赖项都在您的项目中使用，项目从其父级继承的任何依赖项也是如此，或者从它的依赖项，等等。

可以收集依赖项的级别数量没有限制。只有在发现循环依赖时才会出现问题。

使用依赖传递特性，包含库的视图可以很快变得非常大。出于这个原因，还有一些额外的功能可以限制包含哪些依赖项：

- *Dependency mediation*——这决定了当遇到多个版本作为依赖项时，将选择哪个版本的工件。Maven选择“最近的定义”。也就是说，它使用依赖树中最接近您的项目的依赖项的版本。您始终可以通过在项目的POM中显式声明来选择版本。请注意，如果两个依赖项版本在依赖树中处于相同的深度，则第一个声明优先。

- “最近的定义”意味着使用的版本将是依赖树中最接近您的项目的版本。考虑这棵依赖树：

  ```
    A
    ├── B
    │   └── C
    │       └── D 2.0
    └── E
        └── D 1.0
  ```

- 在文本中，A、B和C的依赖项定义为A->B->C->D 2.0和A->E->D 1.0，然后在构建A时将使用D 1.0，因为从A到D到E的路径更短。您可以在A中显式地向D 2.0添加依赖项以强制使用D 2.0，如下所示：

  ```
    A
    ├── B
    │   └── C
    │       └── D 2.0
    ├── E
    │   └── D 1.0
    │
    └── D 2.0      
  ```

- *Dependency management* -这允许项目作者直接指定在传递依赖项中或在未指定版本的依赖项中遇到工件时要使用的工件版本。在上一节的示例中，即使A没有直接使用依赖项，也直接将依赖项添加到A中。相反，A可以将D作为依赖项包含在其dependencyManagement部分中，并直接控制何时或是否引用它时使用哪个版本的D。
- *Dependency scope*-这允许您仅包含适合构建当前阶段的依赖项。这将在下面更详细地描述。
- *Excluded dependencies*-如果项目X依赖于项目Y，而项目Y依赖于项目Z，则项目X的所有者可以使用“排除”元素显式排除项目Z作为依赖项。
- *Optional dependencies*-如果项目Y依赖于项目Z，项目Y的所有者可以使用“可选”元素将项目Z标记为可选依赖项。当项目X依赖于项目Y时，X将仅依赖于Y，而不依赖于Y的可选依赖项Z。然后，项目X的所有者可以根据自己的选择显式添加对Z的依赖项。（可选依赖项可视为“默认排除”。）

尽管传递依赖可以隐含地包含所需的依赖，但明确指定源代码直接使用的依赖是一个很好的做法。这种最佳实践证明了它的价值，尤其是当项目的依赖改变它们的依赖时。例如，假设您的项目A指定了对另一个项目B的依赖，而项目B指定了对项目C的依赖。如果您直接使用项目C中的组件，并且您没有在项目A中指定项目C，则当项目B突然更新/删除其对项目C的依赖时，可能会导致构建失败。

直接指定依赖项的另一个原因是它为您的项目提供了更好的文档说明：只需阅读项目中的POM文件或执行`mvn dependency:tree`即可了解更多信息。Maven还提供了用于分析依赖关系的 [dependency:analyze](https://maven.apache.org/plugins/maven-dependency-plugin/analyze-mojo.html)插件目标：它有助于使这种最佳实践更容易实现。



### Dependency Scope

依赖项范围用于限制依赖项的可传递性并确定依赖项何时包含在类路径中。

有6个范围：

- **compile**
  This is the default scope, used if none is specified. Compile dependencies are available in all classpaths of a project. Furthermore, those dependencies are propagated to dependent projects.
- **provided**
  This is much like `compile`, but indicates you expect the JDK or a container to provide the dependency at runtime. For example, when building a web application for the Java Enterprise Edition, you would set the dependency on the Servlet API and related Java EE APIs to scope `provided` because the web container provides those classes. A dependency with this scope is added to the classpath used for compilation and test, but not the runtime classpath. It is not transitive.
- **runtime**
  This scope indicates that the dependency is not required for compilation, but is for execution. Maven includes a dependency with this scope in the runtime and test classpaths, but not the compile classpath.
- **test**
  This scope indicates that the dependency is not required for normal use of the application, and is only available for the test compilation and execution phases. This scope is not transitive. Typically this scope is used for test libraries such as JUnit and Mockito. It is also used for non-test libraries such as Apache Commons IO if those libraries are used in unit tests (src/test/java) but not in the model code (src/main/java).
- **system**
  This scope is similar to `provided` except that you have to provide the JAR which contains it explicitly. The artifact is always available and is not looked up in a repository.
- **import**
  This scope is only supported on a dependency of type `pom` in the `<dependencyManagement>` section. It indicates the dependency is to be replaced with the effective list of dependencies in the specified POM's `<dependencyManagement>` section. Since they are replaced, dependencies with a scope of `import` do not actually participate in limiting the transitivity of a dependency.

每个作用域（`import`除外）都以不同的方式影响传递依赖关系，如下表所示。如果将依赖关系设置为左列中的作用域，则该依赖关系的传递依赖关系与顶部行中的作用域会决定主项目中的依赖关系，其最终作用域列在交集中。如果未列出作用域，则意味着该依赖关系被省略。

|          | compile    | provided | runtime  | test |
| -------- | ---------- | -------- | -------- | ---- |
| compile  | compile(*) | -        | runtime  | -    |
| provided | provided   | -        | provided | -    |
| runtime  | runtime    | -        | runtime  | -    |
| test     | test       | -        | test     | -    |

**(\*) Note:** it is intended that this should be runtime scope instead, so that all compile dependencies must be explicitly listed. However, if a library you depend on extends a class from another library, both must be available at compile time. For this reason, compile time dependencies remain as compile scope even when they are transitive.



### Dependency Management

maven的核心功能，请通过案例说明来理解，参考：[introduction-to-dependency-mechanism.html](https://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html)。



#### Optional

不会出于（无论出于什么原因）将项目拆分为子模块的目的，而使用`Optional`。一般是，一些依赖项仅用于项目中的某些功能，如果不使用该功能，就不需要了。理想情况下，这样的功能将被拆分为依赖于核心功能项目的子模块。如果需要它们时，子项目将是必选依赖项，因为如要使用到子项目的功能。但是，由于项目不能被拆分（同样，无论出于什么原因），这些依赖项被声明为`Optional`。如果用户想要使用与可选依赖项相关的功能，他们必须在自己的项目中重新声明该可选依赖项。

可选依赖项可以节省空间和内存。它们防止违反许可协议的或导致类路径问题的jar被捆绑到`war、ear、fat jar`等类型的包中。

通过在其依赖声明中将`<optional>`元素设置为true，依赖项被声明为可选：

```xml
<project>
  ...
  <dependencies>
    <!-- declare the dependency to be set as optional -->
    <dependency>
      <groupId>sample.ProjectA</groupId>
      <artifactId>Project-A</artifactId>
      <version>1.0</version>
      <scope>compile</scope>
      <optional>true</optional> <!-- value will be true or false only -->
    </dependency>
  </dependencies>
</project>
```

**作用**

```
Project-A -> Project-B
-------------------------------
Project-X -> Project-A
```

上图表示Project-A依赖于Project-B。当A在其POM中将B声明为可选依赖项时，这种关系保持不变。这就像一个普通的构建，其中Project-B将被添加到Project-A的类路径中。

当另一个项目（Project-X）在其POM中声明Project-A为依赖项时，依赖项的`Optional`特性生效。Project-B不包含在Project-X的类路径中。您需要直接在Project X的POM中声明它，以便B包含在X的类路径中。



#### Exclusion

由于Maven以传递方式解析依赖项，因此不需要的依赖项可能会包含在项目的类路径中。例如，某个较旧的jar可能存在安全问题或与您正在使用的Java版本不兼容。为了解决这个问题，Maven允许您排除特定的依赖项。`Exclusion`设置在POM中的特定依赖项上，并指定特定的groupId和artifactId。当您构建项目时，该工件（通过声明`Exclusion`的依赖项）不会添加到项目的类路径中。

```xml
<project>
  ...
  <dependencies>
    <dependency>
      <groupId>sample.ProjectA</groupId>
      <artifactId>Project-A</artifactId>
      <version>1.0</version>
      <scope>compile</scope>
      <exclusions>
<!-- Add an <exclusions> element in the <dependency> element by which the problematic jar is included. -->
        <exclusion>  <!-- declare the exclusion here -->
          <groupId>sample.ProjectB</groupId>
          <artifactId>Project-B</artifactId>
        </exclusion>
      </exclusions> 
    </dependency>
  </dependencies>
</project>
```

**作用**

```
Project-A
   -> Project-B
        -> Project-D <! -- This dependency should be excluded -->
              -> Project-E
              -> Project-F
   -> Project C
```

该图显示Project-A依赖于Project-B和C。Project-B依赖于Project-D。Project-D依赖于Project-E和F。默认情况下，Project A的类路径将包括：`B, C, D, E, F`

如果您不希望将项目D及其依赖项添加到项目A的类路径中，因为存储库中缺少一些项目D的依赖项，并且您不需要项目B中依赖于项目D的功能。并且D在B的POM中也没有声明为`Optional`

```xml

<project>
  <modelVersion>4.0.0</modelVersion>
  <groupId>sample.ProjectA</groupId>
  <artifactId>Project-A</artifactId>
  <version>1.0-SNAPSHOT</version>
  <packaging>jar</packaging>
  ...
  <dependencies>
    <dependency>
      <groupId>sample.ProjectB</groupId>
      <artifactId>Project-B</artifactId>
      <version>1.0-SNAPSHOT</version>
      <exclusions>
        <exclusion>
          <groupId>sample.ProjectD</groupId> <!-- Exclude Project-D from Project-B -->
          <artifactId>Project-D</artifactId>
        </exclusion>
      </exclusions>
    </dependency>
  </dependencies>
</project>
```

如果您将Project-A部署到存储库，并且Project-X声明了对Project-A的正常依赖项，Project-D仍会被排除在类路径之外。

```
Project-X 
		-> Project-A
		-> Project-Y
               -> Project-B
                    -> Project-D
                       ...
```

Project-Y也依赖于Project-B，它确实需要Project-D支持的功能。因此，它不会在其依赖列表中排除Project-D。它还可以提供一个额外的存储库，从中可以解析Project-E。在这种情况下，不会全局排除Project-D，因为它是Project-Y的合法依赖项。

作为另一种情况，假设您不想要的依赖项是Project-E而不是Project-D。如何排除它？请参阅下图：

```
Project-A
   -> Project-B
        -> Project-D 
              -> Project-E <!-- Exclude this dependency -->
              -> Project-F
   -> Project C
```

`Exclusions `适用于下方的整个依赖关系图。如果您想排除Project-E而不是Project-D，只需将排除更改为指向Project-E，但不会将排除向下移动到Project-D，因为您不能更改Project-D的POM。如果可以，您可以使用`Optional`而不是`Exclusions `，或将Project-D拆分为多个子项目，每个子项目只有正常依赖项。

```xml
<project>
  <modelVersion>4.0.0</modelVersion>
  <groupId>sample.ProjectA</groupId>
  <artifactId>Project-A</artifactId>
  <version>1.0-SNAPSHOT</version>
  <packaging>jar</packaging>
  ...
  <dependencies>
    <dependency>
      <groupId>sample.ProjectB</groupId>
      <artifactId>Project-B</artifactId>
      <version>1.0-SNAPSHOT</version>
      <exclusions>
        <exclusion>
          <groupId>sample.ProjectE</groupId> <!-- Exclude Project-E from Project-B -->
          <artifactId>Project-E</artifactId>
        </exclusion>
      </exclusions>
    </dependency>
  </dependencies>
</project>
```





## Settings Reference

### Quick Overview

settings.xml文件中的settings元素包含用于定义以各种方式配置Maven执行时用到的元素，和pom.xml作用类似，但是所配置的属性不可以和任何特定项目或发布给用户的产品相绑定。这些值包括本地存储库位置、备用远程存储库服务器和Authentication信息。

setting.xml可以存在于两个地方：

- The Maven install: `${maven.home}/conf/settings.xml`
- A user's install: `${user.home}/.m2/settings.xml`

前一个setting.xml也称为全局设置，后一个settings.xml称为用户设置。如果两个文件都存在，它们的内容将被合并，用户特定的settings.xml具有更高优先级。

tips：如果需要从头开始创建用户特定的设置，最简单的方法是将Maven安装中的全局设置复制到${user. home}/.m2目录。Maven的默认setting.xml是一个带有注释和示例的模板，因此您可以快速调整它以满足您的需求。

以下是设置下settings顶部元素的概述：

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 https://maven.apache.org/xsd/settings-1.0.0.xsd">
  <localRepository/>
  <interactiveMode/>
  <offline/>
  <pluginGroups/>
  <servers/>
  <mirrors/>
  <proxies/>
  <profiles/>
  <activeProfiles/>
</settings>
```

xml的内容可以使用以下表达式进行插值：

1. `${user.home}` and all other system properties
2. `${env.HOME}` etc. for environment variable

请注意，settings.xml中配置文件中定义的属性不能用于插值。



### Simple Values

有一半的顶级settings元素是简单值，表示一系列值，这些值描述了构建系统中绝大部分处于活动状态的元素。

```xml
settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 https://maven.apache.org/xsd/settings-1.0.0.xsd">
  <localRepository>${user.home}/.m2/repository</localRepository>
  <interactiveMode>true</interactiveMode>
  <offline>false</offline>
  ...
</settings>
```

- **localRepository**: This value is the path of this build system's local repository. The default value is `${user.home}/.m2/repository`. This element is especially useful for a main build server allowing all logged-in users to build from a common local repository.
- **interactiveMode**: `true` if Maven should attempt to interact with the user for input, `false` if not. Defaults to `true`.
- **offline**: `true` if this build system should operate in offline mode, defaults to `false`. This element is useful for build servers which cannot connect to a remote repository, either because of network setup or security reasons.



### Plugin Groups

此元素包含`pluginGroup`元素列表，每个元素都包含一个`groupId`。使用某个插件时，命令行中未提供groupId，会自动搜索该列表，并且。此列表自动包含`org. apache.maven.plugins`和`org.codehaus.mojo`。

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 https://maven.apache.org/xsd/settings-1.0.0.xsd">
  ...
  <pluginGroups>
    <pluginGroup>org.eclipse.jetty</pluginGroup>
  </pluginGroups>
  ...
</settings>
```

例如，给定上述设置，Maven命令行可以执行`org. eclipse.jetty：jetty-maven-plugin：run`并使用截断命令：

```bash
mvn jetty:run
```



### Servers

下载和部署的存储库由POM的[`repositories`](https://maven.apache.org/pom.html#Repositories) 和[`distributionManagement`](https://maven.apache.org/pom.html#Distribution_Management) 元素定义。但是，某些设置（如用户名和密码）不应该与pom. xml一起分发。这种类型的信息应该存在于构建服务器的设置.xml中。

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 https://maven.apache.org/xsd/settings-1.0.0.xsd">
  ...
  <servers>
    <server>
      <id>server001</id>
      <username>my_login</username>
      <password>my_password</password>
      <privateKey>${user.home}/.ssh/id_dsa</privateKey>
      <passphrase>some_passphrase</passphrase>
      <filePermissions>664</filePermissions>
      <directoryPermissions>775</directoryPermissions>
      <configuration></configuration>
    </server>
  </servers>
  ...
</settings>
```

- **id**: This is the ID of the server *(not of the user to login as)* that matches the `id` element of the repository/mirror that Maven tries to connect to.
- **username**, **password**: These elements appear as a pair denoting the login and password required to authenticate to this server.
- **privateKey**, **passphrase**: Like the previous two elements, this pair specifies a path to a private key (default is `${user.home}/.ssh/id_dsa`) and a `passphrase`, if required. The `passphrase` and `password` elements may be externalized in the future, but for now they must be set plain-text in the `settings.xml` file.
- **filePermissions**, **directoryPermissions**: When a repository file or directory is created on deployment, these are the permissions to use. The legal values of each is a three digit number corresponding to *nix file permissions, e.g. 664, or 775.

注意：如果您使用私钥登录服务器，请确保省略`<password>`元素。否则，该密钥将被忽略。

**Password Encryption**

A new feature - server password and passphrase encryption has been added to 2.1.0+. See details [on this page](https://maven.apache.org/guides/mini/guide-encryption.html)



### Mirrors

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 https://maven.apache.org/xsd/settings-1.0.0.xsd">
  ...
  <mirrors>
    <mirror>
      <id>planetmirror.com</id>
      <name>PlanetMirror Australia</name>
      <url>http://downloads.planetmirror.com/pub/maven2</url>
      <mirrorOf>central</mirrorOf>
    </mirror>
  </mirrors>
  ...
</settings>
```

- **id**, **name**: The unique identifier and user-friendly name of this mirror. The `id` is used to differentiate between `mirror` elements and to pick the corresponding credentials from the [``](https://maven.apache.org/settings.html#Servers) section when connecting to the mirror.
- **url**: The base URL of this mirror. The build system will use this URL to connect to a repository rather than the original repository URL.
- **mirrorOf**: The `id` of the repository that this is a mirror of. For example, to point to a mirror of the Maven `central` repository (`https://repo.maven.apache.org/maven2/`), set this element to `central`. More advanced mappings like `repo1,repo2` or `*,!inhouse` are also possible. This must not match the mirror `id`.

有关镜像的更深入介绍，请阅读 [Guide to Mirror Settings](https://maven.apache.org/guides/mini/guide-mirror-settings.html)。



### Proxies

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 https://maven.apache.org/xsd/settings-1.0.0.xsd">
  ...
  <proxies>
    <proxy>
      <id>myproxy</id>
      <active>true</active>
      <protocol>http</protocol>
      <host>proxy.somewhere.com</host>
      <port>8080</port>
      <username>proxyuser</username>
      <password>somepassword</password>
      <nonProxyHosts>*.google.com|ibiblio.org</nonProxyHosts>
    </proxy>
  </proxies>
  ...
</settings>
```

- **id**: The unique identifier for this proxy. This is used to differentiate between `proxy` elements.
- **active**: `true` if this proxy is active. This is useful for declaring a set of proxies, but only one may be active at a time.
- **protocol**, **host**, **port**: The `protocol://host:port` of the proxy, separated into discrete elements.
- **username**, **password**: These elements appear as a pair denoting the login and password required to authenticate to this proxy server.
- **nonProxyHosts**: This is a list of hosts which should not be proxied. The delimiter of the list is the expected type of the proxy server; the example above is pipe delimited - comma delimited is also common.



### Profiles

settings.xml中的profile元素是pom.xml profile元素的缩减版本。它由`activation`, `repositories`, `pluginRepositories` and `properties`元素组成，其profile元素只包括这四个元素，因为它们与整个构建系统（这是setings.xml文件的作用）有关，而不是与单个项目对象模型设置有关。

如果一个setting的profile从设置中处于活动状态，则其值将覆盖maven项目中的POM或profiles.xml文件中任何等效ID的profile。

#### Activation

Activation是profile的关键。与POM的profile一样，profile的强大功能来自于它仅在特定情况下修改某些值的能力；这些情况是通过Activation元素指定的。

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 https://maven.apache.org/xsd/settings-1.0.0.xsd">
  ...
  <profiles>
    <profile>
      <id>test</id>
      <activation>
        <activeByDefault>false</activeByDefault>
        <jdk>1.5</jdk>
        <os>
          <name>Windows XP</name>
          <family>Windows</family>
          <arch>x86</arch>
          <version>5.1.2600</version>
        </os>
        <property>
          <name>mavenVersion</name>
          <value>2.0.3</value>
        </property>
        <file>
          <exists>${basedir}/file2.properties</exists>
          <missing>${basedir}/file1.properties</missing>
        </file>
      </activation>
      ...
    </profile>
  </profiles>
  ...
</settings>
```

配置方法和`Build Profiles`一样



#### Properties

Maven属性是值占位符，就像Ant中的属性一样。它们的值可以使用符号`${X}`在POM中的任何位置访问，其中X是属性键。它们有五种不同的样式，都可以从settings.xml文件中访问：

1. `env.X`: Prefixing a variable with “env.” will return the shell's environment variable. For example, `${env.PATH}` contains the $path environment variable (`%PATH%` in Windows).
2. `project.x`: A dot (.) notated path in the POM will contain the corresponding element's value. For example: `<project><version>1.0</version></project>` is accessible via `${project.version}`.
3. `settings.x`: A dot (.) notated path in the `settings.xml` will contain the corresponding element's value. For example: `<settings><offline>false</offline></settings>` is accessible via `${settings.offline}`.
4. Java System Properties: All properties accessible via `java.lang.System.getProperties()` are available as POM properties, such as `${java.home}`.
5. `x`: Set within a <properties /> element or an external files, the value may be used as `${someVar}`.

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 https://maven.apache.org/xsd/settings-1.0.0.xsd">
  ...
  <profiles>
    <profile>
      ...
      <properties>
          <!-- The property ${user.install} is accessible from a POM if this profile is active. -->
        <user.install>${user.home}/our-project</user.install>
      </properties>
      ...
    </profile>
  </profiles>
  ...
</settings>
```



#### Repositories

存储库是项目的远程集合，Maven使用这些项目来填充构建系统的本地存储库。Maven将其称为插件和依赖项。不同的远程存储库可能包含不同的项目，并且可以在活动配置文件下搜索它们以查找匹配的版本或快照工件。

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 https://maven.apache.org/xsd/settings-1.0.0.xsd">
  ...
  <profiles>
    <profile>
      ...
      <repositories>
        <repository>
          <id>codehausSnapshots</id>
          <name>Codehaus Snapshots</name>
          <releases>
            <enabled>false</enabled>
            <updatePolicy>always</updatePolicy>
            <checksumPolicy>warn</checksumPolicy>
          </releases>
          <snapshots>
            <enabled>true</enabled>
            <updatePolicy>never</updatePolicy>
            <checksumPolicy>fail</checksumPolicy>
          </snapshots>
          <url>http://snapshots.maven.codehaus.org/maven2</url>
          <layout>default</layout>
        </repository>
      </repositories>
      <pluginRepositories>
        <pluginRepository>
          <id>myPluginRepo</id>
          <name>My Plugins repo</name>
          <releases>
            <enabled>true</enabled>
          </releases>
          <snapshots>
            <enabled>false</enabled>
          </snapshots>
          <url>https://maven-central-eu....com/maven2/</url>
        </pluginRepository>
      </pluginRepositories>
      ...
    </profile>
  </profiles>
  ...
</settings>
```

- **releases**, **snapshots**: These are the policies for each type of artifact, Release or snapshot. With these two sets, a POM has the power to alter the policies for each type independent of the other within a single repository. For example, one may decide to enable only snapshot downloads, possibly for development purposes.
- **enabled**: `true` or `false` for whether this repository is enabled for the respective type (`releases` or `snapshots`).
- **updatePolicy**: This element specifies how often updates should attempt to occur. Maven will compare the local POM's timestamp (stored in a repository's maven-metadata file) to the remote. The choices are: `always`, `daily` (default), `interval:X` (where X is an integer in minutes) or `never`.
- **checksumPolicy**: When Maven deploys files to the repository, it also deploys corresponding checksum files. Your options are to `ignore`, `fail`, or `warn` on missing or incorrect checksums.
- **layout**: In the above description of repositories, it was mentioned that they all follow a common layout. This is mostly correct. Maven 2 has a default layout for its repositories; however, Maven 1.x had a different layout. Use this element to specify which if it is `default` or `legacy`.



#### Plugin Repositories

存储库是两种主要类型工程的所在地。第一种是用作其他工程依赖项的工程。这些是驻留在中心的大多数工程。另一种类型的工程是插件。Maven插件本身就是一种特殊类型的工程。正因为如此，插件存储库可能与其他存储库分离（尽管，我还没有听到令人信服的论据来这样做）。无论如何，pluginRepositories元素块的结构类似于repositories元素。pluginRepository元素每个都指定了Maven可以找到新插件的远程位置。



### Active Profiles

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 https://maven.apache.org/xsd/settings-1.0.0.xsd">
  ...
  <activeProfiles>
    <activeProfile>env-test</activeProfile>
  </activeProfiles>
</settings>
```

setting.xml的最后一部分是activeProfiles元素。这包含一组activeProfile元素，每个元素都有一个配置文件ID的值。任何定义为activeProfile的配置文件ID都将处于活动状态，无论任何环境设置如何。如果没有找到匹配的配置文件，什么也不会发生。例如，如果env-test是activeProfile，则pom.xml（或带有相应ID的profile.xml）中的配置文件将处于活动状态。如果没有找到这样的配置文件，则执行将正常继续。



## Use Config file And Env variable

主要涉及以下几个成员：

- `MAVEN_OPTS`
- `MAVEN_ARGS`
- `.mvn/extensions.xml`
- `.mvn/maven.config`
- `.mvn/jvm.config`

使用说明参考：[configure.html](https://maven.apache.org/configure.html)



## Introduction to Repositories

### Artifact Repositories

Maven中的存储库包含不同类型的构建工件和依赖项。

有两种类型的存储库：本地(local)和远程(remote):

1. 本地存储库是运行Maven的计算机上的一个目录。它缓存远程下载的Artifact和您尚未发布的临时构建工件
2. 远程存储库是指通过各种协议（如file：//和https：//）访问的任何其他类型的存储库。这些存储库可能是由第三方设置的真正的远程存储库，用于提供其工件以供下载（例如，repo.maven.apache.org）。其他“远程”存储库可能是在公司内的文件或HTTP服务器上设置的内部存储库，用于在开发团队之间共享私有工件并用于发布。

本地和远程存储库的结构相同，因此脚本可以在任何一侧运行，也可以同步以供离线使用。但是，存储库的布局设计对Maven用户完全透明。



### Using Repositories

一般来说，您不需要定期使用本地存储库做任何事情，除非在磁盘空间不足时将其清除（或者如果您愿意再次下载所有内容，则将其完全擦除）。

对于远程存储库，它们用于下载和上传（如果您有权这样做）。

#### Downloading from a Remote Repository

Maven中的下载是由一个项目声明本地存储库中不存在的依赖项触发的（或者对于SNAPSHOT，当远程存储库包含更新的依赖项时）。默认情况下，Maven将从中央存储库下载。

要覆盖它，您需要指定一个镜像，如[Using Mirrors for Repositories](https://maven.apache.org/guides/mini/guide-mirror-settings.html)中所示。您可以在`settings.xml`文件中将其设置为全局使用某个镜像。但是，项目通常在其pom.xml中自定义存储库([customise the repository in its](https://maven.apache.org/guides/mini/guide-multiple-repositories.html))，并且您的设置将优先。如果没有找到依赖项，请检查您是否没有覆盖远程存储库。

#### Using Mirrors for the Central Repository

在全球分布有几个的官方Central存储库。您可以更改`settings.xml`文件以使用一个或多个镜像。可以在[Using Mirrors for Repositories](https://maven.apache.org/guides/mini/guide-mirror-settings.html).指南中找到相关说明。



### Building Offline

如果您暂时与互联网断开连接并且需要离线构建项目，您可以使用命令行上的离线开关选型：

```bash
 mvn -o package
```

许多插件支持离线设置，并且不执行任何连接到Internet的操作。

### Uploading to a Remote Repository

虽然这对于任何类型的远程存储库都是可能的，但您必须有权这样做。要上传到Central Maven存储库，请参阅[Repository Center](https://maven.apache.org/repository/index.html)。

### Internal Repositories

​	当使用Maven时，尤其是在公司环境中，出于安全、速度或带宽原因，连接到Internet下载依赖项是不可接受的。因此，希望建立一个内部存储库来存放工件的副本，并将私有工件发布到里面。这样的内部存储库可以使用HTTP或文件系统（带有file：//URL）下载，并使用SCP、FTP或文件复制上传。

​	就Maven而言，这个存储库没有什么特别之处：它是另一个远程存储库，其中包含要下载到用户本地缓存的artifact，并且是工件发布(artifact releases)的发布目的地。

此外，您可能希望与生成的项目站点共享存储库服务器。有关创建和部署站点的更多信息，请参阅 [Creating a Site](https://maven.apache.org/guides/mini/guide-site.html)。

### Setting up the Internal Repository

​	要设置内部存储库，只需要您有一个放置它的地方，然后使用与远程存储库（如repo.maven.apache.org）相同的设计将所需的工件复制到那里。不建议您抓取或同步`Central`的完整副本，因为那里有大量数据，这样做会使您操作被阻塞。您可以使用[Repository Management](https://maven.apache.org/repository-management.html) 页面上描述的程序来运行内部存储库的服务器，根据需要从Internet下载，然后将工件保存在内部存储库中，以便以后更快地下载。

​	其他的办法是手动下载和审查版本，然后将它们复制到内部存储库，或者让Maven为用户下载它们，然后手动将审查过的工件上传到用于发布的内部存储库。此步骤是利用许可证(`license `)禁止自动分发工件的唯一操作办法，例如Sun提供的几个J2EE JAR。有关更多信息，请参阅处理SUN JAR文档指南。



### Using the Internal Repository

使用内部存储库非常简单。只需进行更改以添加`repositories`元素：

```xml
<project>
  ...
  <repositories>
    <repository>
      <id>my-internal-site</id>
      <url>https://myserver/repo</url>
    </repository>
  </repositories>
  ...
</project>
```

如果您的内部存储库需要身份验证，则可以在您的 [settings](https://maven.apache.org/settings.html#Servers)文件中使用id元素来指定登录信息。

### Deploying to the Internal Repository

拥有一个或多个内部存储库的最重要原因之一是能够发布您自己的私有版本。

要发布到存储库，您需要通过SCP、SFTP、FTP、WebDAV或文件系统之一进行访问。连接是通过各种 [wagons](https://maven.apache.org/wagon/wagon-providers/index.html)(传输载体)完成的。一些`wagons `可能需要作为扩展添加到您的构建中。



## Respositories

### Install to Local

参考：[guide-3rd-party-jars-local](https://maven.apache.org/guides/mini/guide-3rd-party-jars-local.html)

### Deploy to Remote

参考：[guide-3rd-party-jars-remote](https://maven.apache.org/guides/mini/guide-3rd-party-jars-remote.html)

- Guide to deploying 3rd party JARs to remote repository
- Deploying a 3rd party JAR with a generic POM
- Deploying a 3rd party JAR with a customized POM
- Deploying Source Jars



### Setting up Multiple Repositories

您可以通过两种不同的方式指定多个存储库的使用。第一种方法是在POM中指定您要使用的存储库。profile元素内部和外部都支持这一点：

```xml
<project>
...
  <repositories>
    <repository>
      <id>my-repo1</id>
      <name>your custom repo</name>
      <url>http://jarsm2.dyndns.dk</url>
    </repository>
    <repository>
      <id>my-repo2</id>
      <name>your custom repo</name>
      <url>http://jarsm2.dyndns.dk</url>
    </repository>
  </repositories>
...
</project>
--------------
<profiles>
    <profile>
        <id>test</id>
        <repositories>
            ...
        </repositories>
    </profile>
</profiles>
```

注意：项目默认将获得 [Super POM](https://maven.apache.org/guides/introduction/introduction-to-the-pom.html#Super_POM)中定义的标准存储库集。

您可以指定多个存储库的另一种方法是在`${user.home}/.m2/settings.xml`或`${maven.home}/conf/settings.xml`文件中创建一个profile元素，如下所示：

```xml

<settings>
 ...
 <profiles>
   ...
   <profile>
     <id>myprofile</id>
     <repositories>
       <repository>
         <id>my-repo2</id>
         <name>your custom repo</name>
         <url>http://jarsm2.dyndns.dk</url>
       </repository>
     </repositories>
   </profile>
   ...
 </profiles>
 <activeProfiles>
   <activeProfile>myprofile</activeProfile>
 </activeProfiles>
 ...
</settings>
```

如果您在profile元素中指定存储库，您必须记得激活该特定配置文件！正如您在上面看到的，我们通过在`activeProfiles`元素中注册一个`profile ID`来执行此操作。

您还可以在命令上激活此配置文件，例如通过执行以下命令：

```bash
mvn -Pmyprofile ...
```

事实上，如果您希望同时激活多个配置文件，-P选项跟随要激活的`profile id`的CSV列表。



#### Repository Order

按以下顺序查询远程存储库URL以获取`artifact`，直到返回有效结果：

1. effective settings:
   1. Global `settings.xml`
   2. User `settings.xml`
2. local effective build POM:
   1. Local `pom.xml`
   2. Parent POMs, recursively
   3. Super POM
3. effective POMs from dependency path to the artifact.

对于这些位置中的每一个，首先按照[Introduction to build profiles](https://maven.apache.org/guides/introduction/introduction-to-profiles.html)中概述的顺序查询配置文件中的存储库。

在从存储库下载之前，会应用镜像配置([mirrors configuration](https://maven.apache.org/guides/mini/guide-mirror-settings.html))。

对于`Effective settings and local build POM`（所包含的profile配置数据会被考虑在内），使用`mvn help:effective-settings`和`mvn help:effective-pom -Dverbose`，可以很容易地查看它们的存储库优先顺序。



#### Repository IDs

每个存储库必须有唯一的ID。有效`settings.xml`文件或有效POM文件中的`repository ID`冲突会导致构建失败。但是，POM中的`repository`会被有效的`settings.xml`中具有相同ID的`repository`覆盖。



### Guide to Large Scale Centralized Deployments

参考：[guide-large-scale-centralized-deployments.html](https://maven.apache.org/guides/mini/guide-large-scale-centralized-deployments.html)



### Using Mirrors for Repositories

使用存储库，您可以指定要从哪些位置下载某些工件，例如依赖项和maven-plugins。存储库可以在项目中声明，这意味着如果您有自己的自定义存储库，共享项目的人可以轻松获得开箱即用的正确设置。但是，您可能希望为特定存储库使用替代镜像，而不更改项目文件。

使用镜像的一些原因是：

- There is a synchronized mirror on the internet that is geographically closer and faster
- You want to replace a particular repository with your own internal repository which you have greater control over
- You want to run a [repository manager](https://maven.apache.org/repository-management.html) to provide a local cache to a mirror and need to use its URL instead

要配置给定存储库的镜像，您可以在设置文件（${user. home}/.m2/settings.xml）中提供它，为新存储库提供自己的id和url，并指定`mirrorOf`元素，即要使用镜像的存储库的ID。例如，默认包含的主Maven Central存储库的ID是`central`，因此要使用不同的镜像实例，您可以配置以下内容：

```xml
<settings>
  ...
  <mirrors>
    <mirror>
      <id>other-mirror</id>
      <name>Other Mirror Repository</name>
      <url>https://other-mirror.repo.other-company.com/maven2</url>
      <mirrorOf>central</mirrorOf>
    </mirror>
  </mirrors>
  ...
</settings>
```

请注意，给定存储库最多只能有一个镜像。换句话说，不能将单个存储库映射到一组镜像，这些镜像都定义了相同的`<mirrorOf>`值。Maven不会聚合镜像，而是简单地选择第一个匹配项。如果您想提供多个存储库的组合视图，请改用 [repository manager](https://maven.apache.org/repository-management.html) 。



#### Using A Single Repository

您可以通过让Maven镜像映射所有存储库请求来强制Maven使用单个存储库。存储库必须包含所有所需的工件，或者能够将请求代理到其他存储库。当使用带有Maven存储库管理器的内部公司存储库来代理外部请求时，此设置非常有用。

为此，请将`mirrorOf`设置为`*`：

**Note:** This feature is only available in Maven 2.0.5+.

```xml
<settings>
  ...
  <mirrors>
    <mirror>
      <id>internal-repository</id>
      <name>Maven Repository Manager running on repo.mycompany.com</name>
      <url>http://repo.mycompany.com/proxy</url>
      <mirrorOf>*</mirrorOf>
    </mirror>
  </mirrors>
  ...
</settings>
```



#### Advanced Mirror Specification

一个镜像可以处理多个存储库。这通常与存储库管理器结合使用，可以轻松集中配置后面的存储库列表。

语法:

- `*` matches all repo ids.
- `external:*` matches all repositories except those using localhost or file based repositories. This is used when you want to exclude redirecting repositories that are defined for Integration Testing.
- since Maven 3.8.0, `external:http:*` matches all repositories using HTTP except those using localhost.
- multiple repositories may be specified using a comma as the delimiter
- an exclamation mark may be used in conjunction with one of the above wildcards to exclude a repository id

注意不要在逗号分隔的列表中包含标识符或通配符周围的额外空格。例如，将`<mirrorOf>`设置为`!repo1, *`不会镜像任何内容，而`！repo1,*`将镜像除repo1之外的所有内容。

通配符在以逗号分隔的存储库标识符列表中的位置并不重要，因为通配符会进一步处理，并且显式包含或排除停止处理，从而覆盖任何通配符匹配。

当您使用高级语法并配置多个镜像时，声明顺序很重要。当Maven查找某个存储库的镜像时，它首先检查其`<mirrorOf>`与存储库标识符完全匹配的镜像。如果没有找到直接匹配，Maven会根据上述规则（如果存在）选择第一个匹配的镜像声明。因此，您可以通过更改`settings.xml`中定义的顺序来影响匹配顺序

Examples:

- `*` = everything
- `external:*` = everything not on the localhost and not file based.
- `repo,repo1` = repo or repo1
- `*,!repo1` = everything except repo1

```xml
<settings>
  ...
  <mirrors>
    <mirror>
      <id>internal-repository</id>
      <name>Maven Repository Manager running on repo.mycompany.com</name>
      <url>http://repo.mycompany.com/proxy</url>
      <mirrorOf>external:*,!foo</mirrorOf>
    </mirror>
    <mirror>
      <id>foo-repository</id>
      <name>Foo</name>
      <url>http://repo.mycompany.com/foo</url>
      <mirrorOf>foo</mirrorOf>
    </mirror>
  </mirrors>
  ...
</settings>
```



## Best Practice

### Creating Assemblies

`maven-assembly-plugin`主要用于创建可运行的maven项目jar包，包含所有用到的依赖(`maven-jar-plugin`只会打包项目代码)。

#### Guide to creating assemblies

Maven中的**assembly**机制提供了一种简单方法，使用POM中的assembly描述符和依赖信息创建工程发布版本。为了使用assembly插件，您需要在POM中配置assembly插件，它可能如下所示：

```xml
<project>
  <parent>
    <artifactId>maven</artifactId>
    <groupId>org.apache.maven</groupId>
    <version>2.0-beta-3-SNAPSHOT</version>
  </parent>
  <modelVersion>4.0.0</modelVersion>
  <groupId>org.apache.maven</groupId>
  <artifactId>maven-embedder</artifactId>
  <name>Maven Embedder</name>
  <version>2.0-beta-3-SNAPSHOT</version>
  <build>
    <plugins>
      <plugin>
        <artifactId>maven-assembly-plugin</artifactId>
        <version>3.3.0</version>
        <configuration>
          <descriptors>
            <descriptor>src/assembly/dep.xml</descriptor>
          </descriptors>
        </configuration>
        <executions>
          <execution>
            <id>create-archive</id>
            <phase>package</phase>
            <goals>
              <goal>single</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>
  ...
</project>
```

您会注意到assembly描述符位于`${project.basedir}/src/Assembly`中，这是assembly描述符的标准位置。

#### Creating a binary assembly

这是assembly插件的最典型用法，您可以在其中创建一般用途的发行版。

```xml
<assembly xmlns="http://maven.apache.org/plugins/maven-assembly-plugin/assembly/1.1.2" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/plugins/maven-assembly-plugin/assembly/1.1.2 http://maven.apache.org/xsd/assembly-1.1.2.xsd">
  <id>bin</id>
  <formats>
    <format>tar.gz</format>
    <format>tar.bz2</format>
    <format>zip</format>
  </formats>
  <fileSets>
    <fileSet>
      <directory>${project.basedir}</directory>
      <outputDirectory>/</outputDirectory>
      <includes>
        <include>README*</include>
        <include>LICENSE*</include>
        <include>NOTICE*</include>
      </includes>
    </fileSet>
    <fileSet>
      <directory>${project.build.directory}</directory>
      <outputDirectory>/</outputDirectory>
      <includes>
        <include>*.jar</include>
      </includes>
    </fileSet>
    <fileSet>
      <directory>${project.build.directory}/site</directory>
      <outputDirectory>docs</outputDirectory>
    </fileSet>
  </fileSets>
</assembly>
```

您可以使用前面提到的手动定义的assembly 描述符，但在这种情况下使用[预定义的assembly描述符][https://maven.apache.org/plugins/maven-assembly-plugin/descriptor-refs.html#bin]更简单。如何使用这种预定义的assembly描述符在[documentation of maven-assembly-plugin](https://maven.apache.org/plugins/maven-assembly-plugin/usage.html#Configuration)中有说明。

