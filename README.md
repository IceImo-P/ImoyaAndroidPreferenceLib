# ImoyaAndroidPreferenceLib

[VoiceClock](https://imoya.net/android/voiceclock) より切り出した、設定画面の共通実装です。

* [android.preference](https://developer.android.com/reference/android/preference/Preference) ライクで、ユーザーが拡張可能な設定画面および設定項目ビューの実装
* 設定画面と SharedPreferences を連携するデフォルトの実装

本ライブラリは Android Jetpack 公開以前に作者がスクラッチ作成したコードがベースとなっているため、 Jetpack が提供する [androidx.preference](https://developer.android.com/jetpack/androidx/releases/preference?hl=ja) との互換性はありません。

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
        implementation 'net.imoya.android.dialog:imoya-android-dialog:1.3.0'
        implementation 'net.imoya.android.fragment:imoya-android-fragment:1.2.0'
        implementation 'net.imoya.android.log:imoya-android-log:1.1.0'
        implementation 'net.imoya.android.preference:imoya-android-preference:1.3.0'
        implementation 'net.imoya.android.util:imoya-android-util:1.6.0'
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

## Copyright notice

This library uses layout resources created by The Android Open Source Project and uses [Material Icons](https://github.com/google/material-design-icons) designed by Google.

NOTE: Both layout resources and Material Icons are licensed under the Apache License 2.0.

## License

Apache license 2.0
