# gitbucket-topic-plugin
<div align="center">
	<a href="https://opensource.org/licenses/Apache-2.0">
		<img src="https://img.shields.io/badge/License-Apache%202.0-blue.svg?style=flat-square" alt="License: Apache 2.0" />
	</a>
</div>
A GitBucket plugin for repository topics

[日本語版READMEはこちら](README.ja.md)

## Overview
This is a plugin for GitBucket that allows you to set topics for repositories, making classification and search easier. This plugin is currently under development.

## Development
This plugin is based on [gitbucket/gitbucket-plugin-template](https://github.com/gitbucket/gitbucket-plugin-template).

### Requirements
- Java 17
- sbt

### Build
```bash
sbt assembly
```
The generated jar file will be output to `target/scala-2.13`.

## License
Released under the Apache License 2.0.  
**[LICENSE](LICENSE)**
