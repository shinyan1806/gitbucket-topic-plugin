# gitbucket-topic-plugin
<div align="center">
  <a href="https://opensource.org/licenses/Apache-2.0">
    <img src="https://img.shields.io/badge/License-Apache%202.0-blue.svg?style=flat-square" alt="License: Apache 2.0" />
  </a>
</div>
リポジトリにトピックを付けるGitBucketプラグイン

## Overview（概要）
リポジトリにトピックを設定して分類・検索しやすくするGitBucketのプラグインです。現在開発中です。

## Development（開発）
本プラグインは [gitbucket/gitbucket-plugin-template](https://github.com/gitbucket/gitbucket-plugin-template) をベースに作成しています。

### Requirements（必要な環境）
- Java 17
- sbt

### Build（ビルド）
```bash
sbt assembly
```
生成された jar ファイルは `target/scala-2.13` に出力されます。

## License（ライセンス）
Apache License 2.0 のもとで公開されています。  
**[LICENSE](LICENSE)**
