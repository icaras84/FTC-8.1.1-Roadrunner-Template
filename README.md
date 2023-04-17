# FTC 8.1.1 Roadrunner Template

An example FTC project using [Road Runner](https://github.com/acmerobotics/road-runner)

## Installation

For more detailed instructions on getting Road Runner setup in your own project, see the [Road Runner README](https://github.com/acmerobotics/road-runner#core).

1. Download or clone this repo with `git clone https://github.com/icaras84/FTC-8.1.1-Roadrunner-Template/`.

1. Open the project in Android Studio and build `TeamCode` like any other `ftc_app` project.

1. If you have trouble with multidex, enable proguard by changing `useProguard` to `true` in `build.common.gradle`.

## Documentation

Check out the [online quickstart documentation](https://acme-robotics.gitbook.io/road-runner/quickstart/introduction) for Road Runner instructions.

## How to change SDK version

Copy the new SDK's FtcRobotController folder and replace this project's folder to patch in the new sdk.
The manifest xml might still have the old version number, but rest assured that you can just change that.
To navigate to the manifest, it will be in the file path: `FtcRobotController/src/main/AndroidManifest.xml`
In there, look for the XML tag attribute, `android:versionName`, and change the string to whatever version you require
- Examples: 
  -`android:versionName="2025.1.1"`
  -`android:versionName="9000.0.1"`
  -`android:versionName="Never.Gonna.Give.You.Up"`
  -`android:versionName="icaras84_was_here"`
  -`android:versionName="4.1826.10-ALPHA"`
