<p align="center"><img width="200px" src="https://static.miraheze.org/projectsekaiwiki/c/c9/Jacket184.png"/></p>
<h1 align="center">Hello, World!</h1>
<br/>
<p align="center">Coding Test Web Service Based on Spring Boot and Docker</p>

### Compact
오직 코딩 테스트만을 위한 기능을 담았다. 누구나 쉽게 테스트를 열고 참여할 수 있다.
### Statistics
응시자 통계를 내기 위한 별도 작업없이 응시자의 평균과 점수분포를 시각적인 그래프로 표시한다.
### Individual
하나의 테스트에 대해 테스트 케이스의 값과 개수를 출제자가 직접 설정할 수 있다.
### Mark Down
마크 다운을 이용하여 코딩 문제의 가독성을 높인다. 출제자는 서식 변경을 간단하게 할 수 있다.

<br/>

# Support Compiler
- [X] C
- [X] Java
- [X] Python
      
<br/>

# Dependency
- Docker 4.16 ≤
- Spring Boot 2.6.3
- Windows PowerShell

<br/>

# Installation
Python compiler
```sh
docker pull python:3
```
Java compiler
```sh
docker pull openjdk:8
```
C compiler
```sh
docker pull gcc:4.9
```
Setting DB Wallet
```sh
C:/Oracle/Wallet_DBLab
```
Run Application
```sh
http://[your-localhost-url]:3000/
```
