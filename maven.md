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

如果你有一组一起构建或处理的项目，您可以创建一个父项目，并让该父项目将这些项目声明为其模块。通过这样做，您只需要构建父项目，其余的就会随之而来。

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

不同的隐式激活类型可以组合在一个配置文件中。只有满足所有条件时，配置文件才会激活（自Maven3.2.2， [MNG-4565](https://issues.apache.org/jira/browse/MNG-4565)起）。不支持在同一配置文件中多次使用相同类型（[MNG-5909](https://issues.apache.org/jira/browse/MNG-5909), [MNG-3328](https://issues.apache.org/jira/browse/MNG-3328)）。



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

来自激活的profile中的所有配置元素都会覆盖具有相同POM名称的全局元素，或者在集合的情况下扩展这些元素。如果同一POM或外部文件中有多个配置文件处于活动状态，则后定义的配置文件优先于前面定义的配置文件（与其配置文件ID和激活顺序无关）。

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

这将导致存储库优先级列表：profile e-2-repo（第一优先）， profile e-1-repo，global-repo。



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
