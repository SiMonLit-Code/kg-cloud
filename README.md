项目自动化构建tag

在父项目中指定将要构建 tag 的版本号 next-tag.version  和 项目构建以后的开发版本号 next-develop.version

```bash
mvn  versions:set -DnewVersion=${next-tag.version} versions:set-scm-tag -DnewTag=v${next-tag.version} versions:commit

mvn scm:checkin -Dmessage="build(tag): release tag v${next-tag.version}" scm:tag -Dtag=v${next-tag.version}

mvn clean package deploy -Dmaven.test.skip=true

mvn  versions:set -DnewVersion=${next-develop.version} versions:set-scm-tag -DnewTag=HEAD versions:commit scm:checkin -Dmessage="build(pom): pom.xml version change to ${next-develop.version}"

```

