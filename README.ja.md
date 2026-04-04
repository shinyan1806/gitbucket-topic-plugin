# gitbucket-topic-plugin
<div align="center">
  <a href="https://opensource.org/licenses/Apache-2.0">
    <img src="https://img.shields.io/badge/License-Apache%202.0-blue.svg?style=flat-square" alt="License: Apache 2.0" />
  </a>
</div>
リポジトリにトピックを付けるGitBucketプラグイン

## Overview（概要）
リポジトリにトピックを設定して分類・検索しやすくするGitBucketのプラグインです。リポジトリトップページへのトピックプルダウンの追加、トピックが紐づくリポジトリの検索ができる一覧画面を提供します。

## ScreenShot（スクリーンショット）

![リポジトリトップページ](images/repository_top_page.png)
リポジトリトップページに、トピックの追加・編集ができるプルダウンが表示されます。

![トピックによるリポジトリ一覧画面](images/repositories_by_topic_page.png)
リポジトリを一覧表示し、トピックに紐づくリポジトリが検索できます。この画面からのトピックの追加・編集も可能です。

## Compatibility（互換性）
プラグインバージョン|GitBucketバージョン
:---|:---
1.0.x|4.46.x -

## Contributing（コントリビュート）
バグの報告、機能追加の提案は大歓迎です。

## Development（開発）
本プラグインは [gitbucket/gitbucket-plugin-template](https://github.com/gitbucket/gitbucket-plugin-template) をベースに作成しています。

### Requirements（必要な環境）
- Java 17
- sbt

### Development Commands（開発用コマンド）
```bash
# 静的解析・修正
sbt codeCheck
# ビルド
sbt assembly
```
生成された jar ファイルは `target/scala-2.13` に出力されます。

## License（ライセンス）
Apache License 2.0 のもとで公開されています。  
**[LICENSE](LICENSE)**
