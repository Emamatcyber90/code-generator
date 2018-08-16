@echo off & setlocal enabledelayedexpansion

set DIR_BIN=%~dp0
cd %DIR_BIN%
echo DIR_BIN=%DIR_BIN%

set JAVA_OPTS=-server -Xms64m -Xmx1024m

java %JAVA_OPTS% -jar code-generator-1.0.0.jar --config=config.json