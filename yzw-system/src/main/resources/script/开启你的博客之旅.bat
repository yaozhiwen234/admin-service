@echo off & setlocal EnableDelayedExpansion

echo.
echo.
echo.
echo ************************************ ���õ�һ����ô��뿪ʼ ********************************************************
:s
echo.
echo.
:: �ֹ�����
::set /p operation=��������Ҫ�Ĳ��� n ������Ҫ�½�һ��markdown�ļ� d ������Ҫ������Ŀ������ :
:: ��ȡ���򴫹����ı���
set operation=%1

D:
cd D:\hexo

if "%operation%" == "n" (
echo.
echo.
set /p name=��������Ҫ�������ļ�����:
echo.
echo.
echo �ļ�����:!name!
echo.
echo.
echo ���Ժ�......
goto n
)


if "%operation%" == "d" (
goto d
)

if "%operation%" NEQ  "n"  (
if "%operation%" NEQ  "d" (
if "%operation%" ==  "" (
echo ����������������� & pause
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
echo �������
echo.
exit