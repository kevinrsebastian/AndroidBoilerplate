# Teko Android App

An android app for Teko technicians' use. The app allows them to register, shows their job schedule, and provide other
tools to help them complete their jobs.


## Architecture and Components

This template follows the MVVM architecture separated by kotlin modules in an attempt to conform to
[clean code](https://www.oncehub.com/blog/explaining-clean-architecture) architecture.

### App Module

The app module is the top module and serves as the entry point to the app. It holds the android components of the 
project, including:
* The Application class and Hilt Modules
* Activities, Fragments, Services and other android classes
* ViewModel that uses feature-model module UseCases
* Application AndroidManifest
* Launcher Icons
* Layout and string resources for UI screens
* Test Hilt Modules (see [Notes and Quirks](#notes-and-quirks))

The app module, as well as other modules with Android UI components will depend on the UI Component module for colors
and theming resources.

This module sits on top of the dependency stack, and should not be added as a dependency in other modules. In the
scheme of MVVM, the app module holds the View and ViewModel.

### Feature-UI Modules

Feature-UI modules are similar to the app module but only contains UI components of a single feature This includes
Activities or Fragments, layouts, and string resources. Like the app module, this also contains the View and ViewModel
in MVVM.

### Feature-Model Modules

Feature-Model modules are a collection of model components related to a specific feature. It holds the data models and
accessors, whether local or remote. A feature-model module is broken down into the following components:
* Hilt Module Class
* Data Models
* UseCases
* Local Database components such as the Database class, DAOs and entities
* Remote components such as APIs and request/response objects

This module is internal, but exposes the Data Models and UseCases. UseCases are injected into the app and IU
modules via the feature Hilt Module Class. In the scheme of MVVM, this module serves as the Model.

### UI Components Module

Contains color and theme resources shared by modules that have layouts for Activities and Fragments.

### Core Module

Provides components and utilities commonly shared by other modules such as:
* EnvConfig class that holds environment variables pulled from BuildConfig
* SharedPreferences

### Test Module

The Test Module contains a collection of utilities to aid in testing such as assertions and data factories, as well as
base classes for [instrumentation](test/src/main/java/ph/teko/app/test/base/BaseInstrumentationTest.kt) and
[unit](test/src/main/java/ph/teko/app/test/base/BaseUnitTest.kt) tests. The test module should be added as
`androidTestImplementation` and `testImplementation` dependencies in other modules.


## Features and Libraries Used

**[DataBinding](https://developer.android.com/topic/libraries/data-binding)**

**[ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
and [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)**

**[Hilt DI](https://developer.android.com/training/dependency-injection/hilt-android)**

**[ReactiveX](https://reactivex.io/)-[RxJava](https://github.com/ReactiveX/RxJava)**

**[Retrofit](https://square.github.io/retrofit/)**

**[Room DB](https://developer.android.com/training/data-storage/room)**

**[AssertJ](https://assertj.github.io/doc)**

**[Espresso](https://developer.android.com/topic/libraries/architecture/viewmodel)**

**[Mockito](https://site.mockito.org/)**\
Android Guide: https://developer.android.com/training/testing/local-tests

**[MockWebServer](https://github.com/square/okhttp/tree/master/mockwebserver)**

**[KTLint](https://ktlint.github.io)**\
Plugin: https://github.com/jlleitschuh/ktlint-gradle


## Notes and Quirks

* Instrumentation Hilt modules in features used to mock/replace actual Hilt modules are not being detected during
testing. When running instrumentation tests, the actual modules are still being detected. As a work-around, the
feature Hilt module is made non-internal with internal functions. The instrumentation Hilt Module of that feature is
then placed in the app instrumentation folder, where it works fine. An alternative would be to add the feature
instrumentation module directory as sourceSets in app `build.gradle`. But that defeats the purpose, as the actual
Hilt module still needs to be non-internal to be detected.

* Related to the issue above, feature module test utilities are not being detected in app module tests. To avoid code
duplication, the test module has been introduced as a [workaround](https://treatwell.engineering/mock-factory-for-android-testing-in-multi-module-system-7654f45808be).
This module holds the common test utilities used by other modules. A [potential fix](https://issuetracker.google.com/issues/139438142)
is currently in development. Check this [article](http://michaelevans.org/blog/2019/09/21/stop-repeating-yourself-sharing-test-code-across-android-modules/)
for more information.

* It is recommended to test RoomDB DAOs in instrumentation, which worked when the project was still a monolith in
app module. When converted to a modular structure, these instrumentation tests in feature modules are no longer
detected. The DAO tests have been removed in the build. No workarounds have been found yet, but potentially running
these as unit tests with Robolectric could work.
