项目升级

项目升级插件基于 Maven Release Plugin
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-release-plugin</artifactId>
    <configuration>
        <tagBase>http://192.168.4.1/kgcloud/kg-cloud-public/tags</tagBase>
        <tagNameFormat>v@{project.version}</tagNameFormat>
    </configuration>
</plugin>
```

预升级 运行
```bash
mvn -B release:prepare -DdryRun=true -DdevelopmentVersion=x.x.x-SNAPSHOT
```
会在项目目录下生成 真正要变更的文件

正式升级
```bash
mvn release:clean -B release:prepare -DdevelopmentVersion=x.x.x-SNAPSHOT release:perform
```

自动 更新版本号 并 打上 tag  推送到 git , 并将tag deploy到maven仓库

其中 x.x.x-SNAPSHOT 指 升级到的版本号

比如 3.0.0-SNAPSHOT 升级到 3.1.0-SNAPSHOT

会生成git 3.0.0 的tag 最终POM.xml版本为3.1.0-SNAPSHOT