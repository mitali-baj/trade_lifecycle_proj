@echo off

REM Navigate to the trade-capture service directory
cd ..\src\trade-capture

REM Clean and package the Spring Boot application
call mvn clean install

REM Check if the Maven build was successful
if %errorlevel% neq 0 (
    echo Maven build failed.
    exit /b %errorlevel%
)

REM Build the Docker image
docker build -t trade-capture:latest .

REM Check if the Docker build was successful
if %errorlevel% neq 0 (
    echo Docker image build failed.
    exit /b %errorlevel%
)

echo Trade Capture Service built successfully.
