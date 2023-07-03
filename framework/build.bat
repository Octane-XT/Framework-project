
javac -d temp framework/src/*.java
cd temp
jar -cf ../test-framework/WEB-INF/lib/fw.jar .
cd ..
rmdir /s /q temp


