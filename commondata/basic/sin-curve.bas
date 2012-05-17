' draw a sin curve
'   t.yamanoue, may,1999
'
' the sin function
def f(x)=sin(x)
'
' transform x value to the x value on the graphics plane
def gx(x)=x+10
'
' transform y value to the y value on the graphics plane
def gy(y)=ymax-y
'
' x-min, x-max, y-min, y-max
xmin=0:xmax=300
ymin=0:ymax=150
'
a=ymax/2
Pi=3.141592
'
' draw the x axis
line (gx(xmin),gy(ymin))-(gx(xmax),gy(ymin))
t=0
s=10
pset(gx(t),gy(a*f(2*Pi*t/xmax)))
for t=s to xmax step s
  line -(gx(t),gy(a*f(2*Pi*t/xmax)))
next t
