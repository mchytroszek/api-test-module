# How to setup your project to deploy package with maven and github actions
<!-- TOC -->
* [How to setup your project to deploy package with maven and github actions](#how-to-setup-your-project-to-deploy-package-with-maven-and-github-actions)
    * [1. Setup your pom file](#1-setup-your-pom-file)
    * [2. Setup setting.xml file](#2-setup-settingxml-file)
    * [3. Create new workflow](#3-create-new-workflow)
    * [4. Setup permissions for a workflow](#4-setup-permissions-for-a-workflow)
<!-- TOC -->

### 1. Setup your pom file

In pom.xml define `distributionManagement` section

```xml
<distributionManagement>
    <repository>
        <id>github</id>
        <name>OWNER</name>
        <url>https://maven.pkg.github.com/OWNER/REPOSITORY</url>
    </repository>
</distributionManagement>
```
Replace `OWNER` and `REPOSITORY` with your user and repository names.

### 2. Setup setting.xml file

Define your github server:
```xml
<servers>
    <server>
        <id>github</id>
        <username>${env.GITHUB_USER}</username>
        <password>${env.GITHUB_TOKEN}</password>
    </server>
</servers>
```
You don't have to add any environment variables, user and token are available by default.


Define profile:
```xml
<profiles>
    <profile>
        <id>github</id>
        <repositories>
            <repository>
                <id>github</id>
                <url>https://maven.pkg.github.com/OWNER/REPOSITORY</url>
                <snapshots>
                    <enabled>true</enabled>
                </snapshots>
            </repository>
        </repositories>
    </profile>
</profiles>
```
Use same `<url>` you used in POM file

### 3. Create new workflow

Example of workflow can be found in `.github/workflows/`

This workflow is going to:
1. Be triggered on push to `main` branch
2. Parse current version of your artifact
3. Generate new version of artifact
4. Commit and push new version of POM file
5. Build and publish new package

### 4. Setup permissions for a workflow
Make sure your workflow has read/write permissions in order to push version change in POM file.
It can be done in repository settings as described [here](https://docs.github.com/en/repositories/managing-your-repositorys-settings-and-features/enabling-features-for-your-repository/managing-github-actions-settings-for-a-repository#configuring-the-default-github_token-permissions)




