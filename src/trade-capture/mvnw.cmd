@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership.  The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM   http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.

@REM ----------------------------------------------------------------------------
@REM Maven Wrapper startup script
@REM
@REM This script is used to start the Maven build process. It will download and
@REM install Maven if it is not already installed.
@REM ----------------------------------------------------------------------------

@echo off

setlocal

set "MAVEN_CMD_LINE_ARGS=%*"

set "MAVEN_HOME="
set "JAVA_HOME="

for %%i in ("%~dp0.") do set "CURRENT_DIR=%%~fi"

:findProjectBaseDir
if exist "%CURRENT_DIR%\.mvn\wrapper\maven-wrapper.jar" (
    goto foundProjectBaseDir
)
if "%CURRENT_DIR%" == "%~dp0" (
    echo Error: Could not find .mvn directory. Please run this script from a Maven project.
    exit /b 1
)
for %%i in ("%CURRENT_DIR%\..") do set "CURRENT_DIR=%%~fi"
goto findProjectBaseDir

:foundProjectBaseDir

pushd "%CURRENT_DIR%"

java -jar ".mvn\wrapper\maven-wrapper.jar" %MAVEN_CMD_LINE_ARGS%

popd

endlocal
