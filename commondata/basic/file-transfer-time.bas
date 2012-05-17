'get minimum start time of the FileTransferFrame
'spawn the FileTransferFrame and broadcast a file
'before running this program
def ministart()={
   xs=0+ex("FileTransferFrame","request start-time")
   lx=ex("node","askToDown-nw l key1-l BasicFrame ex print ministart()")
   rx=ex("node","askToDown-nw r key1-r BasicFrame ex print ministart()")
   if lx<>"na" then {
      lxx=0+ex("node","waitForResult key1-l")
      if xs>lxx then xs=lxx
   }
   if rx<>"na" then {
      rxx=0+ex("node","waitForResult key1-r")
      if xs>rxx then xs=rxx
   }
   ministart=rx
}
'get maximum end time of the FileTransferFrame
'spawn the FileTransferFrame and broadcast a file
'before running this program
def maxend()={
   xs=0+ex("FileTransferFrame","request end-time")
   lx=ex("node","askToDown-nw l key2-l BasicFrame ex print maxend()")
   rx=ex("node","askToDown-nw r key2-r BasicFrame ex print maxend()")
   if lx<>"na" then {
      lxx=0+ex("node","waitForResult key2-l")
      if xs<lxx then xs=lxx
   }
   if rx<>"na" then {
      rxx=0+ex("node","waitForResult key2-r")
      if xs<rxx then xs=rxx
   }
   maxend=rx
}
