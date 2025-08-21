# minisns-portfolio

Spring Boot + MySQL + Thymeleaf で作成したミニSNSポートフォリオです。

## 機能
- ユーザー登録 / ログイン / ログアウト
- プロフィール編集
- 投稿作成・編集・削除
- コメント機能
- 投稿検索
- ページネーション

## 使用技術
- Java (Spring Boot)
- MySQL
- Thymeleaf
- Gradle

## 実行方法
1. プロジェクトをクローンする  
   ```bash
   git clone https://github.com/sgwr1223/minisns-portfolio.git

2. DBを作成して application.properties に設定する

3. ./gradlew bootRun でアプリを起動

4. http://localhost:8080 にアクセス

## 画面/動作

/ … トップ（ナビゲーション）

/register … 新規登録

/login … ログイン

/mypage … マイページ（プロフィール表示・編集導線）

/posts … 投稿一覧（作成・編集・削除、コメント、検索、ページネーション）

検索: GET /posts?keyword=xxx

ページ: GET /posts?page=0（0始まり）

## プロジェクト構成
src/
 └ main/
    ├ java/com/example/demo/
    │   ├ controller/ (PostController, User系, CommentController)
    │   ├ entity/     (User, Post, Comment)
    │   ├ repository/ (UserRepository, PostRepository, CommentRepository)
    │   └ MinisnsApplication.java
    └ resources/
        ├ templates/ (index, login, register, posts, mypage, etc.)
        └ static/css/style.css

## セキュリティ/注意点
現状は学習用のため、パスワードは平文で保存しています。

本番では BCrypt などでハッシュ化が必須です。

CSRF対策、@Valid による入力チェック、エラーハンドリング（@ControllerAdvice）は省略しています。

application.properties の認証情報は公開リポジトリに含めない運用を推奨します。

## 作者
sgwr1223（GitHub）

学習・ポートフォリオ用プロジェクトです。改善提案やご指摘歓迎です！
