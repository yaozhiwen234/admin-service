@echo off & setlocal EnableDelayedExpansion

echo.
echo.
echo.
echo ************************************ 美好的一天从敲代码开始 ********************************************************
:s
echo.
echo.
:: 手工输入
::set /p operation=请输入你要的操作 n 代表你要新建一个markdown文件 d 代表你要部署项目到服务 :
:: 获取程序传过来的变量
set operation=%1

D:
cd D:\hexo

if "%operation%" == "n" (
echo.
echo.
set /p name=请输入你要创建的文件名称:
echo.
echo.
echo 文件名称:!name!
echo.
echo.
echo 请稍候......
goto n
)


if "%operation%" == "d" (
goto d
)

if "%operation%" NEQ  "n"  (
if "%operation%" NEQ  "d" (
if "%operation%" ==  "" (
echo 输入错误，请重新输入 & pause
goto s
)
)
)

:n
echo.
echo.
hexo new  !name! &  start D:\App\app\Typora\Typora.exe   "D:\hexo\source\_posts\%name%.md"

:d
echo.
echo.
hexo clean | hexo g & gulp | hexo d
echo.
echo 部署完成
echo.
exit