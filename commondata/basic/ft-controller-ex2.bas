def mxOnUntilEyOn(x,y)={
  r=ex("FT-Controller-(master)",m1+"set-motor "+x+" 1")
  r=ex("FT-Controller-(master)",m1+"get-digital "+y+master)
  rt=ex("FT-Controller-(master)","getReturn")
  while 0+rt <> 1 {
     r=ex("FT-Controller-(master)","waittime 100")
     r=ex("FT-Controller-(master)",m1+"get-digital "+y+master)
     rt=ex("FT-Controller-(master)","getReturn")
  }
  r=ex("FT-Controller-(master)",m1+"set-motor "+x+" 0")
}

m1="node 2 "
master=" 1"
call mxOnUntilEyOn("1","1")
