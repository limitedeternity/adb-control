_JAVA_OPTIONS="-Xms256m -Xmx1024m"

rm -rf build
mkdir -p build
javac -d ./build ./src/name/schedenig/adbcontrol/*.java
cd build
jar cvmf ../META-INF/MANIFEST.MF ../AdbControl.jar *
