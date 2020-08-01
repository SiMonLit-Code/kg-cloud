项目自动化构建tag

在父项目中指定将要构建 tag 的版本号 next-tag.version  和 项目构建以后的开发版本号 next-develop.version

```bash
mvn -Dchangelist= clean deploy -Dmaven.test.skip=true scm:checkin -Dmessage="build(tag): release tag v${revision}${sha1}${changelist}" scm:tag -Dtag=v${revision}${sha1}${changelist}


// sdk 打包
mvn -Dchangelist= clean deploy -pl kgms-sdk,kgrepo-sdk,nlp-sdk -am

```

