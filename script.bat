path = C:\Users\Octane\Desktop\Projet_MrNaina
cd %path%
mkdir temp
cd temp
mkdir WEB-INF
cd WEB-INF
mkdir classes lib

cd %path%/framework/build
javac -d framework ../src/*.java
cd framework
jar -cf ../fw.jar .
cd ..
copy fw.jar %path%\temp\WEB-INF\lib

cd %path%/framework_test
copy *.jsp %path%\temp
cd WEB-INF 
copy web.xml %path%\temp\WEB-INF
cd classes
copy * %path%\temp\WEB-INF\classes
cd %path%\temp\WEB-INF\classes
javac -d . *.java




