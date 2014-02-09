Reincarnation
==========
minecraft mods
--------
概要
--------
* 蘇生要素を追加するmodです。
使い方
* mobの蘇生
    * 名札でMobに名前をつけます。
    * 名前付きのmobが死亡すると魂が出現します
        * 魂状態はほぼ無敵です。が、素手で振れると砕けます。
    * 空瓶で右クリックするとアイテムになります。
    * 作業台で蘇生可能な状態にします
    * MobEggのように使うと蘇生できます
* playerの蘇生
    * 本modを導入すると通常の死亡時に、瀕死状態となります。
        * チャットウィンドウが開くので適宜助けを求めるか閉じましょう
        * 瀕死の状態でスニークすると、本来の死亡処理に移行します。
    * 破片をマルチで瀕死中のプレイヤーに使うと蘇生できます。
        * HPの出るボスなんかを倒したり、魂に素手で振れると破片が出ます。
        * 瓶詰めにした魂もクラフトで若干多めに手に入ります
        * プレイヤーが瀕死に陥った際にも魂がでますが、瓶には詰められず低確率で破片を落します。
    * 破片を瓶に積めると食べ物扱いとなります。
        * かなり強力な回復薬です。
        * 瀕死中は自分で使用することはできません
        * 何らかの方法で瀕死中に使用状態となると、蘇生できます。

本リポジトリの使い方
---------
0. 開発環境の作成
    * 用意できてるなら不要です。
    1. 初期設定
        * 必要なファイルのDL、デコンパイル等をします
        * 後述をコマンドプロンプト/batファイル等で実行しましょう
            * Eclipseなら
            > gradlew.bat setupDevWorkspace setupDecompWorkspace eclipse
            * Intellij IDEAなら
            > gradlew.bat setupDevWorkspace setupDecompWorkspace idea
    2. S/Cの設定
        1. server設定
            * サーバを起動してみます。
            * eclipse/server.properties が生成されます
            * online-mode=false と書き換えましょう  これでデバッグ環境でローカルサーバに入れます。
        2. client設定
            * clientを起動します。
            * サーバ接続設定を追加します
            > Multiplayer > Add server > Server Address > "localhost:25565"
                * サーバ名は任意で
1. eclipse編
    1. デバッグまで
        1. リポジトリーCloneを作る
            * ビューを開いて、URLをCtrl+V辺りでペースト等、任意のディレクトリにClone作成します
            * ※ビューの開き方例 ウィンドウ>ビュー>その他 "Gitリポジトリー"
        2. プロジェクトにリンクフォルダとして追加
            > 新規＞フォルダー＞拡張＞リンクされたフォルダー
            * 1で作成した作業フォルダを指定します。
        3. ソースフォルダ指定
            * src/main/以下にある java と resourceを選択しソースフォルダとして登録
            * 右クリック＞ビルド･パス＞ソースフォルダーとして使用
        4. ライブラリの準備
            * setup.batを実行します
            * 自動で基本的に必要なライブラリのDLや初期設定がされます
            * ※本modでは特に追加ライブラリは無いので実行だけすれば終わりです。
        5. デバッグ実行
            c/s起動できるか試します。
    2. ビルド
        * build.batを実行します
        * build/libs 配下にjarが生成されます。

2. inteliJ IDEA編
    1. デバッグまで
        1. リポジトリーCloneを作る
            * ※あらかじめgitのコマンドラインツールをインストール＆登録しておいてください。
                > setting > "git"で検索 > git > git.exeの欄に自環境のパスを
                * ※パスが通っている場合は不要
                * 適宜githubアカウント設定など
                    > VCS>Checkout from Version Control>git
            * プロジェクトとして開くか聞かれるかもしれませんが No で
        2. Moduleのインポート
            > File > Import Module
            * 1でCloneしたリポジトリーの作業ディレクトリから build.gradleを開きましょう
        3. 依存関係の設定
            * Gradleタスクから setupDevWorkSpaceを実行します。
            * 自動で基本的に必要なライブラリのDLや初期設定がされます
            * ※本modでは特に追加ライブラリは無いので実行だけすれば終わりです。
            > View > ToolWindow > Gradle > all tasks > リポジトリ名 > setupDevWorkSpace
            * デバッグの起動設定等をもつ、メインのForgeモジュールで読み込むようにします。
            > ForgeのModuleSettings > Dependenciesタブ > + > ModuleDependency > 2でインポートしたModule
        4. デバッグ実行
            * Runから適宜 server / client 実行します。
    5. ビルド
        * Gradleタスクから build を実行します。
        * build/libs 配下にjarが生成されます。