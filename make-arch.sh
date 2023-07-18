for (( i = 0; i < 10; i++ )); do
    clear
done

javac *.java
if [ $? -eq 0 ]; then
    java Main
else
    echo "----------------------------"
    echo "Failed to javac something."
    echo "----------------------------"
fi