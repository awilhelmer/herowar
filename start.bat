@echo off
call play clean
@echo ########################## Resources cleaned... Starting Server now... ########################## 
call play -Dconfig.file=conf/prod.conf start
@echo ##########################         Server has stopped...               ##########################
pause