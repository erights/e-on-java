From http://download2.eclipse.org/downloads/drops/R-2.0.2-200211071448/download.php?dropFile=eclipse-platform-2.0.2-qnx-photon.zip :


To run Eclipse on Neutrino/Photon you will need to do the following:

1) Download the IBM J9 VM for QNX.  There is a link for this on the
   Java Runtime page linked from the drop index page.  (Currently the
   standard Photon VMs do not support Eclipse).

2) Unzip and untar the file into your Eclipse install directory.
   Alternately you can unzip or untar the file anywhere but will need
   to add the */ive/bin directory to your PATH.

3) Download and unzip Eclipse for Photon.

4) Add the SWT library and the J9 VM to your LD_LIBRARY_PATH
   environment variable.  This is done with a line similar to: export
   LD_LIBRARY_PATH=$LD_LIBARRY_PATH:/<J9 install
   dir>/ive/bin:/<eclipse install
   dir>/eclipse/plugins/org.eclipse.swt/os/qnx/x86.

5) Install the J9 Plug-in.  This plug-in is used to configure Eclipse
   to use the J9 VM for running Java programs from within Eclipse.
   The Eclipse preferences to specify the desired VM can not be used
   with J9 since J9 uses a slightly different file structure than
   standard VMs.  Instructions for installing and configuring this
   plug-in can be found on Eclipse Corner at:
   http://dev.eclipse.org/viewcvs/index.cgi/~checkout~/jdt-debug-home/plugins/org.eclipse.jdt.launching.j9/index.html .
   If you don't instlal this program Eclipse will run but you will not
   be able to run any of the code you develop in Eclipse.
