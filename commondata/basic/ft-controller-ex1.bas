m1="node 2 "
master=" 1"
for i=1 to 3
r=ex("FT-Controller-(master)",m1+"set-motor 1 1")
r=ex("FT-Controller-(master)","waittime 1000")
r=ex("FT-Controller-(master)",m1+"set-motor 1 0")
r=ex("FT-Controller-(master)","waittime 1000")
r=ex("FT-Controller-(master)",m1+"set-motor 1 2")
r=ex("FT-Controller-(master)","waittime 1000")
r=ex("FT-Controller-(master)",m1+"set-motor 1 0")
r=ex("FT-Controller-(master)","waittime 1000")
next i

r=ex("FT-Controller-(master)",m1+"get-digital 1"+master)
? ex("FT-Controller-(master)","getReturn")
r=ex("FT-Controller-(master)",m1+"get-digital 2"+master)
? ex("FT-Controller-(master)","getReturn")
