# React와 Spring boot를 한번에 빌드해서 실행하기 (Maven 프로젝트)
## 📁 실행하는 방법

### 1. MAVEN 다운로드하기
다운로드 링크: https://maven.apache.org/download.cgi
![](https://velog.velcdn.com/images/heoze/post/75381030-dd75-4808-9b79-5659031f8270/image.png)

다운로드한 파일을 적절한 폴더 압축을 풀고, 그 경로로 **환경 변수 설정**을 한다.

새 사용자 변수 추가: `MAVEN_HOME` > 압축을 푼 폴더
시스템 변수 편집: `Path` > 압축을 푼 폴더 + \bin

명령 프롬포트에서 `mvn -n`을 입력할 때 버전이 뜨는지 확인되면 잘 설치가 된 것이다.


### 2. 명령 프롬포트를 켜서 프로젝트 폴더로 이동한다.

### 3. 프로젝트를 build해서 `.jar` 파일을 생성한다.

```
mvn clean install
```

이후 target 폴더에 .jar 파일이 생성된 것을 확인할 수 있다.

### 4. `.jar` 파일 실행하기

```
java -jar target/[생성된 jar 파일]
```

### 5. http://localhost:8080/ 으로 접속해서 실행 결과를 확인해본다.
![](https://velog.velcdn.com/images/heoze/post/92ddaf4a-49cf-4b7a-9735-3fdff268c05c/image.png)

<br>

## 🔧 프로젝트 변형하기

### node & npm 버전 설정하기

node와 npm의 버전을 수정하려면 `pom.xml`에서 다음 부분을 수정하면 된다.

```xml
<properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
    <java.version>1.8</java.version>

    <!-- node / npm version -->
    <node.version>[원하는 node 버전을 이 곳에 작성]</node.version>
    <npm.version>[원하는 npm 버전을 이 곳에 작성]</npm.version>

    <!-- react working directory -->
    <react.workingDirectory>src/main/webapp</react.workingDirectory>
</properties>
```

### react 프로젝트 수정하기

src/main/webapp 폴더 안에 create-react-app 프로젝트가 들어있다.
이 안에 파일들을 수정하면 된다.

react 프로젝트의 경로를 바꾸려면 `pom.xml`에서 다음 부분을 수정해야 한다.

```xml
<properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
    <java.version>1.8</java.version>

    <!-- node / npm version -->
    <node.version>v14.20.0</node.version>
    <npm.version>6.14.7</npm.version>

    <!-- react working directory -->
    <react.workingDirectory>[react app의 폴더 경로를 이 곳에 작성]</react.workingDirectory>
</properties>
```

<br>

## 🤔 통합 빌드 & 실행 원리

플러그인 2가지를 활용한다.

### 1. [eirslett/frontend-maven-plugin](https://github.com/eirslett/frontend-maven-plugin)

frontend-maven-plugin은 빌드를 위해 node와 npm을 설치하지 않고도 빌드 과정에서 node와 package.json 안에 적힌 모듈들을 사용할 수 있게 해준다.

이 프로젝트에서는 frontend-maven-plugin을 사용해 React App의 빌드 파일을 생성한다!

`pom.xml`에서 frontend-maven-plugin을 사용하는 부분

```xml
<plugin>
    <groupId>com.github.eirslett</groupId>
    <artifactId>frontend-maven-plugin</artifactId>
    <version>1.6</version>
    <configuration>
        <workingDirectory>${react.workingDirectory}</workingDirectory>
        <installDirectory>target</installDirectory>
    </configuration>
    <executions>
        <execution>
            <id>install node and npm</id>
            <goals>
                <goal>install-node-and-npm</goal>
            </goals>
            <configuration>
                <nodeVersion>${node.version}</nodeVersion>
                <npmVersion>${npm.version}</npmVersion>
            </configuration>
        </execution>
        <execution>
            <id>npm install</id>
            <goals>
                <goal>npm</goal>
            </goals>
            <configuration>
                <arguments>install</arguments>
            </configuration>
        </execution>
        <execution>
            <id>npm run build</id>
            <goals>
                <goal>npm</goal>
            </goals>
            <configuration>
                <arguments>run build</arguments>
            </configuration>
        </execution>
    </executions>
</plugin>
```

### 2. [maven-antrun-plugin](https://maven.apache.org/plugins/maven-antrun-plugin/index.html)

maven-antrun-plugin은 Maven 내에서 Ant 작업을 실행할 수 있게 한다.

\* Ant: Java 기반의 build tool이다
xml 기반의 기능 설정 파일로 미리 정의된 작업(파일 복사, 소스코드 컴파일 등)을 쉽고 빠르게 실행할 수 있다.

<br>

이 프로젝트에서는 maven-antrun-plugin을 이용해 빌드된 React App을 spring 프로젝트 빌드 폴더에 복사한다.

`pom.xml`에서 maven-antrun-plugin을 사용하는 부분

```xml
<plugin>
    <artifactId>maven-antrun-plugin</artifactId>
    <executions>
        <execution>
            <phase>generate-resources</phase>
            <configuration>
                <target>
                    <copy todir="${project.build.directory}/classes/public">
                        <fileset dir="${project.basedir}/${react.workingDirectory}/build"/>
                    </copy>
                </target>
            </configuration>
            <goals>
                <goal>run</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

<br>

[참고] https://github.com/kantega/react-and-spring
이 곳에서는 start.spring.io에서 생성한 spring-boot 프로젝트에서 통합 빌드 환경을 세팅하는 방법이 처음부터 설명이 되어 있다.
