cd /lib/x86_64-linux-gnu/

if [ -e "libudev.so.0" ]
then
   echo "Already configured"
else
  sudo ln -sf libudev.so.1 libudev.so.0
fi