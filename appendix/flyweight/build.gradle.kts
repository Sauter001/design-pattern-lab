// flyweight: 부록. 독립 모듈, 모듈 의존 없음.
// 데모를 ./gradlew :appendix:flyweight:run 으로 돌릴 수 있게 application 플러그인만 얹는다.
plugins {
    application
}

application {
    mainClass.set("lab.appendix.flyweight.FlyweightDemo")
}
