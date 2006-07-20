From http://download2.eclipse.org/downloads/drops/S-RC1-200302211557/download.php?dropFile=eclipse-platform-RC1-macosx-carbon.zip :


This is a very experimental build for Mac OS X 10.2.x (it does not run
earlier versions of Mac OS X!).

At this point please report any problems related to the build to the
platform-releng-dev@eclipse.org mailing list.


Important note about unpacking Eclipse on MacOS X:

Out of the box, Zip files are unzipped using Stuffit Expander. If your
version of Stuffit Expander is older than version 7.0 (released
09/19/2002), it does not handle the long filenames that the Eclipse
build uses and ends up truncating them, which causes numerous
problems.

Furthermore, Mac users use IE 5.2 by default, and the download manager
will by default automatically unzip downloads using this mechanism. In
addition IE 5.2 does not preserve the long file name of the Eclipse
Zip archive, so it ends up in "eclipse-SDK-I20021029-macosx-ca".

So before downloading and unpacking Eclipse we recommend to install
the free upgrade to Stuffit Expander 7.0.

However, if you want to unzip the build yourself and assuming you
download to the desktop, from a terminal window type:

cd ~/desktop
unzip eclipse-SDK-I20021029-macosx-ca
