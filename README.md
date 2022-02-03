# AndroidBoilerplate

An android project that can be used as a boilerplate for future projects. This template project uses the MVVM
architecture and attempts to conform to clean code and SOLID programming principles.

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

**[ReactiveX](https://reactivex.io/)-[RxJava](https://github.com/ReactiveX/RxJava)**

**[Room DB](https://developer.android.com/training/data-storage/room)**

**[AssertJ](https://assertj.github.io/doc)**

**[Espresso](https://developer.android.com/topic/libraries/architecture/viewmodel)**

**[Mockito](https://site.mockito.org/)**\
Android Guide: https://developer.android.com/training/testing/local-tests

**[KTLint](https://ktlint.github.io)**\
Plugin: https://github.com/jlleitschuh/ktlint-gradle

---

### Architecture and Components

This template follows the MVVM architecture separated by kotlin modules in an attempt to conform to
[clean code](https://www.oncehub.com/blog/explaining-clean-architecture) architecture.

#### App Module

The app module holds the android components of the project, including:
* The Application class
* Activities, Fragments, Services and other android classes
* Resources such as layouts, texts, images, colors, etc.
* ViewModel and its Controller that uses the feature module UseCases

This module sits on top of the dependency stack, meaning that it is not visible (internal in Kotlin terms) to lower
modules such as feature and the core module. In the scheme of MVVM, the app module holds the View and ViewModel.

#### Feature Modules

Feature modules are a collection of components related to a specific feature. For example, if there is a feature for
users then there should be a UserModule that holds the models and repositories of user-related features. A feature
module is broken down into the following components:
* Hilt Module Class
* Data Models
* UseCases
* Local Database components such as the Database class, DAOs and entities
* Remote components such as APIs and request/response objects

This module is also internal, but exposes the Data Models and UseCases. UseCases are injected into the app module via
the feature Hilt Module Class. In the scheme of MVVM, this module serves as the Model.

---

### Notes and Quirks

* Instrumentation Hilt modules in features used to mock/replace actual Hilt modules are not being detected during
testing. When running instrumentation tests, the actual modules are still being detected. As a work-around, the
feature Hilt module is made non-internal with internal functions. The instrumentation Hilt Module of that feature is
then placed in the app instrumentation folder, where it works fine. An alternative would be to add the feature
instrumentation module directory as sourceSets in app `build.gradle`. But that defeats the purpose, as the actual
Hilt module still needs to be non-internal to be detected.

* It is recommended to test RoomDB DAOs in instrumentation, which worked when the project was still a monolith in
app module. When converted to a modular structure, these instrumentation tests in feature modules are no longer
detected. The DAO tests have been removed in the build. No workarounds have been found yet, but potentially running
these as unit tests with Robolectric could work.
