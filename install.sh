
echo "This script will only install the files in the actual session"
echo "you need to call 'source $0'"

./gradlew sharedInstall

installDir=$(pwd)/build/install/bin

export PATH=$PATH:${installDir}
