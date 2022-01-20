# AndroidBoilerplate

An android project that can be used as a boilerplate for future projects.

Things to update when cloning to a new project:
- The package of java folders and file package names
- Update package in AndroidManifest
- Update applicationId with the package name in `app/build.gradle`
- Update rootProject.name with the project name in `settings.gradle`
- Update app_name in `strings.xml`
- Rename theme names and update references
- Rename the Application class and update the reference in AndroidManifest

---

### Features and Libraries Used

**[DataBinding](https://developer.android.com/topic/libraries/data-binding)**

**[ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
    and [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)**

**[Hilt DI](https://developer.android.com/training/dependency-injection/hilt-android)**

**[AssertJ](https://assertj.github.io/doc)**

**[Espresso](https://developer.android.com/topic/libraries/architecture/viewmodel)**

**[Mockito](https://site.mockito.org/)**\
Android Guide: https://developer.android.com/training/testing/local-tests

**[KTLint](https://ktlint.github.io)**\
Plugin: https://github.com/jlleitschuh/ktlint-gradle
