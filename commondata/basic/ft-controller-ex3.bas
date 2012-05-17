m1="node 2 "
master=" 1"
ft="FT-Controller-(master)"
for i=1 to 3
r=ex(ft,m1+"set-motor 1 1")
r=ex(ft,"waittime 1000")
r=ex(ft,m1+"set-motor 1 0")
r=ex(ft,"waittime 1000")
r=ex(ft,m1+"set-motor 1 2")
r=ex(ft,"waittime 1000")
r=ex(ft, m1+"set-motor 1 0")
r=ex(ft, "waittime 1000")
next i

r=ex("FT-Controller-(master)",m1+"get-digital 1"+master)
? ex("FT-Controller-(master)","getReturn")
r=ex("FT-Controller-(master)",m1+"get-digital 2"+master)
? ex("FT-Controller-(master)","getReturn")
'set-motor-until-digital on|off <Motor ID> <current> <digital sensor ID>
'<current>=0(off) |1(on +)|2 (on -)
r=ex(ft,m1+"set-motor-until-digital on 2 2 4")
'set-count <diigtal sensor ID> <count>
r=ex(ft,m1+"set-count 3 0")
r=ex(ft,m1+"set-count 1 0")
'set-motor-while-count <rlop> <Motor ID> <current> <digital sensor ID> <count>
'r=ex(ft,m1+"set-motor-while-count lt 2 1 3 10")
'
'initialize machines
m1="node 2 "
r=ex(ft,m1+"set-maxtime 20000")
r=ex(ft,m1+"set-motor-until-digital on 1 2 2")
r=ex(ft,m1+"set-count 1 0")
r=ex(ft,m1+"set-motor-until-digital on 2 2 4")
r=ex(ft,m1+"set-count 3 0")
r=ex(ft,m1+"set-motor-until-digital on 3 2 6")
r=ex(ft,m1+"set-count 5 0")
r=ex(ft,m1+"set-motor-until-digital on 4 2 8")
r=ex(ft,m1+"set-count 7 0")
r=ex(ft,m1+"get-digital 1"+master)
? ex("FT-Controller-(master)","getReturn")
'
m2="node 3 "
r=ex(ft,m2+"set-maxtime 20000")
r=ex(ft,m2+"set-motor-until-digital on 3 1 6")
r=ex(ft,m2+"set-motor-until-digital on 3 1 6")
r=ex(ft,m2+"set-count 5 0")
r=ex(ft,m2+"set-motor-while-count lt 3 2 5 46")
r=ex(ft,m2+"set-count 5 0")
r=ex(ft,m2+"set-motor-until-digital on 4 2 8")
r=ex(ft,m2+"set-count 7 0")
r=ex(ft,m2+"get-digital 1"+master)
? ex("FT-Controller-(master)","getReturn")
'
r=ex(ft,"waittime 7000")
'
' rotation example
' step 1... horizontal rotate 90 degree
r=ex(ft,m2+"set-count 5 0")
r=ex(ft,m2+"set-motor-while-count lt 3 2 5 45")
r=ex(ft,m2+"get-digital 1"+master)
? ex("FT-Controller-(master)","getReturn")

r=ex(ft,"waittime 1000")
'
' step 2... move the dice to the vertical rotator
r=ex(ft,m2+"set-count 7 0")
r=ex(ft,m2+"set-motor-while-count lt 4 1 7 58")
r=ex(ft,m2+"set-motor-until-digital on 4 2 8")
r=ex(ft,m2+"set-count 7 0")
r=ex(ft,m2+"get-digital 1"+master)
? ex("FT-Controller-(master)","getReturn")

r=ex(ft,"waittime 1000")
'
'step 3 ... move the dice to tilter-1
r=ex(ft,m1+"set-count 5 0")
r=ex(ft,m1+"set-motor-while-count lt 3 1 5 58")
r=ex(ft,m1+"set-motor-until-digital on 3 2 6")
r=ex(ft,m1+"get-digital 1"+master)
? ex("FT-Controller-(master)","getReturn")

