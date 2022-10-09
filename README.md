# ImoyaAndroidPreferenceLib

[VoiceClock](https://imoya.net/android/voiceclock) より切り出した、設定画面の共通実装です。

* [android.preference](https://developer.android.com/reference/android/preference/Preference) の便利さをある程度継承しつつ、拡張性と柔軟性を持たせた、設定画面および設定項目ビュー
  * [android.preference](https://developer.android.com/reference/android/preference/Preference) は独自 XML で作成していた設定画面を、レイアウトリソース XML で作成する仕様で実装、通常のビューと設定ビューを混在させた画面の作成が可能
  * デフォルトで定義されている設定項目は、少ないコーディングで既存の AppCompatActivity, Fragment へ組み込み可能
  * 画面回転にも対応
  * 本ライブラリのユーザーが、デフォルトで定義されている設定項目のビューを独自に拡張することや、新しい設定項目のビューを作成することも可能

## Installation

### For GitHub users using Android Studio (using GitHub packages) (recommended)

* This solution is highly recommended as it allows you to view documents and use code completion.

1. Prepare a GitHub personal access token with `read:packages` permission.
   * If you do not have such a token, please create one by referring to the following page: [Creating a personal access token](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token)
2. Create file named `github.properties` in Your project root directory.
3. Set the following content in `github.properties`:

    ```text
    gpr.user=[Your GitHub user ID]
    gpr.token=[Your personal access token]
    ```

4. Add GitHub Packages repository to:
   * `settings.gradle` in Your project root directory:

       ```groovy
       dependencyResolutionManagement {
           // other settings

           repositories {
               // other dependency such as google(), mavenCentral(), etc.

               def githubProperties = new Properties()
               githubProperties.load(new FileInputStream(file("github.properties")))
               maven {
                   name = "GitHubPackages-ImoyaAndroidPreferenceLib"
                   url = uri("https://maven.pkg.github.com/IceImo-P/ImoyaAndroidPreferenceLib")
                   credentials {
                       username = githubProperties.getProperty("gpr.user") ?: System.getenv("GPR_USER")
                       password = githubProperties.getProperty("gpr.token") ?: System.getenv("GPR_TOKEN")
                   }
               }
           }
       }
       ```

   * or `build.gradle` in Your project root directory:

       ```groovy
       allprojects {
           repositories {
               // other dependency such as google(), mavenCentral(), etc.

               def githubProperties = new Properties()
               githubProperties.load(new FileInputStream(rootProject.file("github.properties")))
               maven {
                   name = "GitHubPackages-ImoyaAndroidPreferenceLib"
                   url = uri("https://maven.pkg.github.com/IceImo-P/ImoyaAndroidPreferenceLib")
                   credentials {
                       username = githubProperties.getProperty("gpr.user") ?: System.getenv("GPR_USER")
                       password = githubProperties.getProperty("gpr.token") ?: System.getenv("GPR_TOKEN")
                   }
               }
           }

           // other settings
       }
       ```

5. Add dependencies to your module's `build.gradle`:

    ```groovy
    dependencies {
        // (other dependencies)
        implementation 'net.imoya.android.dialog:imoya-android-dialog:2.1.1'
        implementation 'net.imoya.android.fragment:imoya-android-fragment:1.5.0'
        implementation 'net.imoya.android.log:imoya-android-log:1.2.0'
        implementation 'net.imoya.android.preference:imoya-android-preference:2.3.0'
        implementation 'net.imoya.android.util:imoya-android-util:1.9.0'
        // (other dependencies)
    }
    ```

6. Sync project with Gradle.

### For non-GitHub users, Android application with Android Studio (using aar)

1. Install [ImoyaAndroidLog](https://github.com/IceImo-P/ImoyaAndroidLog) with reading [this section](https://github.com/IceImo-P/ImoyaAndroidLog#for-non-github-users-android-application-with-android-studio-using-aar).
2. Install [ImoyaAndroidUtil](https://github.com/IceImo-P/ImoyaAndroidUtil) with reading [this section](https://github.com/IceImo-P/ImoyaAndroidUtil#for-non-github-users-android-application-with-android-studio-using-aar).
3. Install [ImoyaAndroidDialogLib](https://github.com/IceImo-P/ImoyaAndroidDialogLib) with reading [this section](https://github.com/IceImo-P/ImoyaAndroidDialogLib#for-non-github-users-android-application-with-android-studio-using-aar).
4. Install [ImoyaAndroidFragmentLib](https://github.com/IceImo-P/ImoyaAndroidFragmentLib) with reading [this section](https://github.com/IceImo-P/ImoyaAndroidFragmentLib#for-non-github-users-android-application-with-android-studio-using-aar).
5. Download `imoya-android-preference-release-[version].aar` from [Releases](https://github.com/IceImo-P/ImoyaAndroidPreferenceLib/releases) page.
6. Place `imoya-android-preference-release-[version].aar` in `libs` subdirectory of your app module.
7. Add dependencies to your app module's `build.gradle`:

    ```groovy
    dependencies {
        // (other dependencies)
        implementation files('libs/imoya-android-preference-release-[version].aar')
        // (other dependencies)
    }
    ```

8. Sync project with Gradle.

### For non-GitHub users, Android library with Android Studio (using aar)

1. Install [ImoyaAndroidLog](https://github.com/IceImo-P/ImoyaAndroidLog) with reading [this section](https://github.com/IceImo-P/ImoyaAndroidLog#for-non-github-users-android-library-with-android-studio-using-aar).
2. Install [ImoyaAndroidUtil](https://github.com/IceImo-P/ImoyaAndroidUtil) with reading [this section](https://github.com/IceImo-P/ImoyaAndroidUtil#for-non-github-users-android-library-with-android-studio-using-aar).
3. Install [ImoyaAndroidDialogLib](https://github.com/IceImo-P/ImoyaAndroidDialogLib) with reading [this section](https://github.com/IceImo-P/ImoyaAndroidDialogLib#for-non-github-users-android-library-with-android-studio-using-aar).
4. Install [ImoyaAndroidFragmentLib](https://github.com/IceImo-P/ImoyaAndroidFragmentLib) with reading [this section](https://github.com/IceImo-P/ImoyaAndroidFragmentLib#for-non-github-users-android-library-with-android-studio-using-aar).
5. Download `imoya-android-preference-release-[version].aar` from [Releases](https://github.com/IceImo-P/ImoyaAndroidPreferenceLib/releases) page.
6. Create `imoya-android-preference` subdirectory in your project's root directory.
7. Place `imoya-android-preference-release-[version].aar` in `imoya-android-preference` directory.
8. Create `build.gradle` file in `imoya-android-preference` directory and set content as below:

    ```text
    configurations.maybeCreate("default")
    artifacts.add("default", file('imoya-android-preference-release-[version].aar'))
    ```

9. Add the following line to the `settings.gradle` file in your project's root directory:

    ```text
    include ':imoya-android-preference'
    ```

10. Add dependencies to your library module's `build.gradle`.

     ```groovy
     dependencies {
         // (other dependencies)
         implementation project(':imoya-android-preference')
         // (other dependencies)
     }
     ```

11. Sync project with Gradle.

## Logging

By default, ImoyaAndroidPreferenceLib does not output logs.

If you want to see ImoyaAndroidPreferenceLib's log, please do the following steps:

1. Make string resource `imoya_android_preference_log_level` for setup minimum output log level.

    ```xml
    <resources>
        <!-- (other resources) -->

        <string name="imoya_android_preference_log_level" translatable="false">info</string>

        <!-- (other resources) -->
    </resources>
    ```

    * The values and meanings are shown in the following table:
      | value | meanings |
      | --- | --- |
      | `none` | Output nothing |
      | `all` | Output all log |
      | `v` or `verbose` | Output VERBOSE, DEBUG, INFO, WARN, ERROR, ASSERT log |
      | `d` or `debug` | Output DEBUG, INFO, WARN, ERROR, ASSERT log |
      | `i` or `info` | Output INFO, WARN, ERROR, ASSERT log |
      | `w` or `warn` | Output WARN, ERROR, ASSERT log |
      | `e` or `error` | Output ERROR, ASSERT log |
      | `assert` | Output ASSERT log |
2. Call `net.imoya.android.preference.PreferenceLog.init` method at starting your application or Activity.
    * Sample(Kotlin):

        ```kotlin
        import android.app.Application
        import net.imoya.android.preference.PreferenceLog

        class MyApplication : Application() {
            override fun onCreate() {
                super.onCreate()

                PreferenceLog.init(getApplicationContext())

                // ...
            }

            // ...
        }
        ```

    * Sample(Java):

        ```java
        import android.app.Application;
        import net.imoya.android.preference.PreferenceLog;

        public class MyApplication extends Application {
            @Override
            public void onCreate() {
                super.onCreate();

                PreferenceLog.init(this.getApplicationContext());

                // ...
            }

            // ...
        }
        ```

## Usage and sample application

このライブラリが実装している各 `PreferenceView`, `PreferenceEditor` を、アプリケーションの画面へ実装したサンプルアプリを同梱しております。

使用方法のサンプルとしてご利用ください。

基本的な使用方法は次の通りです。

1. レイアウトリソースへ、設定画面に配置する `PreferenceView` の設定を記述します。
    * サンプルアプリの [`app/src/main/res/layout/sample.xml`](https://github.com/IceImo-P/ImoyaAndroidPreferenceLib/blob/main/app/src/main/res/layout/sample.xml) が、レイアウトリソースのサンプルとなります。
    * `PreferenceView` の設定は、各 view の属性に記載します。
        * `android:key` : この項目が編集する `SharedPreferences` の preference key
        * `android:title` : ビューのタイトル文言
        * `android:summary` : ビューのタイトル下部に表示する、小さめの文字で表示される文言
        * その他、ビュー独自の追加属性。利用可能な属性は、各 `PreferenceView` 実装クラスの[ドキュメント](https://github.com/IceImo-P/ImoyaAndroidPreferenceLib/tree/main/lib/src/main/java/net/imoya/android/preference/view)に記載してあります。
2. Activity または Fragment へ、各 view をタップした時の処理を実装します。
    * このライブラリが提供する基本的な `PreferenceView` はタップ時の処理を実装済みですので、既存の Activity や Fragment へ数行のコード追加のみで実装できます。
    * サンプルアプリの下記ファイルがサンプルとなります。4画面とも、表示内容や動作は同じです。
        1. [`app/src/main/java/net/imoya/android/preference/app/SampleFragment`](https://github.com/IceImo-P/ImoyaAndroidPreferenceLib/blob/main/app/src/main/java/net/imoya/android/preference/app/SampleFragment.kt) : [`PreferenceFragment`](https://github.com/IceImo-P/ImoyaAndroidPreferenceLib/blob/main/lib/src/main/java/net/imoya/android/preference/fragment/PreferenceFragment.kt) を使用したサンプル
        2. [`app/src/main/java/net/imoya/android/preference/app/SamplePlainFragment`](https://github.com/IceImo-P/ImoyaAndroidPreferenceLib/blob/main/app/src/main/java/net/imoya/android/preference/app/SamplePlainFragment.kt) : 一般的な [`Fragment`](https://developer.android.com/reference/androidx/fragment/app/Fragment) へ設定ビューを実装したサンプル
        3. [`app/src/main/java/net/imoya/android/preference/app/SampleActivity`](https://github.com/IceImo-P/ImoyaAndroidPreferenceLib/blob/main/app/src/main/java/net/imoya/android/preference/app/SampleActivity.kt) : [`PreferenceActivity`](https://github.com/IceImo-P/ImoyaAndroidPreferenceLib/blob/main/lib/src/main/java/net/imoya/android/preference/activity/PreferenceActivity.kt) を使用したサンプル
        4. [`app/src/main/java/net/imoya/android/preference/app/SamplePlainActivity`](https://github.com/IceImo-P/ImoyaAndroidPreferenceLib/blob/main/app/src/main/java/net/imoya/android/preference/app/SamplePlainActivity.kt) : 一般的な [`AppCompatActivity`](https://developer.android.com/reference/androidx/appcompat/app/AppCompatActivity) へ設定ビューを実装したサンプル

## Copyright notice

This library uses layout resources created by The Android Open Source Project and uses [Material Icons](https://github.com/google/material-design-icons) designed by Google.

NOTE: Both layout resources and Material Icons are licensed under the Apache License 2.0.

## License

Apache license 2.0
