@echo off
REM call play clean
@echo ########################## Resources cleaned... Starting Server now... ########################## 
call play debug -Dconfig.file=conf/prod.conf ~run
@echo ##########################         Server has stopped...               ##########################
pause